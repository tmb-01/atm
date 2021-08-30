package uz.pdp.bankcard.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;
    private String country;
    private String region;
    private String city;
    private String street;
    private String homeNumber;

    @OneToOne(mappedBy = "address")
    private ATM atm;
}
