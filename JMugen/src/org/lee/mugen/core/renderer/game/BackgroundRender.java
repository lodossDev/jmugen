package org.lee.mugen.core.renderer.game;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.renderer.DrawProperties;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.ImageContainer;
import org.lee.mugen.renderer.PalFxSub;
import org.lee.mugen.renderer.RGB;
import org.lee.mugen.renderer.Trans;
import org.lee.mugen.sprite.background.BG;
import org.lee.mugen.sprite.background.Stage;
import org.lee.mugen.sprite.base.AbstractAnimManager;
import org.lee.mugen.sprite.baseForParse.ImageSpriteSFF;
import org.lee.mugen.sprite.baseForParse.SpriteSFF;
import org.lee.mugen.sprite.entity.SuperpauseSub;

import temp.LBackgroundRender;

public class BackgroundRender implements IBackgroundRenderer {


	public BackgroundRender() {
		

	}

	private void drawTileXY(ImageContainer img, BG bg, float x,
			float y, int width, float moveX, float moveY, int xStartForAll, Trans trans, boolean isHFlip, boolean isVFlip) {
		if (!bg.isEnable())
			return;
		Stage stage = StateMachine.getInstance().getInstanceOfStage();
		int xTile = (int) bg.getTile().getX();
		float startPos = x;
		if (xTile == 0) {
			drawImage(trans, img, (startPos + (bg.getTilespacing().getX()) + moveX
					* bg.getDelta().getX())
					+ xStartForAll, (y + moveY * bg.getDelta().getY()), isHFlip, isVFlip);
		}
		startPos = startPos % width;
		
		while (startPos < width && (xTile > 0 || bg.getTile().getX() == 1)) {
			drawImage(trans, img,  (startPos + (bg.getTilespacing().getX()) + moveX
					* bg.getDelta().getX())
					+ xStartForAll,  (y + moveY * bg.getDelta().getY()), isHFlip, isVFlip);
			startPos += img.getWidth();
			xTile--;
		}
		if (bg.getTile().getX() == 1) {
			startPos =  (x - img.getWidth());
			while (startPos + img.getWidth() + xStartForAll > stage.getCamera()
					.getBoundleft() - xStartForAll*2) {
				drawImage(trans, img,
						 (startPos + (bg.getTilespacing().getX()) + moveX
								* bg.getDelta().getX())
								+ xStartForAll,  (y + moveY
								* bg.getDelta().getY()), isHFlip, isVFlip);
				startPos -= img.getWidth();
			}

		}

	}



	private void drawImage(Trans trans, ImageContainer img, float x, float y, boolean isHFlip, boolean isVFlip) {
		PalFxSub palfx = StateMachine.getInstance().getGlobalEvents().getBgpalfx();

		DrawProperties dp = 
			new DrawProperties(x, y, isHFlip, isVFlip, img);
		if (!palfx.isNoPalFx()) {
			palfx.setDrawProperties(dp);
		}
		dp.setTrans(trans);

		if (StateMachine.getInstance().getGlobalEvents().isSuperPause()) {
			palfx = new PalFxSub();
			palfx.setMul(new RGB(100f, 100f, 100f, 100f ));
			palfx.setDrawProperties(dp);
		}
		GraphicsWrapper.getInstance().draw(dp);	
	}



	private int layerDisplay = 0;


