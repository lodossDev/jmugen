package org.lee.mugen.background;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lee.mugen.renderer.RGB;
import org.lee.mugen.sff.SffReader;
import org.lee.mugen.sprite.baseForParse.SpriteSFF;
import org.lee.mugen.util.BeanTools;

public class BGdef {

	public SpriteSFF getSpr() {
		return spr;
	}

	public BGdef() {
	}

	private SpriteSFF spr;
	private RGB bgclearcolor = new RGB();
	private int debugbg = 0;

	public int getDebugbg() {
		return debugbg;
	}

	public void setDebugbg(int debugbg) {
		this.debugbg = debugbg;
	}


	
	public void parse(Background root, String name, String value, boolean forceImage) throws FileNotFoundException, IOException {
		if (value.indexOf('\\') != -1)
			value = value.replaceAll("\\\\", "/");
		if (name.equals("spr")) {
			File file = new File(root.getCurrentDir(), value);
			if (!file.exists())
				file = new File(root.getCurrentDir().getParentFile(), value);
				
			SffReader sffreader = 
				new SffReader(file.getAbsolutePath(), null);
			spr = new SpriteSFF(sffreader, true, forceImage);
		} else if (name.equals("debugbg")) {
			debugbg = Integer.parseInt(value);
		} else if (name.equals("bgclearcolor")) {
			bgclearcolor = (RGB) BeanTools.getConvertersMap().get(RGB.class).convert(value);
		}
		
	}

	public void free() {
		spr.free();
		
	}

	public RGB getBgclearcolor() {
		return bgclearcolor;
	}
}
