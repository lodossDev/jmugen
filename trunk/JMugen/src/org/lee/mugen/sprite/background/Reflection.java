package org.lee.mugen.sprite.background;

public class Reflection {
	private Stage parent = null;
	public Reflection(Stage stage) {
		parent = stage;
	}
	
	//Intensity of reflection (from 0 to 256). Set to 0 to have no
	//reflection. Defaults to 0.
	private int intensity = 0;

	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}
}
