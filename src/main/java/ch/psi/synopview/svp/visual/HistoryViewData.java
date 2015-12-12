package ch.psi.synopview.svp.visual;

public class HistoryViewData {
	private int rx0;
	private int ry0;
	private double scale;

	public HistoryViewData(int rx0, int ry0, double scale) {
		this.rx0 = rx0;
		this.ry0 = ry0;
		this.scale = scale;
	}

	public int get_rx0() {
		return rx0;
	}

	public int get_ry0() {
		return ry0;
	}

	public double getScale() {
		return scale;
	}
}
