/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.data;

import java.io.Serializable;
import pl.setblack.airomem.chatsample.view.AuthorView;

/**
 *
 * @author jarekr
 */
public class Author implements AuthorView, Serializable {

    public String nickName;

    Author(String nick) {
        this.nickName = nick;
    }

    @Override
    public String getNickName() {
        return nickName;
    }
}
