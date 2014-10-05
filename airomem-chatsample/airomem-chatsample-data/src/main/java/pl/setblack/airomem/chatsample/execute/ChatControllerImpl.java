/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.execute;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import pl.setblack.airomem.chatsample.data.Chat;
import pl.setblack.airomem.chatsample.view.ChatView;
import pl.setblack.airomem.chatsample.view.MessageView;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.builders.PersistenceFactory;
import pl.setblack.airomem.data.DataRoot;

/**
 *
 * @author jarekr
 */
@ApplicationScoped
public class ChatControllerImpl implements ChatController {

    private PersistenceController<DataRoot<ChatView, Chat>, ChatView> controller;

    @PostConstruct
    void initController() {
        final PersistenceFactory factory = new PersistenceFactory();
        controller = factory.initOptional("chat", () -> new DataRoot<>(new Chat()));
    }

    @Override
    public void addMessage(String nick, String message) {
        controller.execute((chat, ctx) -> {

            chat.getDataObject().addMessage(nick, message, LocalDateTime.ofInstant(ctx.time, ZoneId.systemDefault()));
            if (nick.equals("badguy")) {
                throw new SecurityException();
            }
        });
    }

    @Override
    public List<MessageView> getRecentMessages() {
        return controller.query((view) -> view.getRecentMessages());
    }
}
