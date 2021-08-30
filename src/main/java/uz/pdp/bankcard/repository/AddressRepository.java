package uz.pdp.bankcard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.bankcard.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
