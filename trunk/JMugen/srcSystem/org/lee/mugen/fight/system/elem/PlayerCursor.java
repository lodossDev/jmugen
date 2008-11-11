package org.lee.mugen.fight.system.elem;

import java.awt.Point;

import org.lee.mugen.fight.section.Section;
import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.util.BeanTools;

public class PlayerCursor implements Section {
	private Point startcell;
	private Type active;
	private Type done;
	
	@Override
	public void parse(Object root, String name, String value) throws Exception {
		if (name.equals("startcell")) {
			startcell = (Point) BeanTools.getConvertersMap().get(Point.class).convert(value);
		} else if (name.startsWith("active")) {
			if (active == null)
				active = new Type();
			active.setType(Type.getNext(name), active, value, root);
			active.parse(name, value);
		} else if (name.startsWith("done")) {
			if (done == null)
				done = new Type();
			done.setType(Type.getNext(name), done, value, root);
			done.parse(name, value);
		}
		
	}
	
	public Point getStartcell() {
		return startcell;
	}
	public void setStartcell(Point startcell) {
		this.startcell = startcell;
	}
	public Type getActive() {
		return active;
	}
	public void setActive(Type active) {
		this.active = active;
	}
	public Type getDone() {
		return done;
	}
	public void setDone(Type done) {
		this.done = done;
	}

	
}
