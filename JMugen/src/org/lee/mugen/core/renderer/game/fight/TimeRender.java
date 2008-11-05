package org.lee.mugen.core.renderer.game.fight;

import java.awt.Point;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.fight.section.elem.FontType;
import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.MugenDrawer;
import org.lee.mugen.renderer.PalFxSub;
import org.lee.mugen.renderer.RGB;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Roundstate;
import org.lee.mugen.sprite.cns.type.function.Assertspecial.Flag;

public class TimeRender extends BaseRender {
	PalFxSub thisCustompalFx = new PalFxSub();
	{
		thisCustompalFx.setMul(new RGB(255f, 255f, 255f, 0f));
	}
	public void render() {
		if (StateMachine.getInstance().getGlobalEvents().isAssertSpecial(Flag.nobardisplay))
			return;
		
		if (StateMachine.getInstance().getGameState().getRoundState() != Roundstate.COMBAT) {
			thisCustompalFx.getMul().setA(0f);
			return;
		} else if (thisCustompalFx.getMul().getA() < 255) {
			thisCustompalFx.getMul().setA(thisCustompalFx.getMul().getA() + 1f);
		}
		int time = StateMachine.getInstance().getGameState().getRoundTime();
		MugenDrawer md = GraphicsWrapper.getInstance();
		Type counter = StateMachine.getInstance().getFightDef().getTime().getCounter();
		int framepersec = StateMachine.getInstance().getFightDef().getTime().getFramespercount();
		String display = (time/framepersec) + "";
		if (time == -1) {
			display = "00";
		}
		if (counter.getLayerno() != layer)
			return;
		Point pos = StateMachine.getInstance().getFightDef().getTime().getPos();
		Integer fontIdx = ((FontType)counter.getType()).getFontno();
		
		Integer fontSens = ((FontType)counter.getType()).getAlignmt().getCode();
		md.scale(counter.getScale().getX(), counter.getScale().getY());
		StateMachine.getInstance().getFightDef().getFiles().getFont().get(fontIdx).
				draw((int)(pos.x*1f/counter.getScale().getX()), (int)(pos.y*1f/counter.getScale().getY()),
						md, display, fontSens);
		md.scale(1f/counter.getScale().getX(), 1f/counter.getScale().getY());
	}

}
