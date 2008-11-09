package org.lee.mugen.fight.system;

import org.lee.mugen.fight.intro.entity.Fade;
import org.lee.mugen.fight.section.Section;
import org.lee.mugen.fight.system.elem.PlayerVsScreen;

public class VsScreen implements Section {
	int time;
	int originalTime;
	
	Fade fadein;
	Fade fadeout;
	
	PlayerVsScreen p1;
	PlayerVsScreen p2;
		
	
	@Override
	public void parse(Object root, String name, String value) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
