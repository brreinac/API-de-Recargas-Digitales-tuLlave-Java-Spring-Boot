package co.tullave.rcg.exception;

public class RechargeNotFoundException extends RuntimeException {

    public RechargeNotFoundException(Long id) {
        super("Recharge with id " + id + " was not found");
    }
}
