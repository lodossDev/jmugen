package org.lee.mugen.sprite.character.spiteCnsSubClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lee.mugen.sprite.entity.CoupleOfAttrTypeAndLevel;

public class ReversaldefSub extends HitDefSub {
	public static class ReversalAttrClass {
		private List<org.lee.mugen.sprite.character.SpriteCns.Type> types = new ArrayList<org.lee.mugen.sprite.character.SpriteCns.Type>();
		private List<CoupleOfAttrTypeAndLevel> couples = new ArrayList<CoupleOfAttrTypeAndLevel>();


		public boolean isType(org.lee.mugen.sprite.character.SpriteCns.Type type) {
			return types.contains(type);
		}

		public void addType(org.lee.mugen.sprite.character.SpriteCns.Type type) {
			this.types.add(type);
		}

		public void addAttrTypeAndLevel(AttrType attrType, AttrLevel attrLevel) {
			couples.add(new CoupleOfAttrTypeAndLevel(attrType, attrLevel));
		}

		public boolean isAttrTypeAndLevel(AttrType attrType, AttrLevel attrLevel) {
			for (CoupleOfAttrTypeAndLevel c : couples) {
				if ((c.getAttrLevel() == attrLevel || c.getAttrLevel() == null || attrLevel == null)
						&& (c.getAttrType() == attrType || c.getAttrType() == null || attrType == null)
						)
					return true;
			}
			return false;
		}

		public boolean isAttrType(AttrType attrType) {
			for (CoupleOfAttrTypeAndLevel c : couples) {
				if (c.getAttrType() == attrType)
					return true;
			}
			return false;
		}

		public boolean isAttrLevel(AttrType attrType) {
			for (CoupleOfAttrTypeAndLevel c : couples) {
				if (c.getAttrType() == attrType)
					return true;
			}
			return false;
		}

		public Collection<CoupleOfAttrTypeAndLevel> getCouples() {
			return couples;
		}

	}

	private ReversalAttrClass reversal = new ReversalAttrClass();

	public ReversalAttrClass getReversal() {
		return reversal;
	}

	public void setReversal(ReversalAttrClass reversal) {
		this.reversal = reversal;
	}

	public boolean canBlockAttack(HitDefSub otherHitdef) {
		if (getAttr().isType(otherHitdef.getAttr().getType())) {
			for (CoupleOfAttrTypeAndLevel couple: otherHitdef.getAttr().getCouples()) {
				if (getAttr().isAttrTypeAndLevel(couple.getAttrType(), couple.getAttrLevel()))
					return true;
			}
		}
		
		return false; 
	}

}
