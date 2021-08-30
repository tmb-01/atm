package uz.pdp.bankcard.service;

//import org.springframework.security.core.userdetails.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.bankcard.entity.Card;
import uz.pdp.bankcard.entity.User;
import uz.pdp.bankcard.payload.ApiResponse;
import uz.pdp.bankcard.payload.LoginDto;
import uz.pdp.bankcard.repository.CardRepository;
import uz.pdp.bankcard.repository.UserRepository;
import uz.pdp.bankcard.security.JwtProvider;
import uz.pdp.bankcard.util.StringManager;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username.length() == 16 && StringManager.isContainsOnlyNumber(username)) {
            Optional<Card> optionalCard = cardRepository.findByCardNumber(username);
            if (optionalCard.isPresent()) {

                Card card = optionalCard.get();
                if (card.getExpiresAt().before(Timestamp.valueOf(LocalDateTime.now()))) {
                    card.setEnabled(false);
                }

                if (card.getErrPinCode() >= 2) {
                    card.setEnabled(false);
                }

                if (!card.isEnabled()) {
                    card = cardRepository.save(card);
                }
                return card;

            }
            throw new UsernameNotFoundException("card not found");
        } else {
            Optional<User> byEmail = userRepository.findByEmail(username);
            if (byEmail.isPresent()) {
                return byEmail.get();
            }
            throw new UsernameNotFoundException("user not found");
        }
    }

    public ApiResponse login(LoginDto loginDto) {
        String username = loginDto.getUsername();
        try {
            String token = null;
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

            if (username.length() == 16 && StringManager.isContainsOnlyNumber(username)) {
                Card card = (Card) authenticate.getPrincipal();

                token = jwtProvider.generateToken(card.getUsername(), card.getRole());
            } else {
                User user = (User) authenticate.getAuthorities();

                token = jwtProvider.generateToken(user.getUsername(), user.getRole());
            }
            return new ApiResponse(token, true);
        } catch (BadCredentialsException badCredentialsException) {
            if (username.length() == 16 && StringManager.isContainsOnlyNumber(username)) {
                Optional<Card> optionalCard = cardRepository.findByCardNumber(loginDto.getUsername());
                if (optionalCard.isPresent()) {
                    Card card = optionalCard.get();
                    card.setErrPinCode(card.getErrPinCode() + 1);
                }
            }

            return new ApiResponse("username or password error", false);
        }
    }

}
