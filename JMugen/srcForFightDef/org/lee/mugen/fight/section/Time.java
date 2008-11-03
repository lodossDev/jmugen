package org.lee.mugen.fight.section;

import org.lee.mugen.fight.section.elem.FontType;
import org.lee.mugen.fight.section.elem.SimpleElement;
import org.lee.mugen.fight.section.elem.Type;

public class Time extends SimpleElement implements Section {
	Type counter;
	int framespercount;

	public int getFramespercount() {
		return framespercount;
	}

	public Type getCounter() {
		return counter;
	}

	public void setCounter(Type counter) {
		this.counter = counter;
	}

	public void setFramespercount(int framespercount) {
		this.framespercount = framespercount;
	}
	
	@Override
	public void parse(String name, String value) {
		super.parse(name, value);
		if (name.startsWith("counter.")) {
			if (counter == null) {
				counter = new Type();
			}
			counter.setType(Type.getNext(name), counter, value);
			counter.parse(Type.getNext(name), value);
		} else if (name.equalsIgnoreCase("framespercount")) {
			framespercount = Integer.parseInt(value);
		}
	}
}
