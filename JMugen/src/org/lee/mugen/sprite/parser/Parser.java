package org.lee.mugen.sprite.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lee.mugen.lang.Wrap;
import org.lee.mugen.lang.Wrapper;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.MathFunction;
import org.lee.mugen.sprite.cns.eval.operator.CnsOperatorsDef;
import org.lee.mugen.util.BeanTools;
import org.lee.mugen.util.Logger;

public class Parser {
	public static void main(String[] args) {
		String[] tokens = ExpressionFactory.expression2Tokens("Hidefattr = 3");
		Wrap<MathFunction> firstOp = new Wrapper<MathFunction>();
		Wrap<Valueable> value1 = new Wrapper<Valueable>();
		Wrap<MathFunction> compareOp = new Wrapper<MathFunction>();
		Wrap<Valueable> value2 = new Wrapper<Valueable>();
		Wrap<String[]> key = new Wrapper<String[]>();
		getValueForSpecialOpAndReturnPos(tokens, 0, key, firstOp, value1, compareOp, value2);
		
		Logger.log(
								key.getValue()[0] 
				               + firstOp.getValue().getOp()
				               + value1.getValue().getValue("") 
				               + (compareOp.getValue() != null ? compareOp.getValue().getOp() : "") 
				               + (value2.getValue() != null? value2.getValue().getValue(""): ""));
		
	}
	private static final File[] SEARCH_DIR_FOR = new File[] {
		new File("."), new File("resource"), new File("resource/data"), new File("data")
	};
	public static String getExistFile(String filename) {
		File result = new File(filename);
		
		if (result.exists()) {
			return result.getAbsolutePath();
		}
		
		for (File base: SEARCH_DIR_FOR) {
			result = new File(base, filename);
			if (result.exists()) {
				return result.getAbsolutePath();
			}
		}
		throw new IllegalArgumentException("File not exist");
	}
	public static String getExistFile(File currentDir, String filename) {
		File result = new File(currentDir, filename);
		
		if (result.exists()) {
			return result.getAbsolutePath();
		}
		
		for (File base: SEARCH_DIR_FOR) {
			result = new File(base, filename);
			if (result.exists()) {
				return result.getAbsolutePath();
			}
		}
		throw new IllegalArgumentException("File not exist");
	}
	public static interface AccessorParser {

		void parse(GroupText grp) throws Exception;
		boolean isParse();
	}
	

