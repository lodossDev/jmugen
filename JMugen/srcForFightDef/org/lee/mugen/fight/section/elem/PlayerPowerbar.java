package org.lee.mugen.fight.section.elem;

import java.util.List;

public class PlayerPowerbar extends Bar {
	Type counter;
	List<Type> level;
	public Type getCounter() {
		return counter;
	}
	public void setCounter(Type counter) {
		this.counter = counter;
	}
	public List<Type> getLevel() {
		return level;
	}
	public void setLevel(List<Type> level) {
		this.level = level;
	}
}
