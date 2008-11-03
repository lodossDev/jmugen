package org.lee.mugen.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class MugenTools {
	public static boolean isEmpty(Object o) {
		return o == null;
	}
	
	public static boolean isEmpty(Object[] o) {
		return o == null || o.length == 0;
	}
	
	public static String toString(Object[] objects) {
		if (objects == null)
			return "";
		StringBuilder b = new StringBuilder();
		for (Object o : objects)
			b.append(o);
		return b.toString();
	}
//	
//	public static Object clone(Object object) throws Exception {
//		Object clone = object.getClass().getMethod("clone");
//		BeanInfo bi = Introspector.getBeanInfo(object.getClass());
//		PropertyDescriptor[] pds = bi.getPropertyDescriptors();
//		for (PropertyDescriptor pd: pds) {
//			if (pd.getName().equals("class"))
//				continue;
//			Object value = pd.getReadMethod().invoke(object);
//			Object valueClone = value.getClass().getMethod("clone").invoke(value);
//			pd.getWriteMethod().invoke(clone, valueClone);
//		}
//		
//		return clone;
//		
//		
//	}
}
