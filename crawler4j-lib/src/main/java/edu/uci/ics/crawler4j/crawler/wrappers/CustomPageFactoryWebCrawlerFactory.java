package edu.uci.ics.crawler4j.crawler.wrappers;

import edu.uci.ics.crawler4j.crawler.PageFactory;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.crawler.WebCrawlerFactory;

/**
 * Created by Ravn Systems
 * Date: 29/11/12
 * Time: 10:12
 *
 * @author Jan Van Hoecke
 */
public class CustomPageFactoryWebCrawlerFactory<T extends WebCrawler> implements WebCrawlerFactory {
    /// Helpers
    private PageFactory pageFactory;
    private WebCrawlerFactory<T> wrappedFactory;

    public CustomPageFactoryWebCrawlerFactory(WebCrawlerFactory<T> wrappedFactory, PageFactory pageFactory) {
        this.pageFactory = pageFactory;
        this.wrappedFactory = wrappedFactory;
    }

    @Override
    public T create() {
        T crawler = wrappedFactory.create();
        crawler.setPageFactory(pageFactory);
        return crawler;
    }
}
