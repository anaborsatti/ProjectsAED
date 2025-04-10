package aed.tests;

import java.util.ArrayList;
import java.util.List;
import aed.trees.FTree;  // Correct import for FTree class (with uppercase 'T')

import aed.utils.TemporalAnalysisUtils;

public class FTreeTest {

    public static void main(String[] args) {
        // Generate keys (1 million keys for testing)
        List<Integer> keys = generateKeys(1000000);

        // Create two Ftrees with different minimum degrees
        FTree<Integer, Integer> treeDegree128 = new FTree<>(128);  // 2^7 = 128
        FTree<Integer, Integer> treeDegree10 = new FTree<>(10);    // Degree 10

        // Insert keys into both trees
        for (int key : keys) {
            // Use the key as the value (you can modify this to any logic you prefer)
            treeDegree128.put(key, key);
            treeDegree10.put(key, key);
        }

        // Perform empirical tests on the 'put' and 'get' methods
        System.out.println("Testing FTree with min degree 128:");
        testTreePerformance(treeDegree128, keys);

        System.out.println("\nTesting FTree with min degree 10:");
        testTreePerformance(treeDegree10, keys);

        // Print the maximum depth of both trees
        System.out.println("\nMax Depth of FTree with degree 128: " + treeDegree128.height());
        System.out.println("Max Depth of FTree with degree 10: " + treeDegree10.height());

        // Empirical tests for size method (size between two keys)
        System.out.println("\nTesting size method on FTree with degree 128:");
        testSizeMethod(treeDegree128, 1000, 500000);

        System.out.println("\nTesting size method on FTree with degree 10:");
        testSizeMethod(treeDegree10, 1000, 500000);
    }

    private static List<Integer> generateKeys(int size) {
        List<Integer> keys = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            keys.add((int) (Math.random() * size));
        }
        return keys;
    }

    private static void testTreePerformance(FTree<Integer, Integer> tree, List<Integer> keys) {
        // Test 'put' method (insertion time)
        Runnable putTest = () -> {
            for (int key : keys) {
                tree.put(key, key);  // Corrected: passing key and key as value
            }
        };
        long putTime = TemporalAnalysisUtils.getAverageCPUTime(putTest, 30);
        System.out.println("Average put time: " + putTime / 1E6 + " ms");

        // Test 'get' method (retrieval time)
        Runnable getTest = () -> {
            for (int key : keys) {
                tree.get(key);
            }
        };
        long getTime = TemporalAnalysisUtils.getAverageCPUTime(getTest, 30);
        System.out.println("Average get time: " + getTime / 1E6 + " ms");

        // Run Doubling Ratio Test (optional for larger datasets)
        TemporalAnalysisUtils.runDoublingRatioTest(
                n -> generateKeys(n),
                keysList -> {
                    for (int key : keysList) {
                        tree.put(key, key);  // Corrected: passing key and key as value
                    }
                },
                5
        );
    }

    private static void testSizeMethod(FTree<Integer, Integer> tree, int minKey, int maxKey) {
        // Test the size method (number of elements in range [minKey, maxKey])
        Runnable sizeTest = () -> {
            tree.size(minKey, maxKey);
        };
        long sizeTime = TemporalAnalysisUtils.getAverageCPUTime(sizeTest, 30);
        System.out.println("Average size method time (range " + minKey + " to " + maxKey + "): " + sizeTime / 1E6 + " ms");
    }
}
