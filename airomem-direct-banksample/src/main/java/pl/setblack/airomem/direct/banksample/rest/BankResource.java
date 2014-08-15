/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.rest;

import java.math.BigDecimal;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import pl.setblack.airomem.direct.banksample.api.AccountAdapter;
import pl.setblack.airomem.direct.banksample.api.AccountDto;
import pl.setblack.airomem.direct.banksample.domain.Bank;

/**
 * REST Web Service
 *
 * @author jarek ratajski
 */
@Path("bank")
public class BankResource {

    @Context
    private UriInfo context;

    private Bank bank;

    /**
     * Creates a new instance of BankResource
     */
    public BankResource() {
        this.bank = new Bank();
    }

    /**
     * Retrieves representation of an instance of
     * pl.setblack.airomem.direct.banksample.BankResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        return "test";
    }

    /**
     * PUT method for updating or creating an instance of BankResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Path("/account")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDto putAccount() {
        final AccountAdapter adapter = new AccountAdapter();
        try {
            return adapter.marshal(this.bank.registerNewAccount(BigDecimal.ZERO));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
