package utils;

import java.util.Random;

public class RandomUtils {

    public static int nextIntInRange(int min, int max){
        if(min >= max){
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static double nextDouble(){
        return new Random().nextDouble();
    }
}
