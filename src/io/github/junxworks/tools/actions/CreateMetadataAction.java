package io.github.junxworks.tools.actions;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import io.github.junxworks.tools.JunxworksPlugin;
import io.github.junxworks.tools.pojo.db.utils.DbUtil;
import io.github.junxworks.tools.wizards.CreateMetadataWizard;

public class CreateMetadataAction implements IObjectActionDelegate {

	public static final String METADATA_CONFIG_ID = "global_metadata_template";

	public CreateMetadataAction() {
		super();
	}

	public void run(IAction action) {
//		IPreferenceStore store = BDPToolsPlugin.getDefault().getPreferenceStore();
//		String dbTypeVal = store.getString("dbType");
//		String urlVal = store.getString("url");
//		String userVal = store.getString("user");
//		String pwdVal = store.getString("password");
//		try{
//			DbUtil.isConnection(dbTypeVal,urlVal,userVal,pwdVal);
//		}catch(Exception e){
//			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
//					"提示信息",e.getMessage());
//			e.printStackTrace();
//			return;
//		}
		String template = JunxworksPlugin.getDefault().getPreferenceStore().getString(METADATA_CONFIG_ID);
		if (StringUtils.isBlank(template)) {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Junx", "Please config Metadata template first");
		} else {
			CreateMetadataWizard wizard = new CreateMetadataWizard();
			wizard.init(PlatformUI.getWorkbench(), null);
			WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					wizard);
			dialog.open();
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		//
	}

}
