package org.lee.mugen.util;

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
}
