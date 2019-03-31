package io.github.junxworks.tools.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.internal.Workbench;

import io.github.junxworks.tools.EntityModel;
import io.github.junxworks.tools.StringUtil;
import io.github.junxworks.tools.EntityModel.Column;
import swing2swt.layout.BorderLayout;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (java).
 */

@SuppressWarnings("restriction")
public class PojoPage extends WizardPage {

	private Text text_1;
	private Table table;
	private Combo combo;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public PojoPage(ISelection selection) {
		super("wizardPage");
		setTitle("创建实体类");
		setDescription("编辑实体类\r\n注意：实体类名,数据元为必填项！！");
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new BorderLayout(0, 0));
		
		Group group = new Group(container, SWT.NONE);
		group.setText("实体属性");
		group.setLayout(new GridLayout(8, true));
		group.setLayoutData(BorderLayout.NORTH);

		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblNewLabel.setText("实体类名：");
		lblNewLabel.setAlignment(SWT.RIGHT);
		
		text_1 = new Text(group, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		text_1.setToolTipText("实体类名必填");
		
		Label lblNewLabel2 = new Label(group, SWT.NONE);
		lblNewLabel2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblNewLabel2.setText("数据元：");
		lblNewLabel2.setAlignment(SWT.RIGHT);
		
		combo = new Combo(group, SWT.READ_ONLY | SWT.ARROW_DOWN);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		combo.setItems(new String[]{"MONGODB","OTHER"});
		combo.select(1);

		Group group1 = new Group(container, SWT.NONE);
		group1.setText("实体类成员");
		group1.setLayoutData(BorderLayout.CENTER);
		group1.setLayout(new GridLayout(8, true));

		Button btnNewButton_3 = new Button(group1, SWT.NONE);
		btnNewButton_3.setVisible(false);
		Button btnNewButton_2 = new Button(group1, SWT.NONE);
		btnNewButton_2.setVisible(false);
		Button btnNewButton_4 = new Button(group1, SWT.NONE);
		btnNewButton_4.setVisible(false);
		Button btnNewButton_5 = new Button(group1, SWT.NONE);
		btnNewButton_5.setVisible(false);
		Button btnNewButton_6 = new Button(group1, SWT.NONE);
		btnNewButton_6.setVisible(false);
		Button btnNewButton_7 = new Button(group1, SWT.NONE);
		btnNewButton_7.setVisible(false);

		Button btnNewButton = new Button(group1, SWT.NONE);
		btnNewButton.setText("添加");
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		btnNewButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				table.setItemCount(table.getItemCount() + 1); 
				TableItem item = table.getItem(table.getItemCount()-1);
				item.setText(3, "false");
				item.setText(4, "false");
				item.setText(5, "true");
				item.setText(6, "false");
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

		});

		Button btnNewButton_1 = new Button(group1, SWT.NONE);
		btnNewButton_1.setText("删除");
		btnNewButton_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		btnNewButton_1.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				TableItem[] items = table.getItems();
				for (TableItem item : items) {
					if (item.getChecked())
						item.dispose();

				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

		});

		table = new Table(group1, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION | SWT.MULTI);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,8,70));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(30);
		tblclmnNewColumn.setAlignment(SWT.CENTER);
		tblclmnNewColumn.setResizable(false);

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("属性名");
		tblclmnNewColumn_1.setAlignment(SWT.CENTER);
		TableColumn tblclmnNewColumn_7 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_7.setWidth(100);
		tblclmnNewColumn_7.setText("数据类型");
		tblclmnNewColumn_7.setAlignment(SWT.CENTER);
		TableColumn tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(100);
		tblclmnNewColumn_2.setText("是否为主键");
		tblclmnNewColumn_2.setAlignment(SWT.CENTER);
		TableColumn tblclmnNewColumn_3 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_3.setWidth(80);
		tblclmnNewColumn_3.setText("是否唯一");
		tblclmnNewColumn_3.setAlignment(SWT.CENTER);
		TableColumn tblclmnNewColumn_4 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_4.setWidth(100);
		tblclmnNewColumn_4.setText("是否可为空");
		tblclmnNewColumn_4.setAlignment(SWT.CENTER);
		TableColumn tblclmnNewColumn_8 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_8.setWidth(100);
		tblclmnNewColumn_8.setText("是否自动生成");
		tblclmnNewColumn_8.setAlignment(SWT.CENTER);
		TableColumn tblclmnNewColumn_5 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_5.setWidth(60);
		tblclmnNewColumn_5.setText("长度");
		tblclmnNewColumn_5.setAlignment(SWT.CENTER);
		TableColumn tblclmnNewColumn_6 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_6.setWidth(60);
		tblclmnNewColumn_6.setText("精确度");
		tblclmnNewColumn_6.setAlignment(SWT.CENTER);
