/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import pl.setblack.airomem.chatsample.view.ChatView;
import pl.setblack.airomem.chatsample.view.MessageView;
import pl.setblack.airomem.core.WriteChecker;

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
        assert WriteChecker.hasPrevalanceContext();
        final Author author = new Author(nick);
        final Message msg = new Message(author, content, time);
        this.messages.add(msg);

    }

    @Override
    public List<MessageView> getRecentMessages() {
        final List<MessageView> res = this.messages.stream()
                .limit(10)
                .collect(Collectors.toList());
        return res;
    }
}
