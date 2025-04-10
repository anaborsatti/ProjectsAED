package aed.matrix;

//import aed.tables.OpenAdressingHashTable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Sparse2DMatrix {

    private HashMap <Long, Float> ht;
    int rows;
    int cols;

    public Sparse2DMatrix(int rows, int cols)
    {
        assert rows < 10000;
        assert cols < 10000;
        this.rows = rows;
        this.cols = cols;
        ht = new HashMap<>();
    }

    public int getNumberNonZero() {
        return ht.size();
    }

    private long key (int row, int col) {
        return row*0x100000000L + col;
    }

    public void put(int row, int col, float value) //put in table
    {
        if (value != 0)
            ht.put(key(row, col), value);
        else ht.remove(key(row, col));
    }

    public void putKey(long k, float value)//put in matrix
    {
        if (value != 0)
            ht.put(k, value);
        else ht.remove(k);
    }


    public float get(int row, int col)
    {
        return ht.getOrDefault(key(row, col), 0.0f);
    }

    public float getKey(long k)
    {
        return ht.getOrDefault(k, 0.0f);
    }


    public Sparse2DMatrix scalar(float scalar)
    {
        Sparse2DMatrix result = new Sparse2DMatrix(rows, cols);
        for (long k: ht.keySet()){
            result.putKey(k, ht.get(k)*scalar);
        }
        return result;
    }

    public Sparse2DMatrix sum(Sparse2DMatrix that) {
        if (this.rows != that.rows || this.cols != that.cols)
            throw new IllegalArgumentException("Matrices must have the same dimensions to add.");

        Sparse2DMatrix result = new Sparse2DMatrix(rows, cols);

        for (long k : this.ht.keySet()) {
            result.putKey(k, this.getKey(k));
        }

        for (long k : that.ht.keySet()) {
            result.putKey(k, result.getKey(k) + that.getKey(k));
        }

        return result;
    }

    public Sparse2DMatrix multiply(Sparse2DMatrix that)
    {
        if (this.cols != that.rows)
            throw new IllegalArgumentException("A.columns != B.rows");
        Sparse2DMatrix result = new Sparse2DMatrix(this.rows, that.cols);
        for (long thisK : this.ht.keySet()){
            int thisRow = (int) (thisK/0x100000000L);
            int thisCol = (int) (thisK%0x100000000L);
            float thisValue = this.getKey(thisK);

            for (long thatK : that.ht.keySet()){
                int thatRow = (int) (thatK/0x100000000L);
                int thatCol = (int) (thatK%0x100000000L);

                if (thisCol == thatRow){
                    float thatValue = that.getKey(thatK);
                    long resultKey = key(thisRow, thatCol);
                    result.putKey(resultKey, result.getKey(resultKey) + thisValue * thatValue);
                }

            }
        }
        return result;
    }

    public float[] getNonZeroElements()
    {
        float[] result = new float[getNumberNonZero()];
        int i = 0;
        for (long k: ht.keySet())
            result[i++] = getKey(k);
        return result;
    }

    public float[][] getNonSparseMatrix()
    {
        float[][] result = new float[rows][cols];
        for (long k: ht.keySet()){
            int i = (int) (k/0x100000000L);
            int j = (int) (k%0x100000000L);
            result[i][j] = ht.get(k);
        }
        return result;
    }

    private String makeString(){
        String result = rows + "x" + cols + "---" + getNumberNonZero() + "\n";
        float [][] matrix = getNonSparseMatrix();
        for (int i= 0; i < rows; i++)
        {
            result += "[";
            for (int j= 0; j < cols; j++)
                result += (j == 0? "" : ", ") + String.format("%.1f", matrix[i][j]);
            result += "]\n";
        }
        return result;
    }

    private static void unitTestConstructor (){
        Sparse2DMatrix matrix = new Sparse2DMatrix(3, 5);
        assert matrix.makeString().equals("""
                3x5---0
                [0,0, 0,0, 0,0, 0,0, 0,0]
                [0,0, 0,0, 0,0, 0,0, 0,0]
                [0,0, 0,0, 0,0, 0,0, 0,0]
                """) : matrix.makeString();
    }

    private static void unitTestPut (){
        Sparse2DMatrix matrix = new Sparse2DMatrix(3, 5);
        matrix.put (0, 0, 8.2f);
        matrix.put (2, 4, -5.2f);
        assert matrix.makeString().equals("""
                3x5---2
                [8,2, 0,0, 0,0, 0,0, 0,0]
                [0,0, 0,0, 0,0, 0,0, 0,0]
                [0,0, 0,0, 0,0, 0,0, -5,2]
                """) : matrix.makeString();
        matrix.put (1, 1, 0.0f);
        assert matrix.makeString().equals("""
                3x5---2
                [8,2, 0,0, 0,0, 0,0, 0,0]
                [0,0, 0,0, 0,0, 0,0, 0,0]
                [0,0, 0,0, 0,0, 0,0, -5,2]
                """) : matrix.makeString();
        matrix.put (2, 4, 0.0f);
        assert matrix.makeString().equals("""
                3x5---1
                [8,2, 0,0, 0,0, 0,0, 0,0]
                [0,0, 0,0, 0,0, 0,0, 0,0]
                [0,0, 0,0, 0,0, 0,0, 0,0]
                """) : matrix.makeString();

        //uma coluna, uma linha e 2x2
    }

    private static void unitTestScalar (){
        Sparse2DMatrix m1 = new Sparse2DMatrix(3, 5);
        m1.put (0, 0, 8.2f);
        m1.put (2, 4, -5.2f);
        Sparse2DMatrix m2 = m1.scalar(3);
        assert m2.makeString().equals("""
                3x5---2
                [24,6, 0,0, 0,0, 0,0, 0,0]
                [0,0, 0,0, 0,0, 0,0, 0,0]
                [0,0, 0,0, 0,0, 0,0, -15,6]
                """) : m2.makeString();
        Sparse2DMatrix m3 = m2.scalar(0);
        assert m3.makeString().equals("""
                3x5---0
                [0,0, 0,0, 0,0, 0,0, 0,0]
                [0,0, 0,0, 0,0, 0,0, 0,0]
                [0,0, 0,0, 0,0, 0,0, 0,0]
                """) : m3.makeString();

    }

    private static void unitTestSum (){
        Sparse2DMatrix m1 = new Sparse2DMatrix(2, 2);
        m1.put (0, 0, 3);
        m1.put (0, 1, 1);
        m1.put (1, 1, 4);
        assert m1.makeString().equals("""
                2x2---3
                [3,0, 1,0]
                [0,0, 4,0]
                """) : m1.makeString();
        Sparse2DMatrix m2 = new Sparse2DMatrix(2, 2);
        m2.put (0, 0, -3);
        m2.put (0, 1, 5);
        m2.put (1, 0, 2);
        assert m2.makeString().equals("""
                2x2---3
                [-3,0, 5,0]
                [2,0, 0,0]
                """) : m2.makeString();
        Sparse2DMatrix m3 = m1.sum(m2);
        assert m3.makeString().equals("""
                2x2---3
                [0,0, 6,0]
                [2,0, 4,0]
                """) : m3.makeString();

    }

    public static void unitTestMultiply()
    {
        {
            Sparse2DMatrix m1 = new Sparse2DMatrix(2, 2);
            m1.put(0, 0, 1.0f);
            m1.put(0, 1, 2.0f);
            m1.put(1, 0, 3.0f);
            m1.put(1, 1, 4.0f);
            assert m1.makeString().equals("""
                    2x2---4
                    [1,0, 2,0]
                    [3,0, 4,0]
                    """) : m1.makeString();
            Sparse2DMatrix mId = new Sparse2DMatrix(2, 2);
            mId.put(0, 0, 1.0f);
            mId.put(1, 1, 1.0f);
            assert mId.makeString().equals("""
                    2x2---2
                    [1,0, 0,0]
                    [0,0, 1,0]
                    """) : mId.makeString();
            Sparse2DMatrix m = m1.multiply(mId);
            assert m.makeString().equals(m1.makeString()) : m.makeString();
//            System.out.println("----");
        }
        {
            Sparse2DMatrix m1 = new Sparse2DMatrix(2, 2);
            m1.put(0, 0, 1.0f);
            m1.put(0, 1, 2.0f);
            m1.put(1, 0, 3.0f);
            m1.put(1, 1, 4.0f);
            assert m1.makeString().equals("""
                    2x2---4
                    [1,0, 2,0]
                    [3,0, 4,0]
                    """) : m1.makeString();
            Sparse2DMatrix m2 = new Sparse2DMatrix(2, 2);
            m2.put(0, 0, -1.0f);
            m2.put(0, 1, 3.0f);
            m2.put(1, 0, 4.0f);
            m2.put(1, 1, 2.0f);
            assert m2.makeString().equals("""
                    2x2---4
                    [-1,0, 3,0]
                    [4,0, 2,0]
                    """) : m2.makeString();
            {

                Sparse2DMatrix m = m1.multiply(m2);
                assert m.makeString().equals("""
                        2x2---4
                        [7,0, 7,0]
                        [13,0, 17,0]
                        """) : m.makeString();
            }

            {
                Sparse2DMatrix m = m2.multiply(m1);
                assert m.makeString().equals("""
                        2x2---4
                        [8,0, 10,0]
                        [10,0, 16,0]
                        """) : m.makeString();
            }

        }
        {
            Sparse2DMatrix m1 = new Sparse2DMatrix(3, 2);
            m1.put(0, 0, 2.0f);
            m1.put(0, 1, 3.0f);
            m1.put(1, 0, 0.0f);
            m1.put(1, 1, 1.0f);
            m1.put(2, 0, -1.0f);
            m1.put(2, 1, 4.0f);
            assert m1.makeString().equals("""
                    3x2---5
                    [2,0, 3,0]
                    [0,0, 1,0]
                    [-1,0, 4,0]
                    """) : m1.makeString();
            Sparse2DMatrix m2 = new Sparse2DMatrix(2, 3);
            m2.put(0, 0, 1.0f);
            m2.put(0, 1, 2.0f);
            m2.put(0, 2, 3.0f);
            m2.put(1, 0, -2.0f);
            m2.put(1, 1, 0.0f);
            m2.put(1, 2, 4.0f);
            assert m2.makeString().equals("""
                    2x3---5
                    [1,0, 2,0, 3,0]
                    [-2,0, 0,0, 4,0]
                    """) : m2.makeString();
            {
                Sparse2DMatrix m = m1.multiply(m2);
                assert m.makeString().equals("""
                        3x3---8
                        [-4,0, 4,0, 18,0]
                        [-2,0, 0,0, 4,0]
                        [-9,0, -2,0, 13,0]
                        """) : m.makeString();
            }
            {
                Sparse2DMatrix m = m2.multiply(m1);
                assert m.makeString().equals("""
                        2x2---4
                        [-1,0, 17,0]
                        [-8,0, 10,0]
                        """) : m.makeString();
            }
        }
    }



    //unit test NonElements e fazer a função multiply

    private static void unitTests(){
        unitTestConstructor();
        unitTestPut();
        unitTestScalar();
        unitTestSum();
        unitTestMultiply();
    }

    public static void main(String[] args)
    {
        System.out.println("Class Sparse2DMatrix");
        unitTests();
        System.out.println("All tests passed.");

    }
}
