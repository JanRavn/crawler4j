package edu.uci.ics.crawler4j.crawler;

import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Created by Ravn Systems
 * Date: 29/11/12
 * Time: 13:12
 *
 * @author Jan Van Hoecke
 */
public interface PageFactory<T extends Page> {
    T create(WebURL webURL, PageFetchResult result);
}
