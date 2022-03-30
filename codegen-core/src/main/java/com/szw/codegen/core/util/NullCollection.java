package com.szw.codegen.core.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author SZW
 */
public class NullCollection<T> implements Collection<T> {

	public static <T> NullCollection<T> newInstance() {
		return new NullCollection<> ();
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return NullIterator.newInstance ();
	}

	@Override
	public Object[] toArray() {
		return new Object[0];
	}

	@Override
	public boolean add(Object o) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean addAll(Collection c) {
		return false;
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean retainAll(Collection c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection c) {
		return false;
	}

	@Override
	public boolean containsAll(Collection c) {
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object[] toArray(Object[] a) {
		return new Object[0];
	}
}
