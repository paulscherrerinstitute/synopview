package ch.psi.synopview.svp.visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import ch.psi.synopview.svp.devices.Device;
import ch.psi.synopview.svp.devices.DevicesData;

public class ViewProperties {

	public int x0 = 0; // origin (center)
	public int y0 = 0;

	public int rx = 0; // translation (from origin)
	public int ry = 0;

	public double scale = 0.0; // scale
	public int width = 0; // viewport size
	public int height = 0;

	public double bscale = 0.0; // basic view (all devices are visible)
	public int bwidth = 0;
	public int bheight = 0;

	public int xsize = 0;
	public int ysize = 0;

	public Rectangle clip = null;
	private Device hilited = null;

	private DeviceColorModel colorModel = null;
	private Color outlineColor = Constants.outlineColor;
	private Color backgroundColor = Constants.backgroundColor;

	public void center(Dimension size) {
		x0 = y0 = 0;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public DeviceColorModel getColorModel() {
		return colorModel;
	}

	public Device getHilited() {
		return hilited;
	}

	public Color getOutlineColor() {
		return outlineColor;
	}

	public void loadBasic(Dimension size, DevicesData data) {
		this.bscale = data.scale;
		xsize = data.xsize;
		ysize = data.ysize;
		bwidth = (int) (ysize * bscale);
		bheight = (int) (xsize * bscale);
	}

	public void scaleChanged(double scale) {
		this.scale = scale;
		width = (int) (xsize * scale);
		height = (int) (ysize * scale);
	}

	public void setBasicView(Dimension d) {
		scale = bscale;
		width = bwidth;
		height = bheight;
		rx = ry = 0;
		center(d);
	}

	public void setClip(Rectangle clip) {
		this.clip = clip;
	}

	public void setColorModel(DeviceColorModel colorModel) {
		this.colorModel = colorModel;
		if (colorModel != null) {
			outlineColor = colorModel.getOutlineColor();
			backgroundColor = colorModel.getBackgroundColor();
		} else {
			outlineColor = Constants.outlineColor;
			backgroundColor = Constants.backgroundColor;
		}
	}

	public void setHilited(Device dev) {
		hilited = dev;
	}

	public void viewPosition(int rx, int ry, Dimension viewport) {
		this.rx = rx;
		this.ry = ry;
	}
}
