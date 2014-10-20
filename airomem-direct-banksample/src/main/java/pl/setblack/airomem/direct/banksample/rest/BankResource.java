/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.rest;

import java.math.BigDecimal;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.setblack.airomem.direct.Persistent;
import pl.setblack.airomem.direct.PersistentObject;
import pl.setblack.airomem.direct.banksample.domain.Account;
import pl.setblack.airomem.direct.banksample.domain.Bank;

/**
 * REST Web Service
 *
 * @author jarek ratajski
 */
@Path("bank")
@Persistent
public class BankResource {

    @PersistentObject
    private Bank bank;

    public BankResource() {

    }

    
    @GET
    @Path("/account/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@PathParam("id") final String id) {
        return this.bank.getAccount(id);
    }

    @POST
    @Path("/account")
    @Produces(MediaType.APPLICATION_JSON)
    public Account putAccount() {
        return this.bank.registerNewAccount(BigDecimal.ZERO);

    }

    @PUT
    @Path("/account/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account deposit(@PathParam("id") final String id, String value) {
        return this.bank.change(id, new BigDecimal(value));

    }

}
