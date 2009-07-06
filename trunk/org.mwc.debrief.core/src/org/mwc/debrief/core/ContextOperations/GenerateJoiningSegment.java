/**
 * 
 */
package org.mwc.debrief.core.ContextOperations;

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

import Debrief.Wrappers.TrackWrapper;
import Debrief.Wrappers.Track.TrackSegment;
import MWC.GUI.Editable;
import MWC.GUI.Layer;
import MWC.GUI.Layers;

/**
 * @author ian.mayo
 */
public class GenerateJoiningSegment implements RightClickContextItemGenerator
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
		int validItems = 0;
		
		// we're only going to work with two or more items
		if (subjects.length > 1)
		{
			// track the parents
			Layer firstParent = parentLayers[0];
			
			// are they tracks, or track segments
			for (int i = 0; i < subjects.length; i++)
			{
				boolean goForIt = false;
				Editable thisE = subjects[i];
				if (thisE instanceof TrackSegment)
				{
					goForIt = true;
				}
				
				// track the parent layer
				if(parentLayers[i] != firstParent)
				{
					goForIt = false;
				}
				
				if(goForIt)
				{
					validItems++;
				}
				else
				{
					CorePlugin.logError(Status.INFO, "Not allowing join, there's a non-compliant entry", null);
					// may as well drop out - this item wasn't compliant
					continue;
				}
			}
		}

		// ok, is it worth going for?
		if (validItems >= 2)
		{
			final String title = "Generate joining segment";
			// create this operation
			Action doMerge = new Action(title){
				public void run()
				{
					IUndoableOperation theAction = new JoinTracksOperation(title, subjects, theLayers, parentLayers, subjects);
						
					CorePlugin.run(theAction );
				}};
			parent.add(doMerge);
		}
	}

	private static class JoinTracksOperation extends CMAPOperation
	{

		/**
		 * the parent to update on completion
		 */
		private final Layers _layers;
		private final Layer[] _parents;
		private final Editable[] _subjects;
		private Editable[] _target;


		public JoinTracksOperation(String title, Editable[] subjects2, Layers theLayers, Layer[] parentLayers,
				Editable[] subjects)
		{
			super(title);
			_target = subjects2;
			_layers = theLayers;
			_parents = parentLayers;
			_subjects = subjects;
		}

		public IStatus execute(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException
		{
//			int res = TrackWrapper.mergeTracks(_target, _layers, _parents, _subjects);
//			if(res == IStatus.OK)
//				fireModified();
			return new Status(IStatus.OK, null, "Merge successful", null);
		}

		
		
		@Override
		public boolean canRedo()
		{
			return false;
		}

		@Override
		public boolean canUndo()
		{
			return false;
		}
		
		private void fireModified()
		{
			_layers.fireExtended();
		}

		@Override
		public IStatus undo(IProgressMonitor monitor, IAdaptable info)
				throws ExecutionException
		{
			CorePlugin.logError(Status.INFO, "Undo not permitted for merge operation", null);
			return null;
		}
	}
}
