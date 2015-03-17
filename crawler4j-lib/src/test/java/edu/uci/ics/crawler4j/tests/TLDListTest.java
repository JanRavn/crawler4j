package edu.uci.ics.crawler4j.tests;

import edu.uci.ics.crawler4j.url.TLDList;
import org.junit.Test;

import static org.junit.Assert.*;

public class TLDListTest {
    @Test
    public void testTLD() {
        assertEquals("example.com", TLDList.domain("example.com"));
        assertEquals("example.com", TLDList.domain("test.example.com"));
        assertEquals("example.com", TLDList.domain("test2.test.example.com"));
        assertEquals("example.com", TLDList.domain("test3.test2.test.example.com"));
        assertEquals("example.ac.jp", TLDList.domain("www.example.ac.jp"));
        assertEquals("example.ac.jp", TLDList.domain("example.ac.jp"));
        assertEquals("ravn.co.uk", TLDList.domain("www.ravn.co.uk"));
    }

    @Test
    public void testIPAddress() {
        assertEquals("", TLDList.domain("192.168.122.215"));
    }

    @Test
    public void testIllegalAddress() {
        assertEquals("", TLDList.domain(""));
        assertEquals("", TLDList.domain("$^%^&^&"));
    }

    @Test
    public void testNoDomain() {
        assertEquals("", TLDList.domain("intranet"));
    }

    @Test
    public void testInternalDomain() {
        assertEquals("local", TLDList.domain("intranet.local"));
    }

    @Test
    public void testNull() {
        /// Not @Nullable => NullPointerException expected
        try {
            TLDList.domain(null);
            fail();
        } catch (NullPointerException e) {
        }
    }
}
