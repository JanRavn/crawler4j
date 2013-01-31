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

package edu.uci.ics.crawler4j.parser;

import edu.uci.ics.crawler4j.crawler.Configurable;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.Util;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.parser.xml.XMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class Parser extends Configurable {

    protected final static Logger logger = LoggerFactory.getLogger(Parser.class);

    private HtmlParser htmlParser;
    private ParseContext parseContext;

    public Parser(CrawlConfig config) {
        super(config);
        htmlParser = new HtmlParser();
        parseContext = new ParseContext();
    }

    public boolean parse(Page page, String contextURL) {

        if (Util.hasPlainTextContent(page.getContentType())) {
            try {
                TextParseData parseData = new TextParseData();
                parseData.setTextContent(Util.toString(page.getContentDataStream(), page.getContentCharset()));
                page.setParseData(parseData);
                return true;
            } catch (Exception e) {
                logger.error("Error parsing text content: {}", contextURL, e);
            }
            return false;
        }
        else if (Util.hasXMLContent(page.getContentType())) {
            return parseXml(page, contextURL);
        }
        else if (Util.hasBinaryContent(page.getContentType())) {
            if (!config.isIncludeBinaryContentInCrawling()) {
                return false;
            } else {
                page.setParseData(BinaryParseData.getInstance());
                return true;
            }
        }

        return parseHtml(page, contextURL);
    }

    private boolean parseHtml(Page page, String contextURL) {
        Metadata metadata = new Metadata();
        HtmlContentHandler contentHandler = new HtmlContentHandler();
        InputStream inputStream = null;
        try {
            htmlParser.parse(page.getContentDataStream(), contentHandler, metadata, parseContext);
        } catch (Exception e) {
            logger.error("Error parsing HTML content: {}", contextURL, e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignored) {
            }
        }

        if (page.getContentCharset() == null) {
            page.setContentCharset(metadata.get("Content-Encoding"));
        }

        HtmlParseData parseData = new HtmlParseData();
        parseData.setText(contentHandler.getBodyText().trim());
        parseData.setTitle(metadata.get(TikaCoreProperties.TITLE.getName()));

        String baseURL = contentHandler.getBaseUrl();
        if (baseURL != null) {
            contextURL = baseURL;
        }

        parseData.setOutgoingUrls(getOutgoingUrls(contentHandler.getOutgoingUrls(), contextURL));
        parseData.setHtml(Util.toString(page.getContentDataStream(), page.getContentCharset()));
        page.setParseData(parseData);
        return true;
    }

    private boolean parseXml(Page page, String contextURL) {
        try {
            XmlParseData parseData = new XmlParseData();
            XMLParser xmlParser = new XMLParser();
            Metadata metadata = new Metadata();
            XmlContentHandler contentHandler = new XmlContentHandler();
            xmlParser.parse(page.getContentDataStream(), contentHandler, metadata, parseContext);
            parseData.setOutgoingUrls(getOutgoingUrls(contentHandler.getOutgoingUrls(), contextURL));
            page.setParseData(parseData);
            return true;
        }
        catch (Exception e) {
            logger.error("Error parsing XML content: {}", contextURL, e);
        }
        return false;
    }

    private List<WebURL> getOutgoingUrls(List<ExtractedUrlAnchorPair> urlAnchorPairs, String contextURL) {
        List<WebURL> outgoingUrls = new ArrayList<WebURL>();
        int urlCount = 0;
        for (ExtractedUrlAnchorPair urlAnchorPair : urlAnchorPairs) {
            String href = urlAnchorPair.getHref();
            href = href.trim();
            if (href.length() == 0) {
                continue;
            }
            String hrefWithoutProtocol = href.toLowerCase();
            if (href.startsWith("http://")) {
                hrefWithoutProtocol = href.substring(7);
            }
            if (!hrefWithoutProtocol.contains("javascript:") && !hrefWithoutProtocol.contains("@")) {
                String url = URLCanonicalizer.getCanonicalURL(href, contextURL);
                if (url != null) {
                    WebURL webURL = new WebURL();
                    webURL.setURL(url);
                    webURL.setAnchor(urlAnchorPair.getAnchor());
                    outgoingUrls.add(webURL);
                    urlCount++;
                    if (urlCount > config.getMaxOutgoingLinksToFollow()) {
                        break;
                    }
                }
            }
        }
        return outgoingUrls;
    }

}
