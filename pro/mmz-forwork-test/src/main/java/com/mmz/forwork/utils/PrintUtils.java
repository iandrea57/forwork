package com.mmz.forwork.utils;

/**
 * Created by dra on 15-4-11.
 */
public class PrintUtils {

    private PrintUtils() {}

    public static void println(Object[] P) {
        for (Object p : P) {
            System.out.printf("%4s", p);
        }
        System.out.println();
    }

    public static void println(int[] P) {
        for (Object p : P) {
            System.out.printf("%4s", p);
        }
        System.out.println();
    }

    public static void println(char[] P) {
        for (Object p : P) {
            System.out.printf("%4s", p);
        }
        System.out.println();
    }
}
