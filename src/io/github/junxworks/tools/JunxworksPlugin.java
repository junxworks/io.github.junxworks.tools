package io.github.junxworks.tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class JunxworksPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "io.github.junxworks.tools"; //$NON-NLS-1$

	// The shared instance
	private static JunxworksPlugin plugin;

	/**
	 * The constructor
	 */
	public JunxworksPlugin() {
	}

	public static void log(String msg) {
		ILog log = getDefault().getLog();
		Status status = new Status(4, getDefault().getDescriptor().getUniqueIdentifier(), 4, msg + "\n", null);
		log.log(status);
	}

	public static void log(Exception e) {
		ILog log = getDefault().getLog();
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		String msg = stringWriter.getBuffer().toString();

		Status status = new Status(4, getDefault().getDescriptor().getUniqueIdentifier(), 4, msg, null);
		log.log(status);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static JunxworksPlugin getDefault() {
		return plugin;
	}

	public List<IProject> getProjectsInSourcePath() {
		return WorkspaceUtils.getProjectsInWorkspace();
	}

	public static Shell getShell() {
		return plugin.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	public static IWorkbenchPage getWorkbenchPage() {
		return getWorkbenchWindow().getActivePage();
	}

	public static IWorkbenchWindow getWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

}
