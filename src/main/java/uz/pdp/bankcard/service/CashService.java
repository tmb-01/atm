package uz.pdp.bankcard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.bankcard.entity.*;
import uz.pdp.bankcard.entity.enums.ActionType;
import uz.pdp.bankcard.entity.enums.RolesName;
import uz.pdp.bankcard.exception.MoneyInsufficientException;
import uz.pdp.bankcard.exception.MoneyTypeException;
import uz.pdp.bankcard.repository.ATM_Repository;
import uz.pdp.bankcard.repository.CardRepository;
import uz.pdp.bankcard.repository.CashRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CashService {
    @Autowired
    CashRepository cashRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ATM_Repository atmRepository;

    @Autowired
    JavaMailSender javaMailSender;

    public List<Cash> addMoneyByATMId(UUID atmId, List<Cash> cashes) {

        List<Cash> cashList = new ArrayList<>();

        for (Cash cash : cashes) {
            Optional<Cash> optionalCash = cashRepository.findByAtm_IdAndCashAmountAndCurrency(atmId, cash.getCashAmount(), cash.getCurrency());

            if (optionalCash.isPresent()) {
                Cash cashData = optionalCash.get();
                long howMany = cashData.getHowMany();
                cashData.setHowMany(howMany + cash.getHowMany());
                Cash save = cashRepository.save(cashData);
                cashList.add(save);
            } else {
                Cash save = cashRepository.save(cash);
                cashList.add(save);
            }
        }
        return cashList;
    }

    @Transactional
    public Action withdrawMoney(UUID atmId, List<Cash> cashList) throws MoneyInsufficientException {

        List<Cash> cashes = new ArrayList<>();
        Card card = (Card) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ATM atm = atmRepository.findById(atmId).get();

        for (Cash cash : cashList) {
            Optional<Cash> optionalCash = cashRepository.findByAtm_IdAndCashAmountAndCurrency(atmId, cash.getCashAmount(), cash.getCurrency());
            if (optionalCash.isPresent()) {
                Cash cashData = optionalCash.get();
                if (cash.getHowMany() <= cashData.getHowMany()) {
                    cashData.setHowMany(cashData.getHowMany() - cash.getHowMany());
                    Cash save = cashRepository.save(cashData);
                    cashes.add(save);
                } else {
                    throw new MoneyInsufficientException("ATM doesn't have enough money " + cash.getCurrency().name() + " " + cash.getCashAmount());
                }
            }
        }

        List<Cash> cashOfAtm = cashRepository.findByAtm_Id(atmId);

        if (!cashOfAtm.isEmpty()) {

            float totalAmount = 0;

            for (Cash cash : cashOfAtm) {
                totalAmount += cash.getTotalAmount();
            }

//            agar  pul kam qosa masul odamga email junatadi
            if (totalAmount <= atm.getMinValue()) {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom("atm@gmail.com");

                String email = "";

                for (User user : atm.getBank().getUser()) {
                    for (Role role : user.getRole()) {
                        if (role.getRolesName().equals(RolesName.ATM_REFILL)) {
                            email = user.getEmail();
                        }
                    }
                }
                mailMessage.setTo(email);
                mailMessage.setSubject("refill atm");
                mailMessage.setText("refill atm because it is running out of money");
                javaMailSender.send(mailMessage);
            }
        }

//        qancha pul yechish kerak ligin hisoblash
        float totalAmount = 0;
        for (Cash cash : cashes) {
            totalAmount += cash.getTotalAmount();
        }
//        komissani hisoblash
        float commission = 0;
        if (card.getBank() == atm.getBank()) {
            commission = (float) (totalAmount / 100 * atm.getCommissionPercent());
        } else {
            commission = (float) (totalAmount / 100 * atm.getCommissionPercentForSameBankCard());
        }

//        plastikdan pul yechadi commisiyasi bilan
        totalAmount += commission;
        if (card.getTotalAmount() >= totalAmount) {
            card.setTotalAmount(card.getTotalAmount() - totalAmount);
            cardRepository.save(card);
        } else {
            throw new MoneyInsufficientException("You don't have enough money");
        }


        Action action = new Action();
        action.setActionType(ActionType.DIGITAL_TO_CASH);
        action.setCard(card);
        action.setAtm(atm);
        action.setCommissionAmount(commission);
        action.setCash(cashes);

        return action;
    }

    @Transactional
    public List<Cash> cashToCard(UUID atmId, List<Cash> cashList) throws MoneyTypeException {
        List<Cash> cashData = new ArrayList<>();
        for (Cash cash : cashList) {

            Optional<Cash> optionalCash = cashRepository.findByAtm_IdAndCashAmountAndCurrency(atmId, cash.getCashAmount(), cash.getCurrency());

            if (optionalCash.isPresent()) {
                Cash cash1 = optionalCash.get();
                cash1.setHowMany(cash1.getHowMany() + cash.getHowMany());

                Cash save = cashRepository.save(cash1);
                cashData.add(save);
            } else {
                throw new MoneyTypeException("ATM doesn't have this kind of money");
            }
        }

        return cashData;
    }
}
