package org.lee.mugen.fight.select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lee.mugen.fight.section.Section;

public class Characters implements Section {
	private Map<String, String> characterStageMap = new HashMap<String, String>();
	private List<String> charactersOrder = new ArrayList<String>();

	@Override
	public void parse(Object root, String name, String value) throws Exception {
		if (name.replaceAll(" ", "").length() > 0 && name.indexOf(',') > 0) {
			String[] charStage = name.split(" *, *");
			charactersOrder.add(charStage[0]);
			characterStageMap.put(charStage[0], charStage[1]);
			
		}
	}

}
