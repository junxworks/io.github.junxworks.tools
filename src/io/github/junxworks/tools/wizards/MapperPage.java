package io.github.junxworks.tools.wizards;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import swing2swt.layout.BorderLayout;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (java).
 */

public class MapperPage extends WizardPage {
	Text fileName;
	private Text text;
	private Table table;
	protected TableViewer tableViewer;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public MapperPage(ISelection selection) {
		super("wizardPage");
		setTitle("生成Mapper");
		setDescription("根据选中的表，生成Mapper，自定义Mapper模板");
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
		
		Label l1 = new Label(composite, SWT.CENTER);
		l1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		l1.setAlignment(SWT.RIGHT);
		l1.setBounds(5, 19, 100, 25);
		l1.setText("生成文件名：");

		fileName = new Text(composite, SWT.BORDER);
		fileName.setBounds(111, 19, 311, 25);
		fileName.setMessage("例如xxxMapper.java or xxxMapper.xml");
		
		Label lblNewLabel = new Label(composite, SWT.CENTER);
		lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(5, 50, 100, 25);
		lblNewLabel.setText("请选择数据表：");

		text = new Text(composite, SWT.BORDER);
		text.setBounds(111, 50, 311, 25);

		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tableViewer.setInput(new DaoTableModel(text.getText()));
			}
		});
		button.setBounds(431, 50, 80, 27);
		button.setText("查询");

		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.CENTER);

		tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.CHECK | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		table = tableViewer.getTable();
		table.setLinesVisible(true);

		table.addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				return;
			}
		});

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setBounds(5, 55, 535, 239);

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
	}
}