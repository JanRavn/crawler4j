package edu.uci.ics.crawler4j.examples.blockingpipe;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.deferred.MemoryEfficientWebCrawler;
import edu.uci.ics.crawler4j.crawler.deferred.MemoryEfficientWebCrawlerFactory;
import edu.uci.ics.crawler4j.util.DeferredDataBufferProvider;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Ravn Systems
 * Date: 29/11/12
 * Time: 11:03
 *
 * @author Jan Van Hoecke
 */
public class CrawlerFactory extends MemoryEfficientWebCrawlerFactory {
    private BlockingQueue<Page> pipeline;
    public CrawlerFactory(DeferredDataBufferProvider dataBufferProvider, BlockingQueue<Page> pipeline) {
        super(dataBufferProvider);
        this.pipeline = pipeline;
    }

    @Override
    public MemoryEfficientWebCrawler create() {
        BasicCrawler crawler = new BasicCrawler(dataBufferProvider, pipeline);
        return crawler;
    }
}
