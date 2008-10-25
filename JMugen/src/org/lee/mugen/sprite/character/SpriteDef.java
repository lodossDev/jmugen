package org.lee.mugen.sprite.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lee.mugen.sprite.parser.Parser.GroupText;

public class SpriteDef {
	private String parentPath;
	private Info info = new Info();
	private Files files = new Files();
	private Arcade arcade = new Arcade();
	private List<GroupText> cnsGroups;

	
	public static class Arcade {
		private StoryBoard intro = new StoryBoard();
		private StoryBoard ending = new StoryBoard();
		
		public static class StoryBoard {
			private String storyboard;
			public String getStoryboard() {
				return storyboard;
			}
			public void setStoryboard(String storyboard) {
				this.storyboard = storyboard;
			}
		}

		public StoryBoard getEnding() {
			return ending;
		}

		public StoryBoard getIntro() {
			return intro;
		}
	}
	
	public static class Pal {
		List<Integer> defaults = new ArrayList<Integer>();
		
		public void setDefaults(int...params) {
			for (int p: params)
				defaults.add(p);
		}
		public void setDefaults(Object...params) {
			for (Object p: params)
				defaults.add(new Float(p.toString()).intValue());
		}
		public void setDefaults(String...params) {
			if (params != null && params.length == 1) {
				if (params[0].indexOf(',') != -1)
					setDefaults(params[0]);
				return;
			}
			for (String p: params)
				defaults.add(new Float(p).intValue());
		}

		public Integer[] getDefaults() {
			return defaults.toArray(new Integer[0]);
		}
		public void setDefaults(String params) {
			params = params.trim();
			String[] ps = params.split(",");
			setDefaults(ps);
		}

	}

	public static class Info {
		private String name;
		private String displayname;
		private String versiondate;
		private String mugenversion;
		private String author;
		private Pal pal = new Pal();

		
		
		public String getAuthor() {
			return author.substring(1, author.length() - 1);
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getDisplayname() {
			return displayname;
		}

		public void setDisplayname(String displayname) {
			this.displayname = displayname;
		}

		public String getMugenversion() {
			return mugenversion;
		}

		public void setMugenversion(String mugenversion) {
			this.mugenversion = mugenversion;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getVersiondate() {
			return versiondate;
		}

		public void setVersiondate(String versiondate) {
			this.versiondate = versiondate;
		}

		public Pal getPal() {
			return pal;
		}

	}

	public static class Files {
		private String cmd; // Command

		private String cns; // Const

		private List<String> st = new ArrayList<String>(); // State

		private String stcommon; // Common.cns

		private String sprite;

		private String anim;

		private String sound;

		private List<String> pal = new ArrayList<String>();

		public String getAnim() {
			return anim;
		}

		public void setAnim(String anim) {
			this.anim = anim;
		}

		public String getCmd() {
			return cmd;
		}

		public void setCmd(String cmd) {
			this.cmd = cmd;
		}

		public String getCns() {
			return cns;
		}

		public void setCns(String cns) {
			this.cns = cns;
		}

		public String[] getPal() {
			Collections.sort(pal);
			List<String> result = new ArrayList<String>();
			
			for (String s: pal) {
				String[] token = s.split(",");
				if (token.length == 2) {
					result.add(token[1]);
				}
			}
			
			return result.toArray(new String[0]);
		}

		public void setPal(String...pal) {
			addPal(pal);
		}
		public void addPal(String...pal) {
			this.pal.addAll(Arrays.asList(pal));
		}

		public String getSound() {
			return sound;
		}

		public void setSound(String sound) {
			this.sound = sound;
		}

		public String getSprite() {
			return sprite;
		}

		public void setSprite(String sprite) {
			this.sprite = sprite;
		}

		public String[] getSt() {
			Collections.sort(st);
			List<String> result = new ArrayList<String>();
			
			for (String s: st) {
				String[] token = s.split(",");
				if (token.length == 2) {
					result.add(token[1]);
				}
			}
			
			return result.toArray(new String[0]);
		}

		public void addSt(String...st) {
			this.st.addAll(Arrays.asList(st));
		}
		public void setSt(String...st) {
			addSt(st);
		}

		public String getStcommon() {
			return stcommon;
		}

		public void setStcommon(String stcommon) {
			this.stcommon = stcommon;
		}
		
		

	}

	public Files getFiles() {
		return files;
	}

	public Info getInfo() {
		return info;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String path) {
		this.parentPath = path;
	}

	public Arcade getArcade() {
		return arcade;
	}

	public List<GroupText> getCnsGroups() {
		return cnsGroups;
	}

	public void setCnsGroups(List<GroupText> cnsGroups) {
		this.cnsGroups = cnsGroups;
	}
}
