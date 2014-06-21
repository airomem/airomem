/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.data;

import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlElement;
import pl.setblack.airomem.chatsample.view.AuthorView;
import pl.setblack.airomem.chatsample.view.MessageView;

/**
 *
 * @author jarekr
 */
public class Message implements MessageView {

    private final Author author;

    private String content;

    private LocalDateTime time;

    public Message(Author author, String content, LocalDateTime time) {
        this.author = author;
        this.content = content;
        this.time = time;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public LocalDateTime getTime() {
        return time;
    }

    @Override
    @XmlElement
    public AuthorView getAuthorView() {
        return this.author;
    }

    @Override
    public String getAuthorNick() {
        return this.author.getNickName();
    }

}
