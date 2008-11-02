package org.lee.mugen.fight.section.elem;

public class PlayerName extends SimpleElement {
	Type name;

	public Type getName() {
		return name;
	}

	public void setName(Type name) {
		this.name = name;
	}
	
	@Override
	public void parse(String pname, String value) {
		super.parse(pname, value);
		if (pname.startsWith("name")) {
			if (name == null) {
				name = Type.buildType(pname.substring(pname.indexOf(".") + 1));
				if (name == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(pname.substring(pname.indexOf(".") + 1), name, value);
			name.parse(pname.substring(pname.indexOf(".") + 1), value);
		}
	}
}
