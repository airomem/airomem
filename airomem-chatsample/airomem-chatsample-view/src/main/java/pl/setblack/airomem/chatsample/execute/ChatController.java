/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.execute;

import javax.ejb.Local;
import pl.setblack.airomem.chatsample.view.ChatView;

/**
 *
 * @author jarek ratajski
 */
@Local
public interface ChatController {

    ChatView getChatView();

    void addMessage(String nick, String message);

}
