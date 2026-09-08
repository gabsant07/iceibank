package aula.iceibank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull Long sourceAccount,
        @NotNull Long destinationAccount,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount
) {
}