//		TableColumn tblclmnNewColumn_9 = new TableColumn(table, SWT.NONE);
//		tblclmnNewColumn_9.setWidth(150);
//		tblclmnNewColumn_9.setText("描述信息");
//		tblclmnNewColumn_9.setAlignment(SWT.CENTER);
		
		table.addListener(SWT.MouseUp, new Listener() {
			int editColumnIndex = -1;

			@Override
			public void handleEvent(Event e) {

				final Point point = new Point(e.x, e.y);
				final TableItem tableItem = table.getItem(point);
				if (tableItem == null)
					return;
				final TableEditor editor = new TableEditor(table);
				final Control oldEditor = editor.getEditor();
				if (oldEditor != null) {
					oldEditor.dispose();
				}
				Rectangle r;
				for (int i = 0; i < 9; i++) {
					
					r = tableItem.getBounds(i);
					if (r.contains(point)) {
						editColumnIndex = i;
						if(i == 2 || i == 3 || i == 4 || i == 5 || i== 6){
							final Combo combo = new Combo(table, SWT.READ_ONLY | SWT.ARROW_DOWN);
							combo.computeSize(SWT.DEFAULT, table.getItemHeight());
							String iterms[];
							if(i == 2){
								iterms = new String[]{ "String", "Integer","Long","Float","Double","BigDecimal","Boolean","Date","Timestamp" };
								
							}else{
								iterms = new String[] { "true", "false" };
							}
							combo.setItems(iterms);
							editor.grabHorizontal = true;
							editor.minimumHeight = combo.getSize().y;
							editor.minimumWidth = combo.getSize().x;
							editor.setEditor(combo, tableItem, editColumnIndex);
							combo.setText(tableItem.getText(editColumnIndex));
							combo.forceFocus();
							combo.setListVisible(true);
							combo.addFocusListener(new FocusListener() {
								@Override
								public void focusGained(final FocusEvent e) {
									// TODO Auto-generated method stub
								}

								@Override
								public void focusLost(final FocusEvent e) {
									combo.dispose();
								}
							});
							combo.addModifyListener(new ModifyListener() {
								@Override
								public void modifyText(final ModifyEvent e) {
									editor.getItem().setText(editColumnIndex, combo.getText());
								}
							});
							return;
						}
						final Text text = new Text(table, SWT.NONE);
						text.computeSize(SWT.DEFAULT, table.getItemHeight());
						editor.grabHorizontal = true;
						editor.minimumHeight = text.getSize().y;
						editor.minimumWidth = text.getSize().x;
						editor.setEditor(text, tableItem, editColumnIndex);
						text.setText(tableItem.getText(editColumnIndex));
						text.forceFocus();
						text.addModifyListener(new ModifyListener() {
							@Override
							public void modifyText(final ModifyEvent e) {
								editor.getItem().setText(editColumnIndex, text.getText());
							}
						});
						text.addFocusListener(new FocusListener() {
							@Override
							public void focusGained(final FocusEvent e) {
								// TODO Auto-generated method stub
							}

							@Override
							public void focusLost(final FocusEvent e) {
								text.dispose();
							}
						});
						break;
					}
				}

			}
		});
		setControl(container);
	}

	public boolean getData(EntityModel entity){
		
		String className = text_1.getText();
		if(StringUtil.isNull(className)){
			this.setErrorMessage("实体类名不能为空");
			text_1.setFocus();
			return false;
		}else{
			if(!this.virfyServName(className))
				return false;
		}
		
		if(table.getItemCount() > 0){
			
			TableItem[] items = table.getItems();
			Column colum;
			List<Column> columList = new ArrayList<Column>();
			
			for (TableItem item : items) {
				if(StringUtil.isNull(item.getText(1))){
					this.setErrorMessage("实体属性名不能为空");
					return false;
				}
				if(StringUtil.isNull(item.getText(2))){
					this.setErrorMessage("实体属性数据类型不能为空");
					return false;
				}
				if(StringUtil.isNull(item.getText(7)) && !item.getText(2).equals("Boolean") && !item.getText(2).equals("Timestamp") && !item.getText(2).equals("Date")){
					this.setErrorMessage("实体属性长度不能为空");
					return false;
				}
				try{
					if(!StringUtil.isNull(item.getText(7)))
						Integer.parseInt(item.getText(7));
					if(!StringUtil.isNull(item.getText(8)))
						Integer.parseInt(item.getText(8));
				}catch(Exception e){
					this.setErrorMessage("实体属性长度或者精确度必须为数字");
					return false;
				}
				
				
				colum = entity.new Column();
				colum.setColumnName(item.getText(1).toLowerCase());
				colum.setProperName(item.getText(1).toLowerCase());
				colum.setColumnType(item.getText(2));
				colum.setIsPrimaryKey(StringUtil.isNull(item.getText(3)) ? "false" : item.getText(3));
				colum.setIsNullable(StringUtil.isNull(item.getText(5)) ? "false" : item.getText(5));
				colum.setIsUnique(StringUtil.isNull(item.getText(4)) ? "false" : item.getText(4));
				colum.setIsGenare(StringUtil.isNull(item.getText(6)) ? "false" : item.getText(6));
				colum.setLength(StringUtil.isNull(item.getText(7)) ? "":", length="+item.getText(7));
				colum.setDecimalDigits(StringUtil.isNull(item.getText(8)) ? "":", precision="+item.getText(8));
				colum.setMethod(colum.getColumnName().substring(0, 1).toUpperCase() + colum.getColumnName().substring(1));
				//colum.setDesc(item.getText(9) == null ? "" : item.getText(9));
				if("Date".equals(item.getText(2)))
					entity.setImportDate(true);
				if("Timestamp".equals(item.getText(2)))
					entity.setImportTime(true);
				if("BigDecimal".equals(item.getText(2)))
					entity.setIsimportBigsem(true);
				columList.add(colum);
			}
			entity.setClassName(className.toLowerCase());
			entity.setColumnList(columList);
			entity.setEntityDb(combo.getText());
		}
		
		this.setMessage("编辑实体类属性。");
		return true;
	}
	
	private boolean virfyServName(String serviceName){
		
		try{
			ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
			ISelection selection = selectionService.getSelection();
			Object element = ((IStructuredSelection) selection).getFirstElement();
			IJavaElement child[] = ((IPackageFragment) element).getChildren();
			for(IJavaElement e : child){
				if(e.getElementType() == IJavaElement.COMPILATION_UNIT){
					if((serviceName + ".java").equals(e.getElementName())){
						MessageDialog.openWarning(getShell(), "WARNING", "实体类名：" + serviceName + " 重复！");
						return false;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			MessageDialog.openWarning(getShell(), "WARNING", e.getMessage());
			return false;
		}
		return true;
		
	}
}