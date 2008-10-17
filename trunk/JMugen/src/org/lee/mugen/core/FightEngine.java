package org.lee.mugen.core;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.lee.mugen.core.renderer.game.ProjectileRender;
import org.lee.mugen.core.renderer.game.SparkRender;
import org.lee.mugen.core.renderer.game.SparkhitRender;
import org.lee.mugen.core.sound.SoundSystem;
import org.lee.mugen.lang.Wrap;
import org.lee.mugen.lang.Wrapper;
import org.lee.mugen.snd.Snd;
import org.lee.mugen.sprite.base.AbstractSprite;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.SpriteCns;
import org.lee.mugen.sprite.character.SpriteHelper;
import org.lee.mugen.sprite.character.SpriteCns.MoveType;
import org.lee.mugen.sprite.character.SpriteCns.Type;
import org.lee.mugen.sprite.character.spiteCnsSubClass.HitBySub;
import org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub;
import org.lee.mugen.sprite.character.spiteCnsSubClass.NotHitBySub;
import org.lee.mugen.sprite.character.spiteCnsSubClass.ReversaldefSub;
import org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub.AttrClass;
import org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub.AttrType;
import org.lee.mugen.sprite.character.spiteCnsSubClass.ReversaldefSub.ReversalAttrClass;
import org.lee.mugen.sprite.cns.type.function.Assertspecial;
import org.lee.mugen.sprite.cns.type.function.Projectile;
import org.lee.mugen.sprite.entity.ProjectileSprite;
import org.lee.mugen.sprite.entity.ProjectileSub;

/**
 * FightEngine groups hitdefs and decice what's rules for hit, protect, ...
 * @author Dr Wong
 *
 */
public class FightEngine {

	// hitdefs doit être dans cette liste et mis dans le spriteCns une fois
	// qu'il a touche le sprite
	// car un hitdef peut toucher plusieurs sprites
	private List<HitDefSub> hitdefs = new LinkedList<HitDefSub>();
	
	private List<HitDefSub> hitdefsWhenClns1IsDefine = new LinkedList<HitDefSub>();

	public void add(HitDefSub hitdef) {
		hitdefs.add(hitdef);
		if (hitdef instanceof ProjectileSub) {
			ProjectileSub projHitdef = (ProjectileSub) hitdef;
			ProjectileRender projRender = new ProjectileRender(projHitdef);
			StateMachine.getInstance().addRender(projRender);
			StateMachine.getInstance().getOtherSprites().add(
					projHitdef.getSpriteHitter());
		}
	}

	public void process() {
		processPosition();
		Set<HitDefSub> removes = new HashSet<HitDefSub>();
		Set<HitDefSub> notProcessThisTime = new HashSet<HitDefSub>();
		for (HitDefSub hitdef : hitdefs.toArray(new HitDefSub[hitdefs.size()])) {
			for (Sprite sprite : StateMachine.getInstance().getEnnmies(hitdef.getSpriteHitter())) {
				if (hitdef.getSpriteId().equals(sprite.getSpriteId())
						|| notProcessThisTime.contains(hitdef))
					continue;
				if (hitdef.getSpriteHitter() != null && hitdef.getSpriteHitter().isPause())
					continue;
//				if (sprite != null && sprite.isPause())
//					continue;
				process(hitdef, sprite, removes, notProcessThisTime);
			}
			if (hitdef instanceof ProjectileSub) {
				ProjectileSub projHitter = (ProjectileSub) hitdef;
				for (HitDefSub hitdefHitted : hitdefs) {
					if (notProcessThisTime.contains(hitdef))
						continue;
					if (hitdefHitted instanceof ProjectileSub) {
						ProjectileSub projHitted = (ProjectileSub) hitdefHitted;
						if (hitdefHitted != projHitter) {
							process(projHitter, projHitted.getSpriteHitter(),
									removes, notProcessThisTime);
						}
					}
				}
			}
			if (!(hitdef instanceof ProjectileSub)) {
				Sprite spr = StateMachine.getInstance().getSpriteInstance(
						hitdef.getSpriteId());
				if (spr == null || spr.getInfo().getMovetype() != MoveType.A)
					removes.add(hitdef);
			}
			if (hitdefsWhenClns1IsDefine.contains(hitdef) && !(hitdef instanceof ProjectileSub)) {
				if (!hasAttackRect(hitdef.getSpriteHitter()) && hitdef.getHittime() > 0) {
					removes.add(hitdef);
					hitdefsWhenClns1IsDefine.remove(hitdef);	
				}
				
			} else if (hasAttackRect(hitdef.getSpriteHitter())) {
				hitdefsWhenClns1IsDefine.add(hitdef);
			}
		}
		hitdefsWhenClns1IsDefine.removeAll(removes);
		hitdefs.removeAll(removes);
	}
	

	private void process(HitDefSub hitdef, AbstractSprite sprite,
			Set<HitDefSub> removes, Set<HitDefSub> notProcess) {
		if (sprite instanceof Sprite) {
			attemptToProtect(hitdef, (Sprite) sprite);
		}
		if (hitdef instanceof ProjectileSub) {
			if (sprite instanceof ProjectileSprite) {
				processProjectileVsProjectile((ProjectileSub) hitdef,
						(ProjectileSprite) sprite, removes, notProcess);
			} else if (sprite instanceof Sprite) {
				processProjectileVsSprite((ProjectileSub) hitdef,
						(Sprite) sprite, removes, notProcess);
			}
		} else {
			if (sprite instanceof ProjectileSprite) {
				processHitdefVsProjectile(hitdef, (ProjectileSprite) sprite,
						removes, notProcess);

			} else if (sprite instanceof Sprite) {
				processHitdefVsSprite(hitdef, (Sprite) sprite, removes,
						notProcess);
			}
		}
	}

