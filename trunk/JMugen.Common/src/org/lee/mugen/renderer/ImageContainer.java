package org.lee.mugen.renderer;





public class ImageContainer {
	protected Object img;
	protected int width;
	protected int height;
	
//	Texture texture;

	public ImageContainer(Object img, int width, int height) {
		this.img = img;
		this.width = width;
		this.height = height;
	}

	public int getHeight() {
		return height;
	}
	public Object getImg() {
		return img;
//		
//		if (RenderType.JAVA == StateMachine.getInstance().getRenderType()) {
//			return img;
//		} else if ((RenderType.LWJGL == StateMachine.getInstance().getRenderType())) {
//        	TextureLoader loader = ((LwjgGameWindow)StateMachine.getInstance().getWindow()).getTextureLoader();
//			try {
////				ImageScale2x scale2X = new ImageScale2x(bmp);
////				Image img = scale2X.getScaledImage();
////				bmp = new BufferedImage(bmp.getWidth() * 2, bmp.getHeight() * 2, BufferedImage.TYPE_INT_ARGB);
////				bmp.getGraphics().drawImage(img, 0, 0, null);
//				texture = loader.getTexture((BufferedImage) img);
//				img = null;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return texture;
//		}
//		return null;
	}
	public int getWidth() {
		return width;
	}
	
}
