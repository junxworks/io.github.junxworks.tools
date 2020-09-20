package io.github.junxworks.tools.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
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

import io.github.junxworks.tools.ServiceModel;
import io.github.junxworks.tools.StringUtil;
import io.github.junxworks.tools.ServiceModel.Compoment;
import io.github.junxworks.tools.dialog.ChooseDialog;
import swing2swt.layout.BorderLayout;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (java).
 */

@SuppressWarnings("restriction")
public class ServiceConfWizardPage extends WizardPage {

	private Text text_1;
	private Text text_2;
	private Combo combo;
	private Combo combo_1;
	private Combo combo_2;
	private Table table;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public ServiceConfWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("新增服务");
		setDescription("编辑服务属性添加DAO注入。\r\n注意:服务类名、连接池为必填项！！");
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new BorderLayout(0, 0));
		
		Group group = new Group(container, SWT.NONE);
		group.setText("服务属性");
		group.setLayout(new GridLayout(4, true));
		group.setLayoutData(BorderLayout.NORTH);

		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("服务类名:");
		
		text_1 = new Text(group, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 1, 1));
		text_1.setToolTipText("服务类名必填");

		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("事务控制层:");

		combo = new Combo(group, SWT.READ_ONLY | SWT.ARROW_DOWN);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.setItems(new String[] { "SERVICE", "COMP" });
		combo.select(0);

		Label lblNewLabel_2 = new Label(group, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("事务传播:");

		combo_1 = new Combo(group, SWT.READ_ONLY | SWT.ARROW_DOWN);
		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo_1.setItems(
				new String[] { "REQUIRED", "SUPPORTS", "MANDATORY", "REQUIRES_NEW", "NOT_SUPPORTED", "NEVER", "NONE" });
		combo_1.select(0);

		Label lblNewLabel_3 = new Label(group, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("连接池:");

		text_2 = new Text(group, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 1, 1));
		text_2.setToolTipText("连接池必填");
		
		Label lblNewLabel_4 = new Label(group, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("反序列化器:");

		combo_2 = new Combo(group, SWT.READ_ONLY | SWT.ARROW_DOWN);
		combo_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo_2.setItems(new String[] { "Json" });
		combo_2.select(0);

		Label lblNewLabel_5 = new Label(group, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_5.setText("序列化器:");

		Combo combo_3 = new Combo(group, SWT.READ_ONLY | SWT.ARROW_DOWN);
		combo_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo_3.setItems(new String[] { "Json" });
		combo_3.select(0);

		Group group1 = new Group(container, SWT.NONE);
		group1.setText("注入DAO");
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
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,8,50));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(30);
		tblclmnNewColumn.setAlignment(SWT.CENTER);
		tblclmnNewColumn.setResizable(false);

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(200);
		tblclmnNewColumn_1.setText("组件名");
		tblclmnNewColumn_1.setAlignment(SWT.CENTER);

		TableColumn tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(200);
		tblclmnNewColumn_2.setText("组件接口");
		tblclmnNewColumn_2.setAlignment(SWT.CENTER);
		TableColumn tblclmnNewColumn_3 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_3.setWidth(200);
		tblclmnNewColumn_3.setText("组件实现类");
		tblclmnNewColumn_3.setAlignment(SWT.CENTER);

		table.addListener(SWT.MouseDoubleClick, new Listener() {
			int editColumnIndex = -1;

			@Override
			public void handleEvent(Event e) {

				final Point point = new Point(e.x, e.y);
				final TableItem tableItem = table.getItem(point);
				if (tableItem == null)
					return;
				for (int i = 0; i < 4; i++) {
					final Rectangle r = tableItem.getBounds(i);
					if (r.contains(point)) {
						if(i == 2 || i == 3){
							ChooseDialog dialog = new ChooseDialog(getShell());
							if(dialog.open() != 0)
								return;
							String DaoClassName = dialog.getDaoClassName();
							tableItem.setText(i, DaoClassName);
							return;
						}
						editColumnIndex = i;
						final TableEditor editor = new TableEditor(table);
						final Control oldEditor = editor.getEditor();
						if (oldEditor != null) {
							oldEditor.dispose();
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
					}
				}

			}
		});
		setControl(container);
	}

	public boolean getData(ServiceModel service){
		
		String servName = text_1.getText();
		if(StringUtil.isNull(servName)){
			this.setErrorMessage("服务类名不能为空");
			text_1.setFocus();
			return false;
		}else{
			if(!this.virfyServName(servName))
				return false;
		}
		String pool = text_2.getText();
		if(StringUtil.isNull(pool)){
			this.setErrorMessage("连接池不能为空");
			text_2.setFocus();
			return false;
		}
		String transLevel = (combo.getSelectionIndex() == 0) ? "PROPAGATION_TYPE_SERVICE" : "PROPAGATION_TYPE_COMP";
		int transcation = combo_1.getSelectionIndex();
		String trans = "";
		switch(transcation){
		       case 0:
		    	   trans = "PROPAGATION_REQUIRED";
		    	   break;
		       case 1:
		    	   trans = "PROPAGATION_SUPPORTS";
		    	   break;
		       case 2:
		    	   trans = "PROPAGATION_MANDATORY";
		    	   break;
		       case 3:
		    	   trans = "PROPAGATION_REQUIRES_NEW";
		    	   break;
		       case 4:
		    	   trans = "PROPAGATION_NOT_SUPPORTED";
		    	   break;
		       case 5:
		    	   trans = "PROPAGATION_NEVER";
		    	   break;
		       case 6:
		    	   trans = "PROPAGATION_NONE";
		    	   break;
		}
		
		service.setClassName(servName);
		service.setPool(pool);
		service.setTranscationLevel(transLevel);
		service.setTranscation(trans);
		service.setReadTool("DATA_READER_JSON");
		service.setWriteTool("DATA_WRITER_JSON");
		
		if(table.getItemCount() > 0){
			
			TableItem[] items = table.getItems();
			Compoment comp;
			List<Compoment> compList = new ArrayList<Compoment>();
			String className;
			for (TableItem item : items) {
				if(StringUtil.isNull(item.getText(1))){
					this.setErrorMessage("DAO注入的组件名不能为空");
					return false;
				}
				if(StringUtil.isNull(item.getText(2))){
					this.setErrorMessage("DAO注入的组件接口不能为空");
					return false;
				}
				if(StringUtil.isNull(item.getText(3))){
					this.setErrorMessage("DAO注入的组件实现类不能为空");
					return false;
				}
				comp = service.new Compoment();
				comp.setCompClass(item.getText(3));
				comp.setCompName(item.getText(1));
				className = item.getText(2);
				comp.setClassName(className.substring(className.lastIndexOf(".") + 1));
				comp.setPackageName(className);
				compList.add(comp);
			}
			service.setCompList(compList);
		}
		
		this.setMessage("编辑服务属性添加DAO注入！");
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
						MessageDialog.openWarning(getShell(), "WARNING", "服务类名:" + serviceName + " 重复！");
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