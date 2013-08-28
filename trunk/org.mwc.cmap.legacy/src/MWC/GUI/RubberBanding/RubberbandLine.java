package MWC.GUI.RubberBanding;

import java.awt.Component;
import java.awt.Graphics;

import MWC.GUI.Rubberband;


public class RubberbandLine extends Rubberband{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RubberbandLine() {
	}
	public RubberbandLine(final Component component) {
		super(component);
	}
	public void drawLast(final Graphics graphics) {
		graphics.drawLine(anchorPt.x, anchorPt.y, 
							lastPt.x,   lastPt.y);
	}
	public void drawNext(final Graphics graphics) {
		graphics.drawLine(anchorPt.x,    anchorPt.y, 
							stretchedPt.x, stretchedPt.y);
	}
}
