package io.github.junxworks.tools.wizards;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import io.github.junxworks.tools.JunxworksPlugin;
import io.github.junxworks.tools.ServiceModel;
import io.github.junxworks.tools.WorkspaceUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.osgi.framework.Bundle;

@SuppressWarnings("restriction")
public class AddServiceWizard extends Wizard implements INewWizard {
	private ServiceConfWizardPage page;
	private ISelection selection;

	public AddServiceWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	public void addPages() {
		page = new ServiceConfWizardPage(selection);
		addPage(page);
	}
	
	public boolean performFinish() {
		ServiceModel service = new ServiceModel();
		if(!page.getData(service))
			return false;
		String packageName = WorkspaceUtils.getCurrentPackage();
	    service.setPackageName(packageName);
	    service.setAuthor(System.getProperty("user.name"));
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("className", service.getClassName());
		map.put("packName", service.getPackageName());
		map.put("pool", service.getPool());
		map.put("transLevel", service.getTranscationLevel());
		map.put("trans", service.getTranscation());
		map.put("write", service.getWriteTool());
		map.put("read", service.getReadTool());
		map.put("author", service.getAuthor());
		map.put("comp", service.getCompList());
		try{
			//创建java服务文件
			doFinish(map);
			//刷新资源类
			WorkspaceUtils.getCurrentResource().refreshLocal(IResource.DEPTH_ONE, null);
			//获取java服务资源
			final IResource resource = this.getResource(map.get("className").toString());
			((IFile)resource).setPersistentProperty(IDE.EDITOR_KEY, "io.github.junxworks.editors.ServiceEditor");
			//选中新增的服务
			BasicNewResourceWizard.selectAndReveal(resource, Workbench.getInstance().getActiveWorkbenchWindow());
			//在编辑器中打新增服务
			Display display = getShell().getDisplay();
		      if (display != null){
		    	  final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		    	  display.asyncExec(new Runnable() {
			          public void run() {
			        	  try {
							IDE.openEditor(activePage, (IFile)resource);
						} catch (PartInitException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			          }
			        });
		      }
			 
			
		}catch(Exception e){
			MessageDialog.openError(getShell(), "错误提示", e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private IResource getResource(String className){
		ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
		ISelection selection = selectionService.getSelection();
		IResource resource = null;
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (element instanceof IPackageFragment) {
				resource = ((IPackageFragment) element).getCompilationUnit(className + ".java").getResource();
			}
		}
		return resource;
	}
 	@SuppressWarnings("deprecation")
	private void doFinish(Map<String,Object> map){
		try{
			String path = WorkspaceUtils.getCurrentJavaDirectory().getAbsolutePath();
			File javaFile = new File(path + "/" + map.get("className") + ".java");
			if(javaFile.exists())
				javaFile.delete();
			javaFile.createNewFile();
		
			Bundle bundle = Platform.getBundle(JunxworksPlugin.PLUGIN_ID);//从Bundle来查找资源：
			URL pluginUrl = Platform.find(bundle, new org.eclipse.core.runtime.Path("template"));
			URL pluginFileUrl = Platform.asLocalURL(pluginUrl);
			org.eclipse.core.runtime.Path x = new org.eclipse.core.runtime.Path(pluginFileUrl.getPath()); 
			String curPath = x.toString();
			Configuration cfg = new Configuration(); 
            cfg.setDirectoryForTemplateLoading(new File(curPath));
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            Template template = cfg.getTemplate("service.ftl");
            Writer javaWriter = new FileWriter(javaFile);
            template.process(map, javaWriter);
            javaWriter.flush();
            javaWriter.close();
           
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}