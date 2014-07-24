/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.data;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import javax.xml.bind.annotation.XmlElement;
import pl.setblack.airomem.chatsample.view.AuthorView;
import pl.setblack.airomem.chatsample.view.MessageView;

/**
 *
 * @author jarekr
 */
public class Message implements MessageView, Serializable {

    private static final long serialVersionUID = 1;

    public static final SimpleAttribute<Message, ChronoLocalDateTime> MESSAGE_TIME
            = new SimpleAttribute<Message, ChronoLocalDateTime>() {
                @Override
                public ChronoLocalDateTime getValue(Message msg) {
                    return msg.getTime();
                }
            };

    private final Author author;

    private final String content;

    private final LocalDateTime time;

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
