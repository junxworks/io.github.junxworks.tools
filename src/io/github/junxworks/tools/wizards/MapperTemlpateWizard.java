package io.github.junxworks.tools.wizards;

import io.github.junxworks.tools.JunxworksPlugin;
import io.github.junxworks.tools.actions.CreateMapperAction;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class MapperTemlpateWizard extends Wizard implements INewWizard {
	private MapperTemplatePage page;
	private ISelection selection;
	private IPreferenceStore store;

	public MapperTemlpateWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		page = new MapperTemplatePage(selection);
		addPage(page);
		store = JunxworksPlugin.getDefault().getPreferenceStore();
	}

	public boolean performFinish() {
		String template=page.text.getText();
		store.setValue(CreateMapperAction.MAPPER_CONFIG_ID, template);
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}