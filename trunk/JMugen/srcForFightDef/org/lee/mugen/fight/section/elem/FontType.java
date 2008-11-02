package org.lee.mugen.fight.section.elem;

public class FontType extends Type {
	public enum ALIGNMT {
		left(-1), center(0), right(1);
		
		int code;
		ALIGNMT(int code) {
			this.code = code;
		}
		public static ALIGNMT getValue(int alignmt) {
			switch (alignmt) {
			case -1:
				return left;
			case 0:
				return center;
			case 1:
				return right;
			default:
				throw new IllegalArgumentException();
			}
		}

	}
	int fontno;
	int fontbank;
	ALIGNMT alignmt = ALIGNMT.left;
	
	String text;

	public FontType(int fontno, int fontbank, int alignmt) {
		this(fontno, fontbank, ALIGNMT.getValue(alignmt));
	}
	
	public FontType(int fontno, int fontbank, ALIGNMT alignmt) {
		this.fontno = fontno;
		this.fontbank = fontbank;
		this.alignmt = alignmt;
	}
	
	public FontType() {
	}

	public int getFontno() {
		return fontno;
	}

	public void setFontno(int fontno) {
		this.fontno = fontno;
	}

	public int getFontbank() {
		return fontbank;
	}

	public void setFontbank(int fontbank) {
		this.fontbank = fontbank;
	}

	public ALIGNMT getAlignmt() {
		return alignmt;
	}

	public void setAlignmt(ALIGNMT alignmt) {
		this.alignmt = alignmt;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
