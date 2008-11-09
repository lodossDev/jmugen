package org.lee.mugen.fight.system;

import java.awt.Point;

import org.lee.mugen.fight.intro.entity.Fade;
import org.lee.mugen.fight.section.Section;
import org.lee.mugen.fight.section.elem.PlayerFace;
import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.fight.system.elem.Cell;
import org.lee.mugen.fight.system.elem.PlayerSelectInfo;
import org.lee.mugen.fight.system.elem.Stage;

public class SelectInfo implements Section {

	private Fade fadein;
	private Fade fadeout;
	int rows;
	int columns;
	int wrapping;
	Point pos;
	boolean showemptyboxes;
	boolean moveoveremptyboxes;
	Cell cell;
	PlayerSelectInfo p1;
	PlayerSelectInfo p2;
	boolean random$move$snd$cancel;
	Stage stage;
	Type cancel$snd;
	Type portrait;
	Type title;
	int teammenu$move$wrapping;
	
	
	
	@Override
	public void parse(Object root, String name, String value) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
