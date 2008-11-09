package org.lee.mugen.core.renderer.game.system;

import org.lee.mugen.background.BG;
import org.lee.mugen.background.Background;
import org.lee.mugen.core.GameFight;
import org.lee.mugen.core.renderer.game.IBackgroundRenderer;
import org.lee.mugen.fight.system.MugenSystem;
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

public class BackgroundRender implements IBackgroundRenderer {

	private Background background;
	public BackgroundRender(Background background) {
		this.background = background;

	}

	private void drawTileXY(ImageContainer img, BG bg, float x,
			float y, int width, float moveX, float moveY, int xStartForAll, Trans trans, boolean isHFlip, boolean isVFlip) {
		if (!bg.isEnable())
			return;
		int xTile = (int) bg.getTile().getX();
		int yTile = (int) bg.getTile().getY();
		x = x % img.getWidth();
		float startPos = x + moveX;
		if (xTile == 0) {
			drawImage(trans, img, (startPos + (bg.getTilespacing().getX()) 
					* bg.getDelta().getX())
					+ xStartForAll, (y + moveY * bg.getDelta().getY()), isHFlip, isVFlip);
			if (yTile > 1) {
				drawImage(trans, img, (startPos + (bg.getTilespacing().getX())
						* bg.getDelta().getX())
						+ xStartForAll, (y + moveY * bg.getDelta().getY()) + img.getHeight(), isHFlip, isVFlip);
			}
		}
		
//		startPos = startPos % width;
		
		while (startPos  <= width && (xTile > 0 || bg.getTile().getX() == 1)) {
			drawImage(trans, img,  (startPos + (bg.getTilespacing().getX())
					* bg.getDelta().getX())
					+ xStartForAll,  (y + moveY * bg.getDelta().getY()), isHFlip, isVFlip);

			
			
				drawImage(trans, img,  (startPos + (bg.getTilespacing().getX())
						* bg.getDelta().getX())
						+ xStartForAll,  (y + moveY * bg.getDelta().getY()) + img.getHeight(), isHFlip, isVFlip);
			startPos += img.getWidth();
			xTile--;
		}
		if (bg.getTile().getX() == 1) {
			startPos = x + moveX;
//			startPos = startPos % width;
			float originalPos = startPos % width;
			System.out.println(startPos + img.getWidth());
			startPos =  (startPos - img.getWidth());
			while (startPos + img.getWidth() + xStartForAll + moveX>0) {
				drawImage(trans, img,  (startPos + (bg.getTilespacing().getX())
						* bg.getDelta().getX())
						+ xStartForAll,  (y + moveY * bg.getDelta().getY()), isHFlip, isVFlip);
				drawImage(trans, img,  (startPos + (bg.getTilespacing().getX())
						* bg.getDelta().getX())
						+ xStartForAll,  (y + moveY * bg.getDelta().getY()) + img.getHeight(), isHFlip, isVFlip);
				startPos -= img.getWidth();
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
		SpriteSFF sffSprite = background.getBgdef().getSpr();
		if (sffSprite == null)
			sffSprite = MugenSystem.getInstance().getFiles().getSpr();
		
		int xStartForAll = 0;

//		background.process();
		
		if (background.getBgdef().getBgclearcolor() != null) {
			RGB rgb = background.getBgdef().getBgclearcolor();
			GraphicsWrapper.getInstance().setColor(rgb.getR(), rgb.getG(), rgb.getB());
		
			GraphicsWrapper.getInstance().fillRect(0, 0, 640, 480);
		}


		int i = 0;
		for (BG bg : background.getBgs()) {
			if (bg.getLayerno() != layerDisplay)
				continue;
//			if (i != 1)
//				continue;
//			i++;
//			xStartForAll = -(int) bg.getPos().getX();
			float moveX = bg.getStart().getX() + MugenSystem.getInstance().getTitleInfo().getMenu().getPos().x;
			float moveY = bg.getStart().getY();// + MugenSystem.getInstance().getTitleInfo().getMenu().getPos().y;//bg.getWidth().y;
//			moveX = 160;
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
					float y =  -bg.getPos().getY() - imgSprSff.getYAxis();

					drawTileXY(img, bg, x, y, 320, moveX, moveY,
							xStartForAll, bg.getBgTrans(), false, false);
//					drawTileXY(img, bg, 0, 0, 0, 0, 0,
//							xStartForAll, bg.getBgTrans(), false, false);

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
					
					drawTileXY(img, bg, x, y, bg.getWidth().x, moveX, moveY,
							xStartForAll, trans, isHFlip, isVFlip);

				} else if (bg.getBgType() == BG.Type.ANIM && bg.getId() != null) {
					
					bg = background.getBgCtrlDefMap().get(bg.getId()).getBgCopys().get(bg.getOrder());
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
					
					drawTileXY(img, bg, x, y, bg.getWidth().x, moveX, moveY,
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
						float deltaY = bg.getTopXscale()
								+ (v * ((bg.getBottomXscale() - bg
										.getTopXscale()) / img.getHeight()));
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
