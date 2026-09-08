package aula.iceibank.dto;

import aula.iceibank.entity.Account;

import java.math.BigDecimal;
import java.time.Instant;

public record AccountResponse(
        Long accountNumber,
        String holderName,
        BigDecimal balance,
        Integer agencyId,
        Instant createdAt
) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getAccountNumber(), account.getHolderName(), account.getBalance(),
                account.getAgencyId(), account.getCreatedAt());
    }
}
