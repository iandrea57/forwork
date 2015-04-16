package com.mmz.forwork.algorithm.string;

/**
 * Created by dra on 15-4-13.
 */
public class Sunday {

    public static int find(char[] text, char[] find) {
        int[] next = buildNext(find);
        int textLength = text.length;
        int findLength = find.length;
        int i = 0;
        while (i <= textLength - findLength) {
            int j = 0;
            while (j < findLength && text[i + j] == find[j])
                j++;
            if (j == findLength) {
                return i;
            }
            i += next[text[i + findLength]];
        }
        return -1;
    }

    private static int[] buildNext(char[] find) {
        int[] next = new int[256];
        for (int i = 0; i < next.length; i++)
            next[i] = find.length + 1;
        for (int i = 0; i < find.length; i++)
            next[find[i]] = find.length - i;
        return next;
    }

    public static void main(String[] args) {
        String a = "abcdacdaahfacabcdabcdeaa";
        String b = "abcde";
        int index = new Sunday().find(a.toCharArray(), b.toCharArray());
        System.out.println(index);
    }
}
