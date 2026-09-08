package aula.iceibank.repository;

import aula.iceibank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByAgencyId(Integer agencyId);
}
