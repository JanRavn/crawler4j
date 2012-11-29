package edu.uci.ics.crawler4j.crawler.deferred;

import edu.uci.ics.crawler4j.crawler.WebCrawlerFactory;
import edu.uci.ics.crawler4j.util.DeferredDataBufferProvider;

/**
 * Created by Ravn Systems
 * Date: 29/11/12
 * Time: 10:12
 *
 * @author Jan Van Hoecke
 */
public class MemoryEfficientWebCrawlerFactory implements WebCrawlerFactory<MemoryEfficientWebCrawler> {

    /// Helpers
    protected DeferredDataBufferProvider dataBufferProvider;

    public MemoryEfficientWebCrawlerFactory(DeferredDataBufferProvider dataBufferProvider) {
        this.dataBufferProvider = dataBufferProvider;
    }

    @Override
    public MemoryEfficientWebCrawler create() {
        MemoryEfficientWebCrawler crawler = new MemoryEfficientWebCrawler(dataBufferProvider);
        return crawler;
    }
}
