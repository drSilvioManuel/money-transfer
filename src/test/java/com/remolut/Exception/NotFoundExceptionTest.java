package com.remolut.Exception;

import com.remolut.JsonError;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static junit.framework.TestCase.assertEquals;

public class NotFoundExceptionTest {

    @Test
    public void checkDefaultConstructor() {
        NotFoundException e = new NotFoundException();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), e.getResponse().getStatus());
    }

    @Test
    public void checkStringConstructor() {
        NotFoundException e = new NotFoundException("message");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), e.getResponse().getStatus());
        assertEquals("message", e.getResponse().getEntity());
    }

    @Test
    public void checkJseConstructor() {
        JsonError jsonError = new JsonError("Type", "Message");
        NotFoundException e = new NotFoundException(jsonError);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), e.getResponse().getStatus());
        assertEquals(jsonError, e.getResponse().getEntity());
    }
}
