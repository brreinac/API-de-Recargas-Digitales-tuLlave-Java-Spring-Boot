package co.tullave.rcg.mapper;

import co.tullave.rcg.dto.RechargeRequest;
import co.tullave.rcg.dto.RechargeResponse;
import co.tullave.rcg.entity.Recharge;
import org.springframework.stereotype.Component;

@Component
public class RechargeMapper {

    public Recharge toEntity(RechargeRequest request) {
        Recharge recharge = new Recharge();
        recharge.setCardNumber(request.cardNumber());
        recharge.setAmount(request.amount());
        recharge.setPaymentMethod(request.paymentMethod());
        return recharge;
    }

    public RechargeResponse toResponse(Recharge recharge) {
        return new RechargeResponse(
                recharge.getId(),
                recharge.getCardNumber(),
                recharge.getAmount(),
                recharge.getPaymentMethod(),
                recharge.getCreatedAt()
        );
    }
}
