package aula.iceibank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotNull Long accountNumber,
        @NotBlank String holderName,
        @NotNull @DecimalMin(value = "0.00") BigDecimal initialBalance
) {
}
