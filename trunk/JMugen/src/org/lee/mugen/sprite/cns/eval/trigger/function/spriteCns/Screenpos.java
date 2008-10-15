package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import java.util.List;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.background.Stage;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;
import org.lee.mugen.sprite.entity.PointF;

/**
 * 
 * @author Dr Wong
 * @category Trigger : Ok : do Fix Scale
 */
public class Screenpos extends SpriteCnsTriggerFunction {

	public Screenpos() {
		super("screenpos", new String[] {"x", "y"});
	}

	private static PointF getPosRelativeToScreen(Stage stage, float xRealPos, float yRealPos) {

		// TODO SCALE if 
		int _mvX = stage.getCamera().getX();
		int _mvY = stage.getCamera().getY();
		int x = _mvX + stage.getCamera().getWidth()/2;
		int y = stage.getStageinfo().getZoffset() + _mvY;
		
		PointF pos = new PointF(xRealPos + x, yRealPos + y);
		
		return pos;
	
	}
	@Override
	public int parseValue(String[] tokens, int pos, List<Valueable> result) {
		//screenpos x
		//screenpos y
		pos++;
		final Stage stage = StateMachine.getInstance().getInstanceOfStage();
		if ("x".equals(tokens[pos])) {
			Valueable v = new Valueable() {
				public Object getValue(String spriteId, Valueable... params) {
					Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
					return getPosRelativeToScreen(stage, sprite.getRealXPos(), sprite.getRealYPos()).getX();
				}
			};
			result.add(v);
		} else {
			Valueable v = new Valueable() {
				public Object getValue(String spriteId, Valueable... params) {
					Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
					return getPosRelativeToScreen(stage, sprite.getRealXPos(), sprite.getRealYPos()).getY();
				}
			};
			result.add(v);
			
		}
		return pos;
	}
}
