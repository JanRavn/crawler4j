package edu.uci.ics.crawler4j.crawler.deferred;

import edu.uci.ics.crawler4j.crawler.PageFactory;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.DeferredDataBufferProvider;

import java.io.IOException;

/**
 * Created by Ravn Systems
 * Date: 29/11/12
 * Time: 13:19
 *
 * @author Jan Van Hoecke
 */
public class MemoryEfficientPageFactory implements PageFactory<DeferredPage> {
    private DeferredDataBufferProvider dataBufferProvider;

    public MemoryEfficientPageFactory(String tempPath, int threshold) throws IOException {
        this.dataBufferProvider = new DeferredDataBufferProvider(tempPath, threshold);
    }

    public MemoryEfficientPageFactory(DeferredDataBufferProvider dataBufferProvider) {
        this.dataBufferProvider = dataBufferProvider;
    }

    @Override
    public DeferredPage create(WebURL webURL, PageFetchResult result) {
        DeferredPage page = new DeferredPage(webURL, dataBufferProvider.create());
        return page;
    }

    public void close() {
        this.dataBufferProvider.exitWhenFinished();
    }
}