	//
	private void processHitdefVsSprite(HitDefSub hitdef, Sprite sprite,
			Set<HitDefSub> removes, Set<HitDefSub> notProcess) {
		if (sprite.getInfo().getNothitby().getTime() > 0)
			return; // TODO
		if (isBlockState(sprite)
				&& !canOneHitsTwo(hitdef.getHitFlag(), hitdef.getGuardFlag(),
						sprite) && canHitDefByPassHitByAndNotHitBy(hitdef.getAttr(), sprite)) {
			processHitdefVsSpriteProtectedMode(hitdef, sprite, removes,
					notProcess);
		} else if (canOneHitsTwo(hitdef.getHitFlag(), hitdef.getGuardFlag(),
				sprite)) {
			processHitdefVsSpriteHittedMode(hitdef, sprite, removes, notProcess);
		}
	}

	private boolean canHitDefByPassHitByAndNotHitBy(AttrClass attr,
			Sprite sprite) {
		SpriteCns info = sprite.getInfo();
		boolean isHitBy = false;
		boolean isNotHitBy = false;

		boolean isHitByActive = false;
		boolean isNotHitByActive = false;

		if (info.getHitby().getTime() > 0) {
			isHitByActive = true;
			HitBySub hitby = info.getHitby();
			ReversalAttrClass otherAttr = hitby.getValue();
			if (otherAttr == null)
				otherAttr = hitby.getValue2();
			isHitBy = otherAttr.isType(attr.getType()) && attr.isAttrTypesAndLevels(otherAttr.getCouples());
			
		}
		if (info.getNothitby().getTime() > 0) {
			isNotHitByActive = true;
			NotHitBySub notHitby = info.getNothitby();
			ReversalAttrClass otherAttr = notHitby.getValue();
			if (otherAttr == null)
				otherAttr = notHitby.getValue2();
			isNotHitByActive = otherAttr.isType(attr.getType()) && attr.isAttrTypesAndLevels(otherAttr.getCouples());
		}
		return (!isHitByActive || !isHitBy) && (!isNotHitByActive || !isNotHitBy);
	}

	//
	private void processHitdefVsProjectile(HitDefSub hitdef,
			ProjectileSprite sprite, Set<HitDefSub> removes,
			Set<HitDefSub> notProcess) {
//		Wrapper<Rectangle> wrap = new Wrapper<Rectangle>();

		if (isHitdefHitAttackRec(hitdef, sprite)) {
			// TODO
		}
	}

