package ch.psi.synopview.svp.visual;

import java.awt.Color;
import java.util.HashMap;

public class DeviceColorModel {
	public static final int backgroundColor = 0;
	public static final int outlineColor = 255;
	private HashMap<Integer, Color> colors = new HashMap<Integer, Color>();

	public void addColor(int id, Color color) {
		colors.put(new Integer(id), color);
	}

	public Color getBackgroundColor() {
		Color color = getColor(backgroundColor);

		if (color == null) {
			return Constants.backgroundColor;
		} else {
			return color;
		}
	}

	public Color getColor(int id) {
		return (Color) colors.get(new Integer(id));
	}

	public Color getOutlineColor() {
		Color color = getColor(outlineColor);

		if (color == null) {
			return Constants.outlineColor;
		} else {
			return color;
		}
	}
}
