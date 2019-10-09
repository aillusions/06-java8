package com.zalizniak;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {

    //
    public Integer sum(Integer i, Integer j) {
        return IntStream.of(i, j).sum();
    }

    //
    public Integer factorial(Integer n) {
        return IntStream.rangeClosed(1, n + 1).reduce((x, y) -> x * y).getAsInt();
    }

    //
    public List<Integer> fibonacci(Integer series) {
        return Stream.iterate(new int[]{0, 1}, i -> new int[]{i[1], i[0] + i[1]})
                .limit(series)
                .map(i -> i[0])
                .collect(Collectors.toList());
    }

    @Test
    public void lazy() {

        Optional<Integer> result = Stream.of(1, 10, 3, 7, 5)
                .peek(num -> System.out.println("will filter " + num))
                .filter(x -> x > 5)
                .findFirst();

        System.out.println(result.get());
    }


    @Test
    public void testMap() {
        System.out.println(IntStream.of(1, 2, 3, 4, 5).boxed().map(i -> i * i).collect(Collectors.toList()));
    }

    @Test
    public void testFlatMap() {
        System.out.println(IntStream.of(1, 2, 3, 4, 5).boxed().flatMap(i -> Stream.of(i, i * i)).collect(Collectors.toList()));
    }

    @Test
    public void testFlatMap2() {
        List<Integer> together = Stream.of(Arrays.asList(1, 2), Arrays.asList(3, 4)) // Stream of List<Integer>
                .flatMap(List::stream)
                .map(integer -> integer + 1)
                .collect(Collectors.toList());

        Assert.assertEquals(Arrays.asList(2, 3, 4, 5), together);
    }

    @Test
    public void testError() {

        try {
            IntStream.of(1, 2, 3, 4, 5)
                    .boxed()
                    .filter(i -> i >= 2 && i <= 4)
                    .map(i -> {
                        throw new RuntimeException("Ooops...");
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Error happened: " + e);
        }
    }
}
