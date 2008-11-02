package org.lee.mugen.fight.section;

import org.lee.mugen.fight.TurnsFace;

public class Fightdef {
	Files files = new Files();
	Lifebar lifebar = new Lifebar();
	SimulLifebar simullifebar = new SimulLifebar();
	TurnsLifebar turnslifebar = new TurnsLifebar();
	Powerbar powerbar = new Powerbar();
	Face face = new Face();
	SimulFace simulface = new SimulFace();
	TurnsFace turnsface = new TurnsFace();
	Name name = new Name();
	SimulName simulname = new SimulName();
	
	public void parse(String name, String value) {
	}
	
	public Files getFiles() {
		return files;
	}
	public void setFiles(Files files) {
		this.files = files;
	}
	public Lifebar getLifebar() {
		return lifebar;
	}
	public void setLifebar(Lifebar lifebar) {
		this.lifebar = lifebar;
	}
	public SimulLifebar getSimullifebar() {
		return simullifebar;
	}
	public void setSimullifebar(SimulLifebar simullifebar) {
		this.simullifebar = simullifebar;
	}
	public TurnsLifebar getTurnslifebar() {
		return turnslifebar;
	}
	public void setTurnslifebar(TurnsLifebar turnslifebar) {
		this.turnslifebar = turnslifebar;
	}
	public Powerbar getPowerbar() {
		return powerbar;
	}
	public void setPowerbar(Powerbar powerbar) {
		this.powerbar = powerbar;
	}
	public Face getFace() {
		return face;
	}
	public void setFace(Face face) {
		this.face = face;
	}
	public SimulFace getSimulface() {
		return simulface;
	}
	public void setSimulface(SimulFace simulface) {
		this.simulface = simulface;
	}
	public TurnsFace getTurnsface() {
		return turnsface;
	}
	public void setTurnsface(TurnsFace turnsface) {
		this.turnsface = turnsface;
	}
	public Name getName() {
		return name;
	}
	public void setName(Name name) {
		this.name = name;
	}
	public SimulName getSimulname() {
		return simulname;
	}
	public void setSimulname(SimulName simulname) {
		this.simulname = simulname;
	}
	public TurnsName getTurnsname() {
		return turnsname;
	}
	public void setTurnsname(TurnsName turnsname) {
		this.turnsname = turnsname;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	public Combo getCombo() {
		return combo;
	}
	public void setCombo(Combo combo) {
		this.combo = combo;
	}
	public Round getRound() {
		return round;
	}
	public void setRound(Round round) {
		this.round = round;
	}
	public Winicon getWinicon() {
		return winicon;
	}
	public void setWinicon(Winicon winicon) {
		this.winicon = winicon;
	}
	TurnsName turnsname = new TurnsName();
	Time time = new Time();
	Combo combo = new Combo();
	Round round = new Round();
	Winicon winicon = new Winicon(); 

}
