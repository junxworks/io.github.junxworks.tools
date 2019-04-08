package io.github.junxworks.tools.actions;

import io.github.junxworks.tools.JunxworksPlugin;
import io.github.junxworks.tools.wizards.CreateMapperWizard;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class CreateMapperAction implements IObjectActionDelegate {

	public static final String MAPPER_CONFIG_ID = "global_mapper_template";

	public CreateMapperAction() {
		super();
	}

	public void run(IAction action) {
		String template = JunxworksPlugin.getDefault().getPreferenceStore().getString(MAPPER_CONFIG_ID);
		if (StringUtils.isBlank(template)) {
			MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Junx", "请先配置Mapper模板");
		} else {
			CreateMapperWizard wizard = new CreateMapperWizard();
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
