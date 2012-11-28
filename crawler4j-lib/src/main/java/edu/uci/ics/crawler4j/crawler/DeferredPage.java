package edu.uci.ics.crawler4j.crawler;

import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

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

    // Config settings
    private String tempPath;

    // State information

    public DeferredPage(WebURL url) {
        super(url);
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

        // Copy the files

        contentData = EntityUtils.toByteArray(entity);
    }

    @Override
    public InputStream getContentDataStream() {
        return super.getContentDataStream();
    }
}
