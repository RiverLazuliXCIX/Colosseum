package uk.ac.qub.eeecs.gage;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;
import org.junit.runner.RunWith;
import org.junit.Test;

@RunWith(AndroidJUnit4.class)

public class FpsCounterTest {

    private static long lastCheck = 0;
    private static int currentFPS = 0;
    private static int totalFrames = 0;

    @Test
    public void testTotalFramesVReset()
    {
        totalFrames = 20;
        if(System.nanoTime() > lastCheck + 1000000000)
        {
            lastCheck = System.nanoTime();

            currentFPS = totalFrames;

            totalFrames = 0;
        }

        Assert.assertEquals(0, totalFrames);
    }

}
