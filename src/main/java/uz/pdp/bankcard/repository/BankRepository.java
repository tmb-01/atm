package uz.pdp.bankcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.bankcard.entity.Bank;

import java.util.UUID;

public interface BankRepository extends JpaRepository<Bank, UUID> {
}
