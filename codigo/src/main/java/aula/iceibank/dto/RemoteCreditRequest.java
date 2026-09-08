package aula.iceibank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record RemoteCreditRequest(
        @NotNull UUID transactionId,
        @NotNull Long sourceAccount,
        @NotNull Long destinationAccount,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        int sourceAgency,
        long lamportTimestamp
) {
}
