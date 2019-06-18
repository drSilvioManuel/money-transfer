package com.revolut.Exception;

import com.revolut.JsonError;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static junit.framework.TestCase.assertEquals;

public class InvalidOperationExceptionTest {

    @Test
    public void checkDefaultConstructor() {
        InvalidOperationException e = new InvalidOperationException();

        assertEquals(Response.Status.NOT_IMPLEMENTED.getStatusCode(), e.getResponse().getStatus());
    }

    @Test
    public void checkStringConstructor() {
        InvalidOperationException e = new InvalidOperationException("message");

        assertEquals(Response.Status.PRECONDITION_FAILED.getStatusCode(), e.getResponse().getStatus());
        assertEquals("message", e.getResponse().getEntity());
    }

    @Test
    public void checkJseConstructor() {
        JsonError jsonError = new JsonError("Type", "Message");
        InvalidOperationException e = new InvalidOperationException(jsonError);

        assertEquals(Response.Status.PRECONDITION_FAILED.getStatusCode(), e.getResponse().getStatus());
        assertEquals(jsonError, e.getResponse().getEntity());
    }

    @Test
    public void checkJseAndStatusConstructor() {
        JsonError jsonError = new JsonError("Type", "Message");
        InvalidOperationException e = new InvalidOperationException(jsonError, Response.Status.NOT_IMPLEMENTED);

        assertEquals(Response.Status.NOT_IMPLEMENTED.getStatusCode(), e.getResponse().getStatus());
        assertEquals(jsonError, e.getResponse().getEntity());
    }

    @Test
    public void checkCreateDepositNegativeAmount() {
        JsonError jsonError = new JsonError("Deposit error", "The money amount has to be positive value");
        InvalidOperationException e = InvalidOperationException.createDepositNegativeAmount();

        assertEquals(Response.Status.NOT_ACCEPTABLE.getStatusCode(), e.getResponse().getStatus());
        assertEquals(jsonError, e.getResponse().getEntity());
    }

    @Test
    public void checkCreateDepositBalanceLessThanWithdraw() {
        JsonError jsonError = new JsonError("Deposit error", "The money are not enough to perform operation");
        InvalidOperationException e = InvalidOperationException.createDepositBalanceLessThanWithdraw();

        assertEquals(Response.Status.PAYMENT_REQUIRED.getStatusCode(), e.getResponse().getStatus());
        assertEquals(jsonError, e.getResponse().getEntity());
    }
}
