package uz.pdp.bankcard.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.bankcard.entity.enums.PlasticCardType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Card implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 16, unique = true)
    private String cardNumber;

    @Column(nullable = false, length = 3, unique = true)
    private String cvv;

    @ManyToOne
    private User ownedByUser;

    @Column(nullable = false)
    private Timestamp expiresAt;

    @Column(nullable = false, length = 4, unique = true)
    private String password;

    private Integer errPinCode; // pin kod nechi marta xato terigani

    @Column(nullable = false, unique = true)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CardType plasticCardType; // plastik karta turi masalan HUMO, UZCARD, VISA

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Role> role;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bank bank;

    private Float totalAmount;

//    user details service

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.cardNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
