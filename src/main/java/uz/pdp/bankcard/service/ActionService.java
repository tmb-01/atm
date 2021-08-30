package uz.pdp.bankcard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.bankcard.entity.*;
import uz.pdp.bankcard.entity.enums.ActionType;
import uz.pdp.bankcard.entity.enums.RolesName;
import uz.pdp.bankcard.exception.MoneyInsufficientException;
import uz.pdp.bankcard.exception.MoneyTypeException;
import uz.pdp.bankcard.payload.ActionToFill;
import uz.pdp.bankcard.payload.ActionToWithdraw;
import uz.pdp.bankcard.payload.ApiResponse;
import uz.pdp.bankcard.repository.*;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActionService {

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CashRepository cashRepository;

    @Autowired
    CashService cashService;

    @Autowired
    ATM_Repository atmRepository;


    @Transactional
    public ApiResponse fillATM(ActionToFill actionToFill) {
        Optional<ATM> optionalATM = atmRepository.findById(actionToFill.getAtmId());
        Optional<User> optionalUser = userRepository.findById(actionToFill.getUserId());

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        for (Role role : user.getRole()) {

            if (role.getRolesName().equals(RolesName.ATM_REFILL)) {

                if (optionalATM.isPresent()) {
                    Action action = new Action();
                    action.setActionType(ActionType.FILL);
                    action.setAtm(optionalATM.get());
                    action.setUser(optionalUser.get());
                    List<Cash> cash = cashService.addMoneyByATMId(actionToFill.getAtmId(), actionToFill.getCash());
                    action.setCash(cash);
                    actionRepository.save(action);

                    return new ApiResponse("ATM is filled", true);
                }
                return new ApiResponse("ATM not found", false);
            }
        }
        return new ApiResponse("You can't refill ATM", false);
    }

    @Transactional
    public ApiResponse withdrawMoney(ActionToWithdraw actionToWithdraw) {
        UUID atmId = actionToWithdraw.getAtmId();
        List<Cash> cashes = actionToWithdraw.getCash();
        Optional<ATM> optionalATM = atmRepository.findById(atmId);

        if (optionalATM.isPresent()) {
            try {
                Action action = cashService.withdrawMoney(atmId, cashes);
                actionRepository.save(action);
                return new ApiResponse("you get money successfully", true);
            } catch (MoneyInsufficientException e) {
                return new ApiResponse(e.getMessage(), false);
            }
        }
        return new ApiResponse("there is no such ATM", false);
    }

    public ApiResponse cashToCard(UUID atmId, List<Cash> cashList) {

        List<Cash> cashes = null;
        try {
            cashes = cashService.cashToCard(atmId, cashList);
        } catch (MoneyTypeException e) {
            e.printStackTrace();
        }

        Card card = (Card) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<ATM> optionalATM = atmRepository.findById(atmId);

        float totalAmount = 0;
        for (Cash cash : cashes) {
            totalAmount += cash.getTotalAmount();
        }
        card.setTotalAmount(card.getTotalAmount() + totalAmount);
        Card save = cardRepository.save(card);
        Action action = new Action();
        action.setCard(save);
        action.setActionType(ActionType.CASH_TO_DIGITAL);
        action.setAtm(optionalATM.get());
        action.setCash(cashes);
        actionRepository.save(action);

        return new ApiResponse("successfully you put money to your card", true);
    }

    public List<Action> incomeOutcomeById(UUID atmId) {
        return actionRepository.findByAtm_Id(atmId);
    }

    public List<Action> getDailyIncome(UUID atmId) {
        return actionRepository.findByAtm_IdAndActionTypeEqualsAndCreatedAtEquals(atmId, ActionType.DIGITAL_TO_CASH, Timestamp.valueOf(LocalDateTime.now()));
    }

    public List<Action> getDailyOutcome(UUID atmId) {
        return actionRepository.findByAtm_IdAndActionTypeEqualsAndCreatedAtEquals(atmId, ActionType.CASH_TO_DIGITAL, Timestamp.valueOf(LocalDateTime.now()));
    }

    public List<Action> getRefills(UUID atmId) {
        return actionRepository.findByAtm_IdAndActionTypeEqualsAndCreatedAtEquals(atmId, ActionType.FILL, Timestamp.valueOf(LocalDateTime.now()));
    }
}
