package com.houarizegai.calculator;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CalculatorTest {

    private Calculator calc;

    @BeforeMethod
    public void setUp() {
        calc = new Calculator();
    }

    @Test
    public void testAddition() {
        double result = calc.calc(5, "3", "+");
        Assert.assertEquals(result, 8.0);
    }

    @Test
    public void testSubtraction() {
        double result = calc.calc(10, "4", "-");
        Assert.assertEquals(result, 6.0);
    }

    @Test
    public void testMultiplication() {
        double result = calc.calc(6, "7", "*");
        Assert.assertEquals(result, 42.0);
    }

    @Test
    public void testDivision() {
        double result = calc.calc(20, "5", "/");
        Assert.assertEquals(result, 4.0);
    }

    @Test
    public void testPower() {
        double result = calc.calc(2, "3", "^");
        Assert.assertEquals(result, 8.0);
    }
}
