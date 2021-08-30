package uz.pdp.bankcard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.bankcard.entity.enums.PlasticCardType;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CardType {
    @Id
    @GeneratedValue
    private Integer id;

    @Enumerated(value = EnumType.STRING)
    private PlasticCardType plasticCardType;

    @JsonIgnore
    @ManyToMany(mappedBy = "forCards", cascade = CascadeType.ALL)
    private List<ATM> atm;
}
