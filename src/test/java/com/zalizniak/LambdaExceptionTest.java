package com.zalizniak;

import org.junit.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LambdaExceptionTest {

    @Test
    public void test() {
        List<String> list =
                IntStream.range(0, 10)
                        .boxed()
                        .map(wrap(this::generateTask))
                        .collect(Collectors.toList());
        System.out.println(list.toString());
    }

    // Trows checked exception
    private String generateTask(int i) throws Exception {
        return i + "-" + "test";
    }

    private static <T, R> Function<T, R> wrap(CheckedFunction<T, R> checkedFunction) {
        return t -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        R apply(T t) throws Exception;
    }
}
