package com.zalizniak;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    @Test
    public void testSupplier() {
        Supplier<Double> lazyValue = () -> 9d;
        Assert.assertEquals(9d, lazyValue.get(), 0.0);

        // Fib
        long[] fibs = {0, 1};
        Stream<Long> fibonacci = Stream.generate(() -> {
            long result = fibs[1];
            long fib3 = fibs[0] + fibs[1];
            fibs[0] = fibs[1];
            fibs[1] = fib3;
            return result;
        });

        fibonacci.limit(50).forEach(System.out::println);
    }

    @Test
    public void testConsumer() {
        Consumer<String> consumer = System.out::println;
        consumer.accept("abc");

        Stream.of("111", "222", "333").parallel().forEach(consumer);
    }

    @Test
    public void tesOperator() {

        // UnaryOperator
        List<String> names = Arrays.asList("bob", "josh", "megan");
        names.replaceAll(name -> name.toUpperCase());
        System.out.println(names);

        // BinaryOperator is a reduction operation
        Assert.assertEquals(15, IntStream.of(1, 2, 3, 4, 5).reduce(0, (i1, i2) -> i1 + i2));
    }
}
