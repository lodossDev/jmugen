package org.lee.mugen.fight.section.elem;


public class PlayerFace extends SimpleElement {
	Type elem;

	public Type getElem() {
		return elem;
	}

	public void setElem(Type elem) {
		this.elem = elem;
	}
	
	@Override
	public void parse(String name, String value) {
		super.parse(name, value);
		if (name.startsWith("elem")) {
			if (elem == null) {
				elem = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (elem == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), elem, value);
			elem.parse(name.substring(name.indexOf(".") + 1), value);
		}
	}
}
