/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.data;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.setblack.airomem.chatsample.view.MessageView;
import pl.setblack.airomem.core.PrevalanceContext;
import pl.setblack.airomem.core.WriteChecker;

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
        this.time = LocalDateTime.now();
        WriteChecker.setContext(Mockito.mock(PrevalanceContext.class));

    }

    @After
    public void tearDown() {
        WriteChecker.clearContext();
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

    @Test
    public void testGetLast10Messages() {
        assertEquals(0, chat.getRecentMessages().size());
        for (int i = 0; i < 100; i++) {
            chat.addMessage("irek", SAMPLE_CONTENT + i, time);
        }
        List<MessageView> messages = chat.getRecentMessages();
        assertTrue(messages.size() <= 10);
        assertEquals(SAMPLE_CONTENT + 99, messages.get(9).getContent());
    }

}
