package edu.uci.ics.crawler4j.crawler.deferred;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.DeferredDataBuffer;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by Ravn Systems
 * Date: 28/11/12
 * Time: 18:31
 *
 * @author Jan Van Hoecke
 */
public class DeferredPage extends Page {
    // State information
    private DeferredDataBuffer buffer;
    private int size = 0;


    public DeferredPage(WebURL url, DeferredDataBuffer buffer) {
        super(url);
        this.buffer = buffer;
    }

    @Override
    public void load(HttpEntity entity) throws Exception {
        contentType = null;
        Header type = entity.getContentType();
        if (type != null) {
            contentType = type.getValue();
        }

        contentEncoding = null;
        Header encoding = entity.getContentEncoding();
        if (encoding != null) {
            contentEncoding = encoding.getValue();
        }

        Charset charset = ContentType.getOrDefault(entity).getCharset();
        if (charset != null) {
            contentCharset = charset.name();
        }

        // Copy content to the buffer
        IOUtils.copy(entity.getContent(), buffer.getOutputStream());
    }

    @Override
    public InputStream getContentDataStream() {
        return buffer.getInputStream();
    }

    @Override
    public long getContentSize() {
        return size;
    }
}
