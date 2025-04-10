package aed.tables;

import java.util.Iterator;

public class SimpleArrayIterator <T> implements Iterator<T> {

    private final T[] a;
    private final int n;
    private int i;

    public SimpleArrayIterator(T[] a) {
        this.a = a;
        this.n = a.length;
        this.i = 0;
    }

    @Override
    public boolean hasNext() {
        return i<n;
    }

    @Override
    public T next() {
        return a[i++];
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private static void uniTestIterator(){
        {
            Iterator<Integer> it = new SimpleArrayIterator<>(new Integer[]{4, 5, 9, 1, 2, 0});
            while(it.hasNext())
                System.out.println(it.next());
        }
        {
            Iterator<String> it = new SimpleArrayIterator<>(new String[]{"ccc", "kkk", "mmm", "fff"});
            while(it.hasNext())
                System.out.println(it.next());
        }
    }
    public static void main(String[] args) {
        uniTestIterator();
    }
}
