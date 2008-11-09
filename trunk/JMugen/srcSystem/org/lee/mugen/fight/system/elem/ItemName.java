package org.lee.mugen.fight.system.elem;

import org.lee.mugen.fight.section.Section;

public class ItemName implements Section {
	private static final int arcade = 0;
	private static final int versus = 1;
	private static final int teamarcade = 2;
	private static final int teamversus = 3;
	private static final int teamcoop = 4;
	private static final int survival = 5;
	private static final int survivalcoop = 6;
	private static final int training = 7;
	private static final int watch = 8;
	private static final int options = 9;
	private static final int exit = 10;

	private String[] list = new String[11];
	
	public String[] getList() {
		return list;
	}
	private int currentIndex;
	private int lastIndex;
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	public void increaseCurrentIndex() {
		lastIndex = this.currentIndex;
		this.currentIndex++;
		if (currentIndex > list.length - 1)
			currentIndex = 0;
	}
	public void setCurrentIndex(int currentIndex) {
		if (this.currentIndex == currentIndex)
			return;
		if (currentIndex < 0 || currentIndex > list.length)
			return;
		this.lastIndex = this.currentIndex;
		this.currentIndex = currentIndex;
	}
	public void decreaseCurrentIndex() {
		lastIndex = this.currentIndex;
		this.currentIndex--;
		if (currentIndex < 0)
			currentIndex = list.length - 1;
	}
	public int getLastIndex() {
		return lastIndex;
	}
	public void setList(String[] list) {
		this.list = list;
	}

	private String getGoodString(int index) {
		String word = list[index];
		if (word.startsWith("\"") && word.endsWith("\""))
			return word.substring(1, word.length() - 1);
		return word;
	}
	
	private String getGoodString(String word) {
		if (word.startsWith("\"") && word.endsWith("\""))
			return word.substring(1, word.length() - 1);
		return word;
	}
	
	public String getArcade() {
		return getGoodString(arcade);
	}

	public void setArcade(String arcade) {
		list[ItemName.arcade] = arcade;
	}

	public String getVersus() {
		return getGoodString(versus);
	}

	public void setVersus(String versus) {
		list[ItemName.versus] = versus;
	}

	public String getTeamarcade() {
		return getGoodString(teamarcade);
	}

	public void setTeamarcade(String teamarcade) {
		list[ItemName.teamarcade] = teamarcade;
	}

	public String getTeamversus() {
		return getGoodString(teamversus);
	}

	public void setTeamversus(String teamversus) {
		list[ItemName.teamversus] = teamversus;
	}

	public String getTeamcoop() {
		return getGoodString(teamcoop);
	}

	public void setTeamcoop(String teamcoop) {
		list[ItemName.teamcoop] = teamcoop;
	}

	public String getSurvival() {
		return getGoodString(survival);
	}

	public void setSurvival(String survival) {
		list[ItemName.survival] = survival;
	}

	public String getSurvivalcoop() {
		return getGoodString(survivalcoop);
	}

	public void setSurvivalcoop(String survivalcoop) {
		list[ItemName.survivalcoop] = survivalcoop;
	}

	public String getTraining() {
		return getGoodString(training);
	}

	public void setTraining(String training) {
		list[ItemName.training] = training;
	}

	public String getWatch() {
		return getGoodString(watch);
	}

	public void setWatch(String watch) {
		list[ItemName.watch] = watch;
	}

	public String getOptions() {
		return getGoodString(options);
	}

	public void setOptions(String options) {
		list[ItemName.options] = options;
	}

	public String getExit() {
		return getGoodString(exit);
	}

	public void setExit(String exit) {
		list[ItemName.exit] = exit;
	}

	@Override
	public void parse(Object root, String name, String value) throws Exception {
		if (name.equals("arcade")) {
			list[ItemName.arcade] = getGoodString(value);
		} else if (name.equals("versus")) {
			list[ItemName.versus] = getGoodString(value);
		} else if (name.equals("teamarcade")) {
			list[ItemName.teamarcade] = getGoodString(value);
		} else if (name.equals("teamversus")) {
			list[ItemName.teamversus] = getGoodString(value);
		} else if (name.equals("teamcoop")) {
			list[ItemName.teamcoop] = getGoodString(value);
		} else if (name.equals("survival")) {
			list[ItemName.survival] = getGoodString(value);
		} else if (name.equals("survivalcoop")) {
			list[ItemName.survivalcoop] = getGoodString(value);
		} else if (name.equals("training")) {
			list[ItemName.training] = getGoodString(value);
		} else if (name.equals("watch")) {
			list[ItemName.watch] = getGoodString(value);
		} else if (name.equals("options")) {
			list[ItemName.options] = getGoodString(value);
		} else if (name.equals("exit")) {
			list[ItemName.exit] = getGoodString(value);
		}
	}

}
