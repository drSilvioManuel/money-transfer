package com.revolut;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.client.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountServiceTest {

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void checkGetAll() {
        String responseMsg = getMessage("accounts/all").get(String.class);
        assertTrue(responseMsg.startsWith("[{\"id\":0,\"balance\":100.0},{\"id\":1,\"balance\":119.0},{\"id\":2,\"balance\":1000.0},{\"id\":3,\"balance\":"));
    }

    @Test
    public void checkGetById() {
        String responseMsg = getMessage("accounts/2").get(String.class);
        assertEquals("{\"id\":2,\"balance\":1000.0}", responseMsg);
    }

    @Test
    public void checkGetByWrongId() throws ExecutionException, InterruptedException {
        Invocation.Builder builder = getMessage("accounts/222");
        Invocation invocation = builder.buildGet();
        Future<javax.ws.rs.core.Response> responseFuture = invocation.submit();
        javax.ws.rs.core.Response response = responseFuture.get();

        assertEquals(404, response.getStatus());
    }

    @Test
    public void checkAddingAccount() throws ExecutionException, InterruptedException {
        Invocation.Builder builder = getMessage("accounts/add");
        Invocation invocation = builder.buildPost(Entity.json(new Operation(4, 177)));
        Future<javax.ws.rs.core.Response> responseFuture = invocation.submit();
        javax.ws.rs.core.Response response = responseFuture.get();

        assertEquals(201, response.getStatus());
    }

    @Test
    public void checkDepositAccount() throws ExecutionException, InterruptedException {
        String responseMsg = getMessage("accounts/3").get(String.class);
        assertEquals("{\"id\":3,\"balance\":1200.0}", responseMsg);

        Invocation.Builder builder = getMessage("accounts/3/update");
        Invocation invocation = builder.buildPut(Entity.json(new Operation(1, 177)));
        Future<javax.ws.rs.core.Response> responseFuture = invocation.submit();
        javax.ws.rs.core.Response response = responseFuture.get();
        System.out.println(response);

        assertEquals(200, response.getStatus());

        responseMsg = getMessage("accounts/3").get(String.class);
        assertEquals("{\"id\":3,\"balance\":1377.0}", responseMsg);
    }

    @Test
    public void checkWithdrawAccount() throws ExecutionException, InterruptedException {
        String responseMsg = getMessage("accounts/3").get(String.class);
        assertEquals("{\"id\":3,\"balance\":1377.0}", responseMsg);

        Invocation.Builder builder = getMessage("accounts/3/update");
        Invocation invocation = builder.buildPut(Entity.json(new Operation(2, 177)));
        Future<javax.ws.rs.core.Response> responseFuture = invocation.submit();
        javax.ws.rs.core.Response response = responseFuture.get();
        System.out.println(response);

        assertEquals(200, response.getStatus());

        responseMsg = getMessage("accounts/3").get(String.class);
        assertEquals("{\"id\":3,\"balance\":1200.0}", responseMsg);
    }

    @Test
    public void checkTransfer() throws ExecutionException, InterruptedException {
        String responseMsg = getMessage("accounts/1").get(String.class);
        assertEquals("{\"id\":1,\"balance\":119.0}", responseMsg);

        responseMsg = getMessage("accounts/2").get(String.class);
        assertEquals("{\"id\":2,\"balance\":1000.0}", responseMsg);

        Invocation.Builder builder = getMessage("accounts/1/transfer/2");
        Invocation invocation = builder.buildPut(Entity.json(new Operation(3, 19)));
        Future<javax.ws.rs.core.Response> responseFuture = invocation.submit();
        javax.ws.rs.core.Response response = responseFuture.get();
        System.out.println(response);

        assertEquals(200, response.getStatus());

        responseMsg = getMessage("accounts/1").get(String.class);
        assertEquals("{\"id\":1,\"balance\":100.0}", responseMsg);

        responseMsg = getMessage("accounts/2").get(String.class);
        assertEquals("{\"id\":2,\"balance\":1019.0}", responseMsg);
    }

    private Invocation.Builder getMessage(String path) {
        return target.path(path).request();
    }
}
