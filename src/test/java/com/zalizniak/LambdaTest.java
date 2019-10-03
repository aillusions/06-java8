package com.zalizniak;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.ToLongBiFunction;

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
        Function<String, String> fnTrim = String::trim;

        // apply
        Assert.assertEquals("hello", fnTrim.apply(" hello "));

        // compose / andThen
        Assert.assertEquals("ello", fnTrim.compose(a -> a.toString().replace(" h", "")).apply(" hello "));
        Assert.assertEquals("hello", fnTrim.andThen(a -> a.replace(" h", "")).apply(" hello "));

        // computeIfAbsent
        Map<String, Integer> nameMap = new HashMap<>();
        Integer value = nameMap.computeIfAbsent("John", String::length);

        IntFunction<String> intFn = Integer::toString;
        Assert.assertEquals("111", intFn.apply(111));

        ToLongBiFunction<Long, Long> toLongBiFn = (a, b) -> a + b;
        Assert.assertEquals(3, toLongBiFn.applyAsLong(1L, 2L));
    }
}
