package org.lee.mugen.core.renderer.game;

import org.lee.mugen.background.BG;
import org.lee.mugen.core.GameFight;
import org.lee.mugen.parser.air.AirData;
import org.lee.mugen.parser.air.AirData.TypeBlit;
import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.ImageContainer;
import org.lee.mugen.renderer.PalFxSub;
import org.lee.mugen.renderer.RGB;
import org.lee.mugen.renderer.Trans;
import org.lee.mugen.sprite.base.AbstractAnimManager;
import org.lee.mugen.sprite.baseForParse.ImageSpriteSFF;
import org.lee.mugen.sprite.baseForParse.SpriteSFF;
import org.lee.mugen.sprite.entity.SuperpauseSub;
import org.lee.mugen.stage.Stage;

public class StageBackgroundRender implements IBackgroundRenderer {


	public StageBackgroundRender() {
		

	}

	private void drawTileY(ImageContainer img, BG bg, float x,
			float y, float moveX, float moveY, int xStartForAll, Trans trans, boolean isHFlip, boolean isVFlip) {
		Stage stage = GameFight.getInstance().getInstanceOfStage();
		int yTile = (int) bg.getTile().getY();
		float startPosY = y + moveY;
		if (yTile == 0) {
//			drawImage(trans, img, (startPosY + (bg.getTilespacing().getY())
//					* bg.getDelta().getX())
//					+ xStartForAll, (y + moveY * bg.getDelta().getY()), isHFlip, isVFlip);
		
		} else if (yTile == 1) {
			
			startPosY = (y + moveY) % img.getHeight();
			while (startPosY < 240) {
				drawImage(trans, img, (startPosY + (bg.getTilespacing().getY())
						* bg.getDelta().getX())
						+ xStartForAll, (y + moveY * bg.getDelta().getY()), isHFlip, isVFlip);
				startPosY += img.getHeight();
			}
			
			startPosY = (y + moveY) % img.getHeight();
			while (startPosY + img.getHeight() > 0) {
				drawImage(trans, img, (startPosY + (bg.getTilespacing().getY())
						* bg.getDelta().getX())
						+ xStartForAll, (y + moveY * bg.getDelta().getY()), isHFlip, isVFlip);
				startPosY -= img.getHeight();
			}
			
			
		} else {
			startPosY = (y + moveY) % img.getHeight();
			while (startPosY < 240 && yTile > 0) {
				drawImage(trans, img, (startPosY + (bg.getTilespacing().getY())
						* bg.getDelta().getX())
						+ xStartForAll, (y + moveY * bg.getDelta().getY()), isHFlip, isVFlip);
				startPosY += img.getHeight();
				yTile--;
			}
		}
	}
	
	private void drawTileXY(ImageContainer img, BG bg, float x,
			float y, float moveX, float moveY, int xStartForAll, Trans trans, boolean isHFlip, boolean isVFlip) {
		if (!bg.isEnable())
			return;
		Stage stage = GameFight.getInstance().getInstanceOfStage();
		int xTile = (int) bg.getTile().getX();
		float startPosX = x;
		float startPosY = y;
		if (xTile == 0) {
			drawImage(trans, img, (startPosX + (bg.getTilespacing().getX()) + moveX
					* bg.getDelta().getX())
					+ xStartForAll, (y + moveY * bg.getDelta().getY()), isHFlip, isVFlip);
			drawTileY(img, bg, startPosX, startPosY, moveX, moveY, xStartForAll, trans, isHFlip, isVFlip);
		
		}
		startPosX = startPosX % img.getWidth()  + moveX;
		startPosY = startPosY % img.getHeight();
		
		while (startPosX < 320 && (xTile > 0 || bg.getTile().getX() == 1)) {
			drawImage(trans, img,  (startPosX + (bg.getTilespacing().getX())
					* bg.getDelta().getX())
					+ xStartForAll,  (y + moveY * bg.getDelta().getY()), isHFlip, isVFlip);
			

			drawTileY(img, bg, startPosX, startPosY, moveX, moveY, xStartForAll, trans, isHFlip, isVFlip);
			
			startPosX += img.getWidth();
			xTile--;
		}
		if (bg.getTile().getX() == 1) {
			startPosX =  (x - img.getWidth()) + moveX;
			while (startPosX + img.getWidth() + xStartForAll > 0) {
				drawImage(trans, img,
						 (startPosX + (bg.getTilespacing().getX())
								* bg.getDelta().getX())
								+ xStartForAll,  (y + moveY
								* bg.getDelta().getY()), isHFlip, isVFlip);
				drawTileY(img, bg, startPosX, startPosY, moveX, moveY, xStartForAll, trans, isHFlip, isVFlip);
				startPosX -= img.getWidth();
			}

		}

	}



