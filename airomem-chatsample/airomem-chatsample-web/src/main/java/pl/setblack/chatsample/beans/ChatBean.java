/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.chatsample.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import pl.setblack.airomem.chatsample.data.Chat;
import pl.setblack.airomem.chatsample.view.ChatView;
import pl.setblack.airomem.core.PersistenceController;
import pl.setblack.airomem.core.PersistenceFactory;
import pl.setblack.airomem.data.DataRoot;

/**
 *
 * @author jarekr
 */
@ApplicationScoped
public class ChatBean {

    private PersistenceController<DataRoot<ChatView, Chat>, ChatView> controller;

    @PostConstruct
    void initController() {
        final PersistenceFactory factory = new PersistenceFactory();
        controller = factory.initOptional("chat", () -> new DataRoot<>(new Chat()));
    }

    public ChatView getChatView() {
        return controller.query((view) -> view);
    }
}
