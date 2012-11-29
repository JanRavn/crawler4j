package edu.uci.ics.crawler4j.examples.blockingpipe;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.frontier.DocIDServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Ravn Systems
 * Date: 29/11/12
 * Time: 11:28
 *
 * @author Jan Van Hoecke
 */
public class BlockingConsumer {
    protected static final Logger logger = LoggerFactory.getLogger(DocIDServer.class);

    private BlockingQueue<Page> pipeline;
    private Thread consumerThread;

    public BlockingConsumer(int queueCapacity) {
        pipeline = new ArrayBlockingQueue<Page>(queueCapacity);
    }

    public BlockingQueue<Page> getPipeline() {
        return pipeline;
    }

    public void run() {
        Runnable consumer = new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (true) {
                    Page page = null;
                    try {
                        page = pipeline.take();
                    } catch (InterruptedException e) {
                    }

                    if (page != null) {
                        logger.info("Consumer: Crawled page {} - {}", count++, page.getWebURL().toString());
                    }
                    logger.info("Consumer: Items still on the queue {}", pipeline.size());

                    try {
                        //logger.info("Consumer: Sleeping 1s");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };

        consumerThread = new Thread(consumer);
        consumerThread.setName("Consumer");
        consumerThread.setDaemon(true);
        consumerThread.start();
    }
}
