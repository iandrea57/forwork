package com.mmz.forwork.algorithm.sort;

import com.mmz.forwork.utils.PrintUtils;

/**
 * Created by dra on 15-4-13.
 */
public class HeapSort {

    private static void exchange(int[] A, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }

    private static void maxHeapify(int[] A, int size, int i) {
        int l = 2 * i;
        int r = 2 * i + 1;
        int largest = i;
        if (l <= size && A[l - 1] > A[largest - 1])
            largest = l;
        if (r <= size && A[r - 1] > A[largest - 1])
            largest = r;
        if (largest != i) {
            exchange(A, i - 1, largest - 1);
            maxHeapify(A, size, largest);
        }
    }

    private static void buildMaxHeap(int[] A) {
        int size = A.length;
        for (int i = size / 2; i >= 1; i--) {
            maxHeapify(A, size, i);
        }
    }

    public static void sort(int[] A) {
        buildMaxHeap(A);
        for (int i = A.length; i >= 2; i--) {
            exchange(A, 0, i - 1);
            maxHeapify(A, i - 1, 1);
        }
    }

    public static void main(String[] args) {
        int[] A = {12, 323, 11, 23, 4, 14, 53, 6};
        PrintUtils.println(A);
        HeapSort.sort(A);
        PrintUtils.println(A);
    }
}
