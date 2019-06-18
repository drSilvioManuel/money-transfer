package com.remolut;

import com.remolut.Exception.InvalidOperationException;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class BalanceManager {

    public Response buildResponse(RequestMessage.General message) {
        Account from = Account.getById(message.getId());
        Response response;
        switch (message.getOperation().type) {
            case DEPOSIT:
                response = () -> from.deposit(message.getOperation().money);
                break;
            case WITHDRAW:
                response = () -> from.withdraw(message.getOperation().money);
                break;
            case TRANSFER:
                Account to = Account.getById(((RequestMessage.Transfer) message).getIdTo());

                response = () -> {
                    from.withdraw(message.getOperation().money);
                    to.deposit(message.getOperation().money);
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
