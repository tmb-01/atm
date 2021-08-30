package uz.pdp.bankcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.bankcard.entity.ATM;

import java.util.List;
import java.util.UUID;

public interface ATM_Repository extends JpaRepository<ATM, UUID> {

    @Query("select atm.id from ATM as atm group by atm.id")
    List<UUID> findOnlyIdsOfATM();
}
