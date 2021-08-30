package uz.pdp.bankcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.bankcard.entity.Cash;
import uz.pdp.bankcard.entity.enums.Currency;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CashRepository extends JpaRepository<Cash, UUID> {
    Optional<Cash> findByAtm_IdAndCashAmountAndCurrency(UUID atm_id, double cashAmount, Currency currency);
    List<Cash> findByAtm_Id(UUID atm_id);
}
