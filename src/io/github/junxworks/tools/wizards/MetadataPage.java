package io.github.junxworks.tools.wizards;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.SWTResourceManager;

import io.github.junxworks.tools.TableModel;
import io.github.junxworks.tools.utils.BeanCreatUtils;
import swing2swt.layout.BorderLayout;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (java).
 */

public class MetadataPage extends WizardPage {
	private Text text;
	private Table table;
	protected TableViewer tableViewer;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public MetadataPage(ISelection selection) {
		super("wizardPage");
		setTitle("生成Pojo类");
		setDescription("根据选中的表，生成Pojo实体对象");
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new BorderLayout(0, 0));

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(BorderLayout.NORTH);

		Label lblNewLabel = new Label(composite, SWT.CENTER);
		lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(5, 24, 100, 25);
		lblNewLabel.setText("请选择数据表：");

		text = new Text(composite, SWT.BORDER);
		text.setBounds(111, 19, 311, 25);

		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setInput(new DaoTableModel(text.getText()));
			}
		});
		button.setBounds(431, 17, 80, 27);
		button.setText("查询");

		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.CENTER);

		tableViewer = new TableViewer(composite_1, SWT.CHECK | SWT.MULTI);
		table = tableViewer.getTable();
		table.setLinesVisible(true);

		table.addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				return;
			}
		});

		table.setHeaderVisible(true);
		table.setBounds(5, 24, 535, 239);

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_2 = tableViewerColumn_2.getColumn();
		tblclmnNewColumn_2.setResizable(false);
		tblclmnNewColumn_2.setAlignment(SWT.CENTER);
		tblclmnNewColumn_2.setWidth(31);
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(40);
		tableColumn.setText("序号");

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
		tblclmnNewColumn.setAlignment(SWT.CENTER);
		tblclmnNewColumn.setWidth(217);
		tblclmnNewColumn.setText("表名");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_1 = tableViewerColumn_1.getColumn();
		tblclmnNewColumn_1.setAlignment(SWT.CENTER);
		tblclmnNewColumn_1.setWidth(241);
		tblclmnNewColumn_1.setText("描述");
		tableViewer.setLabelProvider(new TvLabelProvider());
		tableViewer.setContentProvider(new TvContentProvider());
		//

		// filte = new TableFilter(tableViewer);
		// text.addKeyListener(new KeyAdapter() {
		// @Override
		// public void keyReleased(KeyEvent e) {
		// if(tableViewer.getInput() == null)
		// tableViewer.setInput(new DaoTableModel());
		// String value = text.getText();
		// filte.setMatch(value);
		// }
		// });
	}
}

class TvLabelProvider implements ITableLabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		TableModel table = (TableModel) element;
		switch (columnIndex) {
		case 1:
			return table.getRowIndex().toString();
		case 2:
			return table.getTableName();
		case 3:
			return table.getDescription();
		}
		return null;
	}
}

class TvContentProvider implements IStructuredContentProvider {
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ((DaoTableModel) inputElement).getModels();
	}
}

class DaoTableModel {

	private String tableName;

	public DaoTableModel(String tableName) {
		this.tableName = tableName;
	}

	public TableModel[] getModels() {
		try {
			List<io.github.junxworks.tools.pojo.db.model.Table> tableList = BeanCreatUtils.getAllTableName(this.tableName);
			if (tableList != null && tableList.size() > 0) {
				TableModel[] rtn = new TableModel[tableList.size()];
				for (int i = 0; i < tableList.size(); i++) {
					rtn[i] = new TableModel((Integer) (i + 1), tableList.get(i).getTableName(),
							tableList.get(i).getTableComment());
				}
				return rtn;
			}
		} catch (Exception ex) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "提示信息",
					ex.getMessage());
			ex.printStackTrace();
		}
		return new TableModel[0];
	}
}