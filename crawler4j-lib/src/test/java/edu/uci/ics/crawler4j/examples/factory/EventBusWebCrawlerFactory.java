package edu.uci.ics.crawler4j.examples.factory;

import edu.uci.ics.crawler4j.crawler.WebCrawlerFactory;

/**
 * Created by Ravn Systems
 * User: janvh
 * Date: 27/11/12
 * Time: 23:53
 */
public class EventBusWebCrawlerFactory implements WebCrawlerFactory<EventBusCrawler> {

    private final EventBus bus;

    public EventBusWebCrawlerFactory(EventBus bus) {
        this.bus = bus;
    }

    @Override
    public EventBusCrawler create() {
        return new EventBusCrawler(bus);
    }
}
