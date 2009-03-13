package org.lee.mugen.fight.system.elem;

import java.awt.Point;

import org.lee.mugen.fight.section.elem.PlayerFace;
import org.lee.mugen.fight.section.elem.PlayerName;
import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.sprite.entity.PointF;
import org.lee.mugen.util.BeanTools;

public class PlayerVsScreen extends PlayerFace {
	Type name;
	Point pos = new Point();
	int facing;
	PointF scale = new PointF(1,1);
	public Point getPos() {
		return pos;
	}

	public void setPos(Point pos) {
		this.pos = pos;
	}

	public int getFacing() {
		return facing;
	}

	public void setFacing(int facing) {
		this.facing = facing;
	}

	public PointF getScale() {
		return scale;
	}

	public void setScale(PointF scale) {
		this.scale = scale;
	}

	public Type getName() {
		return name;
	}

	public void setName(Type name) {
		this.name = name;
	}
	@Override
	public void parse(Object root, String name, String value) {
		super.parse(root, name, value);
		if (name.startsWith("name.")) {
			if (this.name == null) {
				this.name = new Type();
			}
			this.name.setType(Type.getNext(name), this.name, value, root);
			this.name.parse(Type.getNext(name), value);
		} else if (name.startsWith("pos")) {
			pos = (Point) BeanTools.getConvertersMap().get(Point.class).convert(value);
		}  else if (name.startsWith("facing")) {
			facing = Integer.parseInt(value);
		} else if (name.startsWith("scale")) {
			scale = (PointF) BeanTools.getConvertersMap().get(PointF.class).convert(value);
		}
	}
}
