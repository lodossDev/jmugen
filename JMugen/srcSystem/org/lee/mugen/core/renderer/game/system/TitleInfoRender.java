package org.lee.mugen.core.renderer.game.system;

import java.awt.Point;

import org.lee.mugen.core.GameFight;
import org.lee.mugen.core.renderer.game.fight.BaseRender;
import org.lee.mugen.fight.section.elem.FontType;
import org.lee.mugen.fight.system.MugenSystem;
import org.lee.mugen.fight.system.elem.ItemName;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.MugenDrawer;

public class TitleInfoRender extends BaseRender {
	BackgroundRender br;
	@Override
	public int getPriority() {
		return 100;
	}
	public TitleInfoRender() {
		br = new BackgroundRender(MugenSystem.getInstance().getTitleBackground());
	}
	@Override
	public void render() {
		
		MugenDrawer md = GraphicsWrapper.getInstance();
		MugenSystem ms = MugenSystem.getInstance();
		
		Point pos = (Point) ms.getTitleInfo().getMenu().getPos().clone();
			
		ItemName itemName = ms.getTitleInfo().getMenu().getItemname();
		String[] strs = itemName.getList();
		
		br.render();
		int count = MugenSystem.getInstance().getTitleInfo().getMenu().getWindow$visibleitems();
		int base = ms.getTitleInfo().getMenu().getItemname().startIndex();
		for (int i = base; i < strs.length && i < base + count; i++) {
			
			if (i == itemName.getCurrentIndex()) {
				FontType fontType = (FontType) ms.getTitleInfo().getMenu().getItem().getActive().getType();
				fontType.setText(strs[i]);
				render(md, pos, ms.getTitleInfo().getMenu().getItem().getActive());
				pos.x += ms.getTitleInfo().getMenu().getItem().getSpacing().x;
				pos.y += ms.getTitleInfo().getMenu().getItem().getSpacing().y;
				
			} else {
				FontType fontType = (FontType) ms.getTitleInfo().getMenu().getItem().getType();
				fontType.setText(strs[i]);
				render(md, pos, ms.getTitleInfo().getMenu().getItem());
				pos.x += ms.getTitleInfo().getMenu().getItem().getSpacing().x;
				pos.y += ms.getTitleInfo().getMenu().getItem().getSpacing().y;
				
			}
		}
		
	}
	
}
