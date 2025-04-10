package aed.collections;

import java.util.Collection;

public interface IStack<T> extends Iterable<T> {
	
	public boolean isEmpty();
	
	public void push(T item);

	public T pop();

	public T peek();

	public IStack<T> shallowCopy();

}
