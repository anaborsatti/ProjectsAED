package aed.tables;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

public class OpenAddressingHashTable<Key,Value> implements Iterable<Key> {

    public Key[] keys;
    public Value[] values;
    private int capacity;
    private int size;
    private final double maxLoadFactor;
    private int primeIndex;
    private final int[] primes;
    private int deletedNotRemoved;

    private static final int[] largePrimes = {
            17, 37, 79, 163, 331,
            673, 1361, 2729, 5471, 10949,
            21911, 43853, 87719, 175447, 350899,
            701819, 1403641, 2807303, 5614657,
            11229331, 22458671, 44917381, 89834777, 179669557
    };

    private static final int[] fakePrimes = {
            2, 5, 10, 20, 50, 100
    };

    @SuppressWarnings("unchecked")
    public OpenAddressingHashTable()
    {
        this.primes = largePrimes;
        this.capacity = primes[1];
        this.size = 0;
        this.maxLoadFactor = 0.5;
        this.primeIndex = 1;
        this.keys = (Key[]) new Object [capacity];
        this.values = (Value[]) new Object [capacity];
        this.deletedNotRemoved = 0;
    }

    @SuppressWarnings("unchecked") //only for tests
    private OpenAddressingHashTable(int primeIndex, int[] primes, double maxLoadFactor)
    {
        this.primes = primes;
        this.capacity = primes[primeIndex];
        this.size = 0;
        this.maxLoadFactor = maxLoadFactor;
        this.primeIndex = primeIndex;
        this.keys = (Key[]) new Object [capacity];
        this.values = (Value[]) new Object [capacity];
        this.deletedNotRemoved = 0;
    }

    public int size()
    {
        return size;
    }

    public int getCapacity()
    {
        return capacity;
    }

    public float getLoadFactor()
    {
        return size/(float)capacity;
    }

    public int getDeletedNotRemoved()
    {
        return deletedNotRemoved;
    }

    public boolean containsKey(Key k) {
        int i = hash(k);
        int step = hash2(k);
        while (keys[i] != null && !keys[i].equals(k)) {
            i = (i + step) % capacity;
        }
        return keys[i] != null && values[i] != null;
    }

    public Value get(Key k)
    {
        int i = hash(k);
        int step = hash2(k);
        while (keys[i] != null && !keys[i].equals(k))
            i = (i + step)%capacity;
        return keys[i] == null ? null : values[i];
    }

    private void resize(int nextPrimeIndex) {
        OpenAddressingHashTable<Key, Value> newTable = new OpenAddressingHashTable<>(nextPrimeIndex, this.primes, maxLoadFactor);

        for (int i = 0; i < capacity; i++) {
            if (keys[i] != null && values[i] != null) {
                newTable.put(keys[i], values[i]);
            }
        }

        this.keys = newTable.keys;
        this.values = newTable.values;
        this.capacity = newTable.capacity;
        this.primeIndex = nextPrimeIndex;
        this.size = newTable.size;
        this.deletedNotRemoved = 0;
    }


    public void put(Key k, Value v) {
        if (k == null) throw new IllegalArgumentException("Key cannot be null");

        if (v == null) {
            delete(k);
            return;
        }

        int i = hash(k);
        int step = hash2(k);
        while (keys[i] != null) {
            if (keys[i].equals(k)) {
                if (values[i] == null) { //reusar uma key que tinha o valor como null
                    deletedNotRemoved--;
                    size++;
                }
                values[i] = v; //atualizar o valor
                return;
            }
            i = (i + step) % capacity;
        }

        keys[i] = k;
        values[i] = v;
        size++;

        if (getLoadFactor() >= maxLoadFactor) {
            resize(primeIndex + 1);
        }
    }


    public void delete(Key k) {
        if (k == null) return;

        int i = hash(k);
        int step = hash2(k);

        while (keys[i] != null) {
            if (keys[i].equals(k) && values[i] != null) {
                values[i] = null;
                deletedNotRemoved++;
                size--;
                break;
            }
            i = (i + step) % capacity;
        }

        float loadFactor = (float) size/ capacity;
        if (loadFactor < 0.125 && this.capacity > 37) { 
            resize(primeIndex - 1);
        } else if (deletedNotRemoved >= 0.2 * this.capacity) {
            resize(primeIndex);
        }
    }


    public Iterable<Key> keys() {
        return this; //retorna um objeto interavel
    }

    public Iterator<Key> iterator() { //==key set, da todas as chaves e enumera
        return new GoodItensIterator<>(keys, values);
    }

    private int hash (Key key){
        return (key.hashCode() & 0x7fffffff)%capacity; // f = 1111, apagar o sinal no caso do hash code ser negativo, atrapalha o resto
    }

    private int hash2(Key key) {
        return 1 + (key.hashCode() & 0x7fffffff) % (capacity - 1); // Secondary hash function
    }


    private static void uniTestConstructor(){
        {
            OpenAddressingHashTable<String, Integer> ht = new OpenAddressingHashTable<>();
            assert ht.makeString().equals("""
                    <0 37 1 0,00>
                    --------
                    """);
        }
    }

