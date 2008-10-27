package org.lee.mugen.core;

import java.util.HashMap;
import java.util.Map;

import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.SpriteHelper;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Roundstate;
import org.lee.mugen.sprite.cns.type.function.Assertspecial.Flag;


/**
 * Game state holds game time, round number, number of round, ...
 * Everything about game state but in fight not transition like menu, ...
 * @author Dr Wong
 *
 */
public class GameState {
	GameState() {
	}
	
	private static int DEFAULT_TIME = 99 * 60;
	
	
	public static int getDefaultTime() {
		return DEFAULT_TIME;
	}
	public static void setDefaultTime(int defaultTime) {
		DEFAULT_TIME = defaultTime;
	}

	private int gameTime;
	
	private int roundTime = DEFAULT_TIME;
	private int roundno;
	private int roundState = 0; // PRE_INTRO
	private int roundsExisted = 0; 
	
	
	
	private Map<String, Integer> spriteIdStateMap = new HashMap<String, Integer>();
	
	public int getRoundsExisted() {
		return roundsExisted;
	}
	public void setRoundsExisted(int roundsExisted) {
		this.roundsExisted = roundsExisted;
	}
	public int getRoundState() {
		return roundState;
	}
	public void setRoundState(int roundState) {
		this.roundState = roundState;
	}
	private int teamOneWinRound;
	private int teamTwoWinRound;

	private int drawRound;

	private int totalRound = 3;


	public int getTotalRound() {
		return totalRound;
	}

	public void setTotalRound(int totalRound) {
		this.totalRound = totalRound;
	}

	public int getDrawRound() {
		return drawRound;
	}

	public void setDrawRound(int drawRound) {
		this.drawRound = drawRound;
	}

	public int getTeamOneWinRound() {
		return teamOneWinRound;
	}

	public void setTeamOneWinRound(int teamOneWinRound) {
		this.teamOneWinRound = teamOneWinRound;
	}

	public int getTeamTwoWinRound() {
		return teamTwoWinRound;
	}

	public void setTeamTwoWinRound(int teamTwoWinRound) {
		this.teamTwoWinRound = teamTwoWinRound;
	}

	public int getRoundno() {
		return roundno;
	}

	public void setRoundno(int roundno) {
		this.roundno = roundno;
	}

	public int getGameTime() {
		return gameTime;
	}

	public void setGameTime(int gameTime) {
		this.gameTime = gameTime;
	}
	
	public void leave(StateMachine sm) {
		gameTime++;
		if (roundTime > 0)
			roundTime--;
		
	}

	public void enter(StateMachine sm) {
		for (String spriteId: spriteIdStateMap.keySet()) {
			if (sm.getGlobalEvents().isAssertSpecial(spriteId, Flag.intro)) {
				spriteIdStateMap.put(spriteId, Roundstate.INTRO);
			}
			int stateSpr = spriteIdStateMap.get(spriteId);
			if (stateSpr == Roundstate.INTRO && !sm.getGlobalEvents().isAssertSpecial(spriteId, Flag.intro)) {
				spriteIdStateMap.put(spriteId, Roundstate.COMBAT);
			}
		}

		boolean isAllSameState = true;
		String lastId = null;
		int state = 0;
		for (String spriteId: spriteIdStateMap.keySet()) {
			if (lastId == null)
				lastId = spriteId;
			state = spriteIdStateMap.get(spriteId);
			isAllSameState = isAllSameState && (state == spriteIdStateMap.get(lastId));
			lastId = spriteId;
		}
		
		if (isAllSameState) {
			setRoundState(state);
			if (getRoundState() == Roundstate.COMBAT 
					&& getRoundsExisted() == 0) {
				for (String spriteId: spriteIdStateMap.keySet()) {
					sm.getSpriteInstance(spriteId).getInfo().setCtrl(1);
				}
				setRoundsExisted(1);
				roundTime = DEFAULT_TIME;
			}
		}
	}
	
	public int getRoundTime() {
		return roundTime;
	}

	public void setRoundTime(int roundTime) {
		this.roundTime = roundTime;
	}
	public void init(StateMachine sm) {
		setRoundState(0);
		setRoundsExisted(0);
		roundTime = DEFAULT_TIME;
		spriteIdStateMap.clear();
		for (Sprite s: sm.getSprites()) {
			if (s instanceof SpriteHelper)
				continue;
			spriteIdStateMap.put(s.getSpriteId(), Roundstate.PRE_INTRO);
		}
	}
}
