package aed.sorting;

import java.util.Arrays;
import java.util.Random;

public class QuickSort extends Sort {

    private final static Random rand = new Random();
    private final static int Cut_Off = 16;

    private static <T extends Comparable<T>> int partition(T[] a, int start, int end) {
        int pivotIndex = start + rand.nextInt(end - start);
        T pivot = a[pivotIndex];
        exchange(a, start, pivotIndex);
        int left = start + 1;
        int right = end - 1;

        while (left <= right) {
            while (left <= right && less(a[left], pivot)) left++;
            while (left <= right && less(pivot, a[right])) right--;

            if (left <= right) {
                exchange(a, left, right);
                left++;
                right--;
            }
        }
        exchange(a, start, right);
        return right;
    }

    private static <T extends Comparable<T>> void quicksort(T[] a, int start, int end) {
        if (end - start <= Cut_Off) { //se o array ou a partição for menor que o valor cutt of
            insertionSort(a, start, end - 1); //faz o insertion sort para ser mais eficiente
            return;
        }
        int p = partition(a, start, end); // faz a partição
        quicksort(a, start, p); //ordena a parte esqurda
        quicksort(a, p + 1, end); //ordena a parte direita
    }


    public static <T extends Comparable<T>> void sort(T[] a) {
        if (a.length > 1) {
            quicksort(a, 0, a.length);
        }
    }

    private static <T extends Comparable<T>> int medianPartition(T[] a, int start, int end) {
        int i = start;
        int j = end + 1;
        T pivot = a[start];

        while (true){
            while (less(a[++i], pivot)) {};
            while (less(pivot, a[--j])) {};

            if ( i >= j) break;
            exchange(a, i, j);
        }
        exchange(a, start, j);
        return j;
    }

    public static <T extends Comparable<T>> void medianSort(T[] a) {
        if (a.length > 1){
            medianSort(a, 0, a.length -1);
        }
    }

    private static <T extends Comparable<T>> void medianSort(T[] a, int low, int high) {
        if (high - low <= Cut_Off) {
            insertionSort(a, low, high);
            return;
        }

        int pivotIndex = medianOfThree(a, low, high);
        exchange(a, low, pivotIndex);
//pivo nao é aleatorio, e os tamamhos das partições serao mais parecidos por começar no meio
        int partitionIndex = medianPartition(a, low, high);

        medianSort(a, low, partitionIndex - 1);
        medianSort(a, partitionIndex + 1, high);
    }

    private static <T extends Comparable<T>> int medianOfThree(T[] a, int low, int high) {
        int mid = low + (high - low) / 2;
//pega um elemento do incio, meio e fim do array, e troca eles por ordem crescente
        //ordenar os 3
        if (less(a[mid], a[low])) exchange(a, low, mid);
        if (less(a[high], a[low])) exchange(a, low, high);
        if (less(a[high], a[mid])) exchange(a, mid, high);

        exchange(a, low, mid);
        return low;
    }

    private static <T extends Comparable<T>> void insertionSort(T[] a, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            T temp = a[i];
            int j = i;
            while (j > low && less(temp, a[j - 1])) {
                a[j] = a[j - 1];
                j--;
            }
            a[j] = temp;
        }
    }
