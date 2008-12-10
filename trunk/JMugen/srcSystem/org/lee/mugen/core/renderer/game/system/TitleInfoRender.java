package org.lee.mugen.core.renderer.game.system;

import java.awt.Point;

import org.lee.mugen.core.GameFight;
import org.lee.mugen.core.GameMenu;
import org.lee.mugen.core.renderer.game.fight.BaseRender;
import org.lee.mugen.fight.section.elem.FontType;
import org.lee.mugen.fight.system.MugenSystem;
import org.lee.mugen.fight.system.elem.ItemName;
import org.lee.mugen.object.Rectangle;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.MugenDrawer;

public class TitleInfoRender extends BaseRender {
	BackgroundRender br;
	GameMenu gameMenu;
	@Override
	public int getPriority() {
		return 100;
	}
	public TitleInfoRender(GameMenu gameMenu) {
		br = new BackgroundRender(MugenSystem.getInstance().getTitleBackground());
		this.gameMenu = gameMenu;
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
		int base = gameMenu.getLastStartIndex();//ms.getTitleInfo().getMenu().getItemname().startIndex();
		int add = gameMenu.getaddPixel();
		Rectangle box = (Rectangle) MugenSystem.getInstance().getTitleInfo().getMenu().getBoxcursor$coords().clone();
		GraphicsWrapper.getInstance().setClip(
				new Rectangle(
						pos.x + box.getX1(), 
						pos.y + box.getY1(), 
						pos.x + box.getX2(), 
						pos.y + (-box.getY1() + box.getY2()) * count));
		pos.y += add;
		pos = new Point();
		pos.y += MugenSystem.getInstance().getTitleInfo().getMenu().getWindow$margins$y().x;
		pos.x = -box.getX1();
		pos.y += add;
		
		for (int i = base; i < strs.length; i++) {
			
			if (i == itemName.getCurrentIndex()) {
				if (MugenSystem.getInstance().getTitleInfo().getMenu().getBoxcursor$visible() != 0) {
					float coef = (float) Math.abs(Math.sin(gameMenu.getTime()/8f));
					md.setColor(0.5f, 0.5f, 0.5f, coef - 0.2f);
					md.fillRect(pos.x + box.getX1(), pos.y + box.getY1(), 
							-box.getX1() + box.getX2(), -box.getY1() + box.getY2());
					md.setColor(1f, 1f, 1f, 1f);
				}
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
//		GraphicsWrapper.getInstance().setClip(null);
		
//		md.setClip(new Rectangle(
//				159,
//				158,
//				200,
//				200
//		));
		md.setClip(null);
	}
	
}
