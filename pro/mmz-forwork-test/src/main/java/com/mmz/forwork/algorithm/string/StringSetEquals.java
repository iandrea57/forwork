package com.mmz.forwork.algorithm.string;

/**
 * Created by dra on 15-6-14.
 */
public class StringSetEquals {

    public boolean equals(char[] A, char[] B) {
        char[] C = new char[A.length + B.length];
        int cSize = 0;
        for (char a : A) {
            boolean contains = false;
            for (int i = 0; i < cSize; i++) {
                if (charEqualsIgnoreCase(a, C[i])) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                C[cSize++] = a;
            }
        }
        int[] D = new int[cSize];
        for (char b : B) {
            boolean contains = false;
            for (int i = 0; i < cSize; i++) {
                if (charEqualsIgnoreCase(b, C[i])) {
                    contains = true;
                    D[i]++;
                    break;
                }
            }
            if (!contains) {
                return false;
            }
        }
        for (int d : D) {
            if (d == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean charEqualsIgnoreCase(char a1, char a2) {
        if ((a1 >= 'a' && a1 <= 'z' || a1 >= 'A' && a1 <= 'Z') && (a2 >= 'a' && a2 <= 'z' || a2 >= 'A' && a2 <= 'Z')) {
            return (a1 - a2 == 0 || a1 - a2 == ('a' - 'A') || a1 - a2 == ('A' - 'a'));
        } else {
            return a1 - a2 == 0;
        }
    }

    public static void main(String[] args) {
        StringSetEquals equals = new StringSetEquals();
        String A = "Absccseasg";
        String B = "aBsscEQ";
        System.out.println(equals.equals(A.toCharArray(), B.toCharArray()));
    }
}
