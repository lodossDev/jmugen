package org.lee.mugen.fight;

import java.awt.Point;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.fight.Lifebar.Bg;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.baseForParse.ImageSpriteSFF;
import org.lee.mugen.sprite.parser.ExpressionFactory;
import org.lee.mugen.sprite.parser.Parser.AccessorParser;
import org.lee.mugen.sprite.parser.Parser.GroupText;
import org.lee.mugen.util.BeanTools;

public class Face implements AccessorParser {
	private PlayerFace p1 = new PlayerFace("1");
	private PlayerFace p2 = new PlayerFace("2");
	
	public static class BgFace extends Bg {
		public BgFace(String playerId) {
			this.playerId = playerId;
		}
		String playerId;
		public String getPlayerId() {
			return playerId;
		}
		public void setPlayerId(String playerId) {
			this.playerId = playerId;
		}
		@Override
		public ImageSpriteSFF getCurrentImageSff() {
			try {
				if (animMng != null) {

					return StateMachine.getInstance().getSpriteInstance(playerId).getSpriteSFF().getGroupSpr(animMng.getAction())
							.getImgSpr(animMng.getAnimElemNo() - 1);

				} else {
					return StateMachine.getInstance().getSpriteInstance(playerId).getSpriteSFF().getGroupSpr(getSpr().grp).getImgSpr(
									getSpr().getImg());
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
	}
	
	public static class PlayerFace {
		private Point pos = new Point();
		private Bg bg = new Bg();
		private BgFace face;
		public PlayerFace(String playerId) {
			face = new BgFace(playerId);
			
		}
		public Point getPos() {
			return pos;
		}
		public void setPos(Point pos) {
			this.pos = pos;
		}
		public Bg getBg() {
			return bg;
		}
		public void setBg(Bg bg) {
			this.bg = bg;
		}
		public BgFace getFace() {
			return face;
		}
		public void setFace(BgFace face) {
			this.face = face;
		}
		public void process() {
			bg.process();
			face.process();
		}
		
	}

	public PlayerFace getP1() {
		return p1;
	}

	public void setP1(PlayerFace p1) {
		this.p1 = p1;
	}

	public PlayerFace getP2() {
		return p2;
	}

	public void setP2(PlayerFace p2) {
		this.p2 = p2;
	}

	public boolean isParse() {
		return true;
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
				e.printStackTrace();
				System.err.println(this.getClass().getName()
						+ " setProperties cause problem ! >" + accessor + " = "
						+ objectValues);
			}
		}
	}

	public void process() {
		p1.process();
		p2.process();
		
	}
	
	
}
