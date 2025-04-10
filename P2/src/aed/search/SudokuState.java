package aed.search;

//don't forget to place this file in the right folder according to the package definition
// it should be .../src/aed/search

import aed.collections.ShittyStack;
import aed.collections.StackList;

import java.util.ArrayList;
import java.util.List;

public class SudokuState {

    private static final int N = 9;
    private int[][] board;
    private static int[][] board1 = {
            {1, 9, 0, 0, 2, 0, 5, 0 ,8},
            {0, 6, 7, 0, 0, 0, 0, 4, 0},
            {0, 0, 4, 6 , 8, 3, 0, 9, 0},
            {3, 0, 0, 7, 0, 0, 2, 0, 9},
            {0 ,0, 0, 1, 0, 0, 6, 0, 5},
            {0, 0, 0, 5, 9, 8, 0, 0, 4},
            {4, 0, 5, 8, 0, 0, 9, 0, 6},
            {2, 0, 6, 0, 4 ,0, 0, 5, 1},
            {9, 0, 1, 0, 0, 6, 0, 7, 0}
    };
    private static int[][] board2 = {
            {1, 9, 0, 0, 2, 5, 5, 0 ,8},
            {0, 6, 7, 0, 0, 19, 0, 4, 0},
            {0, 0, 1, 6 , 8, 3, 0, 9, 0},
            {3, 0, 0, 7, 0, 0, 2, 0, 9},
            {0 ,0, 1, 1, 1, 1, 6, 0, 5},
            {0, 0, 0, 5, 9, 8, 0, 0, 4},
            {4, 0, 5, 8, 0, 0, 9, 0, 6},
            {2, 0, 6, 0, 4 ,0, 0, 5, 1},
            {9, 0, 1, 0, 0, 6, 0, 7, 0}
    };
    //you can add additional parameters (including internal classes), if needed


    public SudokuState(int[][] board)
    {
        this.board = board;
        //to add additional initialization, if needed
    }

    //this method is here for testing purposes
    public int[][] getBoard()
    {
        return this.board;
    }