	public static void fillBean(Object bean, List<GroupText> grps) throws Exception {
		for (GroupText grp: grps) {
			if (bean instanceof AccessorParser) {
				AccessorParser ap = (AccessorParser) bean;
				ap.parse(grp);
				continue;
			}
			
			
			for (String key: grp.getKeyValues().keySet()) {
				String accessor = grp.getSection() + "." + key;
				Object[] objectValues = null;
				
					Valueable[] values = ExpressionFactory.evalExpression(grp.getKeyValues().get(key));

					objectValues = new Object[values.length];
					for (int i = 0; i < objectValues.length; ++i) {
						objectValues[i] = values[i].getValue(null);
					}
				
					
				
				try {
					if (objectValues.length == 1) {
						BeanTools.setObject(bean, accessor, objectValues[0]);
					} else if (objectValues.length > 1) {
						BeanTools.setObject(bean, accessor, objectValues);
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println(bean.getClass().getName() + " setProperties cause problem ! >" + accessor + " = " + objectValues);
				}
			}
		}
		
	}
	
	
	private static boolean isOpEq(String op) {
		return "=".equals(op) || "<".equals(op) || "<=".equals(op) || ">".equals(op) || ">=".equals(op);
	}
	// Parse for (something()) = valeur1
	// Parse for (something()) = valeur1, [opérateur] valeur2 and return index just a the last value2
	// Parse for (something()) = valeur1 [opérateur] valeur2 and return index just a the last value2
	public static int getValueForSpecialOpAndReturnPos(
			String[] tokens, 
			int pos, 
			Wrap<String[]> key, Wrap<MathFunction> firstOp, Wrap<Valueable> value1, Wrap<MathFunction> compareOp, Wrap<Valueable> value2) {
		int position = pos;
		
		// state 0 = key
		// state 1 = find first op
		// state 2 = find value1
		// state 3 = find the real op
		// state 4 = Find value2

		// state 0 = key
		// state 1 = find first op
		// state 2 = find value1
		// state 3 = find the real op
		// state 4 = Find value2

		int state = 0;
		List<String> tamponKey = new ArrayList<String>();
		List<String> tamponState1 = new ArrayList<String>();
		List<String> tamponState3 = new ArrayList<String>();

//		int toMenos = 0;
		while (position < tokens.length) {
			String token = tokens[position];
			if (!isOpEq(token) && state == 0) {
				tamponKey.add(token);
				position++;
			} else if (isOpEq(token) && state == 0) {
				state = 1;
			} else if (isOpEq(token) && state == 1) {
				MathFunction func = CnsOperatorsDef.getOperator(token);
				firstOp.setValue(func);
				state = 2;
				position++;
			} else if (state == 2) {
				if ("(".equals(token)) {
					String[] subTokens = ExpressionFactory.getCloseInTokens(tokens, position, "(", ")");
					Valueable[] vals = ExpressionFactory.evalExpression(subTokens);
					value1.setValue(vals[0]);
					position += subTokens.length + 2; // "(" and ")"
				} else {
					Valueable[] v = ExpressionFactory.evalExpression(token);
					value1.setValue(v[0]);
					if (position + 1 < tokens.length){
						if (isOpEq(tokens[position + 1]))
							position++;
						else if (",".equals(tokens[position + 1]))
							position++;
					}
					state = 3;
				}
			} else if (state == 3) {
				if (",".equals(token)) {
					position++;
					state = 4;
				} else if (isOpEq(token)) {
					state = 4;
				} else {
					if (state != 4)
						state = 6;
					else
						state = 5;
				}
			} else if (state == 4) {
				if (isOpEq(token)) {
					compareOp.setValue(CnsOperatorsDef.getOperator(token));
					position++;
				} else {
					compareOp.setValue(CnsOperatorsDef.getOperator("="));
//					toMenos++;
				}
				state = 5;
			} else if (state == 5) {
				if ("(".equals(token)) {
					String[] subTokens = ExpressionFactory.getCloseInTokens(tokens, position, "(", ")");
					Valueable[] vals = ExpressionFactory.evalExpression(subTokens);
					value2.setValue(vals[0]);
					position += subTokens.length + 2; // "(" and ")"
				} else {
					Valueable[] v = ExpressionFactory.evalExpression(token);
					value2.setValue(v[0]);
//					position++;
					state = 6;
				}
			}
			if (state == 6)
				break;
		}
		key.setValue(tamponKey.toArray(new String[0]));
//		position = position <= tokens.length - 1? position: position-1;
		return position;// - toMenos - 1;
	}
	
	private static boolean isValidExpression(List<String> tokens) {
		return false;
	}
	public static class GroupText implements Serializable {
		private String section;
		private String sectionRaw;
		private List<String> keysOrdered = new LinkedList<String>();
		private List<String> originalKeysOrdered = new LinkedList<String>();
		public List<String> getOriginalKeysOrdered() {
			return originalKeysOrdered;
		}
		public List<String> getKeysOrdered() {
			return keysOrdered;
		}
		private Map<String, String> keyValues = new HashMap<String, String>();
		private StringBuilder text = new StringBuilder();
		public StringBuilder getText() {
			return text;
		}
		public void appendText(String line) {
			this.text.append(line + "\n");
		}
		public Map<String, String> getKeyValues() {
			return keyValues;
		}
		public void setKeyValues(Map<String, String> keyValues) {
			this.keyValues = keyValues;
		}
		public String getSection() {
			return section;
		}
		public void setSection(String section) {
			this.section = section;
		}
		public String getSectionRaw() {
			return sectionRaw.toLowerCase();
		}
		public void setSectionRaw(String sectionRaw) {
			this.sectionRaw = sectionRaw;
		}

		
	}
	private static final String S_END = "(?:(?:\\s*;.*$)|(?:\\s*$))";
	private static final String S_COMMENT_OR_EMPTY_REGEX = "^ *;.*$|^ *$";
	
	private static final Pattern P_COMMENT_OR_EMPTY_REGEX = Pattern.compile(S_COMMENT_OR_EMPTY_REGEX);
	private static final Pattern P_END = Pattern.compile(S_END);
	private static final Pattern P_SECTION_REGEX = Pattern.compile("^\\s*\\[(.*)\\]" + S_END);

	public static String[] getGroupText(String src) {
		src = src.replaceAll("\\t", " ");
        StringTokenizer strToken = new StringTokenizer(src, "\r\n");
    
        
        ArrayList<String> stringList = new ArrayList<String>();
        String line = "";
        boolean processLine = true;
        while (strToken.hasMoreTokens()) {
            StringBuilder strBuilder = new StringBuilder();

        	if (processLine)
                line = strToken.nextToken();
        	if (P_COMMENT_OR_EMPTY_REGEX.matcher(line).find()) {
            	processLine = true;
            	strBuilder.append(line + "\n");
            	continue;
            }
            if (P_SECTION_REGEX.matcher(line).find()) {
                do {
                    if (!P_COMMENT_OR_EMPTY_REGEX.matcher(line).matches()) {
//                    	line = line.replaceAll(_END, "");
                    	strBuilder.append(line + "\n");
                    }
                    line = strToken.nextToken();
                } while (!P_SECTION_REGEX.matcher(line).find() && strToken.hasMoreTokens());
                if (!strToken.hasMoreTokens())
                    strBuilder.append(line + "\n");
                else
                    processLine = false;
                stringList.add(strBuilder.toString());
            }

        }
        return (String[])stringList.toArray(new String[0]);
    }
	private static String[] getSeparateKeyValue(String line) {
		return getSeparateKeyValue(line, false);
	}
	private static String[] getSeparateKeyValue(String line, boolean caseSensitive) {
		String[] keyValue = new String[2];
		if (P_COMMENT_OR_EMPTY_REGEX.matcher(line).find())
			return null;
		line = caseSensitive? line :line.trim().toLowerCase();
//		line = Parser.toLowerCase(line);
		int indexEnd = line.indexOf(";"); // search comment
		indexEnd = indexEnd == -1 ? line.length() - 1 : indexEnd - 1;
		int indexEqual = line.indexOf("=");
		keyValue[0] = "";
		keyValue[1] = "";
		if (-1 == indexEqual)
			return null;
		keyValue[0] = line.substring(0, indexEqual).trim();
		keyValue[1] = line.substring(indexEqual + 1,
				indexEqual + 1 + indexEnd - indexEqual).trim();
		return keyValue;
	}
	// "^ *\\[.*\\]" + _END
	public static List<GroupText> getGroupTextMap(String src) {
		return getGroupTextMap(src, false);
	}
	public static List<GroupText> getGroupTextMap(String src, boolean caseSensitive) {
		return getGroupTextMap(src, caseSensitive, false);
	}
	public static List<GroupText> getGroupTextMap(String src, boolean caseSensitive, boolean keyCaseSensistive) {
		StringTokenizer strToken = new StringTokenizer(src, "\r\n");
    
        List<GroupText> result = new ArrayList<GroupText>();
        
        String line = "";
        boolean processLine = true;
        Pattern regexIsCommentOrEmpty = P_COMMENT_OR_EMPTY_REGEX;
        while (strToken.hasMoreTokens()) {
            if (processLine)
                line = strToken.nextToken().replaceAll(S_END, "");
            if (regexIsCommentOrEmpty.matcher(line).find()) {
            	processLine = true;
            	continue;
            }
            Matcher matcher = P_SECTION_REGEX.matcher(line);
            if (matcher.find()) {
            	String section = matcher.group(1);
            	GroupText groupText = new GroupText();
            	groupText.setSection(section.toLowerCase());
            	groupText.setSectionRaw(line);
            	line = strToken.nextToken().replaceAll(S_END, "");
            	if (P_SECTION_REGEX.matcher(line).find()) {
            		processLine = false;
            		result.add(groupText);
            		continue;
            	}
            	do {
                    if (!regexIsCommentOrEmpty.matcher(line).matches()) {
                    	line = line.replaceAll(S_END, "");
                    	if (line.indexOf("=") != -1) {
                        	String[] kv = getSeparateKeyValue(line, true);
                        	groupText.getOriginalKeysOrdered().add(kv[0]);
                        	if (!keyCaseSensistive)
                        		kv[0] = kv[0].toLowerCase();
                        	kv[1] = kv[1] == null? "": kv[1];
                        	if (!kv[1].startsWith("\"") || !kv[1].endsWith("\"")) {
                        		if (!caseSensitive)
                        			kv[1] = kv[1].toLowerCase();
                        	} 
//                        	else {
//                        		System.out.println();
//                        	}
                        	
                        	groupText.getKeyValues().put(kv[0], kv[1]);
                        	groupText.getKeysOrdered().add(kv[0]);
                    	}
                    	groupText.appendText(line);
                    }
                    if (strToken.hasMoreTokens())
                    line = strToken.nextToken().replaceAll(S_END, "");
                } while (!P_SECTION_REGEX.matcher(line).find() && strToken.hasMoreTokens());
            	
            	 if (!strToken.hasMoreTokens()) {
                	if (P_SECTION_REGEX.matcher(line).find()) {
                		processLine = false;
                	} else if (!regexIsCommentOrEmpty.matcher(line).matches()) {
                    	line = line.replaceAll(S_END, "");
                    	if (line.indexOf("=") != -1) {
                        	String[] kv = getSeparateKeyValue(line, true);
                        	groupText.getOriginalKeysOrdered().add(kv[0]);
                        	if (!keyCaseSensistive)
                        		kv[0] = kv[0].toLowerCase();
                        	kv[1] = kv[1] == null? "": kv[1];
                        	if (!kv[1].startsWith("\"") || !kv[1].endsWith("\"")) {
                        		if (!caseSensitive)
                        			kv[1] = kv[1].toLowerCase();
                        	}
//                        	else {
//                        		System.out.println();
//                        	}
                        	groupText.getKeyValues().put(kv[0], kv[1]);
                        	groupText.getKeysOrdered().add(kv[0]);
                    	}
                    	groupText.appendText(line);
                    }
                    
                } else {
                	processLine = false;
                }
                result.add(groupText);
            }

        }
        return result;
    }
    
    public static String getText(InputStream in) throws IOException {
    	InputStreamReader isr = new InputStreamReader(in, "utf-8");
    	BufferedReader br = new BufferedReader(isr);
    	String line = null;
    	StringBuilder strBuild = new StringBuilder();
    	while ((line = br.readLine()) != null) {
    		strBuild.append(line.replaceAll("\t", "").replaceAll("\r", "").toLowerCase() + "\n");
//    		strBuild.append(line + "\n");
    	}
    	return strBuild.toString();
    	
    }
	public static float getFloatValue(Object o) {
		try {
			if (o == null)
				return 0;
			if (o instanceof String)
				return new Float(o.toString());
			else if (o instanceof Boolean)
				return ((Boolean)o).booleanValue()? 1: 0;
			return ((Number)o).floatValue();
		} catch (Exception e) {
			throw new IllegalArgumentException("Valueable can be evaluate to String or Float only");
		}
	}

	public static int getIntValue(Object value) {
		try {
			if (value == null)
				return 0;
			return ((Number)value).intValue();
		} catch (Exception e) {
			throw new IllegalArgumentException("Valueable can be evaluate to String or Float only : " + value);
		}
	}

	public static String toLowerCase(String str) {
		StringBuilder buffer = new StringBuilder(str);
		int end = -1;
		int start = 0;
		end = buffer.indexOf("\"");
		boolean isEnter = end != -1;
		while (end != -1) {
			buffer.replace(start, end, buffer.substring(start, end).toLowerCase());
			int tempStart = buffer.indexOf("\"", end + 1);
			if (tempStart == -1) {
				buffer.replace(start, buffer.length(), buffer.substring(start).toLowerCase());
				break;
			}
			start = tempStart + 1;
			end = buffer.indexOf("\"", start + 1);
		}
		if (isEnter) {
			buffer.replace(start, buffer.length(), buffer.substring(start, buffer.length()).toLowerCase());
		}
		return isEnter? buffer.toString(): buffer.toString().toLowerCase() + "";
	}
}
