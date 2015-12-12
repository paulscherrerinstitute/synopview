package ch.psi.synopview.svp.visual;

public class ViewHistory {
	final int lowerbound = -1;
	int pos;
	int first, last;
	int bufferSize;
	HistoryViewData[] views;

	public ViewHistory(int steps2remember) {
		bufferSize = steps2remember;
		views = new HistoryViewData[bufferSize];
		flush();
	}

	public void addView(HistoryViewData view) {
		if (pos == lowerbound)
			pos = last = increment(pos);
		else {
			pos = last = increment(pos);
			if (last == first)
				first = increment(first); // lose first (the "oldest" action)
		}
		views[pos] = view;
		int np = increment(last); // clear lost actions -> finalization!
		while (np != first) {
			views[np] = null;
			np = increment(np);
		}
	}

	private int decrement(int pos) {
		if (pos == first)
			return lowerbound;
		else {
			int np = pos - 1;
			if (np < 0)
				np = bufferSize - 1;
			return np;
		}
	}

	public void flush() {
		first = 0;
		pos = last = lowerbound;
		for (int i = 0; i < bufferSize; i++)
			views[i] = null;
	}

	public HistoryViewData getPreviousView() {
		HistoryViewData hvd = null;
		if (pos != lowerbound) {
			hvd = views[pos];
			pos = decrement(pos);
		}
		return hvd;
	}

	private int increment(int pos) {
		if (pos == lowerbound)
			return first;
		else
			return ((pos + 1) % bufferSize);
	}
}
