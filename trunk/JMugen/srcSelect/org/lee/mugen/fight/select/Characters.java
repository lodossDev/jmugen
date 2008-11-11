package org.lee.mugen.fight.select;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lee.mugen.fight.section.Section;
import org.lee.mugen.fight.system.MugenSystem;
import org.lee.mugen.imageIO.PCXLoader;
import org.lee.mugen.imageIO.PCXPalette;
import org.lee.mugen.renderer.ImageContainer;
import org.lee.mugen.sff.SffReader;
import org.lee.mugen.sprite.character.SpriteDef;
import org.lee.mugen.sprite.parser.Parser;

public class Characters implements Section {
	MugenSystem root;
	private Map<String, String> characterStageMap = new HashMap<String, String>();
	private List<String> charactersOrder = new ArrayList<String>();
	private Map<String, SpriteDef> charactersMap = new HashMap<String, SpriteDef>();
	
	
	public Map<String, String> getCharacterStageMap() {
		return characterStageMap;
	}

	public List<String> getCharactersOrder() {
		return charactersOrder;
	}

	public Map<String, SpriteDef> getCharactersMap() {
		return charactersMap;
	}

	public void setCharactersOrder(List<String> charactersOrder) {
		this.charactersOrder = charactersOrder;
	}
	private Map<String, ImageContainer> portraitsMap = new HashMap<String, ImageContainer>();
	private Map<String, ImageContainer> bigPortraitsMap = new HashMap<String, ImageContainer>();
	
	public ImageContainer getPortrait(String character) {
		ImageContainer ic = portraitsMap.get(character);
		if (ic == null) {
			SpriteDef def = charactersMap.get(character);
			String path = new File("resource/chars/" + character + "/" + def.getFiles().getSprite()).getAbsolutePath();
			BufferedImage img = getImageFromSpriteSFF(path, 9000, 0);
			ic = new ImageContainer(img, img.getWidth(), img.getHeight());
			portraitsMap.put(character, ic);
		}
		return ic;
	}
	
	public ImageContainer getBigPortrait(String character) {
		ImageContainer ic = bigPortraitsMap.get(character);
		if (ic == null) {
			SpriteDef def = charactersMap.get(character);
			String path = new File("resource/chars/" + character + "/" + def.getFiles().getSprite()).getAbsolutePath();
			BufferedImage img = getImageFromSpriteSFF(path, 9000, 1);
			ic = new ImageContainer(img, img.getWidth(), img.getHeight());
			bigPortraitsMap.put(character, ic);
		}
		return ic;
	}
	
	private BufferedImage getImageFromSpriteSFF(String filename, int grp, int num) {
		try {
			byte[] data = SffReader.getImage(new FileInputStream(filename), grp, num, null);
			return PCXLoader.loadImageColorIndexed(new ByteArrayInputStream(data), new PCXPalette(), false, true);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public void parse(Object root, String name, String value) throws Exception {
		if (name.replaceAll(" ", "").length() > 0 && name.indexOf(',') > 0) {
			String[] charStage = name.split(" *, *");
			SpriteDef spriteDef = SpriteDef.parseSpriteDef(new File("resource/chars/" + charStage[0] + "/" + charStage[0] + ".def").getAbsolutePath());
			charactersOrder.add(charStage[0]);
			charactersMap.put(charStage[0], spriteDef);
			characterStageMap.put(charStage[0], charStage[1]);
			
		}
	}

}
