/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.chatsample.web;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import pl.setblack.airomem.chatsample.view.MessageView;
import pl.setblack.chatsample.beans.ChatBean;

/**
 * REST Web Service
 *
 * @author jarek ratajski
 */
@Path("chat")
@RequestScoped
public class ChatService {

    @Context
    private UriInfo context;

    @Inject
    private ChatBean chatBean;

    /**
     * Creates a new instance of ChatService
     */
    public ChatService() {
    }

    /**
     * Retrieves representation of an instance of
     * pl.setblack.chatsample.web.ChatService
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public List<MessageView> getMessages() {
        
        return chatBean.getChatView().getRecentMessages();
    }

    /**
     * PUT method for updating or creating an instance of ChatService
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(IncomingMessage msg) {
        chatBean.addMessage(msg.nick, msg.content);
    }
}
