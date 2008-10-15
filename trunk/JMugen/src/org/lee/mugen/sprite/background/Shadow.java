package org.lee.mugen.sprite.background;

public class Shadow {
	private Stage parent = null;

	public Shadow(Stage stage) {
		parent = stage;
	}

	// This is the shadow darkening intensity. Valid values range from
	// 0 (lightest) to 256 (darkest). Defaults to 128 if omitted.
	private int intensity = 96;

	// This is the shadow color given in r,g,b. Valid values for each
	// range from 0 (lightest) to 255 (darkest).
	// Defaults to 0,0,0 if omitted.
	// intensity and color's effects add up to give the final shadow
	// result.
	private Color color = new Color(0, 0, 0);

	public void setColor(Object... colors) {
		if (colors.length > 0) {
			color.r = colors[0] == null ? 0
					: colors[0] instanceof Number ? ((Number) colors[0])
							.intValue() : new Integer(colors[0].toString())
							.intValue();
		}
		if (colors.length > 1) {
			color.r = colors[1] == null ? 0
					: colors[1] instanceof Number ? ((Number) colors[1])
							.intValue() : new Integer(colors[1].toString())
							.intValue();
		}
		if (colors.length > 2) {
			color.r = colors[2] == null ? 0
					: colors[2] instanceof Number ? ((Number) colors[2])
							.intValue() : new Integer(colors[2].toString())
							.intValue();
		}
	}

	public static class Color {
		public Color(int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

		private int r;

		private int g;

		private int b;

		public int getB() {
			return b;
		}

		public void setB(int b) {
			this.b = b;
		}

		public int getG() {
			return g;
		}

		public void setG(int g) {
			this.g = g;
		}

		public int getR() {
			return r;
		}

		public void setR(int r) {
			this.r = r;
		}

	}

	// This is the scale factor of the shadow. Use a big scale factor
	// to make the shadow longer. You can use a NEGATIVE scale factor
	// to make the shadow fall INTO the screen.
	// Defaults to 0.4 if omitted.
	private float yscale = .3f;

	// This parameter lets you set the range over which the shadow is
	// visible. The first value is the high level, and the second is
	// the middle level. Both represent y-coordinates of the player.
	// A shadow is invisible if the player is above the high level,
	// and fully visible if below the middle level. The shadow is
	// faded in between the two levels. This gives an effect of the
	// shadow fading away as the player gets farther away from the
	// ground. If omitted, defaults to no level effects (shadow is
	// always fully visible).
	private Fade fade = new Fade(0, 0);

	public static class Fade {
		public Fade(int s, int e) {
			start = s;
			end = e;
		}

		int start;

		int end;

		public void setRange(Object... params) {
			if (params.length > 0) {
				start = params[0] == null ? 0
						: params[0] instanceof Number ? ((Number) params[0])
								.intValue() : new Integer(params[0].toString())
								.intValue();
			}
			if (params.length > 1) {
				end = params[1] == null ? 0
						: params[1] instanceof Number ? ((Number) params[1])
								.intValue() : new Integer(params[1].toString())
								.intValue();
			}
		}

		public int getEnd() {
			return end;
		}

		public int getStart() {
			return start;
		}

	}

	private int reflect = 0;

	public int getReflect() {
		return reflect;
	}

	public void setReflect(int reflect) {
		this.reflect = reflect;
	}

	// /////////////////////////////////
	public Color getShadowColor() {
		return color;
	}

	public Fade getFade() {
		return fade;
	}

	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	public float getYscale() {
		return yscale;
	}

	public void setYscale(float yscale) {
		this.yscale = yscale;
	}
}
