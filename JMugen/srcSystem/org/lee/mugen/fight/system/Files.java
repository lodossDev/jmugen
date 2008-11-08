package org.lee.mugen.fight.system;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.lee.mugen.fight.intro.Intro;
import org.lee.mugen.fight.section.Fightdef;
import org.lee.mugen.fight.section.Section;
import org.lee.mugen.fight.select.Select;
import org.lee.mugen.sff.SffReader;
import org.lee.mugen.snd.Snd;
import org.lee.mugen.snd.SndReader;
import org.lee.mugen.sprite.baseForParse.SpriteSFF;
import org.lee.mugen.sprite.common.resource.FontParser;
import org.lee.mugen.sprite.common.resource.FontProducer;

public class Files implements Section {
	private MugenSystem root;
	
	private SpriteSFF spr;
	private Snd snd;
	private Intro logo$storyboard;
	private Intro intro$storyboard;
	private Select select;
	private Fightdef fight;
	private Map<Integer, FontProducer> fonts = new HashMap<Integer, FontProducer>();

	private String getFile(MugenSystem root, String value) {
		return new File(this.root.getCurrentDir().getAbsolutePath(), value).getAbsolutePath();
	}
	@Override
	public void parse(Object root, String name, String value) throws Exception {
		this.root = (MugenSystem) root;
		
		if (name.equals("spr")) {
			SffReader sffReader = new SffReader(getFile(this.root, value), null);
			spr = new SpriteSFF(sffReader, true, true);
		} else if (name.equals("snd")) {
			snd = SndReader.parse(getFile(this.root, value));
		} else if (name.equals("logo.storyboard")) {
			logo$storyboard = new Intro(getFile(this.root, value));
		} else if (name.equals("intro.storyboard")) {
			intro$storyboard = new Intro(getFile(this.root, value));
		} else if (name.equals("fight")) {
			fight = new Fightdef(getFile(this.root, value));
		} else if (name.startsWith("font")) {
			String sNum = name.substring(4, name.indexOf("."));
			int num = 0;
			if (sNum.length() > 0) {
				num = Integer.parseInt(sNum);
			}
			FontProducer fontProducer = FontParser.getFontProducer(getFile(this.root, value));
			fonts.put(num, fontProducer);
			
		}
	}
	public MugenSystem getRoot() {
		return root;
	}
	public void setRoot(MugenSystem root) {
		this.root = root;
	}
	public SpriteSFF getSpr() {
		return spr;
	}
	public void setSpr(SpriteSFF spr) {
		this.spr = spr;
	}
	public Snd getSnd() {
		return snd;
	}
	public void setSnd(Snd snd) {
		this.snd = snd;
	}
	public Intro getLogo$storyboard() {
		return logo$storyboard;
	}
	public void setLogo$storyboard(Intro logo$storyboard) {
		this.logo$storyboard = logo$storyboard;
	}
	public Intro getIntro$storyboard() {
		return intro$storyboard;
	}
	public void setIntro$storyboard(Intro intro$storyboard) {
		this.intro$storyboard = intro$storyboard;
	}
	public Select getSelect() {
		return select;
	}
	public void setSelect(Select select) {
		this.select = select;
	}
	public Fightdef getFight() {
		return fight;
	}
	public void setFight(Fightdef fight) {
		this.fight = fight;
	}
	public Map<Integer, FontProducer> getFonts() {
		return fonts;
	}
	public void setFonts(Map<Integer, FontProducer> fonts) {
		this.fonts = fonts;
	}
	
	

}
