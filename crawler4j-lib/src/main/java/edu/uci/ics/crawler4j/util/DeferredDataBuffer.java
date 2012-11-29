package edu.uci.ics.crawler4j.util;

import org.apache.commons.io.output.DeferredFileOutputStream;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Ravn Systems
 * Date: 28/11/12
 * Time: 18:51
 *
 * DataBuffer which keeps the data in memory until a certain threshold is reached.
 * At that point, the data is persisted to disk (to a specified File).
 *
 * Management of the file (creation of the directory, deletion after usage) is
 * left to the user of this class.
 *
 * @author Jan Van Hoecke
 */
public class DeferredDataBuffer {
    /// State information
    private DeferredFileOutputStream dfos;


    public DeferredDataBuffer(int threshold, File file) {
        this.dfos = new DeferredFileOutputStream(threshold, file);
    }

    public OutputStream getOutputStream() {
        return dfos;

    }

    public InputStream getInputStream() {
        if (dfos.isInMemory()) {
            return new ByteArrayInputStream(dfos.getData());
        } else {
            try {
                return new FileInputStream(dfos.getFile());
            } catch (FileNotFoundException e) {
            }
        }
        return null;
    }


}
