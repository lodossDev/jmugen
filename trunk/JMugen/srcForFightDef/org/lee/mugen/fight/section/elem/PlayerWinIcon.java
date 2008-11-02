package org.lee.mugen.fight.section.elem;

import java.awt.Point;

import org.lee.mugen.util.BeanTools;

public class PlayerWinIcon extends SimpleElement {
	Point iconoffset = new Point();
	Type counter;
	Type n; // Win by normal
	Type h; // Win by Hyper
	Type _throw;// Win by normal throw
	Type c;// Win by cheese
	Type t;// Win timeover
	Type sucide;// Win by suicide (here's a fun one)
	Type teammate;// Opponent beaten by his own teammate (another fun one)
	Type perfect;// Win by perfect (overlay icon)
	
	@Override
	public void parse(String name, String value) {
		super.parse(name, value);
		if (name.equalsIgnoreCase("iconoffset")) {
			iconoffset = (Point) BeanTools.getConvertersMap().get(Point.class).convert(value);
		} else if (name.startsWith("counter.")) {
			if (counter == null) {
				counter = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (counter == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), counter, value);
			counter.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("n.")) {
			if (n == null) {
				n = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (n == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), n, value);
			n.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("h.")) {
			if (h == null) {
				h = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (h == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), h, value);
			h.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("throw.")) {
			if (_throw == null) {
				_throw = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (_throw == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), _throw, value);
			_throw.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("c.")) {
			if (c == null) {
				c = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (c == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), c, value);
			c.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("t.")) {
			if (t == null) {
				t = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (t == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), t, value);
			t.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("sucide.")) {
			if (sucide == null) {
				sucide = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (sucide == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), sucide, value);
			sucide.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("teammate.")) {
			if (teammate == null) {
				teammate = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (teammate == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), teammate, value);
			teammate.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("perfect.")) {
			if (perfect == null) {
				perfect = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (perfect == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), perfect, value);
			perfect.parse(name.substring(name.indexOf(".") + 1), value);
		}
	}
	
	public Point getIconoffset() {
		return iconoffset;
	}
	public void setIconoffset(Point iconoffset) {
		this.iconoffset = iconoffset;
	}
	public Type getCounter() {
		return counter;
	}
	public void setCounter(Type counter) {
		this.counter = counter;
	}
	public Type getN() {
		return n;
	}
	public void setN(Type n) {
		this.n = n;
	}
	public Type getH() {
		return h;
	}
	public void setH(Type h) {
		this.h = h;
	}
	public Type getThrow() {
		return _throw;
	}
	public void setThrow(Type throw1) {
		_throw = throw1;
	}
	public Type getC() {
		return c;
	}
	public void setC(Type c) {
		this.c = c;
	}
	public Type getT() {
		return t;
	}
	public void setT(Type t) {
		this.t = t;
	}
	public Type getSucide() {
		return sucide;
	}
	public void setSucide(Type sucide) {
		this.sucide = sucide;
	}
	public Type getTeammate() {
		return teammate;
	}
	public void setTeammate(Type teammate) {
		this.teammate = teammate;
	}
	public Type getPerfect() {
		return perfect;
	}
	public void setPerfect(Type perfect) {
		this.perfect = perfect;
	}

}
