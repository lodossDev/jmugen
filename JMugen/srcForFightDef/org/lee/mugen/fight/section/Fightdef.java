package org.lee.mugen.fight.section;

import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.lee.mugen.sprite.parser.Parser;
import org.lee.mugen.sprite.parser.Parser.GroupText;

public class Fightdef {
	public static void main(String[] args) throws Exception {
		String res = IOUtils.toString(new FileInputStream("C:/dev/workspace/JMugen/resource/data/fight.def"));
		List<GroupText> groups = Parser.getGroupTextMap(res);
		new Fightdef().parse(groups);
	}
	
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
	TurnsName turnsname = new TurnsName();
	Time time = new Time();
	Combo combo = new Combo();
	Round round = new Round();
	Winicon winicon = new Winicon(); 
	
	public void parse(Section section, GroupText grp) throws Exception {
		for (String key: grp.getKeysOrdered()) {
			section.parse(key, grp.getKeyValues().get(key));
		}
	}
	public void parse(List<GroupText> groups) throws Exception {
		for (GroupText grp: groups) {
			if (grp.getSection().toLowerCase().equals("files")) {
				parse(files, grp);
			} else if (grp.getSection().toLowerCase().equals("lifebar")) {
				parse(lifebar, grp);
			} else if (grp.getSection().toLowerCase().equals("simullifebar")) {
				parse(simullifebar, grp);
			} else if (grp.getSection().toLowerCase().equals("turnslifebar")) {
				parse(turnslifebar, grp);
			} else if (grp.getSection().toLowerCase().equals("powerbar")) {
				parse(powerbar, grp);
			} else if (grp.getSection().toLowerCase().equals("face")) {
				parse(face, grp);
			} else if (grp.getSection().toLowerCase().equals("simulface")) {
				parse(simulface, grp);
			} else if (grp.getSection().toLowerCase().equals("turnsface")) {
				parse(turnsface, grp);
			} else if (grp.getSection().toLowerCase().equals("name")) {
				parse(name, grp);
			} else if (grp.getSection().toLowerCase().equals("simulname")) {
				parse(simulname, grp);
			} else if (grp.getSection().toLowerCase().equals("turnsname")) {
				parse(turnsname, grp);
			} else if (grp.getSection().toLowerCase().equals("time")) {
				parse(time, grp);
			} else if (grp.getSection().toLowerCase().equals("combo")) {
				parse(combo, grp);
			} else if (grp.getSection().toLowerCase().equals("round")) {
				parse(round, grp);
			} else if (grp.getSection().toLowerCase().equals("winicon")) {
				parse(winicon, grp);
			}
		}
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


}
