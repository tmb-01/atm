package uz.pdp.bankcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.bankcard.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
