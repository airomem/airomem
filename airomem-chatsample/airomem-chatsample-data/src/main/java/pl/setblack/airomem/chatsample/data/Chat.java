/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import pl.setblack.airomem.chatsample.view.ChatView;
import pl.setblack.airomem.chatsample.view.MessageView;

/**
 *
 * @author jarekr
 */
public class Chat implements ChatView, Serializable {

    private CopyOnWriteArrayList<Message> messages;

    public Chat() {
        this.messages = new CopyOnWriteArrayList<>();
    }

    public void addMessage(String nick, String content, LocalDateTime time) {
        final Author author = new Author(nick);
        final Message msg = new Message(author, content, time);
        this.messages.add(msg);

    }

    @Override
    public List<MessageView> getRecentMessages() {
        int size = this.messages.size();
        final List<MessageView> res = Collections.unmodifiableList(this.messages);
        return res.subList(Math.max(size - 10, 0), size);
    }
}
