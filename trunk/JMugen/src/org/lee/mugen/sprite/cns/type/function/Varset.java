package org.lee.mugen.sprite.cns.type.function;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Functionable;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.SpriteState;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.parser.ExpressionFactory;
import org.lee.mugen.sprite.parser.Parser;
import org.lee.mugen.util.Logger;

public class Varset extends StateCtrlFunction {
	public Varset() {
		super("varset", new String[] {"v", "fv", "sv", "sfv", "value"});
	}
	private static final String _SYSVAR_REG = "sysvar *\\(([^\\)]*)\\)";
	private static final String _SYSVAR_F_REG = "sysfvar *\\(([^\\)]*)\\)";

	private static final String _VAR_REG = "var *\\(([^\\)]*)\\)";
	private static final String _VAR_F_REG = "fvar *\\(([^\\)]*)\\)";
	
	protected String REG_IS_VAR_PARAM = (
			_SYSVAR_REG + "|" + _SYSVAR_F_REG + "|" + _VAR_REG + "|" + _VAR_F_REG
	);

	
	@Override
    public boolean containsParam(String param) {
    	return super.containsParam(param) || Pattern.matches(REG_IS_VAR_PARAM, param);
    }
	private List<Functionable> listOfValSet = new ArrayList<Functionable>();
	private List<Functionable> listOfValFSet = new ArrayList<Functionable>();
	
	public void addParam(String name, Valueable[] param) {
		if (Pattern.matches(_SYSVAR_REG, name)) {
			Matcher m = Pattern.compile(_SYSVAR_REG).matcher(name);
			m.find();
			final String value = m.group(1);
			String[] tokens = ExpressionFactory.expression2Tokens(value);
			final Valueable[] valueables = param;//ExpressionFactory.evalExpression(tokens, getSpriteId());
			
			Functionable functionable = new Functionable() {
				public void reset() {}

				public Object getValue(String spriteId, Valueable... params) {
					Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
					final SpriteState spriteState = sprite.getSpriteState();
					
					spriteState.getVars().setSysVar(value, valueables[0]);
					return null;
				}};
			listOfValSet.add(functionable);
			return;
		}
		if (Pattern.matches(_SYSVAR_F_REG, name)) {
			Matcher m = Pattern.compile(_SYSVAR_F_REG).matcher(name);
			m.find();
			final String value = m.group(1);
			String[] tokens = ExpressionFactory.expression2Tokens(value);
			final Valueable[] valueables = param;//ExpressionFactory.evalExpression(tokens, getSpriteId());
			
			Functionable functionable = new Functionable() {
				public void reset() {}

				public Object getValue(String spriteId, Valueable... params) {
					Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
					final SpriteState spriteState = sprite.getSpriteState();
					
					spriteState.getVars().setSysFVar(value, valueables[0]);
					return null;
				}};
			listOfValFSet.add(functionable);
			return;
		}
		if (Pattern.matches(_SYSVAR_F_REG, name)) {
			Matcher m = Pattern.compile(_SYSVAR_F_REG).matcher(name);
			m.find();
			final String value = m.group(1);
			String[] tokens = ExpressionFactory.expression2Tokens(value);
			final Valueable[] valueables = param;//ExpressionFactory.evalExpression(tokens, getSpriteId());
			
			Functionable functionable = new Functionable() {
				public void reset() {}

				public Object getValue(String spriteId, Valueable... params) {
					Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
					final SpriteState spriteState = sprite.getSpriteState();
					
					spriteState.getVars().setSysFVar(value, valueables[0]);
					return null;
				}};
			listOfValFSet.add(functionable);
			return;
		}
		if (Pattern.matches(_VAR_REG, name)) {
			Matcher m = Pattern.compile(_VAR_REG).matcher(name);
			m.find();
			final String value = m.group(1);
			String[] tokens = ExpressionFactory.expression2Tokens(value);
			final Valueable[] valueables = param;//ExpressionFactory.evalExpression(tokens, getSpriteId());
			
			Functionable functionable = new Functionable() {
				public void reset() {}

				public Object getValue(String spriteId, Valueable... params) {
					Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
					final SpriteState spriteState = sprite.getSpriteState();
					
					spriteState.getVars().setVar(value, valueables[0]);
					return null;
				}};
			listOfValFSet.add(functionable);
			return;
		}
		if (Pattern.matches(_VAR_F_REG, name)) {
			Matcher m = Pattern.compile(_VAR_F_REG).matcher(name);
			m.find();
			final String value = m.group(1);
			String[] tokens = ExpressionFactory.expression2Tokens(value);
			final Valueable[] valueables = param;//ExpressionFactory.evalExpression(tokens, getSpriteId());
			
			Functionable functionable = new Functionable() {
				public void reset() {}

				public Object getValue(String spriteId, Valueable... params) {
					Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
					final SpriteState spriteState = sprite.getSpriteState();
					
					spriteState.getVars().setFVar(value, valueables[0]);
					return null;
				}};
			listOfValFSet.add(functionable);
			return;
		}
		int index = getParamIndex(name);
		if (index == -1) {
			Logger.log("This line can't be compile in Varset >> " + name);
			return;
		}
			
		valueableParams[index] = param;

	}
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		boolean exec = false;
		for (Functionable f: listOfValSet) {
			f.getValue(spriteId);
			exec = true;
		}
		for (Functionable f: listOfValFSet) {
			f.getValue(spriteId);
			exec = true;
		}
		
