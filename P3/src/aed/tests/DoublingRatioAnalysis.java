package aed.tests;

import aed.sorting.QuickSort;
import aed.utils.ArrayGenerator;
import aed.utils.TemporalAnalysisUtils;

public class DoublingRatioAnalysis {

    public static void main(String[] args) {
        int iterations = 10;

        // Run tests for QuickSort
        System.out.println("QuickSort Doubling Ratio Analysis (Random Array):");
        TemporalAnalysisUtils.runDoublingRatioTest(
                ArrayGenerator::generateRandomArray,
                QuickSort::sort, // QuickSort sorting method
                iterations
        );

        // Run tests for MedianSort
        System.out.println("MedianSort Doubling Ratio Analysis (Random Array):");
        TemporalAnalysisUtils.runDoublingRatioTest(
                ArrayGenerator::generateRandomArray,
                QuickSort::medianSort, // MedianSort method
                iterations
        );

        // Run tests for QuickSelect
        System.out.println("QuickSelect Doubling Ratio Analysis (Random Array):");
        TemporalAnalysisUtils.runDoublingRatioTest(
                size -> {
                    Integer[] array = ArrayGenerator.generateRandomArray(size);
                    int k = size / 2; // Select the median element for simplicity
                    return new QuickSelectTest(array, k);
                },
                test -> QuickSort.quickSelect(test.array, test.k), // QuickSelect method
                iterations
        );
    }

    // Helper class for QuickSelect tests
    private static class QuickSelectTest {
        Integer[] array;
        int k;

        public QuickSelectTest(Integer[] array, int k) {
            this.array = array;
            this.k = k;
        }
    }
}
