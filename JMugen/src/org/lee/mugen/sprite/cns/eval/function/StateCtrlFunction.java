package org.lee.mugen.sprite.cns.eval.function;

import java.lang.reflect.Method;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.SpriteCns;
import org.lee.mugen.sprite.cns.AbstractCnsFunction;
import org.lee.mugen.util.BeanTools;

public abstract class StateCtrlFunction extends AbstractCnsFunction {
	private boolean isInterrupt = false;
	
	public boolean isInterrupt() {
		return isInterrupt;
	}
	public void setInterrupt(boolean isInterrupt) {
		this.isInterrupt = isInterrupt;
	}
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (String paramName: paramNames) {
			int index = getParamIndex(paramName);
			if (valueableParams[index] != null) {
				stringBuilder.append(paramName + " = ");
				Valueable[] vals = valueableParams[index];
			}
			stringBuilder.append("\n");
			
		}
		return getFunctionName() + " : \n" + stringBuilder.toString();
	}
	public void reset() {}

	
	public StateCtrlFunction(String functionName, String[] paramNames) {
		super(functionName, paramNames);
	}

	@Override
	public Valueable[] parseValue(String name, String value) {
		try {
			
			return parseSpecial(name, value);
		} catch (NoSuchMethodException e) {
			try {
				Method m = this.getClass().getMethod("parse", String.class,
						String.class);
				return (Valueable[]) m.invoke(this, name, value);
			} catch (Exception e1) {
				throw new IllegalArgumentException(
						getClass().getName()
						+ " do not describe a static method parse that return valuuable[] for given values ");
			}
		}
	}
	public Valueable[] parseSpecial(String name, String value) throws NoSuchMethodException {
		String nameCaseCamel = Character.toUpperCase(name.charAt(0))
				+ name.substring(1);
		try {
			Method m = this.getClass().getMethod("parseFor"
					+ nameCaseCamel.replace('.', '$'), String.class,
					String.class);
			return (Valueable[]) m.invoke(this, name, value);
		} catch (NoSuchMethodException e) {
			throw e;
		} 
		catch (Exception e) {
			throw new IllegalArgumentException(
					getClass().getName()
							+ " do not describe a static method parse that return valuuable[] for given values ");
		}
	}
	private static Method getMethod(Class clazz, String name) throws NoSuchMethodException {
		for (Method m: clazz.getMethods()) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		throw new NoSuchMethodException();
	}
	private void executeGenerics(String spriteId, Object bean, String prefixFunction, String name, Valueable... pValueables)
			throws Exception {


		int nameIndex = getParamIndex(name);
		Valueable[] values = valueableParams[nameIndex];

		Object[] objectValues = new Object[values == null? 0: values.length];
		Object[] defaultValues = getDefaultValues(name);
		if ((objectValues == null || objectValues.length == 0) && (defaultValues != null && defaultValues.length > 0)) {
			objectValues = new Object[defaultValues.length];
		}

		for (int i = 0; i < objectValues.length; ++i) {
			if (values != null)
				objectValues[i] = values[i].getValue(spriteId);
			if (objectValues[i] == null) {
				if (defaultValues[i] != null && defaultValues[i] instanceof Valueable) {
					objectValues[i] = ((Valueable)defaultValues[i]).getValue(spriteId);
				} else {
					objectValues[i] = defaultValues[i];
					
				}
			}
		}
		if (objectValues.length == 1) {
			BeanTools.setObject(bean, (prefixFunction == null || prefixFunction.length() == 0? "": prefixFunction + ".") + name, objectValues[0]);
		} else if (objectValues.length > 0) {
			BeanTools.setObject(bean, (prefixFunction == null || prefixFunction.length() == 0? "": prefixFunction + ".") + name, objectValues);
		}
	}
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
		SpriteCns sprInfo = sprite.getInfo();
		return getValue(spriteId, sprInfo, getFunctionName(), params);
	}
	
	
	public Object getValue(String spriteId, Object bean, String prefixFunction, Valueable... params) {
		try {
			for (String paramName : getParamNames()) {
				executeGenerics(spriteId, bean, prefixFunction, paramName);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
//			throw new IllegalStateException("This state musn't be reached !!  " + getClass());
		}
		return null;
	}

	protected Object[] getDefaultValues(String name) {
		return null;
	}

	
	
	
	// routine for fill object
    public void fillBean(String spriteId, Object beanSub) {
		String error = null;
			for (String paramName : getParamNames()) {
				try {
					error = paramName;
					fillBeanChild(spriteId, paramName, beanSub);
				} catch (Exception e) {
					System.err.println(beanSub.getClass().getName() + ">>>>>>>>>" + error);
					e.printStackTrace();
	//				throw new IllegalStateException("This state musn't be reached !!  " + getClass());
				}
			}
			

	}
	protected Object[] getValueFromName(String spriteId, String name) {
		int nameIndex = getParamIndex(name);
		Valueable[] values = valueableParams[nameIndex];

		Object[] objectValues = new Object[values == null ? 0 : values.length];
		Object[] defaultValues = getDefaultValues(name);
		if ((objectValues == null || objectValues.length == 0) && (defaultValues != null && defaultValues.length > 0)) {
			objectValues = new Object[defaultValues.length];
		}
		for (int i = 0; i < objectValues.length; ++i) {
			if (values != null && values[i] != null)
				objectValues[i] = values[i].getValue(spriteId);
			if (objectValues[i] == null) {
				if (defaultValues[i] != null && defaultValues[i] instanceof Valueable) {
					objectValues[i] = ((Valueable)defaultValues[i]).getValue(spriteId);
				} else {
					objectValues[i] = defaultValues[i];
					
				}
			}
		}
		return objectValues;
		
	}
	protected void fillBeanChild(String spriteId, String name, Object bean)
			throws Exception {
		Object[] objectValues = getValueFromName(spriteId, name);
		if (objectValues.length == 1) {
			BeanTools.setObject(bean, name,
					objectValues[0]);
		} else if (objectValues.length > 0) {
			BeanTools.setObject(bean, name,
						objectValues);
		}
	}

}