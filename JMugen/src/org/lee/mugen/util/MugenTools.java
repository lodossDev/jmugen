package org.lee.mugen.util;

public class MugenTools {
	public static boolean isEmpty(Object o) {
		return o == null;
	}
	
	public static boolean isEmpty(Object[] o) {
		return o == null || o.length == 0;
	}
}
