package org.lee.mugen.sprite.entity;

import java.awt.Point;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.sprite.background.Stage;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Backedgebodydist;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Frontedgebodydist;

public enum Postype {
	p1, p2, front, back, left, right;

	public PointF computePos(Sprite p1, Sprite p2, Point offset, int facing) {
		return computePos(p1, p2, new PointF(offset), facing);
	} 
	
	public PointF computePos(Sprite p1, Sprite p2, PointF offset, int facing) {
		Postype postype = this;
		offset = new PointF(offset);
 
		PointF pos = new PointF();
		Stage stage = StateMachine.getInstance().getInstanceOfStage();
		switch (postype) {
		case back:
//			pos.setX(p1.getInfo().getXPos() + 
//					(p1.isFlip()? -Backedgebodydist.compute(p1) + offset.getX(): 
//			Backedgebodydist.compute(p1) - offset.getX()));
//			
//			pos.setY(offset.getY() - stage.getStageinfo().getZoffset());
			int _mvX = stage.getCamera().getX();
			float x = -_mvX + stage.getCamera().getWidth()/2;
			if (!p1.isFlip()) {
				pos.setX(p1.getInfo().getXPos() + 
						(-Backedgebodydist.compute(p1) + offset.getX()));
				pos.setY(offset.getY() - stage.getStageinfo().getZoffset());
						
//				pos.setX(offset.getX() - stage.getCamera().getX() - stage.getCamera().getWidth()/2);
//				pos.setY(-offset.getY() - stage.getStageinfo().getZoffset());
			} else {
				pos.setX(p1.getInfo().getXPos() + 
				(Backedgebodydist.compute(p1) - offset.getX()));
				pos.setY(offset.getY() - stage.getStageinfo().getZoffset());
			}

			break;
		case front:
			pos.setX(p1.getInfo().getXPos() + 
					(p1.isFlip()? -Frontedgebodydist.compute(p1):Frontedgebodydist.compute(p1)) - 
					(p1.isFlip()? -offset.getX() : offset.getX()));
			pos.setY(offset.getY() - stage.getStageinfo().getZoffset());
			break;
		case left:
			pos.setX(offset.getX());
			pos.setY(offset.getY());
			break;
		case right:
			pos.setX(stage.getCamera().getWidth()/2 - facing * offset.getX());
			pos.setY(offset.getY());
			break;
		case p1:
			pos.setX(p1.getInfo().getXPos()
					+ (p1.isFlip()? -offset.getX() : offset.getX()));
			pos.setY(p1.getInfo().getYPos() + offset.getY());
			break;
		case p2:
			pos.setX(p2.getInfo().getXPos()
				+ (p2.isFlip() ? offset.getX() : -offset.getX()));
			pos.setY(p2.getInfo().getYPos() + offset.getY());
			break;

		}
		return pos;
	}

}
