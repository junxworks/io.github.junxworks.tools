package io.github.junxworks.tools.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.internal.Workbench;

import io.github.junxworks.tools.JunxworksPlugin;
import io.github.junxworks.tools.WorkspaceUtils;
import io.github.junxworks.tools.utils.BeanCreatUtils;

@SuppressWarnings("restriction")
public class CreateMetadataWizard extends Wizard implements INewWizard {
	private MetadataPage page;
	private ISelection selection;
	private IPreferenceStore store;
	public CreateMetadataWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	public void addPages() {
		page = new MetadataPage(selection);
		addPage(page);
		store = JunxworksPlugin.getDefault().getPreferenceStore();
	}
	
	public boolean performFinish() {
		String packageName = WorkspaceUtils.getCurrentPackage();
		String path = WorkspaceUtils.getCurrentJavaDirectory().getAbsolutePath();
		try{
			TableItem[] tableItems = page.tableViewer.getTable().getItems();
			List<String> tableNames = new ArrayList<String>();
			for(int i=0;i<tableItems.length;i++){
				if(tableItems[i].getChecked()){
					tableNames.add(tableItems[i].getText(2));
				}
			}
			if(tableNames != null && tableNames.size()>0){
				boolean ret = BeanCreatUtils.creatBean(path,packageName,tableNames);
				if(ret){
					MessageDialog.openInformation(page.getShell(),"提示信息","生成实体成功");
					WorkspaceUtils.getCurrentResource().refreshLocal(IResource.DEPTH_ONE, null);
				}
			}else{
				MessageDialog.openWarning(page.getShell(),"提示信息","没有要生成实体的表");
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			MessageDialog.openError(page.getShell(),"提示信息",e.getMessage());
			return false;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		doFinish(map);
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
		/*try{
			String path = (String)map.get("path");
			String packageName = (String)map.get("packageName");
			
			TableItem[] tableItems = page.tableViewer.getTable().getItems();
			List<String> tableNames = new ArrayList<String>();
			for(int i=0;i<tableItems.length;i++){
				if(tableItems[i].getChecked()){
					tableNames.add(tableItems[i].getText(2));
				}
			}
			if(tableNames != null && tableNames.size()>0){
				boolean ret = BeanCreatAction.creatBean(path,packageName,tableNames);
				if(ret){
					MessageDialog.openInformation(page.getShell(),"提示信息","生成实体成功");
					WorkspaceUtils.getCurrentResource().refreshLocal(IResource.DEPTH_ONE, null);
				}
			}else{
				MessageDialog.openWarning(page.getShell(),"提示信息","没有要生成实体的表");
			}
		}catch(Exception e){
			e.printStackTrace();
			MessageDialog.openError(page.getShell(),"提示信息",e.getMessage());
		}*/
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}