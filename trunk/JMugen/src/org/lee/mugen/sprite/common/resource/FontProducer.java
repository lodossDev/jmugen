package org.lee.mugen.sprite.common.resource;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.ImageContainer;
import org.lee.mugen.renderer.MugenDrawer;

public class FontProducer {

	private String name;
	
	private Dimension size;
	private Dimension spacing;
	private Color colors;
	private Dimension offset;
	private String type;

	private ImageContainer mainImage;
	
	Map<Character, Desc> map = new HashMap<Character, Desc>();
	private int computeString(String line) {
		int len = 0;
		for (char c: line.toCharArray()) {
			Desc desc = map.get(c);
			if (desc != null) {
				len += desc.width;
			} else {
				len += size.width;
			}
			len += spacing.width;
		}
		return len;
	}
		
	private int getMaxWidth(String[] lines) {
		int max = 0;
		for (String line: lines)
			if (max < line.length())
				max = computeString(line);
		return max;
	}
	public void draw(int xpos, int ypos, MugenDrawer md, String s) {
		drawLeftToRight(xpos, ypos, md, s);
	}
	public void draw(int xpos, int ypos, MugenDrawer md, String s, int sens) {
		if (sens > 0) {
			drawLeftToRight(xpos, ypos, md, s);
		} else if (sens < 0) {
			drawRightToLeft(xpos, ypos, md, s);
		} else {
			drawCenter(xpos, ypos, md, s);
		}
	}

	
	public ImageContainer getMainImage() {
		return mainImage;
	}

	public void setMainImage(ImageContainer mainImage) {
		this.mainImage = mainImage;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Dimension getSize() {
		return size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}

	public Dimension getSpacing() {
		return spacing;
	}

	public void setSpacing(Dimension spacing) {
		this.spacing = spacing;
	}

	public Color getColors() {
		return colors;
	}

	public void setColors(Color colors) {
		this.colors = colors;
	}

	public Dimension getOffset() {
		return offset;
	}

	public void setOffset(Dimension offset) {
		this.offset = offset;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<Character, Desc> getMap() {
		return map;
	}

	public void setMap(Map<Character, Desc> map) {
		this.map = map;
	}

	public void drawLeftToRight(int x, int y, MugenDrawer md, String s) {
		String[] lines = s.split("\n");
		int width = getMaxWidth(lines);
		int height = lines.length * (size.height + (lines.length > 1? spacing.height: 0));
		
		for (String line: lines) {
			char[] str = line.toCharArray();
			int xposTemp = x;
			for (char c: str) {
				Desc pt = map.get(c);
				if (pt == null) {
					xposTemp += size.width + spacing.width;
					continue;
				}
				DrawProperties dp = new DrawProperties(
						xposTemp, xposTemp + pt.width, y, y - size.height, 
						pt.x , pt.x + pt.width, size.height, 0, 
						false, false,
						mainImage);
				md.draw(dp);
				xposTemp += (pt.width + spacing.width);
			}
			y += size.height + spacing.height;
		}
		
	}
	
	
	public void drawRightToLeft(int x, int y, MugenDrawer md, String s) {
		String[] lines = s.split("\n");
		int width = getMaxWidth(lines);
		int height = lines.length * (size.height + (lines.length > 1? spacing.height: 0));
		x -= width;
		for (String line: lines) {
			char[] str = line.toCharArray();
			int xposTemp = x;
			for (char c: str) {
				Desc pt = map.get(c);
				if (pt == null) {
					xposTemp += size.width + spacing.width;
					continue;
				}
				DrawProperties dp = new DrawProperties(
						xposTemp, xposTemp + pt.width, y, y - size.height, 
						pt.x , pt.x + pt.width, size.height, 0, 
						false, false,
						mainImage);
				md.draw(dp);
				xposTemp += (pt.width + spacing.width);
			}
			y += size.height + spacing.height;
		}
		
	}
	
	public void drawCenter(int x, int y, MugenDrawer md, String s) {
		
		if (s == null)
			s = "";
		String[] lines = s.split("\n");
		int width = getMaxWidth(lines);
		int height = lines.length * (size.height + (lines.length > 1? spacing.height: 0));
		x -= width/2;
		for (String line: lines) {
			char[] str = line.toCharArray();
			int xposTemp = x;
			for (char c: str) {
				Desc pt = map.get(c);
				if (pt == null) {
					xposTemp += size.width + spacing.width;
					continue;
				}
				DrawProperties dp = new DrawProperties(
						xposTemp, xposTemp + pt.width, y, y - size.height, 
						pt.x , pt.x + pt.width, size.height, 0, 
						false, false,
						mainImage);
				md.draw(dp);
				xposTemp += (pt.width + spacing.width);
			}
			y += size.height + spacing.height;
		}
		
	}
}
