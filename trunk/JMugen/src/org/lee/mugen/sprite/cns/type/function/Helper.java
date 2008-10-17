package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.core.renderer.game.CnsRender;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.SpriteCns;
import org.lee.mugen.sprite.character.SpriteHelper;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.entity.HelperSub;
import org.lee.mugen.sprite.entity.PointF;
import org.lee.mugen.sprite.parser.ExpressionFactory;

public class Helper extends StateCtrlFunction {

    // TODO : helper
    public Helper() {
        super("helper", new String[] {
        		"helpertype","name"
        		,"id","pos"
        		,"postype","facing"
        		,"stateno","keyctrl"
        		,"ownpal","supermovetime"
        		,"pausemovetime","size.xscale"
        		,"size.yscale","size.ground.back"
        		,"size.ground.front","size.air.back"
        		,"size.air.front","size.height"
        		,"size.proj.doscale","size.head.pos"
        		,"size.mid.pos","size.shadowoffset"        		
        });
    }

    public void fillBeanSize(String spriteId, SpriteCns beanSub) {
		String error = null;
			for (String paramName : getParamNames()) {
				try {
					error = paramName;
					if (paramName.startsWith("size")) {
						fillBeanChild(spriteId, paramName, beanSub);
					}
				} catch (Exception e) {
					System.err.println(beanSub.getClass().getName() + ">>>>>>>>>" + error);
					e.printStackTrace();
	//				throw new IllegalStateException("This state musn't be reached !!  " + getClass());
				}
			}
	}

    @Override
    public Object getValue(String spriteId, Valueable... params) {
		Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
		Sprite firstEnnemy = StateMachine.getInstance().getFirstEnnmy(spriteId);
		HelperSub helperSub = new HelperSub();
		fillBean(spriteId, helperSub);
		helperSub.setSpriteFrom(sprite);
		

		
		
		final SpriteHelper helperSpr = new SpriteHelper(spriteId + "'s helper " + helperSub.hashCode(), sprite, helperSub);

		fillBeanSize(spriteId, helperSpr.getInfo());
//		System.out.println("Create helper " + helperSub.getId());
		
		
		if (helperSpr.getHelperSub().getFacing() == -1)
			helperSpr.getInfo().setFlip(!helperSpr.getHelperSub().getSpriteFrom().isFlip());
		
		PointF pos = helperSpr.getHelperSub().getPostype().computePos(sprite, firstEnnemy, helperSpr.getHelperSub().getPos(), helperSub.getFacing());
		
		helperSpr.getSprAnimMng().setAction(-1);
		helperSpr.getInfo().setXPos(pos.getX());
		helperSpr.getInfo().setYPos(pos.getY());
		StateMachine.getInstance().addSpriteHelper(helperSpr);
		
		
//		CnsRender cnsRender = new CnsRender(helperSpr) {
//		@Override
//		public boolean remove() {
//			return helperSpr.remove();
//		}
//	};
//	cnsRender.setShowAttackCns(true);
//	StateMachine.getInstance().addRender(cnsRender);
		
		
		helperSpr.getSpriteState().changeStateDef(helperSub.getStateno());

		
		return null;
	}
    @Override
    public Valueable[] parseValue(String name, String value) {
		String[] tokens = ExpressionFactory.expression2Tokens(value);
		Valueable[] vals = ExpressionFactory.evalExpression(tokens);
		return vals;
	}

}
