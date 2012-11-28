package edu.uci.ics.crawler4j.util;

import java.io.File;
import java.io.IOException;

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
    protected final String TEMP_PREFIX = "deferred_buffer_";
    protected final String TEMP_SUFFIX = ".dat";
    /// Config settings
    private String tempPath = null;

    /// Helpers


    private File getTempFile() {
        if (tempPath != null) {
            try {
                return File.createTempFile(TEMP_PREFIX, TEMP_SUFFIX);
            } catch (IOException e) {
            }
        }
        return null;
    }

    public void setTempPath(String path) {

    }

}
