package uz.pdp.bankcard.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.bankcard.entity.ATM;
import uz.pdp.bankcard.entity.Card;
import uz.pdp.bankcard.entity.Cash;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActionToWithdraw {
    private List<Cash> cash;

    private UUID atmId;
}
