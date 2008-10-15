package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.GameState;
import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Matchover extends SpriteCnsTriggerFunction {

	public Matchover() {
		super("matchover", new String[] {});
	}

	@Override
	public Object getValue(String spriteId, Valueable... params) {
		GameState gs = StateMachine.getInstance().getGameState();
		boolean result = gs.getTeamOneWinRound() > gs.getTotalRound()/2 || gs.getTeamTwoWinRound() > gs.getTotalRound()/2;
		return result? 1: 0;
	}
}