//arruma o lado do array onde o indice esta
    private static <T extends Comparable<T>> int quickSelectPartition(T[] a, int low, int high) {
        int pivotIndex = medianOfThree(a, low, high);
        T pivot = a[pivotIndex];
        exchange(a, pivotIndex, high);
        int i = low;

        for (int j = low; j < high; j++) {
            if (less(a[j], pivot)) {
                exchange(a, i, j);
                i++;
            }
        }

        exchange(a, i, high);
        return i;
    }

    //ordena o lado que esta o indice que eu quero, e acha a posição que o n deveria estar quando ordenado
    public static <T extends Comparable<T>> T quickSelect(T[] a, int n) {
        if ( n < 0 || n >= a.length ) {
            throw new IllegalArgumentException("n is out of range");
        }
        return quickSelect(a, 0, a.length - 1, n);
    }

    // faz a partição e decide para qual lado vai
    private static <T extends Comparable<T>> T quickSelect(T[] a, int low, int high, int n) {
        while (low <= high) {
            int pivotIndex = quickSelectPartition(a, low, high);
            if (n == pivotIndex) {
                return a[pivotIndex];
            } else if (n < pivotIndex) {
                high = pivotIndex - 1;
            } else {
                low = pivotIndex + 1;
            }
        }
        return a[low];
    }


    private static void unitTestSort (){
        {
            Integer[] a = {};
            sort(a);
            assert isSorted(a);
        }
        {
            Integer[] a = {27};
            sort(a);
            Integer[] b = {27};
            assert Arrays.equals(a, b);
        }
        {
            Integer[] a = {53, 22};
            sort(a);
            Integer [] b = {22, 53};
            assert Arrays.equals(a, b);
        }
        {
            Integer[] a = {17, 4, 16, 6, 2, 18, 12, 0, 13, 14, 8, 29, 15};
            sort(a);
            Integer[] b = {0, 2, 4, 6, 8, 12, 13, 14, 15, 16, 17, 18, 29};
            assert Arrays.equals(a, b);
        }
        {
            Integer[] a = {6, 6, 6, 6, 6, 6, 6};
            sort(a);
            Integer[] b = {6, 6, 6, 6, 6, 6, 6};
            assert Arrays.equals(a, b);
        }
        {
            Integer[] a = new Integer[1000];
            for (int i = 0; i < a.length; i++)
                a[i] = rand.nextInt();
            sort(a);
            assert isSorted(a);
        }

    }

    private static void unitTestMedian () {
        {
            Integer[] a = {3,2,5,7,11,17,12,6,22,18};
            medianSort(a);
            System.out.println(Arrays.toString(a));
            Integer[] b = {2,3,5,6,7,11,12,17,18,22};
            assert Arrays.equals(a, b);
        }
        {
            Integer[] a = {100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83, 82, 81, 80, 79, 78, 77, 76, 75, 74, 73, 72, 71, 70, 69, 68, 67, 66, 65, 64, 63, 62, 61, 60, 59, 58, 57, 56, 55, 54, 53, 52, 51, 50, 49, 48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33, 32, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
            medianSort(a);
            System.out.println(Arrays.toString(a));
            Integer[] b = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100};
            assert Arrays.equals(a, b);
        }
    }

    private static void unitTestQuickSelect () {
        {
            Integer[] a = {27};
            Integer result = quickSelect(a, 0);
            assert result.equals(27) : "Expected 27, but got " + result;
        }
        {
            Integer[] a = {53, 22};
            Integer result = quickSelect(a, 0);
            assert result.equals(22) : "Expected 22, but got " + result;


            result = quickSelect(a, 1);
            assert result.equals(53) : "Expected 53, but got " + result;
        }

        {
            Integer[] a = {1, 3, 5, 6, 7, 9, 10};
            Integer result = quickSelect(a, 0);
            assert result.equals(1) : "Expected 1, but got " + result;

            result = quickSelect(a, 1);
            assert result.equals(3) : "Expected 1, but got " + result;
        }

        {
            Integer[] a = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
            Integer result = quickSelect(a, 3);
            assert result.equals(4) : "Expected 4, but got " + result;
        }

        {
            Integer[] a = {1, 2, 2, 3, 2, 4, 4, 5};
            Integer result = quickSelect(a, 3);
            assert result.equals(2) : "Expected 2, but got " + result;
        }
    }


    private static void unitTests(){
        unitTestSort();
        unitTestMedian();
        unitTestQuickSelect();
    }

    public static void main(String[] args) {
        unitTests();
        System.out.println("Class quickSort all tests complete.");
    }
}