package com.revolut;

import com.revolut.Exception.InvalidOperationException;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class BalanceManager {

    public Response buildResponse(RequestMessage.General message) {
        Response response;
        switch (message.getOperation().type) {
            case DEPOSIT:
                response = () ->
                        AccountManager.deposit(
                                message.getId(),
                                message.getOperation().money);
                break;
            case WITHDRAW:
                response = () ->
                        AccountManager.withdraw(
                                message.getId(),
                                message.getOperation().money);
                break;
            case TRANSFER:
                RequestMessage.Transfer extendedMessage = (RequestMessage.Transfer) message;

                response = () ->
                        AccountManager.transfer(
                                extendedMessage.getId(),
                                extendedMessage.getIdTo(),
                                extendedMessage.getOperation().money);
                break;
            default:
                response = () -> {
                    throw new InvalidOperationException();
                };
        }
        return response;
    }
}
