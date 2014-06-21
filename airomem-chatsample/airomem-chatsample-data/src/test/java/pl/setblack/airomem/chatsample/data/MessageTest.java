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
 * @author Kanapka
 */
public class MessageTest {

    private static final String SAMPLE_MESSAGE_CONTENT = "Sample message content";

    private Author fakeAuthor;
    private LocalDateTime sampleTime;

    public MessageTest() {
    }

    @Before
    public void setUp() {
        fakeAuthor = new Author("fakeOne");
        sampleTime = LocalDateTime.of(1977, Month.MAY, 20, 9, 36);
    }

    @Test
    public void testGetContent() {
        final Message msg = new Message(fakeAuthor, SAMPLE_MESSAGE_CONTENT, sampleTime);
        assertEquals(SAMPLE_MESSAGE_CONTENT, msg.getContent());
    }

    @Test
    public void testGetTime() {
        final Message msg = new Message(fakeAuthor, SAMPLE_MESSAGE_CONTENT, sampleTime);
        assertEquals(sampleTime, msg.getTime());
    }

}
