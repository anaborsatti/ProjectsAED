package aed.tables;

import java.util.Iterator;
import java.util.function.Predicate;

public class GoodItensIteratorOriginal<T> implements Iterator<T> {

    private final T[] a;
    private final int n;
    private int i;
    private Predicate<T> isGood; //função com argumento T, que retorna um boleano

    public GoodItensIteratorOriginal(T[] a, Predicate<T> isGood) {
        this.a = a;
        this.n = a.length;
        this.i = 0;
        this.isGood = isGood;
        i = advance();
    }

    private int advance() {
        while (i < n && !isGood.test(a[i]))
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

    private static void uniTestIterator(){
        {
            Integer[] a = {4, 5, 9, 1, 2, 0};
            Predicate<Integer> isGood = x -> x % 2 == 0;
            Iterator<Integer> it = new GoodItensIteratorOriginal<>(a, isGood);
            while(it.hasNext())
                System.out.println(it.next());
        }
        {
            String[] a = {"ccc", "k", "mm", "fff", "jj"};
            Iterator<String> it = new GoodItensIteratorOriginal<>(a, x -> x.length() ==3);
            while(it.hasNext())
                System.out.println(it.next());
        }
    }
    public static void main(String[] args) {
        uniTestIterator();
    }
}
