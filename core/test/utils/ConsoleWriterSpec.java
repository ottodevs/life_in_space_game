package utils;

import com.widesteppe.utils.ConsoleWriter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by user on 25.12.2015.
 */
public class ConsoleWriterSpec {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public final void before() {

    }

    @Test
    public void whenTimeSmallerThresholdThenReturnVoidString() {
        String actual = ConsoleWriter.generateString("time to play", 0.1f, ConsoleWriter.SLOW_TYPE_SPEED);
        assertEquals("", actual);
    }

    @Test
    public void whenTimeIsBiggerThresholdThenReturnT() {
        String actual = ConsoleWriter.generateString("time to play", 0.16f, ConsoleWriter.SLOW_TYPE_SPEED);
        assertEquals("t_", actual);
    }

    @Test
    public void whenTimeIsBiggerThatTextThenReturnFullString() {
        String actual = ConsoleWriter.generateString("time to play", 20, ConsoleWriter.SLOW_TYPE_SPEED);
        assertEquals("time to play_", actual);
    }

    @Test
    public void whenShouldWeAddLastCarretChar() {
        String actual = ConsoleWriter.generateString("time to play", 0.31f, ConsoleWriter.SLOW_TYPE_SPEED);
        assertEquals("t_", actual);

    }

    @Test
    public void whenCalledIsReadyThenReturnTrue() {
        assertTrue(ConsoleWriter.isReady("time to play", 2.2f, ConsoleWriter.SLOW_TYPE_SPEED));
    }
}
