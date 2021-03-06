/*
 *    Debrief - the Open Source Maritime Analysis Application
 *    http://debrief.info
 *
 *    (C) 2000-2014, PlanetMayo Ltd
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the Eclipse Public License v1.0
 *    (http://www.eclipse.org/legal/epl-v10.html)
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 */
package org.mwc.debrief.core.editors.painters;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Enumeration;
import java.util.Vector;

import org.mwc.debrief.core.editors.painters.highlighters.SWTPlotHighlighter;
import org.mwc.debrief.core.editors.painters.snail.SnailDrawSWTAnnotation;
import org.mwc.debrief.core.editors.painters.snail.SnailDrawSWTBuoyPattern;
import org.mwc.debrief.core.editors.painters.snail.SnailDrawSWTFix;
import org.mwc.debrief.core.editors.painters.snail.SnailDrawSWTSensorContact;
import org.mwc.debrief.core.editors.painters.snail.SnailDrawSWTTMAContact;

import Debrief.GUI.Tote.Painters.SnailPainter;
import MWC.Algorithms.PlainProjection;
import MWC.GUI.CanvasType;
import MWC.GUI.Editable;
import MWC.GUI.Layer;
import MWC.GUI.Plottable;
import MWC.GUI.Properties.BoundedInteger;
import MWC.GenericData.HiResDate;
import MWC.GenericData.Watchable;
import MWC.GenericData.WatchableList;
import MWC.TacticalData.TrackDataProvider;

/**
 * painter which plots all data, and draws a square rectangle around tactical
 * items at the current dtg
 * 
 * @author ian.mayo
 */
public class SnailHighlighter implements TemporalLayerPainter
{

	public final static String NAME = "Snail";

	// /////////////////////////////////////////////////
	// nested interface for painters which can draw snail trail components
	// /////////////////////////////////////////////////
	public static interface drawSWTHighLight
	{
		public java.awt.Rectangle drawMe(MWC.Algorithms.PlainProjection proj,
				CanvasType dest, WatchableList list, Watchable watch,
				SnailHighlighter parent, HiResDate dtg, Color backColor);

		public boolean canPlot(Watchable wt);
	}

	// private static Color _myColor = Color.white;
	//
	// private static int _mySize = 5;

	final TrackDataProvider _dataProvider;

	/**
	 * the highlight plotters we know about
	 */
	private final Vector<drawSWTHighLight> _myHighlightPlotters;

	/**
	 * the snail track plotter to use
	 */
	private final SnailDrawSWTFix _mySnailPlotter;

	/**
	 * the snail buoy-pattern plotter to use
	 */
	private final SnailDrawSWTBuoyPattern _mySnailBuoyPlotter;

	/**
	 * constructor - remember we need to know about the primary/secondary tracks
	 * 
	 * @param dataProvider
	 */
	public SnailHighlighter(final TrackDataProvider dataProvider)
	{
		_dataProvider = dataProvider;

		_mySnailPlotter = new SnailDrawSWTFix("Snail");
		_mySnailBuoyPlotter = new SnailDrawSWTBuoyPattern();

		_myHighlightPlotters = new Vector<drawSWTHighLight>(0, 1);
		_myHighlightPlotters.addElement(_mySnailPlotter);
		_myHighlightPlotters.addElement(_mySnailBuoyPlotter);
		_myHighlightPlotters.addElement(new SnailDrawSWTAnnotation());
		_myHighlightPlotters.addElement(new SnailDrawSWTSensorContact(
				_mySnailPlotter));
		_myHighlightPlotters
				.addElement(new SnailDrawSWTTMAContact(_mySnailPlotter));

		_mySnailPlotter.setPointSize(new BoundedInteger(5, 0, 0));
		_mySnailPlotter.setVectorStretch(1);
	}

	/**
	 * find out the stretch on the vector for snail plots
	 * 
	 * @return
	 */
	public double getVectorStretch()
	{
		return _mySnailPlotter.getVectorStretch();
	}

	/**
	 * set the snail stretch factor
	 */
	public void setVectorStretch(final double val)
	{
		_mySnailPlotter.setVectorStretch(val);
	}

	/**
	 * accessor for the snail properties
	 * 
	 */
	public SnailDrawSWTFix getSnailProperties()
	{
		return _mySnailPlotter;
	}

	/**
	 * ok, paint this layer, adding highlights where applicable
	 * 
	 * @param theLayer
	 * @param dest
	 * @param dtg
	 */
	public void paintThisLayer(final Layer theLayer, final CanvasType dest, final HiResDate newDTG)
	{
		// right, none of that fannying around painting the whole layer.

		// start off by finding the non-watchables for this layer
		final Vector<Plottable> nonWatches = SnailPainter
				.getNonWatchables(theLayer);

		// cool, draw them
		final Enumeration<Plottable> iter = nonWatches.elements();
		while (iter.hasMoreElements())
		{
			final Plottable p = (Plottable) iter.nextElement();
			p.paint(dest);
		}

		// and now the -watchables
		final Vector<Plottable> watchables = SnailPainter.getWatchables(theLayer);

		// cool, draw them between the valid period

		// got through to highlight the data
		final Enumeration<Plottable> watches = watchables.elements();
		while (watches.hasMoreElements())
		{
			final WatchableList list = (WatchableList) watches.nextElement();
			// is the primary an instance of layer (with it's own line thickness?)
			if (list instanceof Layer)
			{
				final Layer ly = (Layer) list;
				final int thickness = ly.getLineThickness();
				dest.setLineWidth(thickness);
			}

			// ok, clear the nearest items
			final Watchable[] wList = list.getNearestTo(newDTG);
			for (int i = 0; i < wList.length; i++)
			{
				final Watchable watch = wList[i];
				if (watch != null)
				{
					// plot it
					highlightIt(dest.getProjection(), dest, list, watch, newDTG,
							dest.getBackgroundColor());
				}
			}
		}
	}

	private void highlightIt(final PlainProjection projection, final CanvasType dest,
			final WatchableList list, final Watchable watch, final HiResDate newDTG,
			final Color backgroundColor)
	{
		// set the highlight colour
		dest.setColor(Color.white);

		// see if our plotters can plot this type of watchable
		final Enumeration<drawSWTHighLight> iter = _myHighlightPlotters.elements();
		while (iter.hasMoreElements())
		{
			final drawSWTHighLight plotter = (drawSWTHighLight) iter.nextElement();

			if (plotter.canPlot(watch))
			{
				// does this list have a width?
				if (list instanceof Layer)
				{
					final Layer ly = (Layer) list;
					if (dest instanceof Graphics2D)
					{
						final Graphics2D g2 = (Graphics2D) dest;
						g2.setStroke(new BasicStroke(ly.getLineThickness()));
					}
				}

				plotter.drawMe(projection, dest, list, watch, this, newDTG,
						backgroundColor);

				// and drop out of the loop
				break;
			}
		}
	}

	public SWTPlotHighlighter getCurrentPrimaryHighlighter()
	{
		return new SWTPlotHighlighter.RectangleHighlight();
	}

	public String toString()
	{
		return NAME;
	}

	public String getName()
	{
		return toString();
	}

	public boolean hasEditor()
	{
		return true;
	}

	/**
	 * NON-STANDARD implementation, we are returning the editor for our snail
	 * plotter object, not ourself
	 */
	public final Editable.EditorType getInfo()
	{
		return _mySnailPlotter.getInfo();
	}

}
