package org.lee.mugen.core;


/**
 * Game state holds game time, round number, number of round, ...
 * Everything about game state but in fight not transition like menu, ...
 * @author Dr Wong
 *
 */
public class GameState {
	GameState() {
	}
	private int gameTime;
	
	private int roundTime = 99 * 60;
	private int roundno;
	private int roundState = 0; // PRE_INTRO
	private int roundsExisted = 0; 
	
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
	private int gameType;

	
	public int getGameType() {
		return gameType;
	}
	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

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
	
	public void process() {
		gameTime++;
		if (roundTime > 0)
			roundTime--;
		
	}

	public int getRoundTime() {
		return roundTime;
	}

	public void setRoundTime(int roundTime) {
		this.roundTime = roundTime;
	}
	public void init() {
		roundTime = 99 * 60; // TODO
		
	}
}
