package liq_msa_bp_customer.infrastructure.persistence.jpa;

import liq_msa_bp_customer.infrastructure.repository.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
    List<CustomerEntity> findByIdCompany(String idCompany);
    void deleteById(Long id);

}
