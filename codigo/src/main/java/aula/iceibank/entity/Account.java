package aula.iceibank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private Long accountNumber;
    private String holderName;
    private BigDecimal balance;
    private Integer agencyId;
    private Instant createdAt;

    @Version
    private Long version;

    protected Account() {
    }

    public Account(Long accountNumber, String holderName, BigDecimal balance, Integer agencyId) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
        this.agencyId = agencyId;
        this.createdAt = Instant.now();
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Integer getAgencyId() {
        return agencyId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        balance = balance.subtract(amount);
    }
}
