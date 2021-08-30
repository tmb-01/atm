package uz.pdp.bankcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.bankcard.entity.Card;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    Optional<Card> findByCardNumber(String cardNumber);
}
