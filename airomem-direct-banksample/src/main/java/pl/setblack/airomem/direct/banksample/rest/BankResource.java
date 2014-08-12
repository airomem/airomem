/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.direct.banksample.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

/**
 * REST Web Service
 *
 * @author jarek ratajski
 */
@Path("bank")
public class BankResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of BankResource
     */
    public BankResource() {
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
    @Consumes("application/json")
    public void putJson(String content) {
        System.out.println("test");
    }
}
