package edu.uci.ics.crawler4j.util;

import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ravn Systems
 * Date: 28/11/12
 * Time: 18:55
 * <p/>
 * Factory class to make it easier working with DeferredDataBuffers
 *
 * @author Jan Van Hoecke
 */
public class DeferredDataBufferProvider {
    protected static String TEMP_PREFIX = "deferred_buffer_";
    protected static String TEMP_SUFFIX = ".dat";
    protected static int DEFAULT_THRESHOLD = 65536;


    /// Config settings
    private File tempPath = null;
    private int threshold = DEFAULT_THRESHOLD;

    /// Helpers
    private FileCleaningTracker tracker = new FileCleaningTracker();

    public DeferredDataBufferProvider() {
    }

    /**
     * Creates a new DeferredDataBufferProvider which will create temporary files in the
     * provided path.
     * If the path does not exist, it will be created.
     * If the path exists, all files will be deleted. If the path is not a directory
     * an IOException will be thrown.
     *
     * @param path
     */
    public DeferredDataBufferProvider(String path) throws IOException {
        this(path, DEFAULT_THRESHOLD);
    }

    public DeferredDataBufferProvider(String path, int threshold) throws IOException {
        this();
        this.threshold = threshold;

        // Init the path
        tempPath = new File(path);
        if (!tempPath.exists()) {
            tempPath.mkdirs();
        } else if (tempPath.exists() && tempPath.isDirectory()) {
            // Clear if it works. but do not abort in case it does not
            try {
                FileUtils.cleanDirectory(tempPath);
            } catch (Exception e) {

            }
        } else if (tempPath.exists() && !tempPath.isDirectory()) {
            throw new IOException("Provided temporary path exists, but is not a directory");
        }
    }

    public DeferredDataBuffer create() {
        // Create temp file
        File tempFile;
        try {
            tempFile = getTempFile();
            tempFile.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException("Could not create a temporary file for the DeferredDataBuffer", e);
        }

        // Create buffer
        DeferredDataBuffer buffer = new DeferredDataBuffer(threshold, tempFile);

        // Track the buffer / file
        tracker.track(tempFile, buffer);

        return buffer;
    }

    private File getTempFile() throws IOException {
        String filename = TEMP_PREFIX + UUID.randomUUID().toString() + TEMP_SUFFIX;
        if (tempPath == null) {
            return new File(FileUtils.getTempDirectory(), filename);
        } else {
            return new File(tempPath, filename);
        }
    }

    public void exitWhenFinished() {
        tracker.exitWhenFinished();
    }

}
