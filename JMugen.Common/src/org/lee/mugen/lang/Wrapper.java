package org.lee.mugen.lang;

public class Wrapper<T> implements Wrap<T> {
	private Object spec;
	public Wrapper() {}
	public void setValue(T o) {
		spec = o;
	}
	public T getValue() {
		return (T) spec;
	}


}