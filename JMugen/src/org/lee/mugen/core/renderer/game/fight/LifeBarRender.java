package org.lee.mugen.core.renderer.game.fight;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.fight.section.Lifebar;
import org.lee.mugen.fight.section.elem.FontType;
import org.lee.mugen.fight.section.elem.PlayerLifebar;
import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.MugenDrawer;
import org.lee.mugen.renderer.PalFxSub;
import org.lee.mugen.renderer.RGB;
import org.lee.mugen.sprite.base.AbstractAnimManager.SpriteDrawProperties;
import org.lee.mugen.sprite.baseForParse.ImageSpriteSFF;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Roundstate;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Teammode.TeamMode;
import org.lee.mugen.sprite.cns.type.function.Assertspecial.Flag;

public class LifeBarRender extends BaseRender {





	PalFxSub thisCustompalFx = new PalFxSub();
	{
		thisCustompalFx.setMul(new RGB(255f, 255f, 255f, 0f));
	}
	protected void draw(MugenDrawer md, DrawProperties drawProperties) {
		drawProperties.setPalfx(thisCustompalFx);
		super.draw(md, drawProperties);
	}
	protected void render(MugenDrawer md, Point pos, Type type, int x, int x2) {
		ImageSpriteSFF sff = getImageSFF(type);
		if (sff == null)
			return;
		
		SpriteDrawProperties dp = getSpriteDrawProperties(type);
		if (type.getFacing() == -1) {
			DrawProperties drawProperties = new DrawProperties(
					pos.x - sff.getWidth() + sff.getXAxis(), 
					pos.y - sff.getYAxis(), 
					true, 
					false, sff.getImage());
			drawProperties.setXRightSrc(drawProperties.getXRightSrc() + (x - x2));
			drawProperties.setXRightDst(drawProperties.getXRightDst() + (x - x2));

			draw(md, drawProperties);
		} else if (type.getFacing() == 1) {
			DrawProperties drawProperties = new DrawProperties(
					pos.x - sff.getXAxis(), 
					pos.y - sff.getYAxis(), 
					false, 
					false, sff.getImage());
			drawProperties.setXLeftSrc(drawProperties.getXLeftSrc() + (x - x2));
			drawProperties.setXLeftDst(drawProperties.getXLeftDst() + (x - x2));
			
			draw(md, drawProperties);
		}
	
	}
	
	@Override
	public void render() {
		if (StateMachine.getInstance().getGlobalEvents().isAssertSpecial(Flag.nobardisplay))
			return;
		
		if (StateMachine.getInstance().getGameState().getRoundState() != Roundstate.COMBAT) {
			thisCustompalFx.getMul().setA(0f);
			return;
		} else if (thisCustompalFx.getMul().getA() < 255) {
			thisCustompalFx.getMul().setA(thisCustompalFx.getMul().getA() + 1f);
		}
		
		
		
		MugenDrawer md = GraphicsWrapper.getInstance();
		if (StateMachine.getInstance().getTeamOneMode() == TeamMode.SINGLE) {
			Lifebar lifebar = StateMachine.getInstance().getFightDef().getLifebar();
			PlayerLifebar plb = lifebar.getP1();
			
			List<Integer> order = new ArrayList<Integer>();
			order.addAll(plb.getBg().keySet());
			Collections.sort(order);
			Map<Integer, Type> map = plb.getBg();
			
			Point pos = plb.getPos();
			for (Integer key: order) {
				Type bg = map.get(key);
				render(md, pos, bg);
			}

			render(md, pos, plb.getMid(), plb.getRange().getX().x, plb.getRangeMid().getX().x);
			render(md, pos, plb.getFront(), plb.getRange().getX().x, plb.getRangeFront().getX().x);
			
		}
		
		if (StateMachine.getInstance().getTeamTwoMode() == TeamMode.SINGLE) {
			Lifebar lifebar = StateMachine.getInstance().getFightDef().getLifebar();
			PlayerLifebar plb = lifebar.getP2();
			
			List<Integer> order = new ArrayList<Integer>();
			order.addAll(plb.getBg().keySet());
			Collections.sort(order);
			Map<Integer, Type> map = plb.getBg();
			
			Point pos = plb.getPos();
			for (Integer key: order) {
				Type bg = map.get(key);
				render(md, pos, bg);
			}
			render(md, pos, plb.getMid(), plb.getRange().getX().x, plb.getRangeMid().getX().x);
			render(md, pos, plb.getFront(), plb.getRange().getX().x, plb.getRangeFront().getX().x);
		}
		
	}

	@Override
	public void setPriority(int p) {
	}
	
}
