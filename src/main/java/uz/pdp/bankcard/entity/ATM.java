package uz.pdp.bankcard.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.bankcard.entity.enums.PlasticCardType;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ATM {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<CardType> forCards; // qanaqa kartalarni qabul qiladi masalan HUMO, UZCARD, VISA

    private double maxValue; // yechiladigan max pul miqdori
    private double minValue; // min shuncha pul qosa masul odamga xabar junatish
    private double currentAmount; // bankomatda xozir qancha puli borligi

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "atm")
    private List<Cash> cashes;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Bank bank;

    @Column(nullable = false)
    private double commissionPercent; // komisiya miqdori, bankamat banki bilan birxil bulmagan cardlar uchun

    @Column(nullable = false)
    private double commissionPercentForSameBankCard; // komisiya miqdori, bankamat banki bilan birxil bulgan cardlar uchun

    @OneToOne
    private Address address;

    @Transient
    private float totalAmount;

    public float getTotalAmount() {
        float totalAmount = 0;
        for (Cash cash : this.cashes) {
            totalAmount += cash.getTotalAmount();
        }
        return totalAmount;
    }

}
