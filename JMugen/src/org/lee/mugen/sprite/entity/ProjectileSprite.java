package org.lee.mugen.sprite.entity;

import java.awt.Point;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.sprite.background.Camera;
import org.lee.mugen.sprite.base.AbstractSprite;
import org.lee.mugen.sprite.baseForParse.ImageSpriteSFF;
import org.lee.mugen.sprite.character.AnimElement;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.SpriteAnimManager;

public class ProjectileSprite extends AbstractSprite {
	
	private ProjectileSub projectileSub;
	private boolean isFlip;
	
	public ProjectileSprite(ProjectileSub projectile) {
		this.projectileSub = projectile;
		StateMachine machine = StateMachine.getInstance();
		Sprite sprHitter = machine.getSpriteInstance(projectile.getSpriteId());
		isFlip = sprHitter.isFlip();
		if (projectile.getVelocity().getX() < 0)
			isFlip = !isFlip;
		
		sprAnimMng = new SpriteAnimManager(sprHitter.getSpriteId()){

			@Override
			public void setAction(int value) {
//				if (value == -1)
//					return;
				super.setAction(value);
			}
			
		};

		sprAnimMng.setGroupSpriteMap(sprHitter.getSprAnimMng().getGroupSpriteMap());
		spriteSFF = sprHitter.getSpriteSFF();
		sprAnimMng.setAction(projectile.getProjanim());
		/*
- p1 : interprète la position relativement à l'axe de P1. Un offset x positif est vers la direction dans laquelle regarde P1. C'est la valeur défaut pour postype.
- p2 : interprète la position relativement à l'axe de P2. Un offset x positif est vers la direction dans laquelle regarde P2.
- front : interprète la position x relativement au bord de l'écran auquel P1 fait face, et la position y relativement à l'axe y de P1. Un offset
x positif s'éloigne du centre de l'écran alors qu'un offset x négatif s'en rapproche.
- back : interprète la position x relativement au bord de l'écran auquel P1 est de dos, et ypos relativement à l'axe y de P1. Un offset x positif se rapproche du centre de l'écran alors qu'un offset x négatif s'en éloigne.
- left : interprète la position x et la position y relativement au coin haut gauche de l'écran. Un offset x positif est vers la droite de l'écran.
- right : interprète les positions x et y relativement au coin haut droit de l'écran. Un offset x positif est vers la droite de l'écran.
		 */
		
		PointF pos = projectile.getPostype().computePos(sprHitter, StateMachine.getInstance().getFirstEnnmy(sprHitter.getSpriteId()), projectile.getOffset(), 0);
		projectile.setX(pos.getX());
		projectile.setY(pos.getY());
		
	}
	@Override
	public float getRealXPos() {
		return projectileSub.getX();
	}

	@Override
	public float getRealYPos() {
		return projectileSub.getY();
	}

	@Override
	public boolean isFlip() {
		// TODO : ERROR Mugen But test with Goku HR for Super Kamehame it is reverse because of Animation flip and the type = projectile that give a reverse velocity ???, To ask for the communoties
		return isFlip;
	}

	boolean firstTimeConnect = false;
	boolean remove = false;
	
	public boolean remove() {
		if (waitCancelAnim) {
			return getSprAnimMng().getAnimTime() == 0;
		} else {
			return remove;
		}		
	}
	@Override
	public PointF getPosToDraw() {
		ProjectileSub explod = getProjectileSub();
		Postype postype = explod.getPostype();
		Sprite parent = StateMachine.getInstance().getRoot(explod.getSpriteParent());
		PointF pos = postype.computePos(parent, 
				StateMachine.getInstance().getFirstEnnmy(parent.getSpriteId()), 
				explod.getOffset(), 
				0);
		if (explod.getPostype() == Postype.left) {
			pos.setLocation(explod.getOffset().getX(), explod.getOffset().getY());
			return pos;
		} else if (explod.getPostype() == Postype.right) {
			pos.setLocation(320 + explod.getOffset().getX(), explod.getOffset().getY());// - (getCurrentImage().getWidth() * explod.getScale().getX()), explod.getPos().y);
			return pos;
		}
		
		return super.getPosToDraw();
	
	}
	boolean isProjHitSprite = false;
	private boolean isHitAnim() {
		return projectileSub.getProjhitanim() != -1 && projectileSub.getProjhitanim() == sprAnimMng.getAction();
	}
	private boolean isHitAnimFinish() {
		return isHitAnim() && sprAnimMng.getAnimTime() == 0;
	}
	private boolean isInHitAnim() {
		return isProjHitSprite && isHitAnim() && sprAnimMng.getAnimTime() != 0;
	}
	private boolean isInCancelAnim() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void process() {
		
		if (!isProjHitSprite && !isHitAnim() 
				&& sprAnimMng.getAnimTime() == 0 
				&& sprAnimMng.getAction() == projectileSub.getProjanim()
				&& projectileSub.getProjremanim() != -1) {
			sprAnimMng.setAction(projectileSub.getProjremanim());
		} else if ((isProjHitSprite && !isHitAnim()) 
				|| (isProjHitSprite && projectileSub.getProjhitanim() == -1))
			sprAnimMng.setAction(projectileSub.getProjhitanim());
		if ((isProjHitSprite && isHitAnimFinish()) || (isProjHitSprite && sprAnimMng.getAction() == -1)) {
			remove = true;
				
			return;
		}
		if (isPause()) {
			pause--;
			return;
		}
		sprAnimMng.process();
		if (waitCancelAnim)
			return;
		if (isInHitAnim() || isInCancelAnim())
			projectileSub.addHisRemoveVelocity(isFlip());
		else
			projectileSub.addHisVelocity(isFlip());
		
		// remove by removetime
		remove = projectileSub.getProjremovetime() == 0;
		if (remove && !waitCancelAnim) {
			if (projectileSub.getProjcancelanim() != -1) {
				getSprAnimMng().setAction(projectileSub.getProjcancelanim());
				waitCancelAnim = true;
			}

		}
		
		// remove by Projedgebound
		Camera cam = StateMachine.getInstance().getInstanceOfStage().getCamera();
		Point pt = new Point((int)getRealXPos() + cam.getX(), (int)getRealYPos() + cam.getY());
		remove = remove || projectileSub.getProjedgebound() + Math.pow(320*320 + 240*240, 0.5)< pt.distance(new Point(cam.getX(), cam.getY()));
		
		// process calculation
		projectileSub.addProjremovetime(-1);
		
		if (remove) {
			StateMachine.getInstance().getFightEngine().remove(projectileSub);
		}
		linearTime++;
	}
	private boolean waitCancelAnim = false;
	public boolean isRemove() {
		if (waitCancelAnim) {
			return getSprAnimMng().getAnimTime() == 0;
		} else {
			return remove;
		}
	}

	public void setProjHitSprite() {
		if (getProjectileSub().getProjhits() > 0) {
//			&& getProjectileSub().getSpriteParent().getSpriteState().getTimeInState() % getProjectileSub().getProjmisstime() == 0
			getProjectileSub().setProjhits(getProjectileSub().getProjhits() - 1);
		} 
		
		if (getProjectileSub().getProjhits() <= 0) {
			isProjHitSprite = true;		
		} 
		
	}
	public ProjectileSub getProjectileSub() {
		return projectileSub;
	}
	public void setProjectileSub(ProjectileSub projectileSub) {
		this.projectileSub = projectileSub;
	}
	
	
}
