package org.lee.mugen.fight.select;

import java.io.FileInputStream;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.lee.mugen.fight.section.Section;
import org.lee.mugen.sprite.parser.Parser;
import org.lee.mugen.sprite.parser.Parser.GroupText;

public class Select {
	private Characters characters; 
	private ExtraStages extraStages;
	private Options options;
	
	private String filename;
	public Select(String filename) throws Exception {
		this.filename = filename;
		parse();
	}
	
	private void parse() throws Exception {
		String src = IOUtils.toString(new FileInputStream(filename), "utf-8");
		List<GroupText> groups = Parser.getGroupTextMap(src);

		for (GroupText grp: groups) {
			if (grp.getSection().equals("characters")) {
				characters = new Characters();
				parse(characters, grp);
			} else if (grp.getSection().equals("extrastages")) {
				extraStages = new ExtraStages();
				parse(extraStages, grp);				
			} else if (grp.getSection().equals("options")) {
				options = new Options();
				parse(options, grp);
			} 
			
		}
	}

	private void parse(Section section, GroupText grp) throws Exception {
		StringTokenizer token = new StringTokenizer(grp.getText().toString(), "\r\n");
		for (String line = token.nextToken(); token.hasMoreTokens();) {
			section.parse(this, line, null);
		}
	}
}
