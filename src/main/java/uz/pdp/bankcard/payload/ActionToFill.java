package uz.pdp.bankcard.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.bankcard.entity.ATM;
import uz.pdp.bankcard.entity.Card;
import uz.pdp.bankcard.entity.Cash;
import uz.pdp.bankcard.entity.User;
import uz.pdp.bankcard.entity.enums.ActionType;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActionToFill {

    private List<Cash> cash;

    private UUID userId;

    private UUID atmId;
}
