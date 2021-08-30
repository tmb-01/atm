package uz.pdp.bankcard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.bankcard.entity.enums.Currency;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Cash {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    private Currency currency; // valyuta

    private double cashAmount; // pul nechi sum yoki dollarlik ekanligi

    private long howMany; // shunaqa puldan neshta bor ekanligi

    @Transient
    private float totalAmount; // umumiy pul miqdori

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private ATM atm;

    @JsonIgnore
    @ManyToMany(mappedBy = "cash", cascade = CascadeType.ALL)
    private List<Action> action;

    public float getTotalAmount() {
        return (float) (this.howMany * this.cashAmount);
    }
}
