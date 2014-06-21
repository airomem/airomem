/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.view;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jarek ratajski
 */
@XmlRootElement
public interface AuthorView {

    @XmlAttribute
    String getNickName();
}
