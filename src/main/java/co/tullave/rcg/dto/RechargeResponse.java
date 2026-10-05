package co.tullave.rcg.dto;

import co.tullave.rcg.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RechargeResponse(
        Long id,
        String cardNumber,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        LocalDateTime createdAt
) {
}
