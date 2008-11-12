package org.lee.mugen.fight.system.elem;

import java.awt.Point;

import org.lee.mugen.fight.section.Section;
import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.util.BeanTools;

public class Stage implements Section {
	private Point pos;
	
	private Type move;
	private Type done;
	private Type active;
	private Type active2;
	@Override
	public void parse(Object root, String name, String value) throws Exception {
		if (name.equals("pos")) {
			pos = (Point) BeanTools.getConvertersMap().get(Point.class).convert(value);
		} else if (name.startsWith("move.")) {
			if (move == null)
				move = new Type();
			move.setType(Type.getNext(name), move, value, root);
			move.parse(Type.getNext(name), value);
		} else if (name.startsWith("done.")) {
			if (done == null)
				done = new Type();
			done.setType(Type.getNext(name), done, value, root);
			done.parse(Type.getNext(name), value);
		} else if (name.startsWith("active.")) {
			if (active == null)
				active = new Type();
			active.setType(Type.getNext(name), active, value, root);
			active.parse(Type.getNext(name), value);
		} else if (name.startsWith("active2.")) {
			if (active2 == null)
				active2 = new Type();
			active2.setType(Type.getNext(name), active2, value, root);
			active2.parse(Type.getNext(name), value);
		}
		
	}
	public Point getPos() {
		return pos;
	}
	public void setPos(Point pos) {
		this.pos = pos;
	}
	public Type getMove() {
		return move;
	}
	public void setMove(Type move) {
		this.move = move;
	}
	public Type getDone() {
		return done;
	}
	public void setDone(Type done) {
		this.done = done;
	}
	public Type getActive() {
		return active;
	}
	public void setActive(Type active) {
		this.active = active;
	}
	public Type getActive2() {
		return active2;
	}
	public void setActive2(Type active2) {
		this.active2 = active2;
	}
}
