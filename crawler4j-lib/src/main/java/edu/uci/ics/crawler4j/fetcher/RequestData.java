package edu.uci.ics.crawler4j.fetcher;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by Ravn Systems
 * Date: 29/11/12
 * Time: 19:15
 *
 * @author Jan Van Hoecke
 */
public class RequestData {
    /**
     * Map with the header (name, value) pairs returned by the
     * server.
     */
    protected Map<String, String> headers = Maps.newHashMap();

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
