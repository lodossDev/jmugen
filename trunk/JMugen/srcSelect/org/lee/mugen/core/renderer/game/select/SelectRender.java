package org.lee.mugen.core.renderer.game.select;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Iterator;

import org.lee.mugen.core.GameSelect;
import org.lee.mugen.core.renderer.game.fight.BaseRender;
import org.lee.mugen.core.renderer.game.system.BackgroundRender;
import org.lee.mugen.fight.section.elem.FontType;
import org.lee.mugen.fight.select.Characters;
import org.lee.mugen.fight.system.MugenSystem;
import org.lee.mugen.fight.system.elem.Cell;
import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.ImageContainer;
import org.lee.mugen.renderer.MugenDrawer;
import org.lee.mugen.renderer.RGB;

public class SelectRender extends BaseRender {
	@Override
	public int getPriority() {
		return 100;
	}
	BackgroundRender br;
	GameSelect gs;
	public SelectRender(GameSelect gameSelect) {
		br = new BackgroundRender(MugenSystem.getInstance().getSelectBackground());
		gs = gameSelect;
	}
	
	
	@Override
	public void render() {
		
		MugenDrawer md = GraphicsWrapper.getInstance();
		MugenSystem ms = MugenSystem.getInstance();
		
		RGB rgb = new RGB(1,1,1);
		md.setColor(rgb.getR(), rgb.getG(), rgb.getB(), rgb.getA());
		md.fillRect(0, 0, 640, 480);
		br.render();
		
		
		
		////// Title
		FontType fontType = (FontType) MugenSystem.getInstance().getSelectInfo().getTitle().getType();
		fontType.setText("Arcade");
		render(md, null, MugenSystem.getInstance().getSelectInfo().getTitle());
		
		
		///// Cell
		Cell cell = MugenSystem.getInstance().getSelectInfo().getCell();
		Dimension cellSize = cell.getSize();
		int spacing = cell.getSpacing();
		int row = MugenSystem.getInstance().getSelectInfo().getRows();
		int col = MugenSystem.getInstance().getSelectInfo().getColumns();
		Point pos = MugenSystem.getInstance().getSelectInfo().getPos();

		for (int c = 0; c < col; c++) {
			for (int r = 0; r < row; r++) {
				render(md, new Point(pos.x + c * cellSize.width + c * spacing, pos.y + r * cellSize.height + r * spacing), cell.getBg());
			}
		}
		
		Characters characters = MugenSystem.getInstance().getFiles().getSelect().getCharacters();
		Iterator<String> iterCharacter = characters.getCharactersOrder().iterator();
		String name1 = null;
		String name2 = null;
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {
				Point p = new Point(pos.x + c * cellSize.width + c * spacing, pos.y + r * cellSize.height + r * spacing);
				
				if (iterCharacter.hasNext()) {
					String character = iterCharacter.next();
					render(md, p, characters.getPortrait(character));
					if (gs.getPosition().x == r && gs.getPosition().y == c) {
						name1 = character;
						if (gs.isFireOne()) {
							render(md, p, MugenSystem.getInstance().getSelectInfo().getP1().getCursor().getDone());
						} else {
							render(md, p, MugenSystem.getInstance().getSelectInfo().getP1().getCursor().getActive());
						}
					}
					if (gs.getPosition2().x == r && gs.getPosition2().y == c) {
						name2 = character;
						if (gs.isFireTwo()) {
							render(md, p, MugenSystem.getInstance().getSelectInfo().getP2().getCursor().getDone());
						} else {
							render(md, p, MugenSystem.getInstance().getSelectInfo().getP2().getCursor().getActive());
						}
					}
				}
				
			}
		}
		
		
		
		
		////////////////////
		// Big Portrait
		pos = (Point) MugenSystem.getInstance().getSelectInfo().getP1().getFace().getOffset().clone();
		if (MugenSystem.getInstance().getSelectInfo().getP1().getFace().getFacing() == -1) {
			pos.x -= characters.getBigPortrait(name1).getWidth();
		}
		if (MugenSystem.getInstance().getSelectInfo().getP1().getFace().getType() == null) {
			render(md, pos, characters.getBigPortrait(name1));
		}
		
		pos = (Point) MugenSystem.getInstance().getSelectInfo().getP2().getFace().getOffset().clone();
		if (MugenSystem.getInstance().getSelectInfo().getP2().getFace().getFacing() == -1) {
			pos.x -= characters.getBigPortrait(name2).getWidth();
		}
		if (MugenSystem.getInstance().getSelectInfo().getP2().getFace().getType() == null) {
			render(md, pos, characters.getBigPortrait(name2), MugenSystem.getInstance().getSelectInfo().getP2().getFace().getFacing() == -1, false);
		}
		
		
		
		/// Name
		FontType font = (FontType) MugenSystem.getInstance().getSelectInfo().getP1().getName().getType();
		font.setText(characters.getCharactersMap().get(name1).getInfo().getName());
		render(md, null, MugenSystem.getInstance().getSelectInfo().getP1().getName());
		
		font = (FontType) MugenSystem.getInstance().getSelectInfo().getP2().getName().getType();
		font.setText(characters.getCharactersMap().get(name2).getInfo().getName());
		render(md, null, MugenSystem.getInstance().getSelectInfo().getP2().getName());
	}


	private void render(MugenDrawer md, Point p, ImageContainer ic) {
		render(md, p, ic, false, false);
		
	}
	private void render(MugenDrawer md, Point p, ImageContainer ic, boolean isFlipH, boolean isFlipV) {
		DrawProperties dp = new DrawProperties(p.x, p.y, isFlipH, isFlipV, ic);
		GraphicsWrapper.getInstance().draw(dp);
		
	}
}