    private static void unitTestPut(){
        OpenAddressingHashTable<String, Integer> ht = new OpenAddressingHashTable<>(1, fakePrimes, 2.0);
        assert ht.makeString().equals("""
                    <0 5 1 0,00>
                    --------
                    """);
        ht.put("aaa", 123);
        assert ht.makeString().equals("""
                    <1 5 1 0,20>
                    1 aaa 123 1
                    --------
                    """) : ht.makeString();
        ht.put("bbb", 5);
        assert ht.makeString().equals("""
                    <2 5 1 0,40>
                    1 aaa 123 1
                    4 bbb 5 4
                    --------
                    """) : ht.makeString();
        ht.put("vvv", 888);
        assert ht.makeString().equals("""
                    <3 5 1 0,60>
                    0 vvv 888 4
                    1 aaa 123 1
                    4 bbb 5 4
                    --------
                    """) : ht.makeString();
        ht.put("lll", 24);
        assert ht.makeString().equals("""
                    <4 5 1 0,80>
                    0 vvv 888 4
                    1 aaa 123 1
                    2 lll 24 4
                    4 bbb 5 4
                    --------
                    """) : ht.makeString();
        ht.put("rrr", 38);
        assert ht.makeString().equals("""
                    <5 5 1 1,00>
                    0 vvv 888 4
                    1 aaa 123 1
                    2 lll 24 4
                    3 rrr 38 2
                    4 bbb 5 4
                    --------
                    """) : ht.makeString();
    }

    private static void unitTestIterable (){
        OpenAddressingHashTable<String, Integer> ht = new OpenAddressingHashTable<>(1, fakePrimes, 2.0);
        ht.put("aaa", 123);
        ht.put("bbb", 5);
        ht.put("vvv", 888);
        ht.put("lll", 24);
        ht.put("rrr", 38);
        assert ht.makeString().equals("""
                    <5 5 1 1,00>
                    0 vvv 888 4
                    1 aaa 123 1
                    2 lll 24 4
                    3 rrr 38 2
                    4 bbb 5 4
                    --------
                    """) : ht.makeString();
        for (String k: ht)
            System.out.println(k);
    }

    private static void unitTestResize (){
        OpenAddressingHashTable<String, Integer> ht = new OpenAddressingHashTable<>(1, fakePrimes, 0.5);
        ht.put("aaa", 123);
        ht.put("bbb", 5);

        assert ht.makeString().equals("""
                    <2 5 1 0,40>
                    1 aaa 123 1
                    4 bbb 5 4
                    --------
                    """) : ht.makeString();
        ht.put("vvv", 888);
        assert ht.makeString().equals("""
                    <3 10 2 0,30>
                    1 aaa 123 1
                    4 vvv 888 4
                    5 bbb 5 4
                    --------
                    """) : ht.makeString();
        ht.put("lll", 24);
        assert ht.makeString().equals("""
                    <4 10 2 0,40>
                    1 aaa 123 1
                    4 vvv 888 4
                    5 bbb 5 4
                    6 lll 24 4
                    --------
                    """) : ht.makeString();
        ht.put("rrr", 38);
        assert ht.makeString().equals("""
                    <5 20 3 0,25>
                    1 aaa 123 1
                    2 rrr 38 2
                    4 lll 24 4
                    14 vvv 888 14
                    15 bbb 5 14
                    --------
                    """) : ht.makeString();


    }

    private static void unitTests(){
        uniTestConstructor();
        unitTestPut();
        unitTestIterable();
        unitTestResize();
    }

    private String makeString(){
        String result = ("<" + size + " " + capacity + " " + primeIndex + " " + String.format("%.2f", getLoadFactor()) + ">\n");
        for (int i = 0; i < capacity; i++)
            if (keys[i] != null)
                result += i + " " + keys[i] + " " + values[i] + " " + hash(keys[i]) + "\n";
        result += "--------\n";
        return result;
    }


    public static void main(String[] args)
    {
        //implement tests here
        System.out.println("Class Open Addressing Hash Table");
        unitTests();
        System.out.println("All tests passed");
    }
}


class GoodItensIterator<T> implements Iterator<T> {

    private final T[] a;
    private final Object[] values; // Store values array for checking deletion
    private final int n;
    private int i;


    public GoodItensIterator(T[] a, Object[] values) {
        this.a = a;
        this.values = values;
        this.n = a.length;
        this.i = 0;
        i = advance();
    }

    private int advance() {
        while (i < n && (a[i] == null || values[i] == null))  // Salta deleted keys (null values)
            i++;
        return i;
    }


    @Override
    public boolean hasNext() {
        return i<n;
    }

    @Override
    public T next() {
        T result = a[i++];
        i = advance();
        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public static <T> String mkString(Iterator<T> it)
    {
        StringBuilder result = new StringBuilder("[");
        while (it.hasNext())
            result.append("<").append(it.next()).append(">");
        result.append("]");
        return result.toString();
    }

    /*private static void uniTestIterator(){
        {
            Integer[] a = {4, 5, 9, 1, 2, 0};
            Predicate<Integer> isGood = x -> x % 2 == 0;
            Iterator<Integer> it = new GoodItensIterator<>(a, isGood);
            while(it.hasNext())
                System.out.println(it.next());
        }
        {
            String[] a = {"ccc", "k", "mm", "fff", "jj"};
            Iterator<String> it = new GoodItensIterator<>(a, x -> x.length() ==3);
            while(it.hasNext())
                System.out.println(it.next());
        }
    }*/
    public static void main(String[] args) {
        //uniTestIterator();
    }
}