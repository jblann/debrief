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
package org.mwc.debrief.gndmanager;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.mwc.cmap.core.CorePlugin;
import org.mwc.cmap.core.operations.CMAPOperation;
import org.mwc.cmap.core.property_support.RightClickSupport.RightClickContextItemGenerator;
import org.mwc.debrief.gndmanager.Tracks.TrackStoreWrapper;
import org.mwc.debrief.gndmanager.Tracks.TrackStoreWrapper.CouchTrack;

import Debrief.Wrappers.TrackWrapper;
import MWC.GUI.Editable;
import MWC.GUI.Layer;
import MWC.GUI.Layers;

/**
 * @author ian.mayo
 * 
 */
public class ConvertToDebriefTrack implements RightClickContextItemGenerator
{

	/**
	 * @param parent
	 * @param theLayers
	 * @param parentLayers
	 * @param subjects
	 */
	public void generate(final IMenuManager parent, final Layers theLayers,
			final Layer[] parentLayers, final Editable[] subjects)
	{
		int layersValidForConvertToTrack = 0;

		// right, work through the subjects
		for (int i = 0; i < subjects.length; i++)
		{
			final Editable thisE = subjects[i];
			if (thisE instanceof CouchTrack)
			{
				// cool, go for it!
				layersValidForConvertToTrack++;
			}
		}

		// ok, is it worth going for?
		if (layersValidForConvertToTrack > 0)
		{
			final String title;
			if (layersValidForConvertToTrack > 1)
				title = "Convert to Debrief tracks";
			else
				title = "Convert to Debrief track";

			// sort out the couch store
			final TrackStoreWrapper store = (TrackStoreWrapper) parentLayers[0];
			
			// yes, create the action
			final Action convertToTrack = new Action(title)
			{
				public void run()
				{
					// ok, go for it.
					// sort it out as an operation
					final IUndoableOperation convertToTrack1 = new ConvertTrack(title,
							theLayers, subjects, store);

					// ok, stick it on the buffer
					runIt(convertToTrack1);
				}
			};

			// right,stick in a separator
			parent.add(new Separator());

			// ok - flash up the menu item
			parent.add(convertToTrack);
		}

	}

	/**
	 * put the operation firer onto the undo history. We've refactored this into a
	 * separate method so testing classes don't have to simulate the CorePlugin
	 * 
	 * @param operation
	 */
	protected void runIt(final IUndoableOperation operation)
	{
		CorePlugin.run(operation);
	}

	private static class ConvertTrack extends CMAPOperation
	{

		private final Layers _layers;
		private final Editable[] _subjects;

		private Vector<TrackWrapper> _newTracks;
		private final TrackStoreWrapper _store;

		public ConvertTrack(final String title, final Layers layers, final Editable[] subjects, final TrackStoreWrapper parentLayer)
		{
			super(title);
			_layers = layers;
			_subjects = subjects;
			_store = parentLayer;
		}

		public IStatus execute(final IProgressMonitor monitor, final IAdaptable info)
				throws ExecutionException
		{
			// right, get going through the track
			for (int i = 0; i < _subjects.length; i++)
			{
				final Editable thisE = _subjects[i];
				if (thisE instanceof CouchTrack)
				{
					final CouchTrack track = (CouchTrack) thisE;
					final TrackWrapper tw = track.toDebriefTrack();
					if (tw != null)
					{

						_layers.addThisLayer(tw);
						
						// also, remove it from the cache
						_store.removeElement(track);
					
						// and remember it, for the undo operation
						if (_newTracks == null)
							_newTracks = new Vector<TrackWrapper>(0, 1);

						_newTracks.add(tw);
					}
				}
			}

			// sorted, do the update
			_layers.fireExtended();

			return Status.OK_STATUS;
		}

		public IStatus undo(final IProgressMonitor monitor, final IAdaptable info)
				throws ExecutionException
		{
			// forget about the new tracks
			for (final Iterator<TrackWrapper> iter = _newTracks.iterator(); iter.hasNext();)
			{
				final TrackWrapper trk = (TrackWrapper) iter.next();
				_layers.removeThisLayer(trk);
			}

			// and put the subjects back into the couch store
			for (int i = 0; i < _subjects.length; i++)
			{
				final CouchTrack thisT = (CouchTrack) _subjects[i];
				_store.add(thisT);
			}
			
			// and clear the new tracks item
			_newTracks.removeAllElements();
			_newTracks = null;

			return Status.OK_STATUS;
		}

	}

}
