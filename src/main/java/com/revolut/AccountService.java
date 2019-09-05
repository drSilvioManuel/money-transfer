package com.revolut;

import com.revolut.Exception.InvalidOperationException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/accounts")
public class AccountService {

    private final BalanceManager balanceManager = new BalanceManager();

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountManager.ImmutableAccount[] getAllAccounts() {
        return AccountManager.getMockAccountList().toArray(new AccountManager.ImmutableAccount[0]);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountManager.ImmutableAccount getAccount(@PathParam("id") int id) {
        return AccountManager.getAccount(id);
    }

    @POST
    @Path("add")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response addAccount(Operation operation){
        AccountManager.addAccount(operation.money);
        return javax.ws.rs.core.Response.status(201).build();
    }

    @PUT
    @Path("{id}/update")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response updateAccount(@PathParam("id") int id, Operation operation){
        if (operation.type != Operation.TYPE.DEPOSIT && operation.type != Operation.TYPE.WITHDRAW)
            throw InvalidOperationException.createWrongTypeOperationForUpdate();
        balanceManager.buildResponse(new RequestMessage.Update(id, operation)).perform();
        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.OK).build();
    }

    @PUT
    @Path("{id}/transfer/{idTo}")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response depositAccount(@PathParam("id") int from, @PathParam("idTo") int to, Operation operation){
        balanceManager.buildResponse(
                new RequestMessage.Transfer(from, to, new Operation(Operation.TYPE.TRANSFER.getId(), operation.money))
        ).perform();
        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.OK).build();
    }
}
