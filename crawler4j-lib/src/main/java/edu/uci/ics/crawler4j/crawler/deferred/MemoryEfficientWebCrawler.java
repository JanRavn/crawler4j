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

package edu.uci.ics.crawler4j.crawler.deferred;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.DeferredDataBufferProvider;

/**
 * MemoryEfficientWebCrawler class in the Runnable class that is executed by each crawler
 * thread.
 * It replaces the standard Page implementation by a DeferredPage which stores its
 * contents only up to a certain size in memory.
 *
 * @author Jan Van Hoecke
 */
public class MemoryEfficientWebCrawler extends WebCrawler {

    /// Helpers
    private DeferredDataBufferProvider dataBufferProvider;

    public MemoryEfficientWebCrawler(DeferredDataBufferProvider dataBufferProvider) {
        this.dataBufferProvider = dataBufferProvider;
    }

    @Override
    protected Page createPage(WebURL url, PageFetchResult fetchResult) {
        DeferredPage page = new DeferredPage(url, dataBufferProvider.create());
        return page;
    }

    @Override
    public void visit(Page page) {
        super.visit(page);
    }
}
