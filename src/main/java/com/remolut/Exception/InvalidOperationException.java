package com.remolut.Exception;

import com.remolut.JsonError;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class InvalidOperationException  extends WebApplicationException {
    public InvalidOperationException() {
        super(Response.status(Response.Status.NOT_IMPLEMENTED).type(MediaType.TEXT_PLAIN).build());
    }

    public InvalidOperationException(String message) {
        super(Response.status(Response.Status.PRECONDITION_FAILED).entity(message).type(MediaType.TEXT_PLAIN).build());
    }

    public InvalidOperationException(JsonError jse) {
        super(Response.status(Response.Status.PRECONDITION_FAILED).entity(jse).type(MediaType.APPLICATION_JSON).build());
    }

    public InvalidOperationException(JsonError jse, Response.Status status) {
        super(Response.status(status).entity(jse).type(MediaType.APPLICATION_JSON).build());
    }

    public static InvalidOperationException createDepositNegativeAmount() {
        return new InvalidOperationException(
                new JsonError(
                        "Deposit error",
                        "The money amount has to be positive value"),
                Response.Status.NOT_ACCEPTABLE);
    }

    public static InvalidOperationException createDepositBalanceLessThanWithdraw() {
        return new InvalidOperationException(
                new JsonError(
                        "Deposit error",
                        "The money are not enough to perform operation"),
                Response.Status.PAYMENT_REQUIRED);
    }
}
