package uz.pdp.bankcard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.bankcard.entity.*;
import uz.pdp.bankcard.entity.enums.RolesName;
import uz.pdp.bankcard.payload.ApiResponse;
import uz.pdp.bankcard.payload.CardDto;
import uz.pdp.bankcard.repository.BankRepository;
import uz.pdp.bankcard.repository.CardRepository;
import uz.pdp.bankcard.repository.CardTypeRepository;
import uz.pdp.bankcard.repository.UserRepository;

import java.util.Optional;

@Service
public class CardService {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    CardTypeRepository cardTypeRepository;


    public ApiResponse addCard(CardDto cardDto) {

        User userInSystem = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (Role role : userInSystem.getRole()) {
            if (role.getRolesName().equals(RolesName.CARD_ADDER)) {

                Optional<User> optionalUser = userRepository.findById(cardDto.getOwnedByUserId());
                Optional<Bank> optionalBank = bankRepository.findById(cardDto.getBankId());
                Optional<CardType> optionalCardType = cardTypeRepository.findById(cardDto.getPlasticCardTypeId());
                if (optionalUser.isPresent()) {
                    if (optionalBank.isPresent()) {
                        if (optionalCardType.isPresent()) {
                            Card card = new Card();
                            User user = optionalUser.get();
                            Bank bank = optionalBank.get();
                            CardType cardType = optionalCardType.get();

                            card.setCardNumber(cardDto.getCardNumber());
                            card.setCvv(cardDto.getCvv());
                            card.setOwnedByUser(user);
                            card.setExpiresAt(cardDto.getExpiresAt());
                            card.setPassword(cardDto.getPassword());
                            card.setErrPinCode(0);
                            card.setPlasticCardType(cardType);
                            card.setBank(bank);
                            cardRepository.save(card);
                            return new ApiResponse("card saved", true);
                        }
                        return new ApiResponse("card type is not found", false);
                    }
                    return new ApiResponse("bank is not found", false);
                }
                return new ApiResponse("user is not found", false);
            }
        }
        return new ApiResponse("You can't add card", false);
    }

    public ApiResponse enableCard(String cardNumber) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        for (Role role : user.getRole()) {
            if (role.getRolesName().equals(RolesName.CARD_ENABLER)) {
                Optional<Card> optionalCard = cardRepository.findByCardNumber(cardNumber);
                Card card = optionalCard.get();
                card.setEnabled(true);
                card.setErrPinCode(0);

                cardRepository.save(card);
                return new ApiResponse("card enabled", true);
            }
        }
        return new ApiResponse("you can't enable card", false);
    }
}