package edu.uci.ics.crawler4j.parser;

import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;

/**
 * Created by Ravn Systems Ltd
 * User: adam
 * Date: 30/01/13
 * Time: 15:38
 */
public interface OutgoingUrlData {

    public List<WebURL> getOutgoingUrls();
}
