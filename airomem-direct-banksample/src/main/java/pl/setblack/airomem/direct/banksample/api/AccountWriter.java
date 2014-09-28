/*
 *  Copyright (c) Jarek Ratajski, Licensed under the Apache License, Version 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 */
package pl.setblack.airomem.direct.banksample.api;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.jettison.JettisonJaxbContext;
import pl.setblack.airomem.direct.banksample.domain.Account;

/**
 *
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class AccountWriter implements MessageBodyWriter<Account> {

    @Inject
    private AccountAdapter accountAdapter;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Account.class;
    }

    @Override
    public long getSize(Account t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(Account t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {

        try {

            JettisonJaxbContext jaxbContext = new JettisonJaxbContext(AccountDto.class);

            jaxbContext.createJsonMarshaller().marshallToJSON(accountAdapter.marshal(t), entityStream);
        } catch (Exception jaxbException) {
            throw new ProcessingException(
                    "Error serializing a MyBean to the output stream", jaxbException);
        }
    }

}
