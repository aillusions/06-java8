package com.zalizniak;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;
import java.util.function.Predicate;

public class LambdaTest {

    @FunctionalInterface
    interface Square {
        int calculate(int x);
    }

    @Test
    public void testCustomFunctionalInterface() {
        Square s = (int x) -> x * x;
        Assert.assertEquals(9, s.calculate(3));
    }

    @Test
    public void testPredicate() {
        Predicate<String> p = (s) -> s.startsWith("G");
        Assert.assertTrue(p.test("Grr"));
        Assert.assertFalse(p.test("TT"));
    }

    @Test
    public void testFunction() {
        Function<String, String> fnTrim = in -> in.trim();
        Assert.assertEquals("hello", fnTrim.apply(" hello "));
    }
}
