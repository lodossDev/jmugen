package org.lee.mugen.sprite.character;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.renderer.PalFxSub;
import org.lee.mugen.sprite.entity.HelperSub;


public class SpriteHelper extends Sprite {
	private HelperSub helperSub;
	public SpriteHelper(String spriteId, final Sprite sprite, HelperSub helperSub) {
		super();
		Sprite clone = null;
		try {
			clone = (Sprite) sprite.clone();
		} catch (CloneNotSupportedException e) {
		}
		assert clone != null;
		
		this.helperSub = helperSub;
		
		this.cmds = clone.cmds;

		this.definition = clone.definition;

		this.info = clone.info;

		this.spriteSnd = clone.spriteSnd;

		this.spriteState = clone.spriteState;
		

		this.palfx = new PalFxSub();

		this.pause = 0;

		this.priority = clone.getPriority();

		this.sprAnimMng = clone.getSprAnimMng();

		this.spriteSFF = clone.getSpriteSFF();

		setSpriteId(spriteId);
	}
	public HelperSub getHelperSub() {
		return helperSub;
	}
	@Override
	public void process() {
		super.process();
	}
	
	@Override
	public boolean remove() {
		if (StateMachine.getInstance().getSpriteInstance(getSpriteId()) == null) {
			return true; // Security : mean that spritehelper is destroyed
		}
		return super.remove();
	}
	
	@Override
	public boolean isFlip() {
		return getInfo().isFlip();
	}
}
