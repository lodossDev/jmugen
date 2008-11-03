package org.lee.mugen.fight.section;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.fight.section.elem.PlayerLifebar;
import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub;

public class TurnsLifebar implements Section {
	PlayerLifebar p1 = new PlayerLifebar();
	PlayerLifebar p2 = new PlayerLifebar();

	public PlayerLifebar getP1() {
		return p1;
	}

	public void setP1(PlayerLifebar p1) {
		this.p1 = p1;
	}

	public PlayerLifebar getP2() {
		return p2;
	}

	public void setP2(PlayerLifebar p2) {
		this.p2 = p2;
	}

	@Override
	public void parse(String name, String value) throws Exception {
		if (name.startsWith("p1.")) {
			p1.parse(Type.getNext(name), value);
		} else if (name.startsWith("p2.")) {
			p2.parse(Type.getNext(name), value);
		}

	}

	public void process() {
		{
			Sprite sprite = StateMachine.getInstance().getSpriteInstance("1");
			int life = sprite.getInfo().getLife();
			int maxlife = sprite.getInfo().getData().getLife();
			HitDefSub lastHitdef = sprite.getInfo().getLastHitdef();
			p1.process(true, life, maxlife, lastHitdef == null
					|| lastHitdef.getHittime() <= 0);
		}
		{
			Sprite sprite = StateMachine.getInstance().getSpriteInstance("2");
			int life = sprite.getInfo().getLife();
			int maxlife = sprite.getInfo().getData().getLife();
			HitDefSub lastHitdef = sprite.getInfo().getLastHitdef();
			p2.process(true, life, maxlife, lastHitdef == null
					|| lastHitdef.getHittime() <= 0);
		}
	}

}
