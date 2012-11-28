package edu.uci.ics.crawler4j.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Ravn Systems
 * Date: 27/11/12
 * Time: 23:35
 * <p/>
 * Default implementation of the WebCrawlerFactory in order to be backwards compatible
 * with the clazz#newInstance() approach.
 *
 * @author Jan Van Hoecke
 */
public class DefaultWebCrawlerFactory<T extends WebCrawler> implements WebCrawlerFactory {
    protected static final Logger logger = LoggerFactory.getLogger(DefaultWebCrawlerFactory.class);

    private final Class<T> clazz;

    /**
     * Constructor which takes the Class of the WebCrawler it needs to instantiate.
     *
     * @param clazz Class for the WebCrawler this factory needs to instantiate.
     */
    public DefaultWebCrawlerFactory(final Class<T> clazz) {
        this.clazz = clazz;
        assert clazz != null;
    }

    @Override
    public WebCrawler create() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            logger.error("Could not instantiate WebCrawler. Instantiation exception", e);
        } catch (IllegalAccessException e) {
            logger.error("Could not instantiate WebCrawler. Illegal access exception", e);
        }
        return null;
    }
}
