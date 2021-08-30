package uz.pdp.bankcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.bankcard.entity.Action;
import uz.pdp.bankcard.entity.enums.ActionType;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface ActionRepository extends JpaRepository<Action, Long> {
    List<Action> findByAtm_Id(UUID atm_id);

    List<Action> findByAtm_IdAndActionTypeEqualsAndCreatedAtEquals(UUID atm_id, ActionType actionType, Timestamp createdAt);
//    ActionTypeList<Action> findByAtm_IdAndActionTypeEqualsAndCreatedAtEquals(UUID atm_id, ActionType actionType, Timestamp createdAt);

}
