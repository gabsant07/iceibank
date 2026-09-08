package aula.iceibank.repository;

import aula.iceibank.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, UUID> {
    List<BankTransaction> findBySourceAccountOrDestinationAccountOrderByCreatedAtDesc(Long source, Long destination);
}
