package com.mmz.forwork.algorithm.sort;

import com.mmz.forwork.utils.PrintUtils;

/**
 * Created by dra on 15-4-13.
 */
public class QuickSort {

    private static void exchange(int[] A, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }

    private static int partition(int[] A, int p, int r) {
        int x = A[r - 1];
        int i = p - 1;
        for (int j = p; j <= r - 1; j++) {
            if (A[j - 1] <= x) {
                i++;
                exchange(A, i - 1, j - 1);
            }
        }
        i++;
        exchange(A, i - 1, r - 1);
        return i;
    }

    public static void sort(int[] A, int p, int r) {
        if (p < r) {
            int q = partition(A, p, r);
            sort(A, p, q - 1);
            sort(A, q + 1, r);
        }
    }

    public static void main(String[] args) {
        int[] A = {12, 323, 11, 23, 4, 14, 53, 6};
        PrintUtils.println(A);
        QuickSort.sort(A, 1, A.length);
        PrintUtils.println(A);

    }
}
