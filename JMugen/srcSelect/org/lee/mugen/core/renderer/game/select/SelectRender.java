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
import org.lee.mugen.fight.system.elem.StageDisplay;
import org.lee.mugen.object.Rectangle;
import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.ImageContainer;
import org.lee.mugen.renderer.MugenDrawer;
import org.lee.mugen.renderer.RGB;
import org.lee.mugen.sprite.entity.PointF;

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
					}
					if (gs.getPosition2().x == r && gs.getPosition2().y == c) {
						name2 = character;
					}
				}
				long time = System.currentTimeMillis();
				long mod = time % 2;
				if (gs.getPosition().equals(gs.getPosition2()) && gs.getPosition().x == r && gs.getPosition().y == c) {
					if (gs.getPosition().x == r && gs.getPosition().y == c) {
						if (gs.isFireOne()&& !gs.isFireTwo()) {
							render(md, p, MugenSystem.getInstance().getSelectInfo().getP2().getCursor().getActive());
						} else if (mod == 1 && !gs.isFireTwo()) {
							render(md, p, MugenSystem.getInstance().getSelectInfo().getP1().getCursor().getActive());
						}
					}
					if (gs.getPosition2().x == r && gs.getPosition2().y == c) {
						if (gs.isFireTwo()&& !gs.isFireOne()) {
							render(md, p, MugenSystem.getInstance().getSelectInfo().getP1().getCursor().getActive());
						} else if (mod == 0 && !gs.isFireOne()){
							render(md, p, MugenSystem.getInstance().getSelectInfo().getP2().getCursor().getActive());
						}
					}

				} else {
					if (gs.getPosition().x == r && gs.getPosition().y == c) {
						if (gs.isFireOne()) {
							render(md, p, MugenSystem.getInstance().getSelectInfo().getP1().getCursor().getDone());
						} else {
							render(md, p, MugenSystem.getInstance().getSelectInfo().getP1().getCursor().getActive());
						}
					}
					if (gs.getPosition2().x == r && gs.getPosition2().y == c) {
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
		if (characters.getBigPortrait(name1) != null) {
			pos = (Point) MugenSystem.getInstance().getSelectInfo().getP1().getFace().getOffset().clone();
			boolean facing = false;
			facing = MugenSystem.getInstance().getSelectInfo().getP1().getFace().getFacing() == -1;
			PointF scale = MugenSystem.getInstance().getSelectInfo().getP1().getFace().getScale();
			if (facing) {
				pos.x -= characters.getBigPortrait(name1).getWidth() * scale.getX();
			}
			if (MugenSystem.getInstance().getSelectInfo().getP1().getFace().getType() == null) {
				render(md, pos, characters.getBigPortrait(name1), facing, false, scale);
			}
			
		}
		
		if (characters.getBigPortrait(name2) != null) {
			boolean facing = MugenSystem.getInstance().getSelectInfo().getP2().getFace().getFacing() == -1;
			PointF scale = MugenSystem.getInstance().getSelectInfo().getP2().getFace().getScale();
			pos = (Point) MugenSystem.getInstance().getSelectInfo().getP2().getFace().getOffset().clone();
			if (facing) {
				pos.x -= characters.getBigPortrait(name2).getWidth() * scale.getX();
			}
			if (MugenSystem.getInstance().getSelectInfo().getP2().getFace().getType() == null) {
				render(md, pos, characters.getBigPortrait(name2), facing, false, scale);
			}
			
		}
		
		
		
		/// Name
		if (name1 != null) {
			FontType font = (FontType) MugenSystem.getInstance().getSelectInfo().getP1().getName().getType();
			font.setText(characters.getSpriteName(name1) == null? name1: characters.getSpriteName(name1));
			render(md, null, MugenSystem.getInstance().getSelectInfo().getP1().getName());
			
		}
		if (name2 != null) {
			FontType font = (FontType) MugenSystem.getInstance().getSelectInfo().getP2().getName().getType();
			font.setText(characters.getSpriteName(name2) == null? name2: characters.getSpriteName(name2));
			render(md, null, MugenSystem.getInstance().getSelectInfo().getP2().getName());
			
		}
		
		
		////////////
		// Stage
		StageDisplay stageDisplay = MugenSystem.getInstance().getSelectInfo().getStagedisplay();
		if (stageDisplay != null && gs.getStageBackgroundRender() != null && stageDisplay != null && stageDisplay.isEnable()) {
			
			
			int width = stageDisplay.getRectangle().width;
			int height = stageDisplay.getRectangle().height;

			float xCoef = stageDisplay.getScale().getX();
			float yCoef = stageDisplay.getScale().getY();
			int x = stageDisplay.getPos().x;
			int y = stageDisplay.getPos().y;
			Rectangle r = new Rectangle(x, y, x + (int) (width * xCoef), y + (int) (height * yCoef));
			GraphicsWrapper.getInstance().setClip(r);
			GraphicsWrapper.getInstance().scale(xCoef, yCoef);
			gs.getStageBackgroundRender().render();
			GraphicsWrapper.getInstance().scale(1f/xCoef, 1f/yCoef);
			GraphicsWrapper.getInstance().setClip(null);

			
		}
		
		//////////////
		// Stage text
		
		if (gs.getStage() != null && !gs.isFireStage()) {
			FontType font = (FontType) MugenSystem.getInstance().getSelectInfo().getStage().getActive().getType();
			font.setText(gs.getStage().getInfo().getName());
			render(md, 
					MugenSystem.getInstance().getSelectInfo().getStage().getPos(), 
					MugenSystem.getInstance().getSelectInfo().getStage().getActive());
			
		} else {
			FontType font = (FontType) MugenSystem.getInstance().getSelectInfo().getStage().getDone().getType();
			font.setText(gs.getStage().getInfo().getName());
			render(md, 
					MugenSystem.getInstance().getSelectInfo().getStage().getPos(), 
					MugenSystem.getInstance().getSelectInfo().getStage().getDone());
		}

	}

	

	private void render(MugenDrawer md, Point p, ImageContainer ic) {
		render(md, p, ic, false, false, new PointF(1,1));
		
	}
	private void render(MugenDrawer md, Point p, ImageContainer ic, boolean isFlipH, boolean isFlipV, PointF scale) {
		DrawProperties dp = new DrawProperties(p.x, p.y, isFlipH, isFlipV, ic);
		dp.setXScaleFactor(scale.getX());
		dp.setYScaleFactor(scale.getY());
		
		GraphicsWrapper.getInstance().draw(dp);
		
	}
}
