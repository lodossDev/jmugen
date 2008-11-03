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
		int winRoundCount = StateMachine.getInstance().getFightDef().getRound().getMatch().getWins();
		boolean result = gs.getTeamOneWinRound() >= winRoundCount || gs.getTeamTwoWinRound() > winRoundCount;
		return result? 1: 0;
	}
}
