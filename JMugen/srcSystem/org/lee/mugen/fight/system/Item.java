package org.lee.mugen.fight.system;

import java.awt.Point;

import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.util.BeanTools;

public class Item extends Type {
	MugenSystem ms;
	
	Point spacing = new Point();
	Type active;
	
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
		}
	}
	
	@Override
	public void init() {
		super.init();
		if (active != null)
			active.init();
	}
}
