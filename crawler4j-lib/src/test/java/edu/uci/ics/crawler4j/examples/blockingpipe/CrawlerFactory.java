package edu.uci.ics.crawler4j.examples.blockingpipe;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawlerFactory;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Ravn Systems
 * Date: 29/11/12
 * Time: 11:03
 *
 * @author Jan Van Hoecke
 */
public class CrawlerFactory implements WebCrawlerFactory<BasicCrawler> {
    private BlockingQueue<Page> pipeline;

    public CrawlerFactory(BlockingQueue<Page> pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public BasicCrawler create() {
        BasicCrawler crawler = new BasicCrawler(pipeline);
        return crawler;
    }
}
