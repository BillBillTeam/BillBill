package lz.math;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by lenovo on 2017/12/2.
 */
public class CalculatorTest {
    private Calculator calculator;
    @Before
    public void setUp() throws Exception {
        calculator = new Calculator();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void conversion() throws Exception {
        double d;
        d = calculator.calculate("213+321");
        assertEquals(d,213+321,0.001);

    }

}