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
				name = new Type();
			}
			name.setType(Type.getNext(pname), name, value);
			name.parse(Type.getNext(pname), value);
		}
	}
}
