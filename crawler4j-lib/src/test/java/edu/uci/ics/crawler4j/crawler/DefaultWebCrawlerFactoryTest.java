package edu.uci.ics.crawler4j.crawler;

import junit.framework.TestCase;

/**
 * Created by Ravn Systems
 * Date: 27/11/12
 * Time: 23:42
 *
 * @author Jan Van Hoecke
 */
public class DefaultWebCrawlerFactoryTest extends TestCase {
    public void testCreateValidWebCrawler() throws Exception {
        DefaultWebCrawlerFactory factory = new DefaultWebCrawlerFactory(SpecificWebCrawler.class);
        WebCrawler instance = factory.create();

        assertNotNull(instance);
        assertTrue(instance instanceof SpecificWebCrawler);
    }

    public void testNullWebCrawler() throws Exception {
        try {
            DefaultWebCrawlerFactory factory = new DefaultWebCrawlerFactory(null);
            fail("AssertionError expected");
        } catch (AssertionError e) {
        }
    }

    /**
     * Dummy WebCrawler
     */
    public static class SpecificWebCrawler extends WebCrawler {
    }
}
