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

package edu.uci.ics.crawler4j.examples.blockingpipe;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

/**
 * @author Jan Van Hoecke
 */
public class BasicCrawler extends WebCrawler {
    protected static final Logger logger = LoggerFactory.getLogger(BasicCrawler.class);

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    private BlockingQueue<Page> pipeline;

    public BasicCrawler(BlockingQueue<Page> pipeline) {
        this.pipeline = pipeline;
    }

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
        if (FILTERS.matcher(url.getURL()).matches()) {
            logger.debug("Rejecting URL, does not match URL filter: " + url.toString());
            return false;
        } else {
            logger.debug("Accepting URL: " + url.toString());
            return true;
        }
    }

    @Override
    public void visit(Page page) {
        //logger.info("Crawler: Items on the queue {}", pipeline.size());
        logger.info("Crawler: Offering new page to the Pipeline");
        try {
            pipeline.put(page);
        } catch (InterruptedException e) {
        }
        logger.info("Crawler: Page added to the Pipeline");

    }
}
