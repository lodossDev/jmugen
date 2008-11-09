package org.lee.mugen.core.renderer.game.system;

import java.awt.Point;

import org.lee.mugen.core.renderer.game.fight.BaseRender;
import org.lee.mugen.fight.section.elem.FontType;
import org.lee.mugen.fight.system.MugenSystem;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.MugenDrawer;

public class TitleInfoRender extends BaseRender {
	BackgroundRender br = new BackgroundRender(MugenSystem.getInstance().getTitleBackground());
	@Override
	public int getPriority() {
		return 100;
	}
	@Override
	public void render() {
		MugenDrawer md = GraphicsWrapper.getInstance();
		MugenSystem ms = MugenSystem.getInstance();
		
		FontType fontType = (FontType) ms.getTitleInfo().getMenu().getItem().getType();
		Point pos = (Point) ms.getTitleInfo().getMenu().getPos().clone();
			
		String[] strs = {
				ms.getTitleInfo().getMenu().getItemname().getArcade(),
				ms.getTitleInfo().getMenu().getItemname().getVersus(),
				ms.getTitleInfo().getMenu().getItemname().getTeamarcade(),
				ms.getTitleInfo().getMenu().getItemname().getTeamversus(),
				ms.getTitleInfo().getMenu().getItemname().getTeamcoop(),
				ms.getTitleInfo().getMenu().getItemname().getSurvival(),
				ms.getTitleInfo().getMenu().getItemname().getSurvivalcoop(),
				ms.getTitleInfo().getMenu().getItemname().getTraining(),
				ms.getTitleInfo().getMenu().getItemname().getWatch(),
				ms.getTitleInfo().getMenu().getItemname().getOptions(),
				ms.getTitleInfo().getMenu().getItemname().getExit()
		};
		br.render();
//		MugenSystem.getInstance().getTitleInfo().getMenu().getWindow$visibleitems()
		index = 0;
		for (int i = index; i < strs.length && i < index + 5; i++) {
			fontType.setText(strs[i]);
			render(md, pos, ms.getTitleInfo().getMenu().getItem());
			pos.x += ms.getTitleInfo().getMenu().getItem().getSpacing().x;
			pos.y += ms.getTitleInfo().getMenu().getItem().getSpacing().y;
		}
		
	}
	int index = 0;
	
}
