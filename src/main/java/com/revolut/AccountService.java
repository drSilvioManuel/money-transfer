package com.revolut;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/accounts")
public class AccountService {

    private final BalanceManager balanceManager = new BalanceManager();

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Account[] getAllAccounts() {
        return Account.getMockAccountList().toArray(new Account[0]);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@PathParam("id") int id) {
        return Account.getById(id);
    }

    @POST
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response addAccount(Operation operation){
        new Account(operation.money);
        return javax.ws.rs.core.Response.status(201).build();
    }

    @PUT
    @Path("{id}/update")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response updateAccount(@PathParam("id") int id, Operation operation){
        balanceManager.buildResponse(new RequestMessage.Update(id, operation)).perform();
        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.OK).build();
    }

    @PUT
    @Path("{id}/transfer/{idTo}")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response depositAccount(@PathParam("id") int from, @PathParam("idTo") int to, Operation operation){
        balanceManager.buildResponse(new RequestMessage.Transfer(from, to, operation)).perform();
        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.OK).build();
    }
}
