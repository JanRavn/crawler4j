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

package edu.uci.ics.crawler4j.crawler;

import edu.uci.ics.crawler4j.fetcher.CustomFetchStatus;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.frontier.DocIDServer;
import edu.uci.ics.crawler4j.frontier.Frontier;
import edu.uci.ics.crawler4j.parser.OutgoingUrlData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * WebCrawler class in the Runnable class that is executed by each crawler
 * thread.
 *
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class WebCrawler implements Runnable {

    protected static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

    /**
     * The id associated to the crawler thread running this instance
     */
    protected int myId;

    /**
     * The controller instance that has created this crawler thread. This
     * reference to the controller can be used for getting configurations of the
     * current crawl or adding new seeds during runtime.
     */
    protected CrawlController myController;

    /**
     * The Page factory creates new Page object on request. The factory can
     * be overridden in order to provide different Page implementations.
     */
    protected PageFactory pageFactory = new DefaultPageFactory();

    /**
     * The thread within which this crawler instance is running.
     */
    private Thread myThread;

    /**
     * The parser that is used by this crawler instance to parse the content of
     * the fetched pages.
     */
    private Parser parser;

    /**
     * The fetcher that is used by this crawler instance to fetch the content of
     * pages from the web.
     */
    private PageFetcher pageFetcher;

    /**
     * The RobotstxtServer instance that is used by this crawler instance to
     * determine whether the crawler is allowed to crawl the content of each
     * page.
     */
    private RobotstxtServer robotstxtServer;

    /**
     * The DocIDServer that is used by this crawler instance to map each URL to
     * a unique docid.
     */
    private DocIDServer docIdServer;

    /**
     * The Frontier object that manages the crawl queue.
     */
    private Frontier frontier;

    /**
     * The Domain manager keeps track of the domains the WebCrawlers have visited.
     * It can be used to support the stayOnDomain settings.
     */
    private DomainManager domainManager;


    /**
     * Is the current crawler instance waiting for new URLs? This field is
     * mainly used by the controller to detect whether all of the crawler
     * instances are waiting for new URLs and therefore there is no more work
     * and crawling can be stopped.
     */
    private boolean isWaitingForNewURLs;

    /**
     * Initializes the current instance of the crawler
     *
     * @param myId            the id of this crawler instance
     * @param crawlController the controller that manages this crawling session
     */
    public void init(int myId, CrawlController crawlController) {
        this.myId = myId;
        this.pageFetcher = crawlController.getPageFetcher();
        this.robotstxtServer = crawlController.getRobotstxtServer();
        this.docIdServer = crawlController.getDocIdServer();
        this.frontier = crawlController.getFrontier();
        this.parser = new Parser(crawlController.getConfig());
        this.myController = crawlController;
        this.domainManager = crawlController.getDomainManager();
        this.isWaitingForNewURLs = false;
    }

    /**
     * Get the id of the current crawler instance
     *
     * @return the id of the current crawler instance
     */
    public int getMyId() {
        return myId;
    }

    public CrawlController getMyController() {
        return myController;
    }

    /**
     * This function is called just before starting the crawl by this crawler
     * instance. It can be used for setting up the data structures or
     * initializations needed by this crawler instance.
     */
    public void onStart() {
    }

    /**
     * This function is called just before the termination of the current
     * crawler instance. It can be used for persisting in-memory data or other
     * finalization tasks.
     */
    public void onBeforeExit() {
    }

    /**
     * This function is called once the header of a page is fetched.
     * It can be overwritten by sub-classes to perform custom logic
     * for different status codes. For example, 404 pages can be logged, etc.
     */
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
    }

    /**
     * The CrawlController instance that has created this crawler instance will
     * call this function just before terminating this crawler thread. Classes
     * that extend WebCrawler can override this function to pass their local
     * data to their controller. The controller then puts these local data in a
     * List that can then be used for processing the local data of crawlers (if
     * needed).
     */
    public Object getMyLocalData() {
        return null;
    }

    public void run() {
        onStart();
        while (true) {
            List<WebURL> assignedURLs = new ArrayList<WebURL>(50);
            isWaitingForNewURLs = true;
            frontier.getNextURLs(50, assignedURLs);
            isWaitingForNewURLs = false;
            if (assignedURLs.size() == 0) {
                if (frontier.isFinished()) {
                    return;
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                for (WebURL curURL : assignedURLs) {
                    if (curURL != null) {
                        processPage(curURL);
                        frontier.setProcessed(curURL);
                    }
                    if (myController.isShuttingDown()) {
                        logger.info("Exiting because of controller shutdown.");
                        return;
                    }
                }
            }
        }
    }

    /**
     * Classes that extends WebCrawler can overwrite this function to tell the
     * crawler whether the given url should be crawled or not. The following
     * implementation indicates that all urls should be included in the crawl.
     *
     * @param url the url which we are interested to know whether it should be
     *            included in the crawl or not.
     * @return if the url should be included in the crawl it returns true,
     *         otherwise false is returned.
     */
    public boolean shouldVisit(WebURL url) {
        return true;
    }

    /**
     * Internal implementation of the shouldVisit method. First checks off
     * any criteria which are controlled by the configuration settings before calling
     * the shouldVisit() method which can be extended.
     *
     * @param url
     * @return
     */
    private boolean shouldVisitInternal(WebURL url) {
        if (myController.getConfig().getMaxDepthOfCrawling() > -1 & url.getDepth() > myController.getConfig().getMaxDepthOfCrawling()) {
            logger.info("SKIP - {} - Max crawl depth reached", url.toString());
            return false;
        }

        if (myController.getConfig().isStayOnDomain() && !domainManager.isInRegistredDomain(url)) {
            logger.info("SKIP - {} - Not in the same domain", url.toString());
            return false;
        }
        if (!robotstxtServer.allows(url)) {
            logger.debug("SKIP - {} - Not allowed by the robots.txt", url.toString());
            return false;
        }

        return shouldVisit(url);
    }

    /**
     * Classes that extends WebCrawler can overwrite this function to process
     * the content of the fetched and parsed page.
     *
     * @param page the page object that is just fetched and parsed.
     */
    public void visit(Page page) {
    }

    private void processPage(WebURL curURL) {
        if (curURL == null) {
            return;
        }
        PageFetchResult fetchResult = null;
        try {
            fetchResult = pageFetcher.fetchHeader(curURL);
            int statusCode = fetchResult.getStatusCode();
            logger.info("FETCHED - {} - {}", statusCode, curURL.getURL());
            handlePageStatusCode(curURL, statusCode, CustomFetchStatus.getStatusDescription(statusCode));
            if (statusCode != HttpStatus.SC_OK) {
                if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                    if (myController.getConfig().isFollowRedirects()) {
                        String movedToUrl = fetchResult.getMovedToUrl();
                        if (movedToUrl == null) {
                            logger.info("SKIP - {} - Moved to unknown location", curURL.getURL());
                            return;
                        }
                        int newDocId = docIdServer.getDocId(movedToUrl);
                        if (newDocId > 0) {
                            logger.info("SKIP - {} - Already visited", curURL.getURL());
                            // Redirect page is already seen
                            return;
                        }

                        WebURL webURL = new WebURL();
                        webURL.setURL(movedToUrl);
                        webURL.setParentDocid(curURL.getParentDocid());
                        webURL.setParentUrl(curURL.getParentUrl());
                        webURL.setDepth(curURL.getDepth());
                        webURL.setDocid(-1);
                        if (shouldVisitInternal(webURL)) {
                            webURL.setDocid(docIdServer.getNewDocID(movedToUrl));
                            frontier.schedule(webURL);
                        }
                    }
                } else if (fetchResult.getStatusCode() == CustomFetchStatus.PageTooBig) {
                    logger.info("SKIP - {} - Too Big", curURL.getURL());
                }
                return;
            }

            if (!curURL.getURL().equals(fetchResult.getFetchedUrl())) {
                if (docIdServer.isSeenBefore(fetchResult.getFetchedUrl())) {
                    // Redirect page is already seen
                    logger.info("SKIP - {} - Redirect page is already seen", curURL.getURL());
                    return;
                }
                logger.info("REDIRECT - {} to {}", curURL.getURL(), fetchResult.getFetchedUrl());
                curURL.setURL(fetchResult.getFetchedUrl());
                curURL.setDocid(docIdServer.getNewDocID(fetchResult.getFetchedUrl()));
            }

            // Create a Page object for the fetched URL
            Page page = pageFactory.create(curURL, fetchResult);

            int docid = curURL.getDocid();

            if (fetchResult.fetchContent(page) && parser.parse(page, curURL.getURL())) {
                ParseData parseData = page.getParseData();
                if (parseData instanceof OutgoingUrlData) {
                    OutgoingUrlData outgoingUrlData = (OutgoingUrlData)parseData;

                    List<WebURL> toSchedule = new ArrayList<WebURL>();
                    for (WebURL webURL : outgoingUrlData.getOutgoingUrls()) {
                        logger.debug("SCHEDULING: {}", webURL.getURL());
                        webURL.setParentDocid(docid);
                        webURL.setParentUrl(curURL.getURL());
                        int newdocid = docIdServer.getDocId(webURL.getURL());
                        if (newdocid > 0) {
                            // This is not the first time that this Url is
                            // visited. So, we set the depth to a negative
                            // number.
                            webURL.setDepth((short) -1);
                            webURL.setDocid(newdocid);
                        } else {
                            webURL.setDocid(-1);
                            webURL.setDepth((short) (curURL.getDepth() + 1));
                            if (shouldVisitInternal(webURL)) {
                                webURL.setDocid(docIdServer.getNewDocID(webURL.getURL()));
                                toSchedule.add(webURL);
                            }
                        }
                    }
                    frontier.scheduleAll(toSchedule);
                }
                logger.info("VISITING - {}", curURL.getURL());
                visit(page);
            }
        } catch (Exception e) {
            logger.info("FAILED - {} - {}", curURL.getURL(), e.getMessage());
            logger.error(e.getMessage() + ", while processing: " + curURL.getURL());
        } finally {
            if (fetchResult != null) {
                fetchResult.discardContentIfNotConsumed();
            }
        }
    }

    public Thread getThread() {
        return myThread;
    }

    public void setThread(Thread myThread) {
        this.myThread = myThread;
    }

    public boolean isNotWaitingForNewURLs() {
        return !isWaitingForNewURLs;
    }

    public PageFactory getPageFactory() {
        return pageFactory;
    }

    public void setPageFactory(PageFactory pageFactory) {
        this.pageFactory = pageFactory;
    }
}
