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
				mid = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (mid == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), mid, value);
			mid.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("front.")) {
			if (front == null) {
				front = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (front == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), mid, value);
			front.parse(name.substring(name.indexOf(".") + 1), value);
		}
	}
}
