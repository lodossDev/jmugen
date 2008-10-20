package org.lee.mugen.sprite.entity;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.core.renderer.game.SparkRender.SparkRenderFactory;
import org.lee.mugen.sprite.base.AbstractSprite;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.SpriteAnimManager;
import org.lee.mugen.util.MugenRandom;

public class ExplodSprite extends AbstractSprite {

	private ExplodSub explod;

	private PointF pos = new PointF();

	
	
	@Override
	public int getPriority() {
		return explod.getSprpriority();
	}

	boolean error;

	@Override
	public PointF getPosToDraw() {
		ExplodSub explod = getExplod();
		Postype postype = explod.getPostype();
		Sprite parent = StateMachine.getInstance().getRoot(explod.getSprite());
		PointF pos = postype.computePos(explod.getSprite(), 
				StateMachine.getInstance().getFirstEnnmy(parent.getSpriteId()), 
				explod.getPos(), 
				explod.getFacing());
		if (explod.getPostype() == Postype.left) {
			pos.setLocation(pos.getX(), pos.getY());
			return pos;
		} else if (explod.getPostype() == Postype.right) {
			pos.setLocation(pos.getX(), pos.getY());// - (getCurrentImage().getWidth() * explod.getScale().getX()), explod.getPos().y);
			return pos;
		}
		
		return super.getPosToDraw();
	}
	private boolean forceRemove;

	
	@Override
	public float getXScale() {
		return getExplod().getScale().getX();
	}
	
	@Override
	public float getYScale() {
		return getExplod().getScale().getY();
	}
	
	@Override
	public void process() {
		if (forceRemove)
			return;
		this.sprAnimMng.process();
		
		if (explod.getBindtime() > 0 || explod.getBindtime() == -1) {

			
			PointF result = new PointF(explod.getPos());
			if (explod.getRandom().getX() != 0) {
				result.addX(MugenRandom.getRandomNumber(0, (int) explod.getRandom().getX()) - (explod.getRandom().getX()/2));
			}
			if (explod.getRandom().getY() != 0) {
				result.addY(MugenRandom.getRandomNumber(0, (int) explod.getRandom().getY()) - (explod.getRandom().getY()/2));
			}
			Postype postype = explod.getPostype();
			Sprite parent = StateMachine.getInstance().getRoot(explod.getSprite());
			pos = postype.computePos(explod.getSprite(), 
					StateMachine.getInstance().getFirstEnnmy(parent.getSpriteId()), 
					result, 
					explod.getFacing());
			if (explod.getBindtime() > 0) 
				explod.decreaseBindtime();
		} else {
			int mulFace = (explod.getFacing() == 0? -1: explod.getFacing()) * (getExplod().getSprite().isFlip()?-1:1);
			explod.getVel().addX(explod.getAccel().getX());
			explod.getVel().addY(explod.getAccel().getY());
			
			pos.addX(explod.getVel().getX() * mulFace);
			pos.addY(explod.getVel().getY());
			
		}
		
		if (explod.getRemovetime() > 0) {
			explod.decreaseRemovetime();
		}
		explod.decreaseSuperPauseMoveTime();
		explod.decreasePauseMoveTime();
//		process = true;
	}

	@Override
	public boolean remove() {
		if (!StateMachine.getInstance().getOtherSprites().contains(this))
			return true;
		if (forceRemove)
			return true;
		try {
			if (explod.isRemoveongethit()) {
				if (explod.getSprite().getInfo().getLastHitdef() != null) {
					if (explod.getSprite().getInfo().getLastHitdef().getLastTimeHitSomething() == StateMachine.getInstance().getGameState().getGameTime())
					return true;
				}
			}
			if (explod.isSupermove() && StateMachine.getInstance().getGlobalEvents().isSuperPause()
					&& explod.getRemovetime() != -2) {
				return false;
			}

			if (explod.getRemovetime() == -1) {
				return error || forceRemove;// || !StateMachine.getInstance().getGlobalEvents().isSuperPause();
			} else if (explod.getRemovetime() == -2) {
				if (this.sprAnimMng.getCurrentGroupSprite() == null)
					return true;
				return error 
					// cas ou l'explod est creer par un helper et que le helper a ete detruit
//					|| StateMachine.getInstance().getSpriteInstance(explod.getSprite().getSpriteId()) == null 
					|| this.sprAnimMng.getAnimTime() == 0
					|| this.sprAnimMng.getCurrentImageSprite().getDelay() == -1;
				
			} else if (explod.getRemovetime() == 0) {
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private Integer stateWhenExplod;
	private boolean isFlipH;
	public ExplodSprite(ExplodSub explod) {
		stateWhenExplod = explod.getSprite().getSpriteState().getCurrentState().getIntId();
		this.explod = explod;
		SparkRenderFactory instance = SparkRenderFactory.getInstanceOfFightFx();

		final SpriteAnimManager spriteAnimManager = new SpriteAnimManager(
				"Explod" + explod.hashCode());

		if (explod.getAnim() != null && explod.getAnim().isSpriteUse()) {
			spriteAnimManager.setGroupSpriteMap(explod.getSprite().getSprAnimMng()
					.getGroupSpriteMap());
			this.spriteSFF = explod.getSprite().getSpriteSFF();

			spriteAnimManager.setAction(explod.getAnim().getAction());
		} else if (explod.getAnim() != null) {
			spriteAnimManager.setGroupSpriteMap(getMugenFightfx().getAir()
					.getGroupSpriteMap());
			this.spriteSFF = getMugenFightfx().getSff();

			spriteAnimManager.setAction(explod.getAnim().getAction());

		} else {
			this.spriteSFF = null;
			System.err.println("pas de hit spark");
		}
		this.sprAnimMng = spriteAnimManager;

		isFlipH = explod.getSprite().isFlip();
		if (getExplod().getFacing() == 1) {
			isFlipH = getExplod().getSprite().isFlip();
		} else if (getExplod().getFacing() == -1) {
			isFlipH = !getExplod().getSprite().isFlip() ^ (getExplod().getFacing() == -1);
		}
		
		// /
		Postype postype = explod.getPostype();
		PointF result = new PointF(explod.getPos());
		if (explod.getRandom().getX() != 0) {
			result.addX(MugenRandom.getRandomNumber(0, (int) explod.getRandom().getX()) - (explod.getRandom().getX()/2));
		}
		if (explod.getRandom().getY() != 0) {
			result.addY(MugenRandom.getRandomNumber(0, (int) explod.getRandom().getY()) - (explod.getRandom().getY()/2));
		}
		
		pos = postype.computePos(explod.getSprite(), 
				StateMachine.getInstance().getFirstEnnmy(explod.getSprite().getSpriteId()), 
				result, 
				explod.getFacing());

	}

	@Override
	public float getRealXPos() {
		return pos.getX();
	}

	@Override
	public float getRealYPos() {
		return pos.getY();
	}

	@Override
	public boolean isFlip() {
		return isFlipH;
	}

	public boolean isForceRemove() {
		return forceRemove;
	}

	public void setForceRemove(boolean forceRemove) {
		this.forceRemove = forceRemove;
	}

	public ExplodSub getExplod() {
		return explod;
	}

	public void setExplod(ExplodSub explod) {
		this.explod = explod;
	}
	boolean process = true;
	public boolean isProcess() {
		return process;
	}

	public void setProcess(boolean b) {
		process = b;
		
	}

}
