package org.lee.mugen.fight.system.elem;

import java.awt.Point;

import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.fight.system.MugenSystem;
import org.lee.mugen.util.BeanTools;

public class Item extends Type {
	MugenSystem ms;
	
	Point spacing = new Point();
	Type active;
	Type active2;
	Type cursor;
	
	public Item(MugenSystem ms) {
		this.ms = ms;
	}
	
	@Override
	public void parse(String name, String value) {
		super.parse(name, value);
		if (name.equalsIgnoreCase("spacing")) {
			spacing = (Point) BeanTools.getConvertersMap().get(Point.class).convert(value);
		} else if (name.equals("active")) {
			active = new Type();
			active.setType(getNext(name), active, value, ms);
		} else if (name.equals("active2")) {
			active2 = new Type();
			active2.setType(getNext(name), active2, value, ms);
		} else if (name.equals("cursor")) {
			cursor = new Type();
			cursor.setType(getNext(name), cursor, value, ms);
		}
	}
	
	public Point getSpacing() {
		return spacing;
	}

	public Type getActive() {
		return active;
	}

	public Type getActive2() {
		return active2;
	}

	public Type getCursor() {
		return cursor;
	}

	@Override
	public void init() {
		super.init();
		if (active != null)
			active.init();
	}
}
