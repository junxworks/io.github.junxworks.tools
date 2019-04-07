package io.github.junxworks.tools.wizards;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.core.SourceField;
import org.eclipse.jdt.internal.core.SourceFieldElementInfo;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.ui.actions.ActionMessages;
import org.eclipse.jdt.internal.ui.dialogs.OpenTypeSelectionDialog;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;

import io.github.junxworks.tools.EntityModel;
import io.github.junxworks.tools.EntityModel.Column;
import io.github.junxworks.tools.StringUtil;
import swing2swt.layout.BorderLayout;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (java).
 */

@SuppressWarnings("restriction")
public class CreateObjectPage extends WizardPage {

	private static final String name = "name";

	private static final String type = "type";

	private static final String comment = "comment";

	private Text className;

	private Table table;

	private Set<TableItem> selected = new HashSet<>();

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public CreateObjectPage(ISelection selection) {
		super("wizardPage");
		setTitle("创建Pojo类");
		setDescription("注意： 类名为必填项，从前端到后端的数据传输对象为Dto对象，\r\n类名以Dto结尾。从后端到前端数据传输对象为Vo对象，类名以Vo结尾。");
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new BorderLayout(0, 0));

		Group group = new Group(container, SWT.NONE);
		group.setText("Pojo类属性");
		group.setLayout(new GridLayout(4, true));
		group.setLayoutData(BorderLayout.NORTH);

		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblNewLabel.setText("类名：");
		lblNewLabel.setAlignment(SWT.RIGHT);

		className = new Text(group, SWT.BORDER);
		className.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		className.setToolTipText("类名必填");

		Group group1 = new Group(container, SWT.NONE);
		group1.setText("Pojo类属性生成");
		group1.setLayoutData(BorderLayout.CENTER);
		group1.setLayout(new GridLayout(4, true));

		Button btnNewButton = new Button(group1, SWT.NONE);
		btnNewButton.setText("选择元数据类");
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		btnNewButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					OpenTypeSelectionDialog dialog = new OpenTypeSelectionDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), false, PlatformUI.getWorkbench().getProgressService(), SearchEngine.createWorkspaceScope(), IJavaSearchConstants.TYPE);
					dialog.setTitle(ActionMessages.OpenTypeInHierarchyAction_dialogTitle);
					dialog.setMessage(ActionMessages.OpenTypeInHierarchyAction_dialogMessage);
					int result = dialog.open();
					if (result == org.eclipse.jface.window.Window.OK) {
						SourceType type = (SourceType) dialog.getResult()[0];
						ICompilationUnit unit = (ICompilationUnit) type.getParent();
						fillTable(unit);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			/**
			 * 将java属性填充到table中
			 *
			 * @param unit the unit
			 * @throws CoreException 
			 */
			private void fillTable(ICompilationUnit unit) throws CoreException {
				SourceType type = (SourceType) unit.getAllTypes()[0];
				IField[] fs = type.getFields();
				for (IField f : fs) {
					fillColumn(f);
				}
			}

			public void fillColumn(IField f) throws JavaModelException {
				table.setItemCount(table.getItemCount() + 1);
				TableItem item = table.getItem(table.getItemCount() - 1);
				item.setText(1, f.getElementName());
				IAnnotation[] as = f.getAnnotations();
				if (as != null) {
					for (IAnnotation a : as) {
						IMemberValuePair[] vps = a.getMemberValuePairs();
						if (vps != null) {
							for (IMemberValuePair vp : vps) {
								String value = vp.getValue() == null ? "" : vp.getValue().toString();
								if (name.equalsIgnoreCase(vp.getMemberName())) {
									item.setText(2, value);
								}
								if (type.equalsIgnoreCase(vp.getMemberName())) {
									item.setText(3, value);
								}
								if (comment.equalsIgnoreCase(vp.getMemberName())) {
									item.setText(4, value);
								}
							}
						}
					}
				}
				item.setText(5, f.getTypeSignature());
				SourceField sf = (SourceField) f;
				item.setText(5, new String(((SourceFieldElementInfo) sf.getElementInfo()).getTypeName()));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		table = new Table(group1, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 70));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn checkBox = new TableColumn(table, SWT.NONE);
		checkBox.setWidth(30);
		checkBox.setAlignment(SWT.CENTER);
		checkBox.setResizable(false);

		TableColumn name = new TableColumn(table, SWT.NONE);
		name.setWidth(100);
		name.setText("属性名");
		name.setAlignment(SWT.CENTER);
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setWidth(100);
		column.setText("数据库字段名");
		column.setAlignment(SWT.CENTER);
		TableColumn type = new TableColumn(table, SWT.NONE);
		type.setWidth(100);
		type.setText("数据类型");
		type.setAlignment(SWT.CENTER);
		TableColumn comment = new TableColumn(table, SWT.NONE);
		comment.setWidth(150);
		comment.setText("字段描述");
		comment.setAlignment(SWT.CENTER);
		TableColumn javaType = new TableColumn(table, SWT.NONE);
		javaType.setWidth(150);
		javaType.setText("Java类型");
		javaType.setAlignment(SWT.CENTER);
		table.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				final TableItem tableItem = (TableItem) e.item;
				if (e.detail == SWT.CHECK) {
					if (!selected.contains(tableItem)) {
						selected.add(tableItem);
					} else {
						selected.remove(tableItem);
					}
				}
			}
		});
		setControl(container);
	}

	public boolean extractData(EntityModel entity) {

		String _className = className.getText();
		if (StringUtil.isNull(_className)) {
			this.setErrorMessage("实体类名不能为空");
			className.setFocus();
			return false;
		} else {
			if (!this.virfyServName(_className))
				return false;
		}
		if (!selected.isEmpty()) {
			TableItem[] items = selected.toArray(new TableItem[] {});
			Column colum;
			List<Column> columList = new ArrayList<Column>();
			for (TableItem item : items) {
				colum = entity.new Column();
				colum.setProperName(item.getText(1));
				colum.setColumnName(item.getText(2));
				colum.setColumnType(item.getText(3));
				colum.setMethod(colum.getProperName().substring(0, 1).toUpperCase() + colum.getProperName().substring(1));
				colum.setDesc(item.getText(4) == null ? "" : item.getText(4));
				String type = item.getText(5);
				if (type.contains("Date"))
					entity.setImportDate(true);
				if (type.contains("Timestamp"))
					entity.setImportTime(true);
				if (type.contains("BigDecimal"))
					entity.setIsimportBigsem(true);
				colum.setFieldType(type);
				columList.add(colum);
			}
			entity.setClassName(_className);
			entity.setColumnList(columList);
			return true;
		} else {
			this.setErrorMessage("请选择属性");
			className.setFocus();
			return false;
		}
	}

	private boolean virfyServName(String serviceName) {

		try {
			ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
			ISelection selection = selectionService.getSelection();
			Object element = ((IStructuredSelection) selection).getFirstElement();
			IJavaElement child[] = ((IPackageFragment) element).getChildren();
			for (IJavaElement e : child) {
				if (e.getElementType() == IJavaElement.COMPILATION_UNIT) {
					if ((serviceName + ".java").equals(e.getElementName())) {
						MessageDialog.openWarning(getShell(), "WARNING", "实体类名：" + serviceName + " 重复！");
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openWarning(getShell(), "WARNING", e.getMessage());
			return false;
		}
		return true;

	}
}