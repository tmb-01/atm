package uz.pdp.bankcard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.bankcard.entity.ATM;
import uz.pdp.bankcard.entity.Cash;
import uz.pdp.bankcard.entity.Role;
import uz.pdp.bankcard.entity.User;
import uz.pdp.bankcard.entity.enums.RolesName;
import uz.pdp.bankcard.payload.ApiResponse;
import uz.pdp.bankcard.repository.ATM_Repository;
import uz.pdp.bankcard.repository.CardRepository;
import uz.pdp.bankcard.repository.CashRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ATM_Service {

    @Autowired
    ATM_Repository atmRepository;

    @Autowired
    CashRepository cashRepository;

    public List<ATM> moneyOfAtms() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean allow = false;
        for (Role role : user.getRole()) {
            if (role.getRolesName().equals(RolesName.BANK_DIRECTOR)) {
                allow = true;
            }
        }

        if (allow) {
            List<ATM> atmList = new ArrayList<>();
            List<UUID> onlyIdsOfATM = atmRepository.findOnlyIdsOfATM();

            for (UUID uuid : onlyIdsOfATM) {
                float totalAmount = 0;
                List<Cash> cashList = cashRepository.findByAtm_Id(uuid);
                for (Cash cash : cashList) {
                    totalAmount = cash.getTotalAmount();
                }
                ATM atm = atmRepository.findById(uuid).get();
                atm.setTotalAmount(totalAmount);
                atmList.add(atm);
            }
            return atmList;
        }
        return null;
    }
}
