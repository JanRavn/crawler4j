/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.fetcher;

import com.google.common.collect.Maps;
import edu.uci.ics.crawler4j.crawler.Page;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.util.Map;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class PageFetchResult {

    private static final Logger logger = LoggerFactory.getLogger(PageFetchResult.class);

    protected int statusCode;
    protected HttpEntity entity = null;
    protected String fetchedUrl = null;
    protected String movedToUrl = null;
    protected Map<String, String> headers = Maps.newHashMap();

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public HttpEntity getEntity() {
        return entity;
    }

    public void setEntity(HttpEntity entity) {
        this.entity = entity;
    }

    public String getFetchedUrl() {
        return fetchedUrl;
    }

    public void setFetchedUrl(String fetchedUrl) {
        this.fetchedUrl = fetchedUrl;
    }

    public void addHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public boolean fetchContent(Page page) {
        try {
            page.load(entity);
            page.getRequestData().getHeaders().putAll(headers);
            return true;
        } catch (Exception e) {
            logger.info("Exception while fetching content for: " + page.getWebURL().getURL() + " [" + e.getMessage()
                    + "]");
        }
        return false;
    }

    public void discardContentIfNotConsumed() {
        try {
            if (entity != null) {
                EntityUtils.consume(entity);
            }
        } catch (EOFException e) {
            // We can ignore this exception. It can happen on compressed streams
            // which are not
            // repeatable
        } catch (IOException e) {
            // We can ignore this exception. It can happen if the stream is
            // closed.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMovedToUrl() {
        return movedToUrl;
    }

    public void setMovedToUrl(String movedToUrl) {
        this.movedToUrl = movedToUrl;
    }

}
