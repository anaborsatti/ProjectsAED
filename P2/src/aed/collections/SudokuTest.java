package aed.collections;

import aed.search.SudokuState;
import java.util.Random;
import java.util.function.Function;

public class SudokuTest {

    public static void main(String[] args) {
        System.out.println("Testing backtrackingSearch with StackList...");
        empiricalTestWithStack(SudokuState::backtrackingSearch);

        System.out.println("Testing backtrackingSearch with ShittyStack...");
        empiricalTestWithShittyStack(SudokuState::backtrackingSearch);
    }

    private static void empiricalTestWithStack(Function<SudokuState, SudokuState> solver) {
        System.out.printf("Testing StackList:%n");
        System.out.printf("%-2s\t%-10s\t%-15s\t%-10s%n", "i", "complexity", "time(ms)", "estimated r");

        double previousTime = -1;

        for (int emptyCells = 1; emptyCells <= 32; emptyCells *= 2) {
            long totalTime = 0;
            int iterations = 10; // Run multiple iterations to get average time

            for (int i = 0; i < iterations; i++) {
                SudokuState state = generateSudokuBoardWithEmptyCells(emptyCells);
                long startTime = System.nanoTime();
                solver.apply(state);
                long endTime = System.nanoTime();

                totalTime += (endTime - startTime);
            }

            long averageTime = totalTime / iterations;
            double estimatedR = previousTime > 0 ? (double) averageTime / previousTime : Double.NaN;
            previousTime = averageTime;

            System.out.printf("%-2d\t%-10d\t%.5f     \t%.5f%n", (emptyCells), emptyCells, averageTime / 1_000_000.0, estimatedR);
        }
    }

    private static void empiricalTestWithShittyStack(Function<SudokuState, SudokuState> solver) {
        System.out.printf("Testing ShittyStack:%n");
        System.out.printf("%-2s\t%-10s\t%-15s\t%-10s%n", "i", "complexity", "time(ms)", "estimated r");

        double previousTime = -1;

        for (int emptyCells = 1; emptyCells <= 32; emptyCells *= 2) {
            long totalTime = 0;
            int iterations = 10; // Run multiple iterations to get average time

            for (int i = 0; i < iterations; i++) {
                SudokuState state = generateSudokuBoardWithEmptyCells(emptyCells);
                long startTime = System.nanoTime();
                solver.apply(state);
                long endTime = System.nanoTime();

                totalTime += (endTime - startTime);
            }

            long averageTime = totalTime / iterations;
            double estimatedR = previousTime > 0 ? (double) averageTime / previousTime : Double.NaN;
            previousTime = averageTime;

            System.out.printf("%-2d\t%-10d\t%.5f        \t%.5f%n", emptyCells, emptyCells, averageTime / 1_000_000.0, estimatedR);
        }
    }

    private static SudokuState generateSudokuBoardWithEmptyCells(int emptyCells) {
        int[][] board = new int[9][9];
        fillWithValidSudoku(board); // Fill the board with a valid Sudoku configuration

        Random random = new Random();
        // Randomly empty cells until the desired number of empty cells is reached
        while (emptyCells > 0) {
            int row, col;
            do {
                row = random.nextInt(9);
                col = random.nextInt(9);
            } while (board[row][col] == 0); // Ensure we only remove filled cells

            board[row][col] = 0; // Set the cell to empty
            emptyCells--;
        }

        return new SudokuState(board);
    }

    private static void fillWithValidSudoku(int[][] board) {
        // Implement a method to fill the board with a valid Sudoku configuration
        // This is a basic implementation of filling the Sudoku board
        fillSudoku(board);
    }

    private static boolean fillSudoku(int[][] board) {
        // Basic backtracking Sudoku fill implementation
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (fillSudoku(board)) {
                                return true;
                            }
                            board[row][col] = 0; // Backtrack
                        }
                    }
                    return false; // Trigger backtracking
                }
            }
        }
        return true; // Successfully filled the board
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        // Check if num is not in the same row, column, or 3x3 box
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num || board[x][col] == num ||
                    board[row - row % 3 + x / 3][col - col % 3 + x % 3] == num) {
                return false;
            }
        }
        return true;
    }
}
