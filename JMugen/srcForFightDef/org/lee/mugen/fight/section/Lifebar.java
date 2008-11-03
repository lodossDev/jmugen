package org.lee.mugen.fight.section;

import java.util.List;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.fight.section.elem.PlayerLifebar;
import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub;
import org.lee.mugen.sprite.parser.Parser;
import org.lee.mugen.sprite.parser.Parser.GroupText;

public class Lifebar implements Section {
	PlayerLifebar p1 = new PlayerLifebar();
	PlayerLifebar p2 = new PlayerLifebar();
	
	
	public static void main(String[] args) {
		String test = "[Lifebar]" + "\n" + 
			"p1.pos    = 140,12" + "\n" +
			"p1.bg0.anim = 10" + "\n" +
			"p1.bg1.spr = 11,0" + "\n" +
			"p1.mid.spr = 12,0" + "\n" +
			"p1.front.spr = 13,0" + "\n" +
			"p1.range.x  = 0,-127" + "\n" +
			"p2.pos    = 178,12" + "\n" +
			"p2.bg0.anim = 10" + "\n" +
			"p2.bg0.facing = -1" + "\n" +
			"p2.bg1.spr = 11,0" + "\n" +
			"p2.bg1.facing = -1" + "\n" +
			"p2.mid.spr = 12,0" + "\n" +
			"p2.mid.facing = -1" + "\n" +
			"p2.front.spr = 13,0" + "\n" +
			"p2.front.facing = -1" + "\n" +
			"p2.range.x = 0,127" + "\n";
		
		List<GroupText> grps = Parser.getGroupTextMap(test);
		Lifebar lifebar = new Lifebar();
		for (GroupText grp: grps) {
			for (String key: grp.getKeysOrdered()) {
				System.out.println(key);
				lifebar.parse(key, grp.getKeyValues().get(key));
			}
		}
	}
	
	public void parse(String name, String value) {
		if (name.startsWith("p1.")) {
			p1.parse(Type.getNext(name), value);
		} else if (name.startsWith("p2.")) {
			p2.parse(Type.getNext(name), value);
		}
	}
	
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

	public void process() {
		{
			Sprite sprite = StateMachine.getInstance().getSpriteInstance("1");
			int life = sprite.getInfo().getLife();
			int maxlife = sprite.getInfo().getData().getLife();
			HitDefSub lastHitdef = sprite.getInfo().getLastHitdef();
			p1.process(true, life, maxlife, lastHitdef == null || lastHitdef.getHittime() <= 0);
		}	
		{
			Sprite sprite = StateMachine.getInstance().getSpriteInstance("2");
			int life = sprite.getInfo().getLife();
			int maxlife = sprite.getInfo().getData().getLife();
			HitDefSub lastHitdef = sprite.getInfo().getLastHitdef();
			p2.process(false, life, maxlife, lastHitdef == null || lastHitdef.getHittime() <= 0);
		}	
	}


	
}
