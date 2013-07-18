package misc;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class InsertableLinkedHashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, Serializable {
	
	private static final long serialVersionUID = 2101633298123600704L;
	
	private HashMap<E, Node<E>> map;
	private Node<E> lastNode;
	private Node<E> firstNode;
	
	public InsertableLinkedHashSet() {
		map = new HashMap<E, Node<E>>();
	}

	public InsertableLinkedHashSet(Collection<E> c) {
		map = new HashMap<E, Node<E>>(c.size() * 2);
		
		Node<E> previous = null;
		Node<E> current = null;
		Iterator<E> iterator = c.iterator();
		
		if (iterator.hasNext()) {
			E data;
			do  {
				data = iterator.next();
			} while (data != null);
			previous = new Node<E>(data);
			map.put(data, previous);
			firstNode = previous;
		}
		
		while (iterator.hasNext()) {
			E data = iterator.next();
			if (data == null || map.containsKey(data)) {
				continue;
			}
			current = new Node<E>(data);
			current.previous = previous;
			previous.next = current;
			
			map.put(current.data, current);
			previous = current;
		}
		lastNode = current;
	}

	public InsertableLinkedHashSet(int initialCapacity) {
		map = new HashMap<E, Node<E>>(initialCapacity);
	}

	public InsertableLinkedHashSet(int initialCapacity, float loadFactor) {
		map = new HashMap<E, Node<E>>(initialCapacity, loadFactor);
	}
	
	@Override
	public boolean add(E e) {
		if (e == null || map.containsKey(e)) {
			return false;
		}
		Node<E> node = new Node<E>(e);
		
		map.put(e, node);
		
		node.previous = lastNode;
		if (lastNode != null) {
			lastNode.next = node;
		}
		lastNode = node;
		
		return true;
	}
	
	public boolean insertFirst(E e) {
		if (e == null || map.containsKey(e)) {
			return false;
		}
		Node<E> node = new Node<E>(e);
		
		map.put(e, node);
		
		node.next = firstNode;
		if (firstNode != null) {
			firstNode.previous = node;
		}
		firstNode = node;
		return true;
	}
	
	public boolean insertAfter(E newValue, E existingValue) {
		if (newValue == null || !map.containsKey(existingValue)) {
			return false;
		}
		
		Node<E> node = map.get(existingValue);
		Node<E> newNode = new Node<E>(newValue);
		
		if (node == lastNode) {
			newNode.previous = lastNode;
			lastNode.next = newNode;
			newNode = lastNode;
		}
		else {
			node.next.previous = newNode;
			newNode.next = node.next;
			newNode.previous = node;
			node.next = newNode;
		}
		return true;
	}
	
	public boolean insertBefore(E newValue, E existingValue) {
		if (newValue == null || !map.containsKey(existingValue)) {
			return false;
		}
		
		Node<E> node = map.get(existingValue);
		Node<E> newNode = new Node<E>(newValue);
		
		if (node == firstNode) {
			newNode.next = firstNode;
			firstNode.previous = newNode;
			newNode = firstNode;
		}
		else {
			node.previous.next = newNode;
			newNode.previous = node.previous;
			newNode.next = node;
			node.previous = newNode;
		}
		return true;
	}
	
	@Override
	public void clear() {
		map.clear();
	}
	
	@Override
	public Object clone() {
		return new InsertableLinkedHashSet<E>(this);
	}
	
	@Override
	public Iterator<E> iterator() {
		return new LinkedListIterator<E>(firstNode);
	}
	
	public Iterator<E> reverseIterator() {
		return new ReverseLinkedListIterator<E>(lastNode);
	}
	
	@Override
	public boolean remove(Object o) {
		
		if (!map.containsKey(o)) {
			return false;
		}
		
		Node<E> node = map.remove(o);
		
		if (node == firstNode) {
			firstNode = firstNode.next;
			if (firstNode != null) {
				firstNode.previous = null;
			}
		}
		if (node == lastNode) {
			lastNode = lastNode.previous;
			if (lastNode != null) {
				lastNode.next = null;
			}
		}
		return true;
	}
	
	

	@Override
	public int size() {
		return map.size();
	}
}


class Node<E> {
	
	public Node<E> next;
	public Node<E> previous;
	public E data;
	
	public Node(E data) {
		this.data = data;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Node)) {
			return false;
		}
		Node n = (Node) o;
		return n.data.equals(data) && n.previous == previous && n.next == next;
	}
}


class LinkedListIterator<E> implements Iterator<E> {

	private Node<E> current;
	
	
	public LinkedListIterator(Node<E> first) {
		current = first;
	}
	
	
	@Override
	public boolean hasNext() {
		return current.next != null;
	}

	@Override
	public E next() {
		current = current.next;
		return current.data;
	}

	@Override
	public void remove() {
		
	}
	
}

class ReverseLinkedListIterator<E> implements Iterator<E> {

	// Current is the node most that was most recently returned by a call to next()
	private Node<E> current;
	// Next is the node that will next be returned by a call to next()
	private Node<E> next;
	
	public ReverseLinkedListIterator(Node<E> last) {
		next = last;
	}
	
	
	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public E next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		current = next;
		next = next.next;
		return current.data;
	}

	@Override
	public void remove() {
		if (current == null) {
			throw new IllegalStateException();
		}
		if (current.previous != null) {
			current.previous.next = current.next;
		}
		if (current.next != null) {
			current.next.previous = current.previous;
		}
		current = null;
	}
	
}
