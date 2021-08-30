package uz.pdp.bankcard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import uz.pdp.bankcard.entity.enums.RolesName;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue
    private Long id;

    private RolesName rolesName;

    @JsonIgnore
    @ManyToMany(mappedBy = "role")
    private List<User> user;

    @Override
    public String getAuthority() {
        return rolesName.name();
    }
}
