package aed.collections;

import java.util.Iterator;

public class StackList<T> implements IStack<T>
{
	private class Node {
		private final T item;
		private Node next;

		public Node(T item, Node next) {
			this.item = item;
			this.next = next;
		}
	}

	private Node top;
	private int size;

	public StackList (){
		top = null;
		size = 0;
	}

	public int size()
	{
		return size;
	}

	@Override
	//@SuppressWarnings("unchecked")
	public StackList<T> shallowCopy() {
		StackList<T> shallow = new StackList<>();
		if (this.top == null && size == 0) return shallow;

		Node currentOriginalNode = this.top;
		shallow.top = new Node(currentOriginalNode.item, null);
		Node currentCopyNode = shallow.top;

		while (currentOriginalNode.next != null) {
			currentOriginalNode = currentOriginalNode.next;//anda para o próximo node da original
			currentCopyNode.next = new Node(currentOriginalNode.item, null);//criar um node com o valor do node original
			currentCopyNode = currentCopyNode.next;//apontar o node para o próximo (ou juntar os dois)
		}

		shallow.size = this.size;
		return shallow;
	}


	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}

	@Override
	public void push(T item)
	{
		top = new Node(item, top);
		size++;
	}

	@Override
	public T pop()
	{
		if (top == null)
			return null;
		T result = this.top.item;
		top = top.next;
		size--;
		return result;
	}

	@Override
	public T peek() {
		return isEmpty() ? null : top.item;
	}


	@Override
	public Iterator<T> iterator()
	{
		return new StackIterator();
	}

	private class StackIterator implements Iterator<T>{

		private Node current;
		public StackIterator(){
			current = top;
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			T result = current.item;
			current = current.next;
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Iterator doesn't support removal");
		}
	}

	public String toString(){
		StringBuilder result = new StringBuilder("[");
		for (T x : this)
			result.append("<").append(x).append(">");
		result.append("]");
		return result.toString();
	}

	private static void unitTestConstructor () {
		StackList<Integer> s = new StackList<>();
		assert s.isEmpty();
		assert s.toString().equals("[]");
	}

	private static void unitTestPush (){
		{
			StackList<Integer> s = new StackList<>();
			s.push(25);
			s.push(30);
			s.push(-1);
			assert s.size() == 3;
			assert s.toString().equals("[<-1><30><25>]") : s.toString();
		}
		{
			StackList<String> s = new StackList<> ();
			s.push("ana");
			s.push("francisco");
			s.push("pedro");
			s.push ("");
			assert s.size() == 4;
			assert s.toString().equals("[<><pedro><francisco><ana>]") : s.toString();
		}
	}

	private static void unitTestPop (){
		{
			StackList<Integer> s = new StackList<>();
			s.push(25);
			s.push(30);
			s.push(-1);
			assert s.size() == 3;
			assert s.toString().equals("[<-1><30><25>]") : s.toString();
			assert s.peek() == -1;
			int x = s.pop();
			assert x == -1;
			assert s.size() == 2;
			assert s.toString().equals("[<30><25>]") : s.toString();
			x = s.pop();
			assert x == 30;
			assert s.size() == 1;
			x = s.pop();
			assert x == 25;
			assert s.isEmpty();
			Integer y = s.pop();
			assert y == null;
		}
	}

	private static void unitTestShallowCopy () {
		{
			StackList<Integer> s = new StackList<>();
			s.push(25);
			s.push(30);
			s.push(-1);
			s.push(-5);
			StackList<Integer> copy = s.shallowCopy();
			assert s.toString().equals("[<-5><-1><30><25>]") : s.toString();
			assert copy.toString().equals(s.toString()) : copy.toString();
			assert s != copy;
		}
		{
			StackList<String> s = new StackList<> ();
			s.push("ana");
			s.push("francisco");
			s.push("pedro");
			StackList<String> copy = s.shallowCopy();
			assert s.toString().equals("[<pedro><francisco><ana>]") : s.toString();
			assert copy.toString().equals(s.toString()) : copy.toString();
			assert s != copy;
			String s1 = s.peek();
			String s2 = copy.peek();
			assert (Object)s1 == s2;
		}
	}

	private static void unitTests (){
		unitTestConstructor();
		unitTestPush();
		unitTestPop();
		unitTestShallowCopy();
	}

	//função main utilizada para testes, coloque os testes efetuados aqui
	public static void main(String[] args)
	{
		System.out.println("Testing StackList");
		unitTests();
		System.out.println("Testing completed");
	}
}
