package edu.uci.ics.crawler4j.parser;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravn Systems Ltd
 * User: adam
 * Date: 30/01/13
 * Time: 11:35
 */
public class XmlContentHandler extends DefaultHandler {

    List<ExtractedUrlAnchorPair> outgoingUrls;

    public XmlContentHandler() {
        outgoingUrls = new ArrayList<ExtractedUrlAnchorPair>();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (length > 4 && Character.toLowerCase(ch[start]) == 'h' && Character.toLowerCase(ch[start+1]) == 't'
                && Character.toLowerCase(ch[start+2]) == 't' && Character.toLowerCase(ch[start+3]) == 'p') {
            String url = new String(ch, start, length);
            ExtractedUrlAnchorPair curUrl = new ExtractedUrlAnchorPair();
            curUrl.setHref(url);
            outgoingUrls.add(curUrl);
        }
    }

    public List<ExtractedUrlAnchorPair> getOutgoingUrls() {
        return outgoingUrls;
    }
}
