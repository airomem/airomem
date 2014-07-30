/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.execute;

import java.util.List;
import javax.ejb.Local;
import pl.setblack.airomem.chatsample.view.ChatView;
import pl.setblack.airomem.chatsample.view.MessageView;

/**
 *
 * @author jarek ratajski
 */
@Local
public interface ChatController {

    void addMessage(String nick, String message);

    List<MessageView> getRecentMessages();

}
