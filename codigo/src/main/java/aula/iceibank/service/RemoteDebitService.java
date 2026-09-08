package aula.iceibank.service;

import aula.iceibank.entity.Account;
import aula.iceibank.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class RemoteDebitService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public RemoteDebitService(AccountRepository accountRepository, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void debitCommitted(long accountNumber, BigDecimal amount) {
        Account source = accountService.getLocal(accountNumber);
        accountService.ensureFunds(source, amount);
        source.debit(amount);
        accountRepository.save(source);
    }
}
