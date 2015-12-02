package ch.psi.synopview.svp.visual;

/**
 * This type was created in VisualAge.
 */

public class ViewHistory {
	final int lowerbound = -1;
	int pos;
	int first, last; 
	int bufferSize;
	HistoryViewData[] views;

/**
 * UndoManager constructor comment.
 */
public ViewHistory(int steps2remember) {
	bufferSize = steps2remember;
	views = new HistoryViewData[bufferSize];
	flush();
}
/**
 * This method was created in VisualAge.
 * @patam view svp.visual.HistoryViewData
 */
public void addView(HistoryViewData view) {
	if (pos==lowerbound) pos=last=increment(pos);
	else {
		pos=last=increment(pos);
		if (last==first) first=increment(first);		// lose first (the "oldest" action)
	}
	views[pos]=view;
	int np = increment(last);							// clear lost actions -> finalization!
	while (np!=first) {
		views[np]=null;
		np=increment(np);
	}
}
/**
 * This method was created in VisualAge.
 * @return int
 * @param pos int

 */
private int decrement(int pos) {
	if (pos==first) return lowerbound;
	else {
		int np = pos-1;
		if (np<0) np=bufferSize-1;
		return np;
	}
}
/**
 * This method was created in VisualAge.
 */
public void flush() {
	first=0;
	pos=last=lowerbound;
	for (int i=0; i < bufferSize; i++)
		views[i]=null;
}
/**
 * Insert the method's description here.
 * Creation date: (28/7/99 12:06:02)
 * @return svp.visual.HistoryViewData
 */
public HistoryViewData getPreviousView() {
	HistoryViewData hvd = null;
	if (pos!=lowerbound)  {
		hvd = views[pos];
		pos=decrement(pos);
	}
	return hvd;
}
/**
 * This method was created in VisualAge.
 * @return int
 * @param pos int
 */
private int increment(int pos) {
	if (pos==lowerbound) return first;
	else return ((pos+1) % bufferSize);
}
}
