package org.lee.mugen.core.renderer.game.fight;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.core.GameState.WinType;
import org.lee.mugen.fight.section.Round;
import org.lee.mugen.fight.section.elem.FontType;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.MugenDrawer;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Roundstate;

public class RoundRender extends BaseRender {

	
	@Override
	public void render() {
		MugenDrawer md = GraphicsWrapper.getInstance();
		
		Round round = StateMachine.getInstance().getFightDef().getRound();
		
		if (round.getStart().getTime() > 0)
			return;
		
		if (round.getRound().getDefault() != null && round.getRound().getDefault().getDisplaytime() > 0) {
			if (round.getRound().getDefault().getType() instanceof FontType) {
				FontType f = (FontType) round.getRound().getDefault().getType();
				String text = f.getText().replaceAll("%i", StateMachine.getInstance().getGameState().getRoundno() + "");
				f.setText(text);
				
			} else {
				
			}
			render(md, round.getPos(), round.getRound().getDefault());
		}
		
		if ((round.getFight().getDisplaytime() > 0 || round.getFight().getDisplaytime() == -1) 
				&& (round.getRound().getDefault() == null || round.getRound().getDefault().getDisplaytime() <= 0)
				&& StateMachine.getInstance().getGameState().getRoundState() < Roundstate.COMBAT) {
			render(md, round.getPos(), round.getFight());
		}
		
		if (StateMachine.getInstance().getGameState().getRoundState() == Roundstate.VICTORY) {
			if (StateMachine.getInstance().getGameState().getLastWin() == WinType.KO)
				render(md, round.getPos(), round.getKO());
			if (StateMachine.getInstance().getGameState().getLastWin() == WinType.DKO)
				render(md, round.getPos(), round.getDKO());
			if (StateMachine.getInstance().getGameState().getLastWin() == WinType.TO)
				render(md, round.getPos(), round.getTO());
			
			
		}
		
		
		
		
		
		
	}
}
