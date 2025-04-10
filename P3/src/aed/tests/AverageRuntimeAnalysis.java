package aed.tests;

import aed.sorting.QuickSort;
import aed.utils.ArrayGenerator;
import aed.utils.TemporalAnalysisUtils;

public class AverageRuntimeAnalysis {

    public static void main(String[] args) {
        int largeSize = 1_000_000; // Example large array size

        // Generate test arrays
        Integer[] randomArray = ArrayGenerator.generateRandomArray(largeSize);
        Integer[] sortedArray = ArrayGenerator.generateSortedArray(largeSize);
        Integer[] reverseArray = ArrayGenerator.generateReverseArray(largeSize);
        Integer[] partiallySortedArray = ArrayGenerator.generatePartiallySortedArray(largeSize);

        // QuickSort Average Runtime Analysis
        System.out.println("Average Execution Time for Large Arrays (QuickSort):");
        System.out.println("Random Array: " +
                TemporalAnalysisUtils.getAverageCPUTime(() -> QuickSort.sort(randomArray)) + " ns");
        System.out.println("Sorted Array: " +
                TemporalAnalysisUtils.getAverageCPUTime(() -> QuickSort.sort(sortedArray)) + " ns");
        System.out.println("Reverse Array: " +
                TemporalAnalysisUtils.getAverageCPUTime(() -> QuickSort.sort(reverseArray)) + " ns");
        System.out.println("Partially Sorted Array: " +
                TemporalAnalysisUtils.getAverageCPUTime(() -> QuickSort.sort(partiallySortedArray)) + " ns");

        // MedianSort Average Runtime Analysis
        System.out.println("Average Execution Time for Large Arrays (MedianSort):");
        System.out.println("Random Array: " +
                TemporalAnalysisUtils.getAverageCPUTime(() -> QuickSort.medianSort(randomArray)) + " ns");
        System.out.println("Sorted Array: " +
                TemporalAnalysisUtils.getAverageCPUTime(() -> QuickSort.medianSort(sortedArray)) + " ns");
        System.out.println("Reverse Array: " +
                TemporalAnalysisUtils.getAverageCPUTime(() -> QuickSort.medianSort(reverseArray)) + " ns");
        System.out.println("Partially Sorted Array: " +
                TemporalAnalysisUtils.getAverageCPUTime(() -> QuickSort.medianSort(partiallySortedArray)) + " ns");

        // QuickSelect Average Runtime Analysis
        int k = largeSize / 2; // Example: finding the median element
        System.out.println("Average Execution Time for Large Arrays (QuickSelect):");
        System.out.println("Random Array: " +
                TemporalAnalysisUtils.getAverageCPUTime(() -> QuickSort.quickSelect(randomArray, k)) + " ns");
        System.out.println("Sorted Array: " +
                TemporalAnalysisUtils.getAverageCPUTime(() -> QuickSort.quickSelect(sortedArray, k)) + " ns");
        System.out.println("Reverse Array: " +
                TemporalAnalysisUtils.getAverageCPUTime(() -> QuickSort.quickSelect(reverseArray, k)) + " ns");
        System.out.println("Partially Sorted Array: " +
                TemporalAnalysisUtils.getAverageCPUTime(() -> QuickSort.quickSelect(partiallySortedArray, k)) + " ns");
    }
}
