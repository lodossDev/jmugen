package org.lee.mugen.core.renderer.game.fight;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.fight.section.Fightdef;
import org.lee.mugen.fight.section.elem.AnimType;
import org.lee.mugen.fight.section.elem.FontType;
import org.lee.mugen.fight.section.elem.PlayerFace;
import org.lee.mugen.fight.section.elem.PlayerName;
import org.lee.mugen.fight.section.elem.SprType;
import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.MugenDrawer;
import org.lee.mugen.renderer.PalFxSub;
import org.lee.mugen.renderer.RGB;
import org.lee.mugen.sprite.baseForParse.ImageSpriteSFF;
import org.lee.mugen.sprite.character.AnimElement;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Roundstate;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Teammode.TeamMode;
import org.lee.mugen.sprite.cns.type.function.Assertspecial.Flag;


public class FaceRender extends BaseRender {
	private void render(PlayerFace p, String id) {
		MugenDrawer md = GraphicsWrapper.getInstance();
		List<Integer> order = new ArrayList<Integer>();
		order.addAll(p.getBg().keySet());
		Collections.sort(order);
		Map<Integer, Type> map = p.getBg();
		
		Point pos = p.getPos();
		for (Integer key: order) {
			Type bg = map.get(key);
			render(md, pos, bg);
		}
		if (p.getElem() != null)
			render(md, pos, p.getElem(), id);
	}
	public void render(MugenDrawer md, Point pos, Type type, String id) {
		if (type.getLayerno() != layer)
			return;
		ImageSpriteSFF sff = getImageSFF(type, id);
//		SpriteDrawProperties dp = getSpriteDrawProperties(type, id);
		
		if (sff == null)
			return;
		if (type.getFacing() == -1) {
			DrawProperties drawProperties = new DrawProperties(
					pos.x - sff.getWidth() + sff.getXAxis() + type.getOffset().x, 
					pos.y - sff.getYAxis() + type.getOffset().y, 
					true, 
					false, sff.getImage());
			drawProperties.setXScaleFactor(type.getScale().getX());
			drawProperties.setYScaleFactor(type.getScale().getY());
			draw(md, drawProperties);
		} else if (type.getFacing() == 1) {
			DrawProperties drawProperties = new DrawProperties(
					pos.x - sff.getXAxis() + type.getOffset().x, 
					pos.y - sff.getYAxis() + type.getOffset().y, 
					false, 
					false, sff.getImage());
			drawProperties.setXScaleFactor(type.getScale().getX());
			drawProperties.setYScaleFactor(type.getScale().getY());
			draw(md, drawProperties);
		}
	
	}
	private ImageSpriteSFF getImageSFF(Type type, String id) {
		Fightdef fightdef = StateMachine.getInstance().getFightDef();
		Sprite sprite = StateMachine.getInstance().getSpriteInstance(id);
		if (type.getType() instanceof AnimType) {
			AnimType anim = (AnimType) type.getType();
			AnimElement animElem = anim.getAnim().getCurrentImageSprite();
			
			return sprite.getSpriteSFF().getGroupSpr(animElem.getAirData().getGrpNum()).getImgSpr(animElem.getAirData().getImgNum());
//			dp = anim.getAnim().getSpriteDrawProperties();
		} else if (type.getType() instanceof SprType) {
			SprType spr = (SprType) type.getType();
			return sprite.getSpriteSFF().getGroupSpr(spr.getSpritegrp()).getImgSpr(spr.getSpriteno());
		}
		return null;
	}
	private void render(PlayerName p, String id) {
		if (p.getName().getLayerno() != layer)
			return;
		String name = StateMachine.getInstance().getSpriteInstance(id).getDefinition().getInfo().getName();
		name = name.substring(1, name.length() - 1);
		MugenDrawer md = GraphicsWrapper.getInstance();
		Point pos = p.getPos();
		FontType font = (FontType) p.getName().getType();
		
		int fontSens = font.getAlignmt().getCode();
		md.scale(p.getName().getScale().getX(), p.getName().getScale().getY());
		StateMachine.getInstance().getFightDef().getFiles().getFont().get(font.getFontno()).
				draw((int)(pos.x*1f/p.getName().getScale().getX()), (int)(pos.y*1f/p.getName().getScale().getY()),
						md, name, fontSens);
		md.scale(1f/p.getName().getScale().getX(), 1f/p.getName().getScale().getY());
	}
	
	
	
	PalFxSub thisCustompalFx = new PalFxSub();
	{
		thisCustompalFx.setMul(new RGB(255f, 255f, 255f, 0f));
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
		if (StateMachine.getInstance().getTeamOneMode() == TeamMode.SINGLE) {
			render(StateMachine.getInstance().getFightDef().getFace().getP1(), "1");
			render(StateMachine.getInstance().getFightDef().getName().getP1(), "1");
			
		}
		
		if (StateMachine.getInstance().getTeamTwoMode() == TeamMode.SINGLE) {
			render(StateMachine.getInstance().getFightDef().getFace().getP2(), "2");
			render(StateMachine.getInstance().getFightDef().getName().getP2(), "2");
			
		}
	}
}
