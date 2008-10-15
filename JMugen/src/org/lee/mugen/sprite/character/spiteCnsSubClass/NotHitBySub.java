package org.lee.mugen.sprite.character.spiteCnsSubClass;

import org.lee.mugen.sprite.character.spiteCnsSubClass.ReversaldefSub.ReversalAttrClass;

public class NotHitBySub implements Cloneable {

	private ReversalAttrClass value;
	private ReversalAttrClass value2;
	
	
	private int time;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public ReversalAttrClass getValue() {
		return value;
	}
	public void setValue(ReversalAttrClass value) {
		this.value = value;
	}
	public ReversalAttrClass getValue2() {
		return value2;
	}
	public void setValue2(ReversalAttrClass value2) {
		this.value2 = value2;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public void addTime(int i) {
		time += i;
	}
	
}
