package org.lee.mugen.core.renderer.game.fight;

import java.awt.Point;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.fight.section.Fightdef;
import org.lee.mugen.fight.section.elem.AnimType;
import org.lee.mugen.fight.section.elem.FontType;
import org.lee.mugen.fight.section.elem.SprType;
import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.MugenDrawer;
import org.lee.mugen.renderer.Renderable;
import org.lee.mugen.sprite.base.AbstractAnimManager.SpriteDrawProperties;
import org.lee.mugen.sprite.baseForParse.ImageSpriteSFF;
import org.lee.mugen.sprite.character.AnimElement;

public class BaseRender implements Renderable {

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public boolean isProcess() {
		return true;
	}

	@Override
	public boolean remove() {
		return false;
	}

	@Override
	public void render() {
		
	}

	@Override
	public void setPriority(int p) {
		
	}

	
	
	
	///////////////////////////////
	protected void draw(MugenDrawer md, DrawProperties drawProperties) {
		
		md.draw(drawProperties);
	}
	protected ImageSpriteSFF getImageSFF(Type type) {
		Fightdef fightdef = StateMachine.getInstance().getFightDef();
		if (type.getType() instanceof AnimType) {
			AnimType anim = (AnimType) type.getType();
			AnimElement animElem = anim.getAnim().getCurrentImageSprite();
			if (animElem.getAirData().getGrpNum() == -1)
				return null;
			return fightdef.getFiles().getSff().getGroupSpr(animElem.getAirData().getGrpNum()).getImgSpr(animElem.getAirData().getImgNum());
//			dp = anim.getAnim().getSpriteDrawProperties();
		} else if (type.getType() instanceof SprType) {
			SprType spr = (SprType) type.getType();
			if (fightdef.getFiles().getSff().getGroupSpr(spr.getSpritegrp()) == null 
					|| fightdef.getFiles().getSff().getGroupSpr(spr.getSpritegrp()).getImgSpr(spr.getSpriteno()) == null)
				return null;
			return fightdef.getFiles().getSff().getGroupSpr(spr.getSpritegrp()).getImgSpr(spr.getSpriteno());
		}
		return null;
	}
	protected SpriteDrawProperties getSpriteDrawProperties(Type type) {
		if (type.getType() instanceof AnimType) {
			AnimType anim = (AnimType) type.getType();
			return anim.getAnim().getSpriteDrawProperties();
		} else if (type.getType() instanceof SprType) {
		}
		return null;
	}
	
	
	public void render(MugenDrawer md, Point pos, Type type) {
		float xScale = 1;
		float yScale = 1;
		
		if (type.getScale().getX() != 1 || type.getScale().getY() != 1) {
			xScale = type.getScale().getX();
			yScale = type.getScale().getY();
		}
		if ((type.getType() instanceof AnimType) || (type.getType() instanceof SprType)) {
			ImageSpriteSFF sff = getImageSFF(type);
			SpriteDrawProperties dp = getSpriteDrawProperties(type);

			if (sff == null)
				return;
			if (type.getFacing() == -1) {
				DrawProperties drawProperties = new DrawProperties(
						pos.x - sff.getWidth() + sff.getXAxis() + type.getOffset().x, 
						pos.y - sff.getYAxis() + type.getOffset().y, 
						true, 
						false, sff.getImage());
				drawProperties.setXScaleFactor(xScale);
				drawProperties.setYScaleFactor(yScale);
				draw(md, drawProperties);
			} else if (type.getFacing() == 1) {
				DrawProperties drawProperties = new DrawProperties(
						pos.x - sff.getXAxis() + type.getOffset().x, 
						pos.y - sff.getYAxis() + type.getOffset().y, 
						false, 
						false, sff.getImage());
				drawProperties.setXScaleFactor(xScale);
				drawProperties.setYScaleFactor(yScale);
				draw(md, drawProperties);
			}
			
		} else if (type.getType() instanceof FontType) {
			FontType font = (FontType) type.getType();
			String text = font.getText();
			
			int fontSens = font.getAlignmt().getCode();
			
			StateMachine.getInstance().getFightDef().getFiles().getFont().get(font.getFontno()).draw(pos.x + type.getOffset().x, pos.y + type.getOffset().y, md, text, fontSens);
		}
	}
}
