package edu.uci.ics.crawler4j.crawler;

import org.apache.commons.io.output.DeferredFileOutputStream;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Ravn Systems
 * Date: 28/11/12
 * Time: 18:46
 *
 * @author Jan Van Hoecke
 */
public class DeferredPageTest {
    @Test
    public void testDeferredOutputStream() throws IOException {
        File temp = File.createTempFile("crawler", ".dat");
        DeferredFileOutputStream os = new DeferredFileOutputStream(1024, temp);

        for (int i = 0; i < 1000; i++)
            os.write(i);

        assertNotNull(os.getData());
        assertTrue(os.isInMemory());

        for (int i = 0; i < 1000; i++)
            os.write(i);

        assertNull(os.getData());
        assertNotNull(os.getFile());
        assertFalse(os.isInMemory());

        temp.delete();
    }
}
