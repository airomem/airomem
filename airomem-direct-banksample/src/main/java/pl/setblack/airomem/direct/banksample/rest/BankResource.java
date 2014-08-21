/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.rest;

import java.math.BigDecimal;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.setblack.airomem.direct.Persistent;
import pl.setblack.airomem.direct.PersistentObject;
import pl.setblack.airomem.direct.banksample.api.AccountAdapter;
import pl.setblack.airomem.direct.banksample.api.AccountDto;
import pl.setblack.airomem.direct.banksample.api.BankAdapter;
import pl.setblack.airomem.direct.banksample.api.BankDto;
import pl.setblack.airomem.direct.banksample.domain.Bank;
import pl.setblack.badass.Politician;

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

    @Inject
    private BankAdapter bankAdapter;

    @Inject
    private AccountAdapter accountAdapter;

    public BankResource() {

    }

    @GET
    @Path("/bank")
    @Produces(MediaType.APPLICATION_JSON)
    public BankDto getJson() {
        return Politician.beatAroundTheBush(() -> bankAdapter.marshal(this.bank));
    }

    @PUT
    @Path("/account")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDto putAccount() {
        return Politician.beatAroundTheBush(()
                -> accountAdapter.marshal(this.bank.registerNewAccount(BigDecimal.ZERO)));

    }

}
