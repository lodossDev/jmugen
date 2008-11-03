package org.lee.mugen.fight.section.elem;

public class PlayerLifebar extends Bar {
	Type mid;
	Type front;
	public Type getMid() {
		return mid;
	}
	public void setMid(Type mid) {
		this.mid = mid;
	}
	public Type getFront() {
		return front;
	}
	public void setFront(Type front) {
		this.front = front;
	}
	
	@Override
	public void parse(String name, String value) {
		super.parse(name, value);
		if (name.startsWith("mid.")) {
			if (mid == null) {
				mid = new Type();
			}
			mid.setType(Type.getNext(name), mid, value);
			mid.parse(Type.getNext(name), value);
		} else if (name.startsWith("front.")) {
			if (front == null) {
				front = new Type();
			}
			front.setType(Type.getNext(name), mid, value);
			front.parse(Type.getNext(name), value);
		}
	}
}
