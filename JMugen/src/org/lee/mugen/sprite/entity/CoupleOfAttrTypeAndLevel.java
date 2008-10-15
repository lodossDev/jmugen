package org.lee.mugen.sprite.entity;

import org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub.AttrLevel;
import org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub.AttrType;

public class CoupleOfAttrTypeAndLevel {
	public CoupleOfAttrTypeAndLevel() {
		
	}
	public CoupleOfAttrTypeAndLevel(AttrType attrType, AttrLevel attrLevel) {
		this.attrLevel = attrLevel;
		this.attrType = attrType;
	}
	private AttrType attrType;
	private AttrLevel attrLevel;
	public AttrLevel getAttrLevel() {
		return attrLevel;
	}
	public void setAttrLevel(AttrLevel attrLevel) {
		this.attrLevel = attrLevel;
	}
	public AttrType getAttrType() {
		return attrType;
	}
	public void setAttrType(AttrType attrType) {
		this.attrType = attrType;
	}
	
}

