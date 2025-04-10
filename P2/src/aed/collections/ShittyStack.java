package aed.collections;

import java.util.Iterator;

//Esta classe tem este nome, para ser muito claro que esta é uma implementação muito má
//para uma stack. Iremos usá-la apenas por razões pedagógicas, para ilustrar o que pode
//acontecer quando temos uma má implementação de uma estrutura de dados
public class ShittyStack<T> implements  IStack<T> {

    private T[] items;
    private int size;

    private static final int INITIAL_ARRAY_LENGTH = 10;

    @SuppressWarnings("unchecked")
    public ShittyStack()
    {
        this.size = 0;
        this.items = (T[]) new Object[INITIAL_ARRAY_LENGTH];

    }

    @SuppressWarnings("unchecked")
    private void resize(int newArrayLength)
    {
        if(newArrayLength < INITIAL_ARRAY_LENGTH) return;

        T[] resizedArray = (T[]) new Object[newArrayLength];
        for(int i = 0; i < this.size; i++)
        {
            resizedArray[i] = this.items[i];
        }
        this.items = resizedArray;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public void push(T item) {

        if(this.size == this.items.length)
        {
            resize(this.items.length*2);
        }
        this.size++;

        //shift right
        for(int i = size-1; i>0; i--)
        {
            this.items[i] = this.items[i-1];
        }

        this.items[0] = item;
    }

    @Override
    public T pop() {
        if(this.size == 0) return null;

        T result = this.items[0];
        this.size--;
        //shift left
        for(int i = 0; i < this.size; i++)
        {
            this.items[i] = this.items[i+1];
        }
        this.items[size] = null;

        if(this.size < this.items.length/4)
        {
            resize(this.items.length/2);
        }

        return result;
    }

    @Override
    public T peek() {
        if(this.size == 0) return null;
        return this.items[0];
    }

    @Override
    public IStack<T> shallowCopy() {
        ShittyStack<T> copy = new ShittyStack<>();
        copy.size = this.size;
        copy.items = this.items.clone();
        return copy;
    }

    @Override
    public Iterator<T> iterator() {
        return new ShittyStackIterator();
    }

    //inner class that implements a shittystack iterator
    //a stack iterator should iterate over items using the natural stack order
    //i.e. from the top of the stack, to the bottom
    private class ShittyStackIterator implements Iterator<T>
    {
        //we don't need a pointer to a shittystack because a innerclass can automatically
        //access the members of its outerclass (basically a innerclass has a pointer to the
        //outerclass, but this is automatically created by java).
        int i;

        ShittyStackIterator()
        {
            this.i = 0;
        }

        @Override
        public boolean hasNext()
        {
            //the member size correspond to the size of the ShittyStack that this Iterator corresponds to
            return this.i < size;
        }

        @Override
        public T next()
        {
            T result = items[i++];
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Stack Iterator does not support removal");
        }
    }
}
