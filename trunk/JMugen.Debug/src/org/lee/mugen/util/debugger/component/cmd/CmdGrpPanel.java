package org.lee.mugen.util.debugger.component.cmd;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.lee.mugen.core.JMugenConstant;
import org.lee.mugen.sprite.character.SpriteDef;
import org.lee.mugen.sprite.parser.Parser.GroupText;


public class CmdGrpPanel {
	private List<String> cmdOrderedNames;
	static class CmdNameGrpMap {
		public CmdNameGrpMap(String category) {
			this.category = category;
		}
		String category;
		List<String> names = new ArrayList<String>();
		Map<String, GroupText> map = new HashMap<String, GroupText>();
		void add(GroupText grp) {
			names.add(grp.getSection());
			map.put(grp.getSection(), grp);
		}
		@Override
		public String toString() {
			return category;
		}
	}

	static class CmdData {
		List<CmdNameGrpMap> categories = new ArrayList<CmdNameGrpMap>();
		void add(CmdNameGrpMap cng) {
			categories.add(cng);
		}
		
	}

	public static final String S_END = "(?:(?:\\s*;.*$)|(?:\\s*$))";
	public static final String S_COMMENT_OR_EMPTY_REGEX = "^\\s*;.*$|^\\s*$";
	
	public static final Pattern P_COMMENT_OR_EMPTY_REGEX = Pattern.compile(S_COMMENT_OR_EMPTY_REGEX);
	public static final Pattern P_END = Pattern.compile(S_END);
	public static final Pattern P_SECTION_REGEX = Pattern.compile("^\\s*\\[(.*)\\]" + S_END);

	// ;-| Super Motions |--------------------------------------------------------
	private static final Pattern grpPattern = Pattern.compile("^\\s*;-\\|\\s*(.*)\\s*\\|-+.*");

	public static void main(String[] args) throws Exception {
//		String test = ";-| Super Motions |--------------------------------------------------------";
//		Matcher m = grpPattern.matcher(test);
//		m.find();
//		System.out.println(m.group(1));
		String name = "A.B.A";
		String sFile = JMugenConstant.RESOURCE + "chars/" + name + "/" + name + ".def";
		SpriteDef spriteDef = SpriteDef.parseSpriteDef(sFile);
		new CmdGrpPanel(spriteDef);

	}
	
	private CmdData cmdData;
	
	public CmdGrpPanel(SpriteDef spriteDef) throws Exception {
		File cmdFile = new File(spriteDef.getFiles().getCmd());
		List<InputStream> ins = new ArrayList<InputStream>();
		ins.add(new FileInputStream(new File(spriteDef.getParentPath(), cmdFile.getName())));
		ins.add(new ByteArrayInputStream(";-| Mandatory State for basic Move |-\n".getBytes()));
		ins.add(new FileInputStream(JMugenConstant.RESOURCE + "data/common.cmd"));
		Enumeration<InputStream> eIns = Collections.enumeration(ins);
		SequenceInputStream in = new SequenceInputStream(eIns);
		
		BufferedReader r = new BufferedReader(new InputStreamReader(in, "utf-8"));
//		String src = IOUtils.toString(r);
		cmdData = getCmdData(r, true, false);
		System.out.println(cmdData);
	}
	public CmdData getCmdData(BufferedReader r, boolean caseSensitive, boolean keyCaseSensistive) throws IOException {
	    CmdData result = new CmdData();
		CmdNameGrpMap cng = new CmdNameGrpMap("not category");
		result.add(cng);
		
		String line;
		GroupText grp = null;
		while ((line = r.readLine()) != null) {
			line = line.replaceAll(S_END, "");
			Matcher m = grpPattern.matcher(line);
			if (m.find()) {
				String category = m.group(1);
				cng = new CmdNameGrpMap(category);
				result.add(cng);
			}
			if (P_COMMENT_OR_EMPTY_REGEX.matcher(line).find()) {
				continue;
			}
			m = P_SECTION_REGEX.matcher(line);
			if (m.find()) {
				if (!caseSensitive)
					line = line.toLowerCase();
				grp = new GroupText();
				grp.setSection(m.group(1));
				grp.setSectionRaw(line);
				cng.add(grp);
			} else {
				String[] keyValue = getSeparateKeyValue(line, true);
				grp.appendText(line);
				grp.getOriginalKeysOrdered().add(keyValue[0]);
				keyValue[0] = keyCaseSensistive? keyValue[0]: 
					caseSensitive? keyValue[0]: keyValue[0].toLowerCase();
				keyValue[1] = caseSensitive? keyValue[1]: keyValue[1].toLowerCase();
				grp.getKeysOrdered().add(keyValue[0]);
				grp.getKeyValues().put(keyValue[0], keyValue[1]);
				
			}
		}
        return result;
    }
	private static String[] getSeparateKeyValue(String line, boolean caseSensitive) {
		String[] keyValue = new String[2];
		if (P_COMMENT_OR_EMPTY_REGEX.matcher(line).find())
			return null;
		line = caseSensitive? line :line.trim().toLowerCase();
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
	
}
