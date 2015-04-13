package com.mmz.forwork.algorithm.string;

import com.mmz.forwork.utils.PrintUtils;

/**
 * Created by dra on 15-4-12.
 */
public class BM {

    public static int find(char[] text, char[] find) {
        if (text == null || find == null || text.length == 0 || find.length == 0) {
            return -1;
        }
        int textLength = text.length;
        int findLength = find.length;
        if (textLength < findLength) {
            return -1;
        }
        int[] bad = buildBad(find);
        int[] good = buildGood(find);
        int i = findLength - 1;
        int j = i;
        while (i < textLength) {
            //发现目标传与模式传从后向前第1个不匹配的位置
            while(j > 0 && text[i] == find[j]) {
                i--;
                j--;
            }
            if (j == 0 && text[i] == find[j]) {
                return i;
            } else {
                int badSkip = bad[text[i]];
                badSkip = badSkip > findLength - 1 - j ? badSkip : findLength - j;
                i += good[j] > badSkip ? good[j] : badSkip;
            }
            j = findLength - 1;
        }
        return -1;
    }

    private static int[] buildGood(char[] find) {
        int findLength = find.length;
        int[] suffix = suffix(find);
        int[] goodSuffix = new int[findLength];
        for (int i = 0; i < findLength; i++) {
            goodSuffix[i] = findLength;
        }
        int j = 0;
        for (int i = findLength - 1; i >= 0; i--) {
            if (suffix[i] == i + 1) {
                for (; j < findLength - 1 - i; j++) {
                    if (goodSuffix[j] == findLength) {
                        goodSuffix[j] = findLength - 1 - i;
                    }
                }
            }
        }
        for (int i = 0; i <= findLength - 2; i++) {
            goodSuffix[findLength - 1 - suffix[i]] = findLength - 1 - i;
        }
        PrintUtils.println(find);
        PrintUtils.println(goodSuffix);
        return goodSuffix;
    }

    private static int[] suffixOld(char[] find) {
        int findLength = find.length;
        int[] suffix = new int[findLength];
        suffix[findLength - 1] = findLength;
        for (int i = findLength - 2; i >= 0; i--) {
            int j = i;
            while (j >= 0 && find[j] == find[findLength - 1 - i + j])
                j--;
            suffix[i] = i - j;
        }

        PrintUtils.println(find);
        PrintUtils.println(suffix);
        return suffix;
    }

    private static int[] suffix(char[] find) {
        int findLength = find.length;
        int[] suffix = new int[findLength];
        suffix[findLength - 1] = findLength;
        int g = findLength - 1;
        int f = 0;
        for (int i = findLength - 2; i >= 0; i--) {
            if (i > g && suffix[findLength - 1 + i - f] < i - g) {
                suffix[i] = suffix[findLength - 1 + i - f];
            } else {
                if (i < g) {
                    g = i;
                }
                f = i;
                while (g >= 0 && find[g] == find[findLength - 1 - i + g]) {
                    g--;
                }
                suffix[i] = f - g;
            }
        }

        PrintUtils.println(find);
        PrintUtils.println(suffix);
        return suffix;
    }

    private static int[] buildBad(char[] find) {
        int[] bad = new int[256];
        int findLength = find.length;
        for (int i = 0; i < 256; i++) {
            bad[i] = findLength;
        }
        for (int i = 0; i < findLength - 1; i++) {
            bad[find[i]] = findLength - 1 - i;
        }
        // print
        PrintUtils.println(find);
        for (char ch : find) {
            System.out.printf("%4s", bad[ch]);
        }
        System.out.println();
        return bad;
    }

    public static void main(String[] args) {
        String text = "abcaacbabbacabcabababb";
        String find = "bcababab";
        System.out.println(KMP.find(text.toCharArray(), find.toCharArray()));
        System.out.println(BM.find(text.toCharArray(), find.toCharArray()));

        String test = "aaaaabaaaa";
        BM.suffix(test.toCharArray());
    }
}
