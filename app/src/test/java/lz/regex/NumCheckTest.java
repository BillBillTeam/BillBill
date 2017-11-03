package lz.regex;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 数字匹配的测试
 * Created by LiZeC on 2017/11/3.
 */
public class NumCheckTest {
    @Test
    public void matchDouble() throws Exception {
        assertTrue(NumCheck.matchDouble("12.34"));
        assertTrue(NumCheck.matchDouble("3.212"));
        assertTrue(NumCheck.matchDouble("0.23"));
        assertFalse(NumCheck.matchDouble("1.2.3"));
    }

}