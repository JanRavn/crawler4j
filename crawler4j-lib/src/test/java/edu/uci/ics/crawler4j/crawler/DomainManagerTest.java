package edu.uci.ics.crawler4j.crawler;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Ravn Systems
 * Date: 28/11/12
 * Time: 12:32
 *
 * @author Jan Van Hoecke
 */
public class DomainManagerTest {
    @Test
    public void testRegisterUrl() throws Exception {
        DomainManager manager = new DomainManager();

        manager.registerUrl("http://www.ravn.co.uk");
        manager.registerUrl("http://www.ravn.co.uk/index.html");
        manager.registerUrl("http://www.test.com");
    }

    @Test
    public void testIsRegisteredDomain() throws Exception {
        DomainManager manager = new DomainManager();

        manager.registerUrl("http://www.ravn.co.uk");
        manager.registerUrl("http://www.ravn.co.uk/index.html");
        manager.registerUrl("http://www.test.com");

        assertTrue(manager.isRegisteredDomain("ravn.co.uk"));
        assertFalse(manager.isRegisteredDomain("co.uk"));
        assertTrue(manager.isRegisteredDomain("test.com"));

    }    @Test
    public void testIsInRegistredDomain() throws Exception {
        DomainManager manager = new DomainManager();

        manager.registerUrl("http://www.ravn.co.uk");
        manager.registerUrl("http://www.ravn.co.uk/index.html");
        manager.registerUrl("http://www.test.com");

        assertTrue(manager.isInRegistredDomain("http://www.ravn.co.uk/test?inaction=pig"));
        assertTrue(manager.isInRegistredDomain("https://www.ravn.co.uk"));
        assertFalse(manager.isInRegistredDomain("https://www.ravn.be"));

    }
}
