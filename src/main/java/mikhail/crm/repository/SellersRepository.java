package mikhail.crm.repository;

import mikhail.crm.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellersRepository extends JpaRepository<Seller, Long> {
}

