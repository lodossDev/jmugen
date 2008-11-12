package org.lee.mugen.fight.select;

import java.util.LinkedList;

import org.lee.mugen.fight.section.Section;

public class ExtraStages implements Section {
	LinkedList<String> stages = new LinkedList<String>();
	@Override
	public void parse(Object root, String name, String value) throws Exception {
		stages.add(name);
		
	}
	public LinkedList<String> getStages() {
		return stages;
	}

}
