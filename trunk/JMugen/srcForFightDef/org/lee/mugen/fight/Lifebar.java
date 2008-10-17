package org.lee.mugen.fight;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.base.AbstractAnimManager.SpriteDrawProperties;
import org.lee.mugen.sprite.baseForParse.ImageSpriteSFF;
import org.lee.mugen.sprite.character.SpriteAnimManager;
import org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub;
import org.lee.mugen.sprite.parser.ExpressionFactory;
import org.lee.mugen.sprite.parser.Parser.GroupText;
import org.lee.mugen.util.BeanTools;

public class Lifebar {

	public static class PlayerLifeBar {
		private Point pos = new Point();
		private Map<Integer, Bg> bg = new HashMap<Integer, Bg>() {
			@Override
			public Bg get(Object key) {
				if (super.get(key) == null) {
					put((Integer) key, new Bg());
				}

				return super.get(key);
			}
		};
		private Bg mid = new Bg();
		private Bg front = new Bg();
		private Range range = new Range();
		
		private Range rangeFront = new Range();
		private Range rangeMid = new Range();

		public Range getRangeFront() {
			return rangeFront;
		}

		public void setRangeFront(Range rangeFront) {
			this.rangeFront = rangeFront;
		}

		public Range getRangeMid() {
			return rangeMid;
		}

		public void setRangeMid(Range rangeMid) {
			this.rangeMid = rangeMid;
		}

		public Point getPos() {
			return pos;
		}

		public void setPos(Point pos) {
			this.pos = pos;
		}

		public Map<Integer, Bg> getBg() {
			return bg;
		}

		public void setBs(Map<Integer, Bg> bg) {
			this.bg = bg;
		}

		public Bg getMid() {
			return mid;
		}

		public void setMid(Bg mid) {
			this.mid = mid;
		}

		public Bg getFront() {
			return front;
		}

		public void setFront(Bg front) {
			this.front = front;
		}

		public Range getRange() {
			return range;
		}

		public void setRange(Range range) {
			this.range = new Range();
			this.range.getX().x = range.getX().x;
			this.range.getX().y = range.getX().y;

			this.rangeFront.getX().x = range.getX().x;
			this.rangeFront.getX().y = range.getX().y;

			this.rangeMid.getX().x = range.getX().x;
			this.rangeMid.getX().y = range.getX().y;
}

		public void process() {
			for (Bg bg: getBg().values()) {
				bg.process();
			}

			mid.process();
			front.process();
		}

	}

	public static class Spr implements Cloneable {
		Integer grp;
		Integer img;

		public Spr(int grp, int img) {
			this.grp = grp;
			this.img = img;
		}

		public Spr() {
			// TODO Auto-generated constructor stub
		}

		public Integer getGrp() {
			return grp;
		}

		public void setGrp(Integer grp) {
			this.grp = grp;
		}

		public Integer getImg() {
			return img;
		}

		public void setImg(Integer img) {
			this.img = img;
		}
	}

	public static class Bg {
		private Integer anim;
		private Integer facing = 1; // (-1, 0, 1)
		protected SpriteAnimManager animMng;
		private Spr spr;
		private Point offset = new Point();
		private Integer layerno;

		public Integer getAnim() {
			return anim;
		}

		public void setAnim(Integer anim) {
			this.anim = anim;
			animMng = new SpriteAnimManager("Bg", StateMachine.getInstance()
					.getFightDef().getAnim().getGroupSpriteMap());
			animMng.setAction(this.anim);
		}

		public void process() {
			if (animMng != null)
				animMng.process();
		}

