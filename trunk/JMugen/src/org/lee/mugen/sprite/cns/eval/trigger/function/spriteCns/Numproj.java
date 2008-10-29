package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import java.util.Collection;

import org.lee.mugen.core.FightEngine;
import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.base.AbstractSprite;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;
import org.lee.mugen.sprite.entity.ProjectileSprite;
import org.lee.mugen.sprite.entity.ProjectileSub;

public class Numproj extends SpriteCnsTriggerFunction {

	public Numproj() {
		super("numproj", new String[] {});
	}

	@Override
	public Object getValue(String spriteId, Valueable... params) {
		int count = 0;

		FightEngine engine = StateMachine.getInstance().getFightEngine();
		Collection<HitDefSub> hitdefs = engine.getHitdefBySpriteHitter(spriteId);
		Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
		for (HitDefSub hitdef : hitdefs) {
			if ((hitdef instanceof ProjectileSub) && StateMachine.getInstance().getRoot((Sprite) hitdef.getSpriteHitter()) == sprite)
				count++;
		}
		for (AbstractSprite spr: StateMachine.getInstance().getOtherSprites()) {
			if (spr instanceof ProjectileSprite) {
				ProjectileSprite proj = (ProjectileSprite) spr;
				if (StateMachine.getInstance().getRoot(proj.getProjectileSub().getSpriteParent()) == sprite)
					count++;
			}
		}
		return count;
	}
}
