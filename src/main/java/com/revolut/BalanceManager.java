package com.revolut;

import com.revolut.Exception.InvalidOperationException;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Optional;

@ThreadSafe
public class BalanceManager {

    public Response buildResponse(RequestMessage.General message) {
        Account from = Account.getById(message.getId());
        Response response;
        switch (message.getOperation().type) {
            case DEPOSIT:
                response = () -> from.deposit(message.getOperation().money, Optional.empty());
                break;
            case WITHDRAW:
                response = () -> from.withdraw(message.getOperation().money, Optional.empty());
                break;
            case TRANSFER:
                Account to = Account.getById(((RequestMessage.Transfer) message).getIdTo());

                response = () -> {
                    Optional<Integer> toVersion = Optional.of(to.getVersion());
                    from.withdraw(message.getOperation().money, Optional.of(from.getVersion()));
                    to.deposit(message.getOperation().money, toVersion);
                };
                break;
            default:
                response = () -> {
                    throw new InvalidOperationException();
                };
        }
        return response;
    }
}
