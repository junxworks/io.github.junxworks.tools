package io.github.junxworks.tools.wizards;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import io.github.junxworks.tools.JunxworksPlugin;
import io.github.junxworks.tools.actions.CreateMapperAction;
import io.github.junxworks.tools.actions.CreateMetadataAction;
import swing2swt.layout.BorderLayout;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (java).
 */

public class MapperTemplatePage extends WizardPage {
	Text text;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public MapperTemplatePage(ISelection selection) {
		super("wizardPage");
		setTitle("Junx Mapper模板");
		setDescription("配置Mapper生成模板");
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		String template = JunxworksPlugin.getDefault().getPreferenceStore().getString(CreateMapperAction.MAPPER_CONFIG_ID);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(BorderLayout.NORTH);
		text = new Text(composite, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		text.setBounds(0, 0, 800, 400);
		text.setText(template);
		setControl(composite);
	}
}