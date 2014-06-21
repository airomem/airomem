/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.view;

import java.time.LocalDateTime;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jarek ratajski
 */
@XmlRootElement
public interface MessageView {

    @XmlElement
    String getContent();
    
    @XmlElement
    AuthorView getAuthorView();
    
    @XmlAttribute
    LocalDateTime getTime();
    
    String getAuthorNick();
}
