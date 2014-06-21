/*
 *  Created by Jarek Ratajski
 */
package pl.setblack.airomem.chatsample.data;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jarek ratajski
 */
public class AuthorTest {

    public AuthorTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testGetNickName() {

        Author author = new Author("Czeslaw");
        assertEquals("Czeslaw", author.getNickName());
    }

}
