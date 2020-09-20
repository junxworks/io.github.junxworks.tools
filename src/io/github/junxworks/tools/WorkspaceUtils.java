package io.github.junxworks.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.internal.Workbench;

public class WorkspaceUtils {

	private static Map<String,Properties> config=new HashMap<>();
	
	/**
	 * 将工作空间中所有的Java项目返回
	 * @return
	 */
	public static List<IProject> getProjectsInWorkspace() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] allProjects = root.getProjects();
		List<IProject> res = new ArrayList<>();
		for (IProject p : allProjects) {
			if (p.isOpen() && p.isAccessible()) {
				try {
					if (p.hasNature("org.eclipse.jdt.core.javanature")) {
						res.add(p);
					}
				} catch (CoreException e) {
					JunxworksPlugin.log(e);
				}
			}
		}
		return res;
	}

	/**
	 * 获取当前选中的工程
	 * @return
	 */
	public static IProject getCurrentProject() {
		ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();

		ISelection selection = selectionService.getSelection();

		IProject project = null;
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();

			if (element instanceof IResource) {
				project = ((IResource) element).getProject();
			} else if (element instanceof PackageFragmentRootContainer) {
				IJavaProject jProject = ((PackageFragmentRootContainer) element).getJavaProject();
				project = jProject.getProject();
			} else if (element instanceof IJavaElement) {
				IJavaProject jProject = ((IJavaElement) element).getJavaProject();
				project = jProject.getProject();
			}
		}
		return project;
	}

	public static String currentProjectDir() {
		return getCurrentProject().getLocation().toString();
	}

	public static synchronized Properties getProjectConfig() {
		String dir= currentProjectDir();
		if(config.get(dir)!=null) {
			return config.get(dir);
		}
		try {
			File configF = new File(dir + File.separator + ".junxworks");
			if (!configF.exists()) {
				configF.createNewFile();
			}
			try (FileInputStream fi = new FileInputStream(configF)) {
				Properties p = new Properties();
				p.load(fi);
				config.put(dir, p);
				return p;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void saveProjectConfig() {
		String dir= currentProjectDir();
		try {
			File configF = new File(dir + File.separator + ".junxworks");
			if (!configF.exists()) {
				configF.createNewFile();
			}
			try (FileOutputStream fo = new FileOutputStream(configF)) {
				config.get(dir).save(fo, "");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前选择的路径位置
	 * @return
	 */
	public static String getCurrentPackage() {
		ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
		ISelection selection = selectionService.getSelection();
		String path = null;
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (element instanceof IJavaElement) {
				path = ((IJavaElement) element).getElementName();
			}
		}
		return path;
	}

	/**
	 * 获取当前选择的路径位置
	 * @return
	 */
	public static File getCurrentJavaDirectory() {
		ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
		ISelection selection = selectionService.getSelection();
		File path = null;
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (element instanceof IJavaElement) {
				path = ((IJavaElement) element).getResource().getLocation().toFile();
			}
		}
		return path;
	}

	public static IResource getCurrentResource() {
		ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
		ISelection selection = selectionService.getSelection();
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (element instanceof IJavaElement) {
				return ((IJavaElement) element).getResource();
			}
		}
		return null;
	}
}