	public void render() {

		
		Stage stage = StateMachine.getInstance().getInstanceOfStage();
		int xStartForAll = stage.getCamera().getWidth() / 2;
		
		int left = stage.getBound().getScreenleft();
		int right = stage.getBound().getScreenright();

		int leftLimit = left + stage.getCamera().getBoundleft()
				+ stage.getCamera().getTension();

		int rightLimit = -right + stage.getCamera().getBoundright()
				- stage.getCamera().getTension();
		
		int width = (Math.abs(stage.getCamera().getBoundleft()) + Math
				.abs(stage.getCamera().getBoundright())) + stage.getCamera().getWidth();
		float _mvX = stage.getCamera().getX();
		float _mvY = stage.getCamera().getY();

		SpriteSFF sffSprite = stage.getSpriteSFF();

		GraphicsWrapper.getInstance().scale(stage.getScaling().getXscale(), stage.getScaling().getYscale());

		
		for (BG bg : stage.getBgs()) {
			if (bg.getLayerno() != layerDisplay)
				continue;
			float moveX = _mvX;
			float moveY = _mvY;
			try {
				if (bg.getBgType() == BG.Type.NORMAL
						|| bg.getBgType() == BG.Type.NORM) {
					int grpno = bg.getSpriteno().getGrp();
					int imgno = bg.getSpriteno().getImg();
					ImageSpriteSFF imgSprSff = sffSprite.getGroupSpr(grpno)
							.getImgSpr(imgno);
					ImageContainer img = (ImageContainer) imgSprSff.getImage();
					img = getImgProcessSuperpause(img);



					float x =  (bg.getPos().getX() - imgSprSff.getXAxis());
					float y =  bg.getPos().getY() - imgSprSff.getYAxis();

					if (bg.getBgTrans() != Trans.NONE) {
//						g.setComposite(AlphaComposite.getInstance(
//								AlphaComposite.SRC_OVER, 0.5f));
					}

					drawTileXY(img, bg, x, y, width, moveX, moveY,
							xStartForAll, bg.getBgTrans(), false, false);

//					g.setComposite(AlphaComposite.getInstance(
//							AlphaComposite.SRC_OVER, 1f));

				} else if (bg.getBgType() == BG.Type.ANIM && bg.getId() == null) {

					AbstractAnimManager animMng = bg.getAnimManager();
					if (animMng.getCurrentImageSprite() == null)
						continue;
					int grpno = animMng.getCurrentImageSprite().getAirData().getGrpNum();
					int imgno = animMng.getCurrentImageSprite().getAirData().getImgNum();
					if (sffSprite.getGroupSpr(grpno) == null)
						continue;
					ImageSpriteSFF imgSprSff = sffSprite.getGroupSpr(grpno)
							.getImgSpr(imgno);
					if (imgSprSff == null)
						continue;
					ImageContainer img = (ImageContainer) imgSprSff.getImage();
					img = getImgProcessSuperpause(img);

					boolean isVFlip = animMng.getCurrentImageSprite().getAirData().isMirrorV();
					boolean isHFlip = animMng.getCurrentImageSprite().getAirData().isMirrorH();

					float x =  (bg.getPos().getX() - imgSprSff.getXAxis() + animMng.getCurrentImageSprite().getAirData().getXOffSet());
					float y =  bg.getPos().getY() - imgSprSff.getYAxis() + animMng.getCurrentImageSprite().getAirData().getYOffSet();

//					drawImage(bg.getBgTrans(), img,  (x + moveX * bg.getDelta().getX())
//							+ xStartForAll,  (y + moveY * bg.getDelta().getY()));
					drawTileXY(img, bg, x, y, width, moveX, moveY,
							xStartForAll, bg.getBgTrans(), isHFlip, isVFlip);

				} else if (bg.getBgType() == BG.Type.ANIM && bg.getId() != null) {
					
					bg = stage.getBgCtrlDefMap().get(bg.getId()).getBgCopys().get(bg.getOrder());
					if (!bg.isVisible())
						continue;
					AbstractAnimManager animMng = bg.getAnimManager();
					if (animMng.getCurrentImageSprite() == null)
						continue;
					int grpno = animMng.getCurrentImageSprite().getAirData().getGrpNum();
					int imgno = animMng.getCurrentImageSprite().getAirData().getImgNum();
					if (sffSprite.getGroupSpr(grpno) == null || sffSprite.getGroupSpr(grpno).getImgSpr(imgno) == null)
						continue;
					
					ImageSpriteSFF imgSprSff = sffSprite.getGroupSpr(grpno)
							.getImgSpr(imgno);
					ImageContainer img = (ImageContainer) imgSprSff.getImage();
					img = getImgProcessSuperpause(img);

					
					float x =  (bg.getPos().getX() - imgSprSff.getXAxis() + animMng.getCurrentImageSprite().getAirData().getXOffSet());
					float y =  bg.getPos().getY() - imgSprSff.getYAxis() + animMng.getCurrentImageSprite().getAirData().getYOffSet();
					
					boolean isVFlip = animMng.getCurrentImageSprite().getAirData().isMirrorV();
					boolean isHFlip = animMng.getCurrentImageSprite().getAirData().isMirrorH();
					
//					drawImage(bg.getBgTrans(), img,  (x + moveX * bg.getDelta().getX())
//							+ xStartForAll,  (y + moveY * bg.getDelta().getY()));
					drawTileXY(img, bg, x, y, width, moveX, moveY,
							xStartForAll, bg.getBgTrans(), isHFlip, isVFlip);
					

				} else if (bg.getBgType() == BG.Type.PARALLAX) {
					int grpno = bg.getSpriteno().getGrp();
					int imgno = bg.getSpriteno().getImg();
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
						drawImage(img, x1, y2, x1 + img.getWidth(), y2 + 1,
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

	private void drawImage(ImageContainer img, float xl, float yt, float xr,
			float yb, int xlSrc, int yTopSrc, int xrSrc, int yBottomSrc) {
		PalFxSub palfx = StateMachine.getInstance().getGlobalEvents().getBgpalfx();

		DrawProperties dp = 
			new DrawProperties(xl, xr, yt,
					yb, xlSrc, xrSrc, yTopSrc, yBottomSrc, false, false, img);
		if (!palfx.isNoPalFx()) {
			palfx.setDrawProperties(dp);
		}
		GraphicsWrapper.getInstance().draw(dp);
//		GraphicsWrapper.getInstance().setColor(Color.RED);
//		GraphicsWrapper.getInstance().drawLine((int)xl, (int)yt, (int)xr, (int)yb);
		
	}

	private ImageContainer getImgProcessSuperpause(ImageContainer img) {
		if (StateMachine.getInstance().getGlobalEvents().isSuperPause()) {
			SuperpauseSub superpause = StateMachine.getInstance().getGlobalEvents().getSuperpause();
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
