package edu.uci.ics.crawler4j.crawler;

import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Created by Ravn Systems
 * Date: 29/11/12
 * Time: 13:13
 *
 * @author Jan Van Hoecke
 */
public class DefaultPageFactory implements PageFactory<Page> {
    @Override
    public Page create(WebURL webURL, PageFetchResult result) {
        return new Page(webURL);
    }
}
