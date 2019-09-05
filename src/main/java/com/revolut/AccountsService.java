package com.revolut;

import com.revolut.Exception.InvalidOperationException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/accounts")
public class AccountsService {

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountManager.ImmutableAccount[] getAllAccounts() {
        return AccountManager.getMockAccountList().toArray(new AccountManager.ImmutableAccount[0]);
    }
}
