package org.lee.mugen.core.renderer.game.fight;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.fight.section.Powerbar;
import org.lee.mugen.fight.section.elem.PlayerPowerbar;
import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.MugenDrawer;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Roundstate;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Teammode.TeamMode;
import org.lee.mugen.sprite.cns.type.function.Assertspecial.Flag;

public class PowerBarRender extends LifeBarRender {
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
			Powerbar powerbar = StateMachine.getInstance().getFightDef().getPowerbar();
			PlayerPowerbar plb = powerbar.getP1();
			
			List<Integer> order = new ArrayList<Integer>();
			order.addAll(plb.getBg().keySet());
			Collections.sort(order);
			Map<Integer, Type> map = plb.getBg();
			
			Point pos = plb.getPos();
			for (Integer key: order) {
				Type bg = map.get(key);
				render(md, pos, bg);
			}

			if (plb.getMid() != null)
				render(md, pos, plb.getMid(), plb.getRange().getX().x, plb.getRangeMid().getX().x);
			if (plb.getFront() != null)
				render(md, pos, plb.getFront(), plb.getRange().getX().x, plb.getRangeFront().getX().x);
			
		}
		
		if (StateMachine.getInstance().getTeamTwoMode() == TeamMode.SINGLE) {
			Powerbar powerbar = StateMachine.getInstance().getFightDef().getPowerbar();
			PlayerPowerbar plb = powerbar.getP2();
			
			List<Integer> order = new ArrayList<Integer>();
			order.addAll(plb.getBg().keySet());
			Collections.sort(order);
			Map<Integer, Type> map = plb.getBg();
			
			Point pos = plb.getPos();
			for (Integer key: order) {
				Type bg = map.get(key);
				render(md, pos, bg);
			}
			if (plb.getMid() != null)
				render(md, pos, plb.getMid(), plb.getRange().getX().x, plb.getRangeMid().getX().x);
			if (plb.getFront() != null)
				render(md, pos, plb.getFront(), plb.getRange().getX().x, plb.getRangeFront().getX().x);
		}
		
	}
	
}
