package edu.uci.ics.crawler4j.fetcher;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import org.junit.Test;

import static org.junit.Assert.*;

import javax.net.ssl.SSLContext;

/**
 * Created by janvh on 03/03/14.
 */
public class PageFetcherTest {
    @Test
    public void testGetSSLContext() throws Exception {
        CrawlConfig config = new CrawlConfig();

        config.setKeyStorePath("/home/janvh/tmp/dmt-private.jks");
        config.setKeyStorePass("ravnravn");
        config.setKeyStoreType("JKS");
        config.setKeyPass("ravn");

        config.setTrustStorePath("/home/janvh/tmp/dmt-public.jks");
        config.setTrustStorePass("ravnravn");
        config.setTrustStoreType("JKS");

        SSLContext context = PageFetcher.getSSLContext(config);

        assertNotNull(context);
    }

    @Test
    public void testGetSSLContextNullPassword() throws Exception {
        CrawlConfig config = new CrawlConfig();

        config.setKeyStorePath("/home/janvh/tmp/dmt-private.jks");
        config.setKeyStorePass(null);
        config.setKeyStoreType("JKS");
        config.setKeyPass("ravn");

        config.setTrustStorePath("/home/janvh/tmp/dmt-public.jks");
        config.setTrustStorePass(null);
        config.setTrustStoreType("JKS");

        SSLContext context = PageFetcher.getSSLContext(config);

        assertNotNull(context);
    }

    @Test
    public void testGetSSLContextNull() throws Exception {
        CrawlConfig config = new CrawlConfig();

        config.setKeyStorePath(null);
        config.setKeyStorePass("ravnravn");
        config.setKeyStoreType("JKS");
        config.setKeyPass("ravn");

        config.setTrustStorePath(null);
        config.setTrustStorePass("ravnravn");
        config.setTrustStoreType("JKS");

        SSLContext context = PageFetcher.getSSLContext(config);

        assertNotNull(context);
    }

}
