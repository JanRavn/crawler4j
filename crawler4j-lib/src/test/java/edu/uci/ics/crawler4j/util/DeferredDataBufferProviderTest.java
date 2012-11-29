package edu.uci.ics.crawler4j.util;

import org.junit.Test;

import java.io.File;

/**
 * Created by Ravn Systems
 * Date: 29/11/12
 * Time: 11:07
 *
 * @author Jan Van Hoecke
 */
public class DeferredDataBufferProviderTest {
    @Test
    public void testCreate() throws Exception {
        File temp = File.createTempFile("test_", ".dat", new File("/tmp/crawler/data"));
        System.out.println(temp.exists());
    }

    @Test
    public void testExitWhenFinished() throws Exception {

    }
}
