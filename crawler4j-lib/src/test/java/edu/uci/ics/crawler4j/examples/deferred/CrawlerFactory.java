package edu.uci.ics.crawler4j.examples.deferred;

import edu.uci.ics.crawler4j.crawler.deferred.MemoryEfficientWebCrawler;
import edu.uci.ics.crawler4j.crawler.deferred.MemoryEfficientWebCrawlerFactory;
import edu.uci.ics.crawler4j.util.DeferredDataBufferProvider;

/**
 * Created by Ravn Systems
 * Date: 29/11/12
 * Time: 11:03
 *
 * @author Jan Van Hoecke
 */
public class CrawlerFactory extends MemoryEfficientWebCrawlerFactory {
    public CrawlerFactory(DeferredDataBufferProvider dataBufferProvider) {
        super(dataBufferProvider);
    }

    @Override
    public MemoryEfficientWebCrawler create() {
        BasicCrawler crawler = new BasicCrawler(dataBufferProvider);
        return crawler;
    }
}
