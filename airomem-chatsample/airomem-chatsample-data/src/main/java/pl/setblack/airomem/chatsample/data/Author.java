/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import pl.setblack.airomem.chatsample.view.AuthorView;

/**
 *
 * @author jarekr
 */
@XmlRootElement
public class Author implements AuthorView, Serializable {

    private static final long serialVersionUID = 1;

    public String nickName;

    public Author() {

    }

    Author(String nick) {
        this.nickName = nick;
    }

    @XmlAttribute
    @Override
    public String getNickName() {
        return nickName;
    }
}
