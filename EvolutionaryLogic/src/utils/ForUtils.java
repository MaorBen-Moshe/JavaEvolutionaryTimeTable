package utils;

import java.util.function.Consumer;
import java.util.stream.IntStream;

public class ForUtils {
    public static void forI(int start, int endInclusive, Consumer<Integer> doSomething){
        IntStream.range(start, endInclusive + 1).forEach(doSomething::accept);
    }
}
