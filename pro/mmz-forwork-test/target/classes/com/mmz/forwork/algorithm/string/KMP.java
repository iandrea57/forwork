package com.mmz.forwork.algorithm.string;

import com.mmz.forwork.utils.PrintUtils;

/**
 * Created by dra on 15-4-11.
 */
public class KMP {

    public static int find(char[] text, char[] find) {
        if (text == null || find == null || text.length == 0 || find.length == 0) {
            return -1;
        }
        int textLength = text.length;
        int findLength = find.length;
        if (textLength < findLength) {
            return -1;
        }
        PrintUtils.println(find);
        int[] prefix = computePrefixFunction(find);
        PrintUtils.println(prefix);
        int q = -1;
        for (int i = 0; i < textLength; i++) {
            while (q > -1 && find[q+1] != text[i])
                q = prefix[q];
            if (find[q+1] == text[i])
                q++;
            if (q == findLength - 1) {
                return i - q;
            }
        }
        return -1;
    }

    private static int[] computePrefixFunction(char[] find) {
        int length = find.length;
        int[] prefix = new int[length];
        prefix[0] = -1;
        int k = -1;
        for (int i = 1; i < length; i++) {
            while(k > -1 && find[k+1] != find[i])
                k = prefix[k];
            if (find[k+1] == find[i])
                k++;
            prefix[i] = k;
        }
        return prefix;
    }
}
