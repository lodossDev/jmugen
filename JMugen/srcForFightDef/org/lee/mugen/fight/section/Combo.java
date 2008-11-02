package org.lee.mugen.fight.section;

import org.lee.mugen.fight.section.elem.FontType;
import org.lee.mugen.fight.section.elem.SimpleElement;
import org.lee.mugen.fight.section.elem.Start;
import org.lee.mugen.fight.section.elem.Type;

public class Combo extends SimpleElement {

	
	Start start = new Start();
	Type counter;
	FontType text;
	int displaytime;
	int counter$shake;
	
	@Override
	public void parse(String name, String value) {
		super.parse(name, value);
		if (name.equals("displaytime")) {
			displaytime = Integer.parseInt(value);
		} else if (name.equals("text.")) {
			if (text == null) {
				text = new FontType();
			}
			text.parse(Type.getNext(name), value);
		} else if (name.equals("start.")) {
			getStart().setX(Integer.parseInt(value));
		} else if (name.startsWith("counter.")) {
			if (name.equals("counter.shake")) {
				counter$shake = Integer.parseInt(value);
			} else {
				if (counter == null) {
					counter = Type.buildType(Type.getNext(name));
					if (counter == null) {
						throw new IllegalStateException("You Must specifie type anim, font, or spr first");
					}
				}
				Type.setValue(name.substring(name.indexOf(".") + 1), counter, value);
				counter.parse(name.substring(name.indexOf(".") + 1), value);
				
			}
		}
	}
	
	
	
	public int getCounter$shake() {
		return counter$shake;
	}



	public void setCounter$shake(int counter$shake) {
		this.counter$shake = counter$shake;
	}



	public Start getStart() {
		return start;
	}
	public void setStart(Start start) {
		this.start = start;
	}
	public Type getCounter() {
		return counter;
	}
	public void setCounter(Type counter) {
		this.counter = counter;
	}
	public FontType getText() {
		return text;
	}
	public void setText(FontType text) {
		this.text = text;
	}
	public int getDisplaytime() {
		return displaytime;
	}
	public void setDisplaytime(int displaytime) {
		this.displaytime = displaytime;
	}
}
