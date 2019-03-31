package io.github.junxworks.tools.wizards;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import io.github.junxworks.tools.JunxworksPlugin;
import io.github.junxworks.tools.actions.CreateMetadataAction;

public class MetadataTemlpateWizard extends Wizard implements INewWizard {
	private MetadataTemplatePage page;
	private ISelection selection;
	private IPreferenceStore store;

	public MetadataTemlpateWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		page = new MetadataTemplatePage(selection);
		addPage(page);
		store = JunxworksPlugin.getDefault().getPreferenceStore();
	}

	public boolean performFinish() {
		String template=page.text.getText();
		store.setValue(CreateMetadataAction.METADATA_CONFIG_ID, template);
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}