package com.revolut;

import com.revolut.Exception.InvalidOperationException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/account")
public class AccountService {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountManager.ImmutableAccount getAccount(@PathParam("id") int id) {
        return AccountManager.getAccount(id);
    }

    @POST
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response addAccount(Operation operation){
        AccountManager.addAccount(operation.money);
        return javax.ws.rs.core.Response.status(201).build();
    }

    @PUT
    @Path("deposit/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response depositAccount(@PathParam("id") int id, Operation operation){

        BalanceManager.buildDepositResponse(id, operation.getMoney()).perform();
        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.OK).build();
    }

    @PUT
    @Path("withdraw/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response withdrawAccount(@PathParam("id") int id, Operation operation){

        BalanceManager.buildWithdrawResponse(id, operation.getMoney()).perform();
        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.OK).build();
    }

    @PUT
    @Path("transfer/{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public javax.ws.rs.core.Response depositAccount(@PathParam("from") int from, @PathParam("to") int to, Operation operation){
        BalanceManager.buildTransferResponse(from, to, operation.getMoney()).perform();
        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.OK).build();
    }
}
