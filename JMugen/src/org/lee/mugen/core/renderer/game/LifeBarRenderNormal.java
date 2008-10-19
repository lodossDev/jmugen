package org.lee.mugen.core.renderer.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.fight.Face.PlayerFace;
import org.lee.mugen.fight.Lifebar.Bg;
import org.lee.mugen.fight.Lifebar.PlayerLifeBar;
import org.lee.mugen.fight.Name.PlayerName;
import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.MugenDrawer;
import org.lee.mugen.renderer.PalFxSub;
import org.lee.mugen.renderer.RGB;
import org.lee.mugen.renderer.Renderable;
import org.lee.mugen.sprite.base.AbstractAnimManager.SpriteDrawProperties;
import org.lee.mugen.sprite.baseForParse.ImageSpriteSFF;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Roundstate;

public class LifeBarRenderNormal implements Renderable {

	public LifeBarRenderNormal() {
	}
	private void render(MugenDrawer md, Point pos, Bg bg, int x, int x2) {
		ImageSpriteSFF sff = bg.getCurrentImageSff();
		if (sff == null)
			return;
		
		SpriteDrawProperties dp = bg.getSpriteDrawProperties();
		if (bg.getFacing() == -1) {
			DrawProperties drawProperties = new DrawProperties(
					pos.x - sff.getWidth() + sff.getXAxis(), 
					pos.y - sff.getYAxis(), 
					true, 
					false, sff.getImage());
			drawProperties.setXRightSrc(drawProperties.getXRightSrc() + (x - x2));
			drawProperties.setXRightDst(drawProperties.getXRightDst() + (x - x2));

			draw(md, drawProperties);
		} else if (bg.getFacing() == 1) {
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
	
	private void draw(MugenDrawer md, DrawProperties drawProperties) {
		drawProperties.setPalfx(thisCustompalFx);
		md.draw(drawProperties);
	}
	
	public void render(MugenDrawer md, Point pos, Bg bg) {
		ImageSpriteSFF sff = bg.getCurrentImageSff();
		if (sff == null)
			return;
		SpriteDrawProperties dp = bg.getSpriteDrawProperties();
		if (bg.getFacing() == -1) {
			DrawProperties drawProperties = new DrawProperties(
					pos.x - sff.getWidth() + sff.getXAxis() + bg.getOffset().x, 
					pos.y - sff.getYAxis() + bg.getOffset().y, 
					true, 
					false, sff.getImage());
			
			draw(md, drawProperties);
		} else if (bg.getFacing() == 1) {
			DrawProperties drawProperties = new DrawProperties(
					pos.x - sff.getXAxis() + bg.getOffset().x, 
					pos.y - sff.getYAxis() + bg.getOffset().y, 
					false, 
					false, sff.getImage());
			draw(md, drawProperties);
		}
	
	}
	public void render(PlayerLifeBar p) {
		MugenDrawer md = GraphicsWrapper.getInstance();
		List<Integer> order = new ArrayList<Integer>();
		order.addAll(p.getBg().keySet());
		Collections.sort(order);
		Map<Integer, Bg> map = p.getBg();
		
		Point pos = p.getPos();
		for (Integer key: order) {
			Bg bg = map.get(key);
			render(md, pos, bg);
		}
		render(md, pos, p.getMid(), p.getRange().getX().x, p.getRangeMid().getX().x);
		render(md, pos, p.getFront(), p.getRange().getX().x, p.getRangeFront().getX().x);
	}
	
	private void render(PlayerFace p) {
		MugenDrawer md = GraphicsWrapper.getInstance();
		Point pos = p.getPos();
		render(md, pos, p.getBg());
		render(md, pos, p.getFace());
	}
	private void render(PlayerName p, String id) {
		String name = StateMachine.getInstance().getSpriteInstance(id).getDefinition().getInfo().getName();
		name = name.substring(1, name.length() - 1);
		MugenDrawer md = GraphicsWrapper.getInstance();
		Point pos = p.getPos();
		Integer fontIdx = p.getName().getFont()[0];
		
		Integer fontSens = 1;
		if (p.getName().getFont().length > 2)
			fontSens = p.getName().getFont()[2];
		
		StateMachine.getInstance().getFightDef().getFiles().getFont().get(fontIdx).draw(pos.x, pos.y, md, name, fontSens);
	}
	
	
	
	PalFxSub thisCustompalFx = new PalFxSub();
	{
		thisCustompalFx.setMul(new RGB(255f, 255f, 255f, 0f));
	}

	
	public void render() {
		if (StateMachine.getInstance().getGameState().getRoundState() != Roundstate.COMBAT) {
			thisCustompalFx.getMul().setA(0f);
//			thisCustompalFx.getMul().setR(0f);
//			thisCustompalFx.getMul().setG(0f);
//			thisCustompalFx.getMul().setB(0f);
			return;
		} else if (thisCustompalFx.getMul().getA() < 255) {
			thisCustompalFx.getMul().setA(thisCustompalFx.getMul().getA() + 1f);
//			thisCustompalFx.getMul().setR(thisCustompalFx.getMul().getR() + 1f);
//			thisCustompalFx.getMul().setG(thisCustompalFx.getMul().getG() + 1f);
//			thisCustompalFx.getMul().setB(thisCustompalFx.getMul().getB() + 1f);
		}
//		GraphicsWrapper.getInstance().scale(0.5f, 0.5f);
		render(StateMachine.getInstance().getFightDef().getLifebar().getP1());
		render(StateMachine.getInstance().getFightDef().getLifebar().getP2());
		
		render(StateMachine.getInstance().getFightDef().getPowerbar().getP1());
		render(StateMachine.getInstance().getFightDef().getPowerbar().getP2());
		
		render(StateMachine.getInstance().getFightDef().getFace().getP1());
		render(StateMachine.getInstance().getFightDef().getFace().getP2());
		
		render(StateMachine.getInstance().getFightDef().getName().getP1(), "1");
		render(StateMachine.getInstance().getFightDef().getName().getP2(), "2");
		
	}

	public int getPriority() {
		return 10;
	}

	public boolean isProcess() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean remove() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPriority(int p) {
		// TODO Auto-generated method stub
		
	}

}
