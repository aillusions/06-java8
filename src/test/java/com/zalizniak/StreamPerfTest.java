package com.zalizniak;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// Stream is ~30 times slower for same operation...
public class StreamPerfTest {

    int iterations = 100;
    List<Integer> list = Arrays.asList(1, 10, 3, 7, 5);


    // 55 ms
    @Test
    public void stream() {

        for (int i = 0; i < iterations; i++) {
            Optional<Integer> result = list.stream()
                    .filter(x -> x > 5)
                    .findFirst();

            System.out.println(result.orElse(null));
        }
    }

    // 2 ms
    @Test
    public void loop() {

        for (int i = 0; i < iterations; i++) {
            Integer result = null;
            for (Integer walk : list) {
                if (walk > 5) {
                    result = walk;
                    break;
                }
            }
            System.out.println(result);
        }
    }
}
