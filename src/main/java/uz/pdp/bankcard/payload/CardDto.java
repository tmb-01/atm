package uz.pdp.bankcard.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.bankcard.entity.Bank;
import uz.pdp.bankcard.entity.CardType;
import uz.pdp.bankcard.entity.Role;
import uz.pdp.bankcard.entity.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardDto {

    private String cardNumber;

    private String cvv;

    private UUID ownedByUserId;

    private Timestamp expiresAt;

    private String password = "0000"; // plastik karta pin kodi defolt xolatda yani kartani qushganda paroli 0000 user keyinchalik uzi bu parolni uzgartirishi mumkun

    private Integer plasticCardTypeId; // plastik karta turi masalan HUMO, UZCARD, VISA

    private UUID bankId;

}
