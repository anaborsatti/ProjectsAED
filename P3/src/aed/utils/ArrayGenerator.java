package aed.utils;

import java.util.Arrays;
import java.util.Random;

public class ArrayGenerator {

    public static Integer[] generateSortedArray(int n) {
        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) arr[i] = i;
        return arr;
    }

    public static Integer[] generateReverseArray(int n) {
        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) arr[i] = n - i - 1;
        return arr;
    }

    public static Integer[] generatePartiallySortedArray(int n) {
        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) arr[i] = i < n / 2 ? i : n - i;
        return arr;
    }

    public static Integer[] generateRandomArray(int n) {
        Integer[] arr = new Integer[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) arr[i] = random.nextInt(n);
        return arr;
    }
}
