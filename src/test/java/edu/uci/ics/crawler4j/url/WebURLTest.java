package edu.uci.ics.crawler4j.url;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Ravn Systems
 * Date: 28/11/12
 * Time: 11:19
 *
 * @author Jan Van Hoecke
 */
public class WebURLTest {
    @Test
    public void testGetDomain() throws Exception {
        WebURL url = new WebURL();
        url.setURL("http://www.ravn.co.uk");

        assertEquals("www", url.getSubDomain());
        assertEquals("ravn.co.uk", url.getDomain());
    }
}
