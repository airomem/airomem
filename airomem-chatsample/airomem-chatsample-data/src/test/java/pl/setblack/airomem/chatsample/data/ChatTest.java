/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.data;

import java.time.LocalDateTime;
import java.time.Month;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jarek ratajski
 */
public class ChatTest {

    private static final String SAMPLE_CONTENT = "sample content";

    private Chat chat;

    private LocalDateTime time;

    public ChatTest() {
    }

    @Before
    public void setUp() {
        this.chat = new Chat();
        this.time = LocalDateTime.of(2014, Month.JUNE, 21, 9, 6);
    }

    @Test
    public void testCreateChat() {
        assertNotNull(chat);
    }

    @Test
    public void testAddMessage() {
        assertEquals(0, chat.getRecentMessages().size());
        chat.addMessage("irek", SAMPLE_CONTENT, time);
        assertEquals(1, chat.getRecentMessages().size());
        assertEquals(SAMPLE_CONTENT, chat.getRecentMessages().get(0).getContent());
    }

}
