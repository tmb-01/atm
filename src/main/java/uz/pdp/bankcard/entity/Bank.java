package uz.pdp.bankcard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Bank {
    @Id
    @GeneratedValue
    private UUID id;

    private String bankName;

    @JsonIgnore
    @OneToMany(mappedBy = "bank")
    private List<User> user; // bank ishchilari

    @JsonIgnore
    @OneToMany(mappedBy = "bank")
    private List<ATM> atmList;

    @JsonIgnore
    @OneToMany(mappedBy = "bank")
    private List<Card> cards;
}
