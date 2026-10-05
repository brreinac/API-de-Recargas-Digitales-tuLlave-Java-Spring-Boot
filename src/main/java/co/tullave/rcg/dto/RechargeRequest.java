package co.tullave.rcg.dto;

import co.tullave.rcg.entity.PaymentMethod;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record RechargeRequest(
        @NotBlank(message = "cardNumber is required")
        @Pattern(regexp = "\\d{16}", message = "cardNumber must contain exactly 16 numeric digits")
        String cardNumber,

        @NotNull(message = "amount is required")
        @DecimalMin(value = "2000", message = "amount must be at least 2000")
        @DecimalMax(value = "200000", message = "amount must not exceed 200000")
        @Digits(integer = 6, fraction = 2, message = "amount must have at most 6 integer digits and 2 decimal places")
        BigDecimal amount,

        @NotNull(message = "paymentMethod is required")
        PaymentMethod paymentMethod
) {
}
