package uz.pdp.bankcard.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.bankcard.entity.enums.ActionType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Action {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private ActionType actionType; // pul olingan (WITHDRAW) yoki quyilganligi (FILL)

    @Column(unique = true, nullable = false)
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Cash> cash; // pull miqdori

    @ManyToOne
    private Card card; // pul olinsa (withdraw) card malumtlari saqlanadi

    @ManyToOne
    private User user; // pul tuldirilsa, pul tuldirilgan user malumotlari saqlanadi

    @ManyToOne
    private ATM atm;

    @CreationTimestamp
    private Timestamp createdAt;

    private double commissionAmount;

}