	private void drawImage(Trans trans, ImageContainer img, float x, float y, boolean isHFlip, boolean isVFlip) {
		PalFxSub palfx = GameFight.getInstance().getGlobalEvents().getBgpalfx();

		DrawProperties dp = 
			new DrawProperties(x, y, isHFlip, isVFlip, img);
		if (!palfx.isNoPalFx()) {
			palfx.setDrawProperties(dp);
		}
		dp.setTrans(trans);

		if (GameFight.getInstance().getGlobalEvents().isSuperPause()) {
			palfx = new PalFxSub();
			palfx.setMul(new RGB(100f, 100f, 100f, 100f ));
			palfx.setDrawProperties(dp);
		}
		GraphicsWrapper.getInstance().draw(dp);	
	}



	private int layerDisplay = 0;


	public void render() {

		
		Stage stage = GameFight.getInstance().getInstanceOfStage();
		int xStartForAll = (int) (stage.getCamera().getWidth()/2 * 1f/stage.getScaling().getXscale());

		
		float _mvX = stage.getCamera().getX();
		float _mvY = stage.getCamera().getY();

		SpriteSFF sffSprite = stage.getBackground().getBgdef().getSpr();

		GraphicsWrapper.getInstance().scale(stage.getScaling().getXscale(), stage.getScaling().getYscale());

		
		for (BG bg : stage.getBackground().getBgs()) {
			if (bg.getLayerno() != layerDisplay)
				continue;
			float moveX = _mvX;
			float moveY = _mvY;
			try {
				if (bg.getBgType() == BG.Type.NORMAL
						|| bg.getBgType() == BG.Type.NORM) {
					int grpno = 0;
					try {
						grpno = bg.getSpriteno().getSpritegrp();
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					int imgno = bg.getSpriteno().getSpriteno();
					ImageSpriteSFF imgSprSff = sffSprite.getGroupSpr(grpno)
							.getImgSpr(imgno);
					ImageContainer img = (ImageContainer) imgSprSff.getImage();
					img = getImgProcessSuperpause(img);



					float x =  (bg.getPos().getX() - imgSprSff.getXAxis());
					float y =  bg.getPos().getY() - imgSprSff.getYAxis();

					drawTileXY(img, bg, x, y, moveX, moveY,
							xStartForAll, bg.getBgTrans(), false, false);

				} else if (bg.getBgType() == BG.Type.ANIM && bg.getId() == null) {

					AbstractAnimManager animMng = bg.getAnimManager();
					if (animMng.getCurrentImageSprite() == null)
						continue;
					AirData air = animMng.getCurrentImageSprite().getAirData();
					int grpno = air.getGrpNum();
					int imgno = air.getImgNum();
					if (sffSprite.getGroupSpr(grpno) == null)
						continue;
					ImageSpriteSFF imgSprSff = sffSprite.getGroupSpr(grpno)
							.getImgSpr(imgno);
					if (imgSprSff == null)
						continue;
					ImageContainer img = (ImageContainer) imgSprSff.getImage();
					img = getImgProcessSuperpause(img);

					boolean isVFlip = air.isMirrorV();
					boolean isHFlip = air.isMirrorH();

					float x =  (bg.getPos().getX() - imgSprSff.getXAxis() + air.getXOffSet());
					float y =  bg.getPos().getY() - imgSprSff.getYAxis() + air.getYOffSet();

					Trans trans = bg.getBgTrans();
					trans = air.type == TypeBlit.ASD ? Trans.ADD: trans;
					
					drawTileXY(img, bg, x, y, moveX, moveY,
							xStartForAll, trans, isHFlip, isVFlip);

				} else if (bg.getBgType() == BG.Type.ANIM && bg.getId() != null) {
					
					bg = stage.getBackground().getBgCtrlDefMap().get(bg.getId()).getBgCopys().get(bg.getOrder());
					if (!bg.isVisible())
						continue;
					AbstractAnimManager animMng = bg.getAnimManager();
					if (animMng.getCurrentImageSprite() == null)
						continue;
					AirData air = animMng.getCurrentImageSprite().getAirData();
					int grpno = air.getGrpNum();
					int imgno = air.getImgNum();
					if (sffSprite.getGroupSpr(grpno) == null || sffSprite.getGroupSpr(grpno).getImgSpr(imgno) == null)
						continue;
					
					ImageSpriteSFF imgSprSff = sffSprite.getGroupSpr(grpno)
							.getImgSpr(imgno);
					ImageContainer img = (ImageContainer) imgSprSff.getImage();
					img = getImgProcessSuperpause(img);

					
					float x =  (bg.getPos().getX() - imgSprSff.getXAxis() + air.getXOffSet());
					float y =  bg.getPos().getY() - imgSprSff.getYAxis() + air.getYOffSet();
					
					boolean isVFlip = air.isMirrorV();
					boolean isHFlip = air.isMirrorH();
					
					Trans trans = bg.getBgTrans();
					trans = air.type == TypeBlit.ASD ? Trans.ADD: trans;
					
					drawTileXY(img, bg, x, y, moveX, moveY,
							xStartForAll, trans, isHFlip, isVFlip);
					

				} else if (bg.getBgType() == BG.Type.PARALLAX) {
					int grpno = bg.getSpriteno().getSpritegrp();
					int imgno = bg.getSpriteno().getSpriteno();
					ImageSpriteSFF imgSprSff = sffSprite.getGroupSpr(grpno)
							.getImgSpr(imgno);
					ImageContainer img = (ImageContainer) imgSprSff.getImage();
					img = getImgProcessSuperpause(img);


					float x =  (bg.getPos().getX() - imgSprSff.getXAxis());
					float y =  bg.getPos().getY() - imgSprSff.getYAxis();


					for (int v = 0; v < img.getHeight(); ++v) {
						float deltaY = bg.getXscale().getX()
								+ (v * ((bg.getXscale().getY() - bg
										.getXscale().getX()) / img.getHeight()));
						deltaY = deltaY * bg.getDelta().getX();
						float x1 = (x + (deltaY * moveX)) + xStartForAll;
						float y2 = (y + moveY * bg.getDelta().getY()) + v;
						if (!bg.isEnable())
							continue;
						drawImage(bg.getTrans(), img, x1, y2, x1 + img.getWidth(), y2 + 1,
								0, v, img.getWidth(), v + 1);
					}
				}

			} catch (Exception e) {
				 e.printStackTrace();

				continue;
			}
		}
		GraphicsWrapper.getInstance().scale(1f/stage.getScaling().getXscale(), 1f/stage.getScaling().getYscale());

	}

	private void drawImage(Trans trans, ImageContainer img, float xl, float yt, float xr,
			float yb, int xlSrc, int yTopSrc, int xrSrc, int yBottomSrc) {
		PalFxSub palfx = GameFight.getInstance().getGlobalEvents().getBgpalfx();

		DrawProperties dp = 
			new DrawProperties(xl, xr, yt,
					yb, xlSrc, xrSrc, yTopSrc, yBottomSrc, false, false, img);
		dp.setTrans(trans);
		if (!palfx.isNoPalFx()) {
			palfx.setDrawProperties(dp);
		}
		GraphicsWrapper.getInstance().draw(dp);
		
	}

	private ImageContainer getImgProcessSuperpause(ImageContainer img) {
		if (GameFight.getInstance().getGlobalEvents().isSuperPause()) {
			SuperpauseSub superpause = GameFight.getInstance().getGlobalEvents().getSuperpause();
			for (int type: superpause.getDarken()) {
//				img = superpause.getFilter(type).filter(img, null);
			}
		}

		return img;
	}
	
	public int getPriority() {
		return 100;
	}

	public boolean isProcess() {
		return true;
	}

	public boolean remove() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lee.mugen.core.renderer.game.java.IBackgroundRenderer#getLayerDisplay()
	 */
	public int getLayerDisplay() {
		return layerDisplay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lee.mugen.core.renderer.game.java.IBackgroundRenderer#setLayerDisplay
	 */
	public void setLayerDisplay(int layerDisplay) {
		this.layerDisplay = layerDisplay;
	}

	public void setPriority(int p) {
		// TODO Auto-generated method stub

	}

}
