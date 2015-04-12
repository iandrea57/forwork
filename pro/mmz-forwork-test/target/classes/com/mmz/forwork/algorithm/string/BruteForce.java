package com.mmz.forwork.algorithm.string;

/**
 * Created by dra on 15-4-11.
 */
public class BruteForce {

    public static int find(char[] text, char[] find) {
        if (text == null || find == null || text.length == 0 || find.length == 0) {
            return -1;
        }
        int textLength = text.length;
        int findLength = find.length;
        if (textLength < findLength) {
            return -1;
        }
        char first = find[0];
        for (int i = 0; i < textLength; i++) {
            while(i < textLength && text[i] != first) {
                i++;
            }
            if (i < textLength) {
                int j = i + 1;
                int end = j + findLength - 1;
                for (int k = 1; j < end && text[j] == find[k]; j++,k++);
                if (j == end) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String text = "here isimmplee example";
        String find = "example";
        System.out.println(find(text.toCharArray(), find.toCharArray()));
        System.out.println(KMP.find(text.toCharArray(), find.toCharArray()));
        System.out.println(BM.find(text.toCharArray(), find.toCharArray()));
        text.indexOf("ddd");
    }
}
