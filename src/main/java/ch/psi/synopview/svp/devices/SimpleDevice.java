package ch.psi.synopview.svp.devices;

import java.awt.*;

import ch.psi.synopview.svp.visual.DeviceColorModel;
import ch.psi.synopview.svp.visual.ViewProperties;

public class SimpleDevice extends Device {
	
	public SimpleDevice(String name, String parent, String cmd, int machine, int system, int color, Polygon form, DeviceColorModel colorModel) {
		super(name, parent, cmd, machine, system, color, form, colorModel);
	}

	protected void finalize() throws Throwable {
		super.finalize();
	}

	public void paintDevice(Graphics g, ViewProperties view) {
		Polygon poly = new Polygon(scaled.xpoints, scaled.ypoints, scaled.npoints);
		poly.translate(-view.rx + view.x0, -view.ry + view.y0);
		if (poly.getBounds().intersects(view.clip)) { // clipping
			if (this == view.getHilited())
				g.setColor(new Color(0xFFFFFF - color.getRGB()));
			else
				g.setColor(color);

			g.fillPolygon(poly);
			if (this == view.getHilited())
				g.setColor(Color.red); // bounding poly
			else
				g.setColor(view.getOutlineColor());
			g.drawPolygon(poly);
		}
	}
}