		if (exec)
			return null;
		
		int vIndex = getParamIndex("v");
		int vfIndex = getParamIndex("fv");
		int svIndex = getParamIndex("sv");
		int svfIndex = getParamIndex("sfv");
		int valueIndex = getParamIndex("value");
		

		
		Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
		
		SpriteState spriteState = sprite.getSpriteState();

		Valueable v = valueableParams[vIndex] == null? null: valueableParams[vIndex][0];
		Valueable fv = valueableParams[vfIndex] == null ? null: valueableParams[vfIndex][0];
		Valueable sv = valueableParams[svIndex] == null? null: valueableParams[svIndex][0];
		Valueable sfv = valueableParams[svfIndex] == null ? null: valueableParams[svfIndex][0];
		Valueable value = valueableParams[valueIndex] == null? null: valueableParams[valueIndex][0];

		if (v != null) {
			final int intValue = Parser.getIntValue(value.getValue(spriteId));
			int intV = Parser.getIntValue(v.getValue(spriteId));
			spriteState.getVars().setVar(intV + "", new Valueable() {

				public Object getValue(String spriteId, Valueable... params) {
					return intValue;
				}});
		} else if (fv != null) {
			final float floatValue = Parser.getFloatValue(value.getValue(spriteId));
			int intV = Parser.getIntValue(fv.getValue(spriteId));
			spriteState.getVars().setFVar(intV + "", new Valueable() {

				public Object getValue(String spriteId, Valueable... params) {
					return floatValue;
				}});
			
		} else if (sv != null) {
			final int intValue = Parser.getIntValue(value.getValue(spriteId));
			int intV = Parser.getIntValue(sv.getValue(spriteId));
			spriteState.getVars().setSysVar(intV + "", new Valueable() {

				public Object getValue(String spriteId, Valueable... params) {
					return intValue;
				}});
		} else if (sfv != null) {
			final float floatValue = Parser.getFloatValue(value.getValue(spriteId));
			int intV = Parser.getIntValue(sfv.getValue(spriteId));
			spriteState.getVars().setSysFVar(intV + "", new Valueable() {

				public Object getValue(String spriteId, Valueable... params) {
					return floatValue;
				}});
			
		} else {
			throw new IllegalStateException("This state musn't be reached : Varset none of v  or vf");
		}
		
		return null;
	}
	
//	public AbstractCnsFunction copy(String spriteId) {
//		Varset scf;
//		try {
//			scf = this.getClass().getConstructor().newInstance();
//			scf.setSpriteId(spriteId);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new IllegalStateException("Normalement il n'y pas de probleme a cette reflexion");
//		}
//		return scf;
//	}

}
