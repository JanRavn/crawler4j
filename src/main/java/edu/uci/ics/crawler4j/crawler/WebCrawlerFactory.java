package edu.uci.ics.crawler4j.crawler;

/**
 * Created by Ravn Systems
 * Date: 27/11/12
 * Time: 23:33
 *
 * Factory classes implementing WebCrawlerFactory allow provide
 * flexibilty to inject WebCrawlers useful members.
 *
 * @author Jan Van Hoecke
 */
public interface WebCrawlerFactory<T extends WebCrawler> {
    T create();
}
