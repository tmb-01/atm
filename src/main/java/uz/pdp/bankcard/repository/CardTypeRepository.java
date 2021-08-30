package uz.pdp.bankcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.bankcard.entity.CardType;

public interface CardTypeRepository extends JpaRepository<CardType, Integer> {
}
