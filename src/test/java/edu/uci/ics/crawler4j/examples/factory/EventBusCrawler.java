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

package edu.uci.ics.crawler4j.examples.factory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;

/**
 * WebCrawler which publishes events for each page which is crawled.
 *
 * @author Jan Van Hoecke
 */
public class EventBusCrawler extends WebCrawler {

    private final EventBus bus;

    public EventBusCrawler(EventBus bus) {
        this.bus = bus;
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {

        EventBus.Event event = new EventBus.Event();
        event.setUrl(page.getWebURL().getURL());
        event.setDomain(page.getWebURL().getDomain());
        event.setPath(page.getWebURL().getPath());
        event.setSubDomain(page.getWebURL().getSubDomain());
        event.setParentUrl(page.getWebURL().getParentUrl());

        bus.notify(event);
    }
}