    public boolean isSolution() {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                if (board[i][j] == 0) return false;
            }
        }
        return validBoard(board);
    }

    private static int countRow(int[][] a, int r, int x){ //conta quantas vezes x aparece na linha
        int result = 0;
        for (int j= 0; j < N; j++)
            result += a[r][j] == x ? 1 : 0;
        return result;
    }

    private static int countColumn(int[][] a, int c, int x){ //conta quantas vezes x aparece na coluna
        int result = 0;
        for (int i = 0; i < N; i++){
            result += a[i][c] == x ? 1 : 0;
        }
        return result;
    }

    private static int countBox(int[][] a, int r, int c, int x){ //conta quantas vezes x aparece na box
        int result = 0;
        for (int i = 0; i < N/3; i++)
            for (int j = 0; j < N/3; j++)
                result += a[r+i][c+j] == x ? 1 : 0;
        return result;
    }

    private static int countNumbersInRow(int[][] a, int r){ // conta quantos numeros tem na linha
        int result = 0;
        for (int x = 0; x <= N; x++) //less equal
            result += countRow(a, r, x);
        return result;
    }

    private static int countNumbersInColl(int[][] a, int c){// conta quantos numeros tem na coluna
        int result = 0;
        for (int x = 0; x <= N; x++) //less equal
            result += countColumn(a, c, x);
        return result;
    }

    private static int countNumbersInBox(int[][] a, int r, int c){ // conta quantos numeros tem na box
        int result = 0;
        for (int x = 0; x <= N; x++) //less equal
            result += countBox(a, r, c, x);
        return result;
    }

    private static boolean isRowValid(int[][] a, int r){ //testa se cada numero so aparece uma vez na linha
        for (int x = 1; x <= N; x++)
            if (countRow(a, r, x) > 1)
                return false;
        return true;
    }

    private static boolean isColValid(int[][] a, int c){//testa se cada numero so aparece uma vez na coluna
        for (int x = 1; x <= N; x++)
            if (countColumn(a, c, x) > 1)
                return false;
        return true;
    }

    private static boolean isBoxValid(int[][] a, int r, int c){ //testa se cada numero so aparece uma box
        for (int x = 1; x <= N; x++)
            if (countBox(a, r, c, x) > 1)
                return false;
        return true;
    }

    private static boolean allRowsValid(int[][] a){ // testa se todas as linhas nao tem numeros repetidos
        for (int i = 0; i< N; i++){
            if (!isRowValid(a, i))
                return false;
        }
        return true;
    }

    private static boolean allColsValid(int[][] a){ // testa se todas as colunas nao tem numeros repetidos
        for (int j = 0; j< N; j++){
            if (!isColValid(a, j))
                return false;
        }
        return true;
    }

    private static boolean allBoxesValid(int[][] a){ // testa se todas as box nao tem numeros repetidos
        for (int i = 0; i < N/3; i++)
            for (int j = 0; j< N/3; j++)
                if (!isBoxValid(a, 3 * i,3 * j))
                    return false;
        return true;
    }

    private static boolean validBoard(int[][] a){ // se tudo estiver correto, testa o board total 9x9
        return allRowsValid(a) && allColsValid(a) && allBoxesValid(a);
    }



    public boolean isValidAction(int row, int column, int digit) {
        if (countRow(board, row, digit) > 0) {
            return false;
        }

        if (countColumn(board, column, digit) > 0) {
            return false;
        }

        int boxStartRow = row - row % 3;
        int boxStartCol = column - column % 3;

        if (countBox(board, boxStartRow, boxStartCol, digit) > 0) {
            return false;
        }

        return true;
    }


    public SudokuState generateNextState(int row, int column, int value) {
        if (!isValidAction(row, column, value)) {
            return null;
        }

        int[][] nextState = new int[N][N];
        for (int i = 0; i < N; i++) {
            nextState[i] = this.board[i].clone();
        }

        nextState[row][column] = value;

        return new SudokuState(nextState);
    }


    public List<SudokuState> generateValidNextStates() {
        List<SudokuState> validStates = new ArrayList<>();

        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                if (board[row][column] == 0) {
                    for (int digit = 1; digit <= 9; digit++) {
                        if (isValidAction(row, column, digit)) {
                            SudokuState newState = generateNextState(row, column, digit);
                            if (newState != null) {
                                validStates.add(newState);
                            }
                        }
                    }
                    return validStates;
                }
            }
        }
        return validStates;
    }

    public static SudokuState backtrackingSearch(SudokuState initialState)
    {
        ShittyStack<SudokuState> stack = new ShittyStack<>();
        stack.push(initialState);

        while (!stack.isEmpty()) {
            SudokuState currentState = stack.pop();

            if (currentState.isSolution()){
                return currentState;
            }

            List<SudokuState> nextStates = currentState.generateValidNextStates();

            for (SudokuState nextState : nextStates) {
                stack.push(nextState);
            }
        }
        return null;
    }

    public SudokuState clone()
    {
        //you can use this method if needed
        int[][] newBoard = this.board.clone();
        //we need to be careful when copying bidimentional arrays. We need to do this:
        for(int i = 0; i < N; i++)
        {
            newBoard[i] = this.board[i].clone();
        }
        SudokuState newState = new SudokuState(newBoard);

        //copy/initialize additional parameters, if needed

        return newState;
    }

    //method useful for debugging and testing purposes
    public String toString()
    {
        String s = "";
        for(int i = 0 ; i < N ; i++)
        {
            if(i % 3 == 0)
            {
                s+= "----------------------\n";
            }
            for(int j = 0; j < N ; j++)
            {
                if(j % 3 == 0)
                {
                    s+= "|";
                }
                if(this.board[i][j] == 0)
                {
                    s+= "_ ";
                }
                else s+= this.board[i][j]+" ";
            }
            s+="|\n";
        }
        s+= "----------------------\n";
        return s;
    }

    private static void unitTestCountRow(){
        assert countRow(board1, 0, 9) == 1;
        assert countRow(board1, 3, 0) == 5;
        assert countRow(board1, 8, 5) == 0;

        assert countRow(board2, 0, 0) == 3;
        assert countRow(board2, 4, 1) == 4;
        assert countRow(board2, 8, 0) == 5;
    }

    private static void unitTestCountColumn() {
        assert countColumn(board1, 0, 1) == 1;
        assert countColumn(board1, 2, 6) == 1;
        assert countColumn(board1, 8, 7) == 0;

        assert countColumn(board2, 0, 5) == 0;
        assert countColumn(board2, 5, 19) == 1;
        assert countColumn(board2, 8, 9) == 1;
    }

    private static void unitTestCountBox() {
        assert countBox(board1, 0, 0, 1) == 1;
        assert countBox(board1, 3, 3, 7) == 1;
        assert countBox(board1, 6, 6, 9) == 1;

        assert countBox(board2, 0, 0, 1) == 2;
        assert countBox(board2, 3, 3, 1) == 3;
        assert countBox(board2, 6, 6, 9) == 1;
    }

    private static void unitTestCountNumbersInRow() {
        assert countNumbersInRow(board1, 0) == 9;
        assert countNumbersInRow(board1, 3) == 9;
        assert countNumbersInRow(board1, 8) == 9;

        assert countNumbersInRow(board2, 0) == 9;
        assert countNumbersInRow(board2, 4) == 9;
        assert countNumbersInRow(board2, 8) == 9;
    }

    private static void unitTestCountNumbersInColl() {
        assert countNumbersInColl(board1, 0) == 9;
        assert countNumbersInColl(board1, 3) == 9;
        assert countNumbersInColl(board1, 8) == 9;

        assert countNumbersInColl(board2, 0) == 9;
        assert countNumbersInColl(board2, 3) == 9;
        assert countNumbersInColl(board2, 8) == 9;
    }

    private static void unitTestCountNumbersInBox() {
        assert countNumbersInBox(board1, 0, 0) == 9;
        assert countNumbersInBox(board1, 3, 3) == 9;
        assert countNumbersInBox(board1, 6, 6) == 9;

        assert countNumbersInBox(board2, 0, 0) == 9;
        assert countNumbersInBox(board2, 3, 3) == 9;
        assert countNumbersInBox(board2, 6, 6) == 9;
    }

    private static void unitTestIsRowValid() {
        assert isRowValid(board1, 0);
        assert isRowValid(board1, 1);
        assert isRowValid(board1, 8);

        assert !isRowValid(board2, 0);
        assert !isRowValid(board2, 4);
        assert isRowValid(board2, 8);
    }

    private static void unitTestIsColValid() {
        assert isColValid(board1, 0) == true;
        assert isColValid(board1, 1) == true;
        assert isColValid(board1, 8) == true;

        assert isColValid(board2, 0) == true;
        assert isColValid(board2, 5) == true; //tecnicamente devia ser false, pois ha um 19, mas a função testa isso
        assert isColValid(board2, 8) == true;
    }

    private static void unitTestIsBoxValid() {
        assert isBoxValid(board1, 0, 0) == true;
        assert isBoxValid(board1, 3, 3) == true;
        assert isBoxValid(board1, 6, 6) == true;

        assert isBoxValid(board2, 0, 0) == false;
        assert isBoxValid(board2, 3, 3) == false;
        assert isBoxValid(board2, 6, 6) == true;
    }

    private static void unitTestAllRowsValid() {
        assert allRowsValid(board1) == true;
        assert allRowsValid(board2) == false;
    }

    private static void unitTestAllColsValid() {
        assert allColsValid(board1) == true;
        assert allColsValid(board2) == false;
    }

    private static void unitTestAllBoxesValid() {
        assert allBoxesValid(board1) == true;
        assert allBoxesValid(board2) == false;
    }

    private static void unitTestValidBoard() {
        assert validBoard(board1) == true;
        assert validBoard(board2) == false;
    }

//    private static void unitTestValidAction() {
//        assert isValidAction (0, 2, 3) == true;
//        assert isValidAction (3, 0, 6) == false;
//        assert isValidAction (6, 3, 0) == false;
//
//
//    }


    private static void unitTests(){
        unitTestCountRow();
        unitTestCountColumn();
        unitTestCountBox();
        unitTestCountNumbersInRow();
        unitTestCountNumbersInColl();
        unitTestCountNumbersInBox();
        unitTestIsRowValid();
        unitTestIsColValid();
        unitTestIsBoxValid();
        unitTestValidBoard();
        //unitTestValidAction();

    }

    public static void main(String[] args) {
        System.out.println("Testing SudokuState");
        unitTests();
        System.out.println("Everything is OK");

    }
}