package io.github.junxworks.tools.wizards;

import io.github.junxworks.tools.WorkspaceUtils;
import io.github.junxworks.tools.actions.CreateMapperAction;
import io.github.junxworks.tools.utils.BeanCreatUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class CreateMapperWizard extends Wizard implements INewWizard {
	private MapperPage page;

	private ISelection selection;

	public CreateMapperWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		page = new MapperPage(selection);
		addPage(page);
	}

	public boolean performFinish() {
		String packageName = WorkspaceUtils.getCurrentPackage();
		String path = WorkspaceUtils.getCurrentJavaDirectory().getAbsolutePath();
		try {
			String fileName = page.fileName.getText();
			if (fileName == null || fileName.trim().length() == 0) {
				MessageDialog.openWarning(page.getShell(), "提示信息", "请输入生成文件名");
				return false;
			}
			TableItem[] tableItems = page.tableViewer.getTable().getItems();
			List<String> tableNames = new ArrayList<String>();
			for(int i=0;i<tableItems.length;i++){
				if(tableItems[i].getChecked()){
					tableNames.add(tableItems[i].getText(2));
				}
			}
			if (tableNames.size() > 1 || tableNames.size() == 0) {
				MessageDialog.openWarning(page.getShell(), "提示信息", "请选择1个表生成Mapper");
				return false;
			}
			String tableName = tableNames.get(0);
			boolean ret = BeanCreatUtils.creatBean(path, fileName, CreateMapperAction.MAPPER_CONFIG_ID, packageName, tableName);
			if (ret) {
				MessageDialog.openInformation(page.getShell(), "提示信息", "生成实体成功");
				WorkspaceUtils.getCurrentResource().refreshLocal(IResource.DEPTH_ONE, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openError(page.getShell(), "提示信息", e.getMessage());
			return false;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		doFinish(map);
		return true;
	}

	@SuppressWarnings("deprecation")
	private void doFinish(Map<String, Object> map) {
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