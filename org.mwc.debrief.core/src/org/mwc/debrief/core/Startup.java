package org.mwc.debrief.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.dynamichelpers.IExtensionChangeHandler;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

public class Startup implements IStartup
{

	@Override
	public void earlyStartup()
	{
		removePerspective();
		removePreferencePages();
	}

	private void removePreferencePages()
	{
		PreferenceManager preferenceManager = PlatformUI.getWorkbench()
				.getPreferenceManager();
		if (preferenceManager == null) {
			return;
		}
		preferenceManager.remove("org.eclipse.debug.ui.DebugPreferencePage");
		preferenceManager.remove("org.eclipse.debug.ui.LaunchingPreferencePage");
		preferenceManager
				.remove("org.eclipse.debug.ui.ViewManagementPreferencePage");
		preferenceManager.remove("org.eclipse.debug.ui.ConsolePreferencePage");
		preferenceManager
				.remove("org.eclipse.debug.ui.StringVariablePreferencePage");
		preferenceManager.remove("org.eclipse.debug.ui.PerspectivePreferencePage");
		preferenceManager.remove("org.eclipse.debug.ui.LaunchConfigurations");
		preferenceManager.remove("org.eclipse.debug.ui.LaunchDelegatesPreferencePage");
		preferenceManager.remove("org.eclipse.team.ui.TeamPreferences");

	}

	private void removePerspective()
	{
		IPerspectiveRegistry registry = PlatformUI.getWorkbench().getPerspectiveRegistry();
		if (registry == null)
		{
			return;
		}
		List<IPerspectiveDescriptor> descriptors = new ArrayList<IPerspectiveDescriptor>();

		addDescriptor(registry, descriptors, "org.eclipse.debug.ui.DebugPerspective");
		addDescriptor(registry, descriptors, "org.eclipse.team.ui.TeamSynchronizingPerspective");

		// FIXME this method doesn't work on Eclipse E4 (Juno/Kepler)
		if (registry instanceof IExtensionChangeHandler && !descriptors.isEmpty())
		{
			IExtensionChangeHandler handler = (IExtensionChangeHandler) registry;
			handler.removeExtension(null, descriptors.toArray());
		}
	}

	private void addDescriptor(IPerspectiveRegistry registry,
			List<IPerspectiveDescriptor> descriptors, String id)
	{
		IPerspectiveDescriptor perspectiveDescriptor = registry.findPerspectiveWithId(id);
		if (perspectiveDescriptor != null)
		{
			descriptors.add(perspectiveDescriptor);
		}
	}

}