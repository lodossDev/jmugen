package org.lee.mugen.fight.system;

import org.lee.mugen.fight.intro.entity.Fade;
import org.lee.mugen.fight.section.Section;
import org.lee.mugen.fight.section.elem.Type;

public class TitleInfo implements Section {
	Fade fadein;
	Fade fadeout;
	

	@Override
	public void parse(Object root, String name, String value) throws Exception {
		if (name.startsWith("fadein.")) {
			fadein.parse(Type.getNext(name), value);
		} else if (name.startsWith("fadeout.")) {
			fadeout.parse(Type.getNext(name), value);
			
		}
		
	}

	public void process() {
		
	}
	
}