		public ImageSpriteSFF getCurrentImageSff() {
			try {
				if (animMng != null) {

					return StateMachine.getInstance().getFightDef().getFiles()
							.getSff().getGroupSpr(animMng.getAction())
							.getImgSpr(animMng.getAnimElemNo() - 1);

				} else {
					return StateMachine.getInstance().getFightDef().getFiles()
							.getSff().getGroupSpr(getSpr().grp).getImgSpr(
									getSpr().getImg());
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		public Integer getFacing() {
			return facing;
		}

		public void setFacing(Integer facing) {
			this.facing = facing;
		}

		public Spr getSpr() {
			return spr;
		}

		public void setSpr(Spr spr) {
			this.spr = spr;
		}

		public Point getOffset() {
			return offset;
		}

		public void setOffset(Point offset) {
			this.offset = offset;
		}

		public Integer getLayerno() {
			return layerno;
		}

		public void setLayerno(Integer layerno) {
			this.layerno = layerno;
		}

		public SpriteAnimManager getAnimMng() {
			return animMng;
		}

		public void setAnimMng(SpriteAnimManager animMng) {
			this.animMng = animMng;
		}

		public SpriteDrawProperties getSpriteDrawProperties() {
			if (animMng != null)
				return animMng.getSpriteDrawProperties();
			else
				return new SpriteDrawProperties();
		}
	}

	public static class Range {
		private Point x = new Point();

		public Point getX() {
			return x;
		}

		public void setX(Point x) {
			this.x = x;
		}
	}

	protected PlayerLifeBar p1 = new PlayerLifeBar();
	protected PlayerLifeBar p2 = new PlayerLifeBar();

	public PlayerLifeBar getP1() {
		return p1;
	}

	public void setP1(PlayerLifeBar p1) {
		this.p1 = p1;
	}

	public PlayerLifeBar getP2() {
		return p2;
	}

	public void setP2(PlayerLifeBar p2) {
		this.p2 = p2;
	}

	public void parse(GroupText grp) throws Exception {
		for (String key : grp.getKeyValues().keySet()) {
			String accessor = key;
			Object[] objectValues = null;

			Valueable[] values = ExpressionFactory.evalExpression(grp
					.getKeyValues().get(key));

			objectValues = new Object[values.length];
			for (int i = 0; i < objectValues.length; ++i) {
				objectValues[i] = values[i].getValue(null);
			}

			try {
				if (objectValues.length == 1) {
					BeanTools.setObject(this, accessor, objectValues[0]);
				} else if (objectValues.length > 1) {
					BeanTools.setObject(this, accessor, objectValues);

				}
			} catch (Exception e) {
//				e.printStackTrace();

				System.err.print(this.getClass().getName()
						+ " doesn't know this property > " + accessor + " = ");
				int i = 0;
				for (Object o: objectValues) {
					i++;
					System.err.print(o + (i < objectValues.length? ", ": ""));

				}
				System.err.println();
			}
		}
	}

	public boolean isParse() {
		return true;
	}

	public void process() {
		{
			float p1CurrentLife = StateMachine.getInstance().getSpriteInstance("1").getInfo().getLife();
			float p1MaxLife = StateMachine.getInstance().getSpriteInstance("1").getInfo().getData().getLife();
			
			float rangeStart = p1.getRange().getX().x;
			float rangeEnd = p1.getRange().getX().y;
			
			int speed = rangeStart > rangeEnd? -1: 1;
			
			float newRangeFrontStart = (p1MaxLife - p1CurrentLife) * rangeEnd / p1MaxLife;
			
			if (p1.getRangeFront().getX().x > (int)newRangeFrontStart && p1.getRangeFront().getX().x >= rangeEnd) {
				p1.getRangeFront().getX().x += speed;
			} else if (p1.getRangeFront().getX().x < (int)newRangeFrontStart && p1.getRangeFront().getX().x < rangeStart) {
				p1.getRangeFront().getX().x -= speed;
			} 
			HitDefSub lastHitdef = StateMachine.getInstance().getSpriteInstance("1").getInfo().getLastHitdef();
			if (lastHitdef == null || lastHitdef.getHittime() <= 0) {
				if (p1.getRangeMid().getX().x > (int)newRangeFrontStart && p1.getRangeMid().getX().x >= rangeEnd) {
					p1.getRangeMid().getX().x += speed;
				} else if (p1.getRangeMid().getX().x < (int)newRangeFrontStart && p1.getRangeMid().getX().x < rangeStart) {
					p1.getRangeMid().getX().x -= speed;
				} 
			}
		}
		p1.process();
		
		{
			float p2CurrentLife = StateMachine.getInstance().getSpriteInstance("2").getInfo().getLife();
			float p2MaxLife = StateMachine.getInstance().getSpriteInstance("2").getInfo().getData().getLife();
			
			float rangeStart = p2.getRange().getX().x;
			float rangeEnd = p2.getRange().getX().y;
			
			int speed = rangeStart > rangeEnd? -1: 1;
			
			float newRangeFrontStart = (p2MaxLife - p2CurrentLife) * rangeEnd / p2MaxLife;
			
			if (p2.getRangeFront().getX().x < (int)newRangeFrontStart && p2.getRangeFront().getX().x <= rangeEnd) {
				p2.getRangeFront().getX().x += speed;
			} else if (p2.getRangeFront().getX().x > (int)newRangeFrontStart && p2.getRangeFront().getX().x > rangeStart) {
				p2.getRangeFront().getX().x -= speed;
			} 
			HitDefSub lastHitdef = StateMachine.getInstance().getSpriteInstance("2").getInfo().getLastHitdef();
			if (lastHitdef == null || lastHitdef.getHittime() <= 0) {
				if (p2.getRangeMid().getX().x < (int)newRangeFrontStart && p2.getRangeMid().getX().x <= rangeEnd) {
					p2.getRangeMid().getX().x += speed;
				} else if (p2.getRangeMid().getX().x > (int)newRangeFrontStart && p2.getRangeMid().getX().x > rangeStart) {
					p2.getRangeMid().getX().x -= speed;
				} 
			}		
		}
		p2.process();
	}

	/*
	 * p1.pos = 140,12 p1.bg0.anim = 10 p1.bg1.spr = 11,0 p1.mid.spr = 12,0
	 * p1.front.spr = 13,0 p1.range.x = 0,-127 ;Player 2 p2.pos = 178,12
	 * p2.bg0.anim = 10 p2.bg0.facing = -1 p2.bg1.spr = 11,0 p2.bg1.facing = -1
	 * p2.mid.spr = 12,0 p2.mid.facing = -1 p2.front.spr = 13,0 p2.front.facing =
	 * -1 p2.range.x = 0,127
	 */
}
