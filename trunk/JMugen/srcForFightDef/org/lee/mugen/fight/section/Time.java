package org.lee.mugen.fight.section;

import org.lee.mugen.fight.section.elem.SimpleElement;
import org.lee.mugen.fight.section.elem.Type;

public class Time extends SimpleElement {
	Type counter;
	int framespercount;
	public Type getCounter() {
		return counter;
	}
	public void setCounter(Type counter) {
		this.counter = counter;
	}
	public int getFramespercount() {
		return framespercount;
	}
	public void setFramespercount(int framespercount) {
		this.framespercount = framespercount;
	}
	
	@Override
	public void parse(String name, String value) {
		super.parse(name, value);
		if (name.startsWith("counter.")) {
			if (counter == null) {
				counter = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (counter == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), counter, value);
			counter.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.equalsIgnoreCase("framespercount")) {
			framespercount = Integer.parseInt(value);
		}
	}
}
