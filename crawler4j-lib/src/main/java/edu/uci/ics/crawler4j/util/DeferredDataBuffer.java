package edu.uci.ics.crawler4j.util;

import org.apache.commons.io.output.DeferredFileOutputStream;

import java.io.File;

/**
 * Created by Ravn Systems
 * Date: 28/11/12
 * Time: 18:51
 *
 * @author Jan Van Hoecke
 */
public class DeferredDataBuffer {
    /// State information
    private int threshold;
    private File file;
    private DeferredFileOutputStream dfos;


    public DeferredDataBuffer(int threshold, File file) {
        this.threshold = threshold;
        this.file = file;
        this.dfos = new DeferredFileOutputStream(threshold, file);
    }


}