	private void processHitdefVsSpriteProtectedMode(HitDefSub hitdef,
			Sprite sprite, Set<HitDefSub> removes, Set<HitDefSub> notProcess) {
//		Wrapper<Rectangle> wrap = new Wrapper<Rectangle>();

		if (isHitdefHitCollisionRec(hitdef, sprite)) {
			Sprite spriteHitter = StateMachine.getInstance().getSpriteInstance(
					hitdef.getSpriteId());
			
			hitdef.setTargetId(sprite.getSpriteId());
			// TODO Make a class for shaking

			try {
				playSoundGuard(hitdef, sprite);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			spriteHitter.setPause(hitdef.getGuard().getPausetime().getP1_pausetime());

			sprite.getInfo().setLastHitdef(hitdef);


			setSpriteBlockBySpriteState(hitdef, sprite);
			
			sprite.getInfo().getShake().setAmpl(2);
			sprite.getInfo().getShake().setTime(
					hitdef.getGuard().getPausetime().getP2_shaketime());
			sprite.setPause(hitdef.getGuard().getPausetime().getP2_shaketime());
			
			
			// envshake
    		StateMachine.getInstance().getInstanceOfStage().getCamera().setEnvShake(hitdef.getEnvshake());
			
			

			removes.add(hitdef);
			drawSparkHit(hitdef, sprite);
//			drawSparkHit(hitdef, sprite, wrap.getValue());
		}
	}

	private void processHitdefVsSpriteHittedMode(HitDefSub hitdef,
			Sprite sprite, Set<HitDefSub> removes, Set<HitDefSub> notProcess) {
		if (hasAttackRect(sprite)) {
			processHitdefVsAttackRec(hitdef, sprite, removes, notProcess);
		} else {
			processHitdefVsCollisionRec(hitdef, sprite, removes, notProcess);
		}
	}

	private void processHitdefVsCollisionRec(final HitDefSub hitdef,
			final Sprite sprite, Set<HitDefSub> removes,
			Set<HitDefSub> notProcess) {
		processHitdefVsCollisionRec(hitdef, sprite, removes, notProcess, false);
	}

	private void processHitdefVsCollisionRec(final HitDefSub hitdef,
			final Sprite sprite, Set<HitDefSub> removes,
			Set<HitDefSub> notProcess, boolean force) {
		
		Wrapper<Rectangle> wrap = new Wrapper<Rectangle>();
		if (hitdef instanceof ReversaldefSub)
			return;
		if (isHitdefHitCollisionRec(hitdef, sprite, wrap) || force) {
			hitdef.setTargetId(sprite.getSpriteId());

			Sprite spriteHitter = StateMachine.getInstance().getSpriteInstance(
					hitdef.getSpriteId());

			// TODO Make a class for shaking

			try {
				playSoundHit(hitdef, sprite);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (spriteHitter != null) // It's a helper
				spriteHitter.setPause(hitdef.getPausetime().getP1_pausetime());
			
			sprite.getInfo().setLastHitdef(hitdef);
			sprite.getInfo().addLife(-hitdef.getDamage().getHit_damage());
			setSpriteHitSpriteState(hitdef, sprite);
			
			sprite.getInfo().getShake().setAmpl(2);
			sprite.getInfo().getShake().setTime(
					hitdef.getPausetime().getP2_shaketime());
			sprite.setPause(hitdef.getPausetime().getP2_shaketime());
//			sprite.getInfo().addLife(-10);

			// envshake
    		StateMachine.getInstance().getInstanceOfStage().getCamera().setEnvShake(hitdef.getEnvshake());

			
			drawSparkHit(hitdef, sprite, wrap.getValue());

			removes.add(hitdef);
		}
	}

	private void processHitdefVsAttackRec(HitDefSub hitdef, Sprite sprite,
			Set<HitDefSub> removes, Set<HitDefSub> notProcess) {
//		Wrapper<Rectangle> wrap = new Wrapper<Rectangle>();

		boolean isHitdefHitAttackRec = isHitdefHitAttackRec(hitdef, sprite);
		if (hitdef instanceof ReversaldefSub 
				&& isHitdefHitAttackRec && 
				((ReversaldefSub)hitdef).canBlockAttack(getFirstHitdefBySpriteHitter(sprite.getSpriteId()))) {

			hitdef.setTargetId(sprite.getSpriteId());

			Sprite spriteHitter = StateMachine.getInstance().getSpriteInstance(
					hitdef.getSpriteId());

			// TODO Make a class for shaking

			try {
				playSoundHit(hitdef, sprite);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			spriteHitter.setPause(hitdef.getPausetime().getP1_pausetime());
			
			sprite.getInfo().setLastHitdef(hitdef);
			sprite.getInfo().addLife(-hitdef.getDamage().getGuard_damage());

			setSpriteHitSpriteState(hitdef, sprite);
			
			sprite.getInfo().getShake().setAmpl(2);
			sprite.getInfo().getShake().setTime(
					hitdef.getPausetime().getP2_shaketime());
			sprite.setPause(hitdef.getPausetime().getP2_shaketime());


			// envshake
    		StateMachine.getInstance().getInstanceOfStage().getCamera().setEnvShake(hitdef.getEnvshake());

			
			drawSparkHit(hitdef, sprite);

			removes.add(hitdef);
		
		} else if (isHitdefHitAttackRec) {
			int onePrio = hitdef.getPriority().getHit_prior();
			HitDefSub hitdefTwo = getFirstHitdefBySpriteHitter(sprite.getSpriteId());
			if (hitdefTwo == null)
				return; // and do nothing wait next time
			int twoPrio = hitdefTwo.getPriority().getHit_prior();
			notProcess.add(hitdefTwo);
			removes.add(hitdefTwo);

			// /////////////
			HitDefSub hitdefOne = hitdef;
			Sprite one = StateMachine.getInstance().getSpriteInstance(
					hitdef.getSpriteId());
			Sprite two = sprite;

			removes.add(hitdefOne);

			// One is hitted
			if (onePrio <= twoPrio) {
				processHitdefVsCollisionRec(hitdefTwo, one, removes,
						notProcess, true);
				// one.getInfo().setLastHitdef(hitdef);
				// hitdefOne.setTargetId(two.getSpriteId());
				//				
				//				
				// // TODO Make a class for shaking
				//				
				// playSoundHit(hitdefOne, one);
				//
				// one.getInfo().getShake().setAmpl(2);
				// one.getInfo().getShake().setTime(hitdef.getPausetime().getP2_shaketime());
				//				
				// setSpriteHitSpriteState(hitdefOne, one);
				// drawSparkHit(hitdefOne, one);
				// StateMachine.getInstance().getSpriteRenderInstance(hitdefOne.getSpriteId()).setPriority(SpriteRender.DEF_PRIO
				// + 10);
				// StateMachine.getInstance().getSpriteRenderInstance(one.getSpriteId()).setPriority(SpriteRender.DEF_PRIO);
			}
			if (onePrio >= twoPrio) {
				processHitdefVsCollisionRec(hitdefOne, two, removes,
						notProcess, true);

				// // Two is hit
				// two.getInfo().setLastHitdef(hitdef);
				// hitdefTwo.setTargetId(one.getSpriteId());
				//				
				//				
				// // TODO Make a class for shaking
				//				
				// playSoundHit(hitdefTwo, two);
				//
				// two.getInfo().getShake().setAmpl(2);
				// two.getInfo().getShake().setTime(hitdef.getPausetime().getP2_shaketime());
				//				
				// setSpriteHitSpriteState(hitdefTwo, two);
				// drawSparkHit(hitdefTwo, two);
				// StateMachine.getInstance().getSpriteRenderInstance(hitdefTwo.getSpriteId()).setPriority(SpriteRender.DEF_PRIO
				// + 10);
				// StateMachine.getInstance().getSpriteRenderInstance(two.getSpriteId()).setPriority(SpriteRender.DEF_PRIO);
				//
				//				
			}

			// TODO
		}
	}

	// //////////////////////////////////////////
	//
	private void processProjectileVsProjectile(ProjectileSub hitdef,
			ProjectileSprite sprite, Set<HitDefSub> removes,
			Set<HitDefSub> notProcess) {
//		Wrapper<Rectangle> wrap = new Wrapper<Rectangle>();

		if (isHitdefHitAttackRec(hitdef, sprite)) {
			// TODO
		}
	}

	private void processProjectileVsSprite(ProjectileSub hitdef, Sprite sprite,
			Set<HitDefSub> removes, Set<HitDefSub> notProcess) {
		if (isBlockState(sprite)) {
			processProjectileVsSpriteProtectedMode(hitdef, sprite, removes,
					notProcess);
		} else {
			processProjectileVsSpriteHittedMode(hitdef, sprite, removes,
					notProcess);
		}
	}

	//
	private void processProjectileVsSpriteProtectedMode(ProjectileSub hitdef,
			Sprite sprite, Set<HitDefSub> removes, Set<HitDefSub> notProcess) {
		
//		Wrapper<Rectangle> wrap = new Wrapper<Rectangle>();

		if (isHitdefHitCollisionRec(hitdef, sprite)) {

			
			hitdef.setTargetId(sprite.getSpriteId());

			Sprite spriteHitter = StateMachine.getInstance().getSpriteInstance(
					hitdef.getSpriteId());

			// TODO Make a class for shaking

			try {
				playSoundGuard(hitdef, sprite);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			spriteHitter.setPause(hitdef.getGuard().getPausetime().getP1_pausetime());

			long currentTime = StateMachine.getInstance().getGameState().getGameTime();
			hitdef.setLastTimeBlockBySomething(currentTime);
			hitdef.setLastTimeHitSomething(currentTime);
			
			sprite.getInfo().setLastHitdef(hitdef);
			setProjectileBlockBySpriteState(hitdef, sprite);
			
			sprite.getInfo().getShake().setAmpl(2);
			sprite.getInfo().getShake().setTime(
					hitdef.getGuard().getPausetime().getP2_shaketime());
			sprite.setPause(hitdef.getGuard().getPausetime().getP2_shaketime());

			
			// TODO Projectile Life
			if (hitdef.getProjremove() != 0) {
				removes.add(hitdef);
				hitdef.getSpriteHitter().setProjHitSprite();

			}
			hitdef.addProjhits(-1);
			if (hitdef.getProjhits() <= 0) {
				removes.add(hitdef);
			}
//			drawSparkHit(hitdef, sprite, wrap.getValue());
			drawSparkHit(hitdef, sprite);
		}
	}

	//
	private void processProjectileVsSpriteHittedMode(ProjectileSub hitdef,
			Sprite sprite, Set<HitDefSub> removes, Set<HitDefSub> notProcess) {
		if (hasAttackRect(sprite)) {
			processProjectileVsAttackRec(hitdef, sprite, removes, notProcess);
		} else {
			processProjectileVsCollisionRec(hitdef, sprite, removes, notProcess);
		}
	}

	private void processProjectileVsAttackRec(ProjectileSub hitdef,
			Sprite sprite, Set<HitDefSub> removes, Set<HitDefSub> notProcess) {
		
		Wrapper<Rectangle> wrap = new Wrapper<Rectangle>();

		if (isHitdefHitAttackRec(hitdef, sprite, wrap)) {
			// TODO
		}
	}

	private void processProjectileVsCollisionRec(ProjectileSub hitdef,
			Sprite sprite, Set<HitDefSub> removes, Set<HitDefSub> notProcess) {

//		Wrapper<Rectangle> wrap = new Wrapper<Rectangle>();
		if (isHitdefHitCollisionRec(hitdef, sprite)) {
			long time = StateMachine.getInstance().getGameState().getGameTime();
			hitdef.setLastTimeHitSomething(time);
			
			hitdef.setTargetId(sprite.getSpriteId());

			Sprite spriteHitter = StateMachine.getInstance().getSpriteInstance(
					hitdef.getSpriteId());

			// TODO Make a class for shaking

			try {
				playSoundHit(hitdef, sprite);
				
			} catch (Exception e) {
				e.printStackTrace();
			}



			sprite.getInfo().setLastHitdef(hitdef);
			
			setProjectileHitSpriteState(hitdef, sprite);
			// TODO Projectile Life
			if (hitdef.getProjremove() != 0) {
				removes.add(hitdef);
				hitdef.getSpriteHitter().setProjHitSprite();

			}
			Projectile.setHitedFor(hitdef.getProjid());
			hitdef.addProjhits(-1);
			if (hitdef.getProjhits() <= 0) {
				removes.add(hitdef);
			}
			
			
			spriteHitter.setPause(hitdef.getPausetime().getP1_pausetime());

			sprite.getInfo().getShake().setAmpl(2);
			sprite.getInfo().getShake().setTime(
					hitdef.getPausetime().getP2_shaketime());
			sprite.setPause(hitdef.getPausetime().getP2_shaketime());
			
			drawSparkHit(hitdef, sprite);
		}
	}

	// //////////////////////////////////////
	// Set Sprites states
	// /////////////////////////////////////

	/**
	 * Set Sprite, Sprite state
	 */
	private void setSpriteHitSpriteState(HitDefSub hitdef, Sprite sprite) {

		if (hitdef instanceof ProjectileSub)
			throw new IllegalArgumentException("Only Hitdef");
		Sprite sprHitter = (Sprite) hitdef.getSpriteHitter();
		
		if (hitdef.getP2facing() == -1) {
			sprite.getInfo().setFlip(sprHitter.isFlip());
		}
		if (hitdef.getP2facing() == 1) {
			sprite.getInfo().setFlip(!sprHitter.isFlip());
		}
		if (hitdef.getSnap() != null) {
			sprite.getInfo().setXPos((float) (hitdef.getSpriteHitter().getRealXPos() + hitdef.getSnap().getX()));
			sprite.getInfo().setYPos((float) (hitdef.getSpriteHitter().getRealYPos() + hitdef.getSnap().getY()));
		}
		
		if (hitdef.getP2stateno() != null) {
//			sprite.getSprAnimMng().setChangeAnim2(true, sprHitter.getSpriteId());
			sprite.getSpriteState().targetState(sprHitter.getSpriteId(),
					hitdef.getP2stateno() + "");

		} else if (!hitdef.getAttr().isAttrType(AttrType.T) || hitdef.getP2stateno() == null) {
//			hitdef.setInitialType(sprite.getInfo().getType());
			if (sprite.getInfo().getType() == Type.S) {
				if (hitdef.getGround().getType() == org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub.Type.TRIP) {
					sprite.getSpriteState().changeStateDef(5070);
					
				} else {
					sprite.getSpriteState().changeStateDef(5000);
					
				}
			} else if (sprite.getInfo().getType() == Type.C) {
				if (hitdef.getGround().getType() == org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub.Type.TRIP) {
					sprite.getSpriteState().changeStateDef(5070);
					
				} else {
					sprite.getSpriteState().changeStateDef(5010);					
				}
				
			} else if (sprite.getInfo().getType().getBit() == Type.A.getBit()) {
				if (hitdef.getGround().getType() == org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub.Type.TRIP) {
					sprite.getSpriteState().changeStateDef(5070);
					
				} else {
					sprite.getSpriteState().changeStateDef(5020);					
				}
			}

		}
		if (hitdef.getP1stateno() != null) {
			sprHitter.getSpriteState().changeStateDef(hitdef.getP1stateno());

		}
		if (hitdef.getPalfx().getTime() > 0) {
			sprite.getPalfx().init();
			sprite.setPalfx(hitdef.getPalfx());
		}
		if (hitdef.getP1facing() == -1) {
			sprHitter.getInfo().setFlip(!sprHitter.getInfo().isFlip());
		} 
//		if (hitdef.getP2facing() == 1) {
//			sprite.getInfo().setFlip(sprHitter.getInfo().isFlip());
//		}
//		if (hitdef.getP2facing() == -1) {
//			sprite.getInfo().setFlip(!sprHitter.getInfo().isFlip());
//		}
		
		
		// TODO other facing
	}

	/**
	 * Set Sprite, Sprite state
	 */
	private void setSpriteBlockBySpriteState(HitDefSub hitdef, Sprite sprite) {
		if (hitdef instanceof ProjectileSub)
			throw new IllegalArgumentException("Only Hitdef");
		Sprite sprHitted = sprite;

		hitdef.setLastTimeBlockBySomething(StateMachine.getInstance().getGameState().getGameTime());
		if (sprHitted.getInfo().getType() == Type.C) {
			sprHitted.getSpriteState().changeStateDef(152);

		} else if (sprHitted.getInfo().getType() == Type.S) {
			sprHitted.getSpriteState().changeStateDef(150);
		}

	}

	/**
	 * Set Sprite, Sprite state
	 */
	private void setProjectileBlockBySpriteState(ProjectileSub hitdef,
			Sprite sprite) {
		Sprite sprHitted = sprite;

		if (sprHitted.getInfo().getType() == Type.C) {
			sprHitted.getSpriteState().changeStateDef(152);

		} else if (sprHitted.getInfo().getType() == Type.S) {
			sprHitted.getSpriteState().changeStateDef(150);
		}

	}

	/**
	 * Set Projectile, Sprite
	 * 
	 * @param hitdef
	 * @param sprite
	 */
	private void setProjectileHitSpriteState(ProjectileSub hitdef, Sprite sprite) {
		if (hitdef.getP2stateno() != null) {
			sprite.getSpriteState().targetState(hitdef.getSpriteId(),
					hitdef.getP2stateno() + "");

		} else if (!hitdef.getAttr().isAttrType(AttrType.T)) {
			if (sprite.getInfo().getType() == Type.S)
				sprite.getSpriteState().changeStateDef(5000);
			else if (sprite.getInfo().getType() == Type.C)
				sprite.getSpriteState().changeStateDef(5010);
			else if (sprite.getInfo().getType() == Type.A)
				sprite.getSpriteState().changeStateDef(5020);

		}

	}

	/**
	 * Set Projectile, Projectile
	 * 
	 * @param hitdef
	 * @param sprite
	 */
	private void setProjectileHitProjectileState(ProjectileSub hitdef,
			ProjectileSprite sprite) {
		// TODO
	}

	// ///////////////////////////////////
	// Sound
	// ////////////////////////////////////
	private static void playSoundHit(HitDefSub hitdef, AbstractSprite sprite) {
		if (hitdef.getHitsound().getSnd_grp() < 0)
			return;
		if (hitdef.getHitsound().isPlaySpriteSnd()) {
			AbstractSprite aspr = hitdef.getSpriteHitter();
			Sprite spriteHitter = null;
			if (aspr instanceof ProjectileSprite) {
				ProjectileSprite pspr = (ProjectileSprite) aspr;
				spriteHitter = pspr.getProjectileSub().getSpriteParent();
			} else {
				spriteHitter = (Sprite) aspr;
			}
			try {
				SoundSystem.Sfx.playSnd(spriteHitter.getSpriteSnd().getGroup(
						hitdef.getHitsound().getSnd_grp()).getSound(
						hitdef.getHitsound().getSnd_item()),
						new AtomicBoolean(true));
			} catch (Exception e) {
				System.err.println("Sound Error " 
						+ hitdef.getHitsound().isPlaySpriteSnd() + " "
						+ hitdef.getHitsound().getSnd_grp() + " "
						+ hitdef.getHitsound().getSnd_item());
			}


		} else {
			Snd commonSnd = StateMachine.getInstance().getFightDef().getFiles().getCommon().getSnd();
			try {
				SoundSystem.Sfx.playSnd(commonSnd.getGroup(
						hitdef.getHitsound().getSnd_grp()).getSound(
						hitdef.getHitsound().getSnd_item()), new AtomicBoolean(
						true));
			} catch (Exception e) {
				System.err.println("Sound Error " 
						+ hitdef.getHitsound().isPlaySpriteSnd() + " "
						+ hitdef.getHitsound().getSnd_grp() + " "
						+ hitdef.getHitsound().getSnd_item());
			}

		}
	}

	private static void playSoundGuard(HitDefSub hitdef, AbstractSprite sprite) {
		if (hitdef.getGuardsound().getSnd_grp() < 0)
			return;
		if (hitdef.getHitsound().isPlaySpriteSnd()) {
			Sprite spriteHitter = StateMachine.getInstance().getSpriteInstance(
					hitdef.getSpriteId());
			SoundSystem.Sfx.playSnd(spriteHitter.getSpriteSnd().getGroup(
					hitdef.getGuardsound().getSnd_grp()).getSound(
					hitdef.getGuardsound().getSnd_item()), new AtomicBoolean(
					true));
		} else {
			Snd commonSnd = StateMachine.getInstance().getFightDef().getFiles().getCommon().getSnd();
			
			try {
				SoundSystem.Sfx.playSnd(commonSnd.getGroup(
						hitdef.getGuardsound().getSnd_grp()).getSound(
						hitdef.getGuardsound().getSnd_item()),
						new AtomicBoolean(true));
			} catch (Exception e) {
				System.err.println("Sound Error " 
						+ hitdef.getHitsound().isPlaySpriteSnd() + " "
						+ hitdef.getHitsound().getSnd_grp() + " "
						+ hitdef.getHitsound().getSnd_item());
			}
		}
	}
	private void drawSparkHit(final HitDefSub hitdef, Sprite sprite) {
		drawSparkHit(hitdef, sprite, null);
	}
	private void drawSparkHit(final HitDefSub hitdef, Sprite sprite, Rectangle union) {
		if (hitdef.getAttr().isAttrType(AttrType.T))
			return;
		if (hitdef.getSparkno() == null)// && hitdef.getSparkno().getAction() < 0)
			return;
		AbstractSprite sprToRender = null;
		try {
			sprToRender = SparkRender.SparkRenderFactory.getInstanceOfSparkSpriteFor(hitdef, sprite, union);
			
		} catch (Exception e) {
			return;
			// TODO: handle exception
		}
		
		
		StateMachine.getInstance().addRender(new SparkhitRender(sprToRender));
		StateMachine.getInstance().getOtherSprites().add(sprToRender);

	}

	// /////////////////////////////////////
	// process position
	// ///////////////////////////////////

	private static void processPosition() {
		Sprite one = null;
		Sprite two = null;
		for (Sprite s : StateMachine.getInstance().getSprites()) {
			if (s instanceof SpriteHelper)
				continue;
			if (one == null) {
				one = s;
			} else {
				two = s;
				break;
			}
		}

		boolean isTempFlip = one.getInfo().getXPos() > two.getInfo().getXPos();
		if (!StateMachine.getInstance().getGlobalEvents().isAssertSpecial(one.getSpriteId(), Assertspecial.Flag.noautoturn))
		if (
				one.getInfo().getCtrl() == 1
				&& 
				one.getInfo().getMovetype() == MoveType.I
				&& (one.getInfo().getPhysics() == SpriteCns.Physics.S || one
						.getInfo().getPhysics() == SpriteCns.Physics.C)) {
			if (one.getSprAnimMng().getAction() == 0 && one.getInfo().isFlip() != isTempFlip
					&& (one.getSprAnimMng().getAction() != 5 && one
							.getSprAnimMng().getAction() != 6)) {
				if (one.getInfo().getPhysics() == SpriteCns.Physics.S) {
					if (one.getInfo().getMovetype() == MoveType.I)
						one.getInfo().setFlip(isTempFlip);
					one.getSprAnimMng().setAction(5);
				} else if (one.getInfo().getPhysics() == SpriteCns.Physics.C) {
					if (one.getInfo().getMovetype() == MoveType.I)
						one.getInfo().setFlip(isTempFlip);
					one.getSprAnimMng().setAction(6);
				}

			} else {
				if (isTempFlip != one.getInfo().isFlip())
					one.getInfo().setFlip(isTempFlip);
			}



		}
		if (!StateMachine.getInstance().getGlobalEvents().isAssertSpecial(two.getSpriteId(), Assertspecial.Flag.noautoturn))
		if (
				two.getInfo().getCtrl() == 1
				&& 
				two.getInfo().getMovetype() == MoveType.I
				&& (two.getInfo().getPhysics() == SpriteCns.Physics.S || two
						.getInfo().getPhysics() == SpriteCns.Physics.C)) {
			if (two.getSprAnimMng().getAction() == 0 && two.getInfo().isFlip() != !isTempFlip
					&& (two.getSprAnimMng().getAction() != 5 && two
							.getSprAnimMng().getAction() != 6)) {
				if (two.getInfo().getPhysics() == SpriteCns.Physics.S) {
					two.getSprAnimMng().setAction(5); // 5 stand crounch
				} else if (two.getInfo().getPhysics() == SpriteCns.Physics.C) {
					two.getSprAnimMng().setAction(6); // 6 crounch
				}
			}
			if (two.getInfo().getMovetype() == MoveType.I) {
				if (!isTempFlip != two.getInfo().isFlip())
					two.getInfo().setFlip(!isTempFlip);
			}
		}
	}

	// /////////////////////////////////////////////
	// Others utils Function
	// ////////////////////////////////////////////
	public static float getXDistatnce(AbstractSprite sprOne,
			AbstractSprite sprTwo) {
		return (float) Point.distance(sprOne.getRealXPos(), 0, sprTwo
				.getRealXPos(), 0);

	}

	public static float getDistatnce(AbstractSprite sprOne,
			AbstractSprite sprTwo) {
		return (float) Point.distance(sprOne.getRealXPos(), sprOne.getRealYPos(), sprTwo
				.getRealXPos(), sprTwo.getRealYPos());

	}
	private static void attemptToProtect(HitDefSub hitdef, Sprite sprTwo) {
		int stateTwo = sprTwo.getSpriteState().getCurrentState().getIntId();
		if (stateTwo >= 120 && stateTwo <= 155 && stateTwo == 140)
			return;
		if (sprTwo.getInfo().getMovetype() != MoveType.H) {
//			Wrapper<Rectangle> wrap = new Wrapper<Rectangle>();

			if (isHitdefHitCollisionRec(hitdef, sprTwo) //{
				 || getXDistatnce(hitdef.getSpriteHitter(), sprTwo) <= 160)
				// {//sprTwo.getInfo().getSize().getProj().getAttack().getDist())
				 {

				if (hasAttackRect(hitdef.getSpriteHitter())) {
					if (sprTwo.getInfo().getCommand("\"holdback\"") == 1
							&& (stateTwo < 120 || stateTwo > 155)
							&& sprTwo.getInfo().getMovetype() != MoveType.A) 
//							&& sprTwo.getInfo().getMovetype() != MoveType.H
//							&& sprTwo.getSpriteState().getTimeInState() > 2)
						sprTwo.getSpriteState().changeStateDef(120);
				}
			} else if (!hasAttackRect(hitdef.getSpriteHitter())
					&& (stateTwo >= 120 && stateTwo <= 155 && stateTwo != 140)) {
				sprTwo.getSpriteState().changeStateDef(140);

			}

		}
	}

	public static boolean isBlockState(Sprite spr) {
		return isBlockState(spr.getSpriteState().getCurrentState().getIntId());
	}

	public static boolean isBlockState(int state) {
		return state >= 120 && state <= 155;
	}

	public static boolean isHitdefHitCollisionRec(HitDefSub hitdef,
			AbstractSprite sprite) {
		return isHitdefHitCollisionRec(hitdef, sprite, null);
	}

	public static boolean isHitdefHitCollisionRec(HitDefSub hitdef,
			AbstractSprite sprite, Wrap<Rectangle> outUnion) {
		boolean isTouch = false;
		List<Rectangle> rectangles = hitdef.getSpriteHitter().getCns1();
		for (Rectangle rAttack : rectangles) {
			for (Rectangle rCln2 : sprite.getCns2()) {
				if (rAttack.intersects(rCln2)) {
					if (outUnion != null) {
						outUnion.setValue(rAttack.intersection(rCln2));
					}
					isTouch = true;
					break;
				}
			}
		}
		return isTouch;
	}
	public static boolean isHitdefHitAttackRec(HitDefSub hitdef,
			AbstractSprite sprite) {
		return isHitdefHitAttackRec(hitdef, sprite, null);
	}

	public static boolean isHitdefHitAttackRec(HitDefSub hitdef,
			AbstractSprite sprite, Wrap<Rectangle> outUnion) {
		boolean isTouch = false;
		for (Rectangle rAttack : hitdef.getSpriteHitter().getCns1()) {
			for (Rectangle rCln1 : sprite.getCns1()) {
				if (rAttack.intersects(rCln1)) {
					if (outUnion != null) {
						outUnion.setValue(rAttack.intersection(rCln1));
					}

					isTouch = true;
					break;
				}
			}
		}
		return isTouch;
	}

	public static boolean hasAttackRect(AbstractSprite sprite) {
		if (sprite.getSprAnimMng().getCurrentImageSprite() == null)
			return false;
		org.lee.mugen.parser.air.Rectangle[] r = sprite.getSprAnimMng()
				.getCurrentImageSprite().getAtacksRec();
		return r != null && r.length > 0;
	}

	private static boolean canOneHitsTwo(int hittedType, int hittedGuard,
			Sprite hitted) {

		if (isMatch(HitDefSub.HitFlag.M.bit, hittedType)
				&& (hitted.getInfo().getType() == Type.S || hitted.getInfo()
						.getType() == Type.C)) {
			return canOneHitsTwo(hittedGuard, hitted);
		} else if (isMatch(HitDefSub.HitFlag.H.bit, hittedType)
				&& hitted.getInfo().getType() == Type.S) {
			return canOneHitsTwo(hittedGuard, hitted);
		} else if (isMatch(HitDefSub.HitFlag.L.bit, hittedType)
				&& hitted.getInfo().getType() == Type.C) {
			return canOneHitsTwo(hittedGuard, hitted);
		} else if (isMatch(HitDefSub.HitFlag.A.bit, hittedType)
				&& hitted.getInfo().getType() == Type.A) {
			return canOneHitsTwo(hittedGuard, hitted);
		} else if (isMatch(HitDefSub.HitFlag.F.bit, hittedType)
				&& hitted.getInfo().getType() == Type.A
				&& hitted.getInfo().getMovetype() == MoveType.H) {
			return canOneHitsTwo(hittedGuard, hitted);
		} else if (isMatch(HitDefSub.HitFlag.D.bit, hittedType)
				&& hitted.getInfo().getType() == Type.L) {
			return canOneHitsTwo(hittedGuard, hitted);
		}

		return false;
	}

	private static boolean canOneHitsTwo(int hittedGuard, Sprite hitted) {
		boolean isBlock = isBlockState(hitted.getSpriteState()
				.getCurrentState().getIntId());

		if (isMatch(HitDefSub.GuardFlag.M.bit, hittedGuard)
				&& (hitted.getInfo().getType() == Type.S || hitted.getInfo()
						.getType() == Type.C)) {
			return !isBlock;
		} else if (isMatch(HitDefSub.GuardFlag.H.bit, hittedGuard)
				&& hitted.getInfo().getType() == Type.S) {
			return !isBlock;
		} else if (isMatch(HitDefSub.GuardFlag.L.bit, hittedGuard)
				&& hitted.getInfo().getType() == Type.C) {
			return !isBlock;
		} else if (isMatch(HitDefSub.GuardFlag.A.bit, hittedGuard)
				&& hitted.getInfo().getType() == Type.A) {
			return !isBlock;
		}
		return true;
	}

	private static boolean isMatch(int constBit, int bit) {
		return (constBit & bit) == constBit;
	}

	public void remove(HitDefSub hitdef) {
		hitdefs.remove(hitdef);
	}

	public HitDefSub getFirstHitdefBySpriteHitter(String spriteHitterId) {
		for (HitDefSub hitdef : hitdefs)
			if (hitdef.getSpriteId().equals(spriteHitterId))
				return hitdef;
		return null;
	}

	public LinkedList<HitDefSub> getHitdefBySpriteHitter(String spriteHitterId) {
		LinkedList<HitDefSub> result = new LinkedList<HitDefSub>();
		for (HitDefSub hitdef : hitdefs)
			if (hitdef.getSpriteId().equals(spriteHitterId))
				result.add(hitdef);
		return result;
	}

	public Sprite getTarget(Sprite sprite, int id) {
		String sid = String.valueOf(id);
		if (id >= 0) {
			HitDefSub hit = null;
			for (HitDefSub hitdef: hitdefs) {
				if (hitdef instanceof ProjectileSub) {
					ProjectileSub p = (ProjectileSub) hitdef;
					Sprite s = p.getSpriteHitter().getProjectileSub().getSpriteParent();
					if (s.getSpriteId().equals(sprite.getSpriteId()) && hitdef.getId().equals(sid)) {
						hit = hitdef;
						break;
					}
				} else {
					if (hitdef.getSpriteId().equals(sprite.getSpriteId()) && hitdef.getId().equals(sid)) {
						hit = hitdef;
						break;
					}
					
				}
			}
			if (hit != null) {
				for (Sprite s: StateMachine.getInstance().getSprites()) {
					if (s.getInfo().getLastHitdef() != null && s.getInfo().getLastHitdef().equals(hit))
						return s;
				}
			}
		} else {
			HitDefSub hit = null;
			for (HitDefSub hitdef: hitdefs) {
				if (hitdef instanceof ProjectileSub) {
					ProjectileSub p = (ProjectileSub) hitdef;
					Sprite s = p.getSpriteHitter().getProjectileSub().getSpriteParent();
					if (s.getSpriteId().equals(sprite.getSpriteId())) {
						hit = hitdef;
						break;
					}
				} else {
					if (hitdef.getSpriteId().equals(sprite.getSpriteId())) {
						hit = hitdef;
						break;
					}
					
				}
			}
			if (hit != null) {
				for (Sprite s: StateMachine.getInstance().getSprites()) {
					if (s.getInfo().getLastHitdef() != null && s.getInfo().getLastHitdef().equals(hit))
						return s;
				}
			} else {
				for (Sprite s: StateMachine.getInstance().getEnnmies(sprite)) {
					if (s.getInfo().getLastHitdef() != null && s.getInfo().getLastHitdef().getSpriteHitter().equals(sprite))
						return s;
				}
			}
		}
		for (Sprite s: StateMachine.getInstance().getSprites()) {
			if (!s.equals(sprite) && s.getInfo().getLastHitdef().getSpriteHitter().equals(sprite))
				return s;
		}
		return null;
	}

	public int getTargetsCount(Sprite sprite, int id) {
		String sid = String.valueOf(id);
		int count = 0;
		if (id >= 0) {
			HitDefSub hit = null;
			for (HitDefSub hitdef: hitdefs) {
				if (hitdef instanceof ProjectileSub) {
					ProjectileSub p = (ProjectileSub) hitdef;
					Sprite s = p.getSpriteHitter().getProjectileSub().getSpriteParent();
					if (s.getSpriteId().equals(sprite.getSpriteId()) && hitdef.getId().equals(sid)) {
						hit = hitdef;
						break;
					}
				} else {
					if (hitdef.getSpriteId().equals(sprite.getSpriteId()) && hitdef.getId().equals(sid)) {
						hit = hitdef;
						break;
					}
					
				}
			}
			if (hit != null) {
				for (Sprite s: StateMachine.getInstance().getSprites()) {
					if (s.getInfo().getLastHitdef() != null && s.getInfo().getLastHitdef().equals(hit))
						count++;
				}
			}
		} else {
			HitDefSub hit = null;
			for (HitDefSub hitdef: hitdefs) {
				if (hitdef instanceof ProjectileSub) {
					ProjectileSub p = (ProjectileSub) hitdef;
					Sprite s = p.getSpriteHitter().getProjectileSub().getSpriteParent();
					if (s.getSpriteId().equals(sprite.getSpriteId())) {
						hit = hitdef;
						break;
					}
				} else {
					if (hitdef.getSpriteId().equals(sprite.getSpriteId())) {
						hit = hitdef;
						break;
					}
					
				}
			}
			if (hit != null) {
				for (Sprite s: StateMachine.getInstance().getSprites()) {
					if (s.getInfo().getLastHitdef() != null && s.getInfo().getLastHitdef().equals(hit))
						count++;
				}
			} else {
				for (Sprite s: StateMachine.getInstance().getEnnmies(sprite)) {
					if (s.getInfo().getLastHitdef() != null && s.getInfo().getLastHitdef().getSpriteHitter().equals(sprite))
						count++;
				}
			}
		}
		return count;
	}

	public Sprite getEnemynear(Sprite sprite, Integer id) {
		float dist = 0;
		Sprite enemy = null;
		for (Sprite s: StateMachine.getInstance().getEnnmies(sprite)) {
			if (s instanceof SpriteHelper &&((SpriteHelper)s).getHelperSub().getHelpertype().equals("normal"))
				continue;
			float tempDist = getDistatnce(sprite, s);
			if (enemy == null) {
				dist = tempDist;
				enemy = s;
			}
			if (tempDist < dist) {
				enemy = s;
			}
		}

		return enemy;
	}

	public List<ProjectileSub> getProjectiles() {
		return getProjectiles(null);
	}
	public List<ProjectileSub> getProjectiles(Integer id) {
		List<ProjectileSub> projectiles = new ArrayList<ProjectileSub>();
		if (id != null && id > 0) {
			for (HitDefSub hitdef: hitdefs) {
				if (hitdef instanceof ProjectileSub) {
					ProjectileSub sub = (ProjectileSub) hitdef;
					if (sub.getProjid() == id.intValue()) {
						projectiles.add((ProjectileSub) hitdef);
					}
				}
			}
		} else {
			for (HitDefSub hitdef: hitdefs) {
				if (hitdef instanceof ProjectileSub)
					projectiles.add((ProjectileSub) hitdef);
			}
		}
		for (Sprite s: StateMachine.getInstance().getSprites()) {
			if (s.getClass() == Sprite.class) {
				if (s.getInfo().getLastHitdef() != null && s.getInfo().getLastHitdef() instanceof ProjectileSub) {
					if (!projectiles.contains(s.getInfo().getLastHitdef())) {
						ProjectileSub sub = (ProjectileSub) s.getInfo().getLastHitdef();
						if (id != null && id > 0) {
							if (sub.getProjid() != null && sub.getProjid() == id.intValue()) {
								projectiles.add((ProjectileSub) sub);
							}
						} else {
							projectiles.add((ProjectileSub) sub);
						}
					}
				}
			}
		}
		return projectiles;
	}
	

}
