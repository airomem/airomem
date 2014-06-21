/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.view;

import java.time.LocalDateTime;

/**
 *
 * @author jarek ratajski
 */
public interface MessageView {

    String getContent();

    AuthorView getAuthorView();

    LocalDateTime getTime();
}
