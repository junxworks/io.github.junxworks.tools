package io.github.junxworks.tools.dialog;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import io.github.junxworks.tools.JunxworksPlugin;
import io.github.junxworks.tools.pojo.db.utils.DbUtil;
import swing2swt.layout.BorderLayout;

public class DbConnectDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Combo combo;
	private IPreferenceStore store;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DbConnectDialog(Shell parent) {
		super(parent);
		setText("SWT Dialog");
		store = JunxworksPlugin.getDefault().getPreferenceStore();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		
		shell = new Shell(getParent(), SWT.BORDER | SWT.CLOSE);
		shell.setSize(380, 252);
		shell.setText("Database configuration");
		shell.setLayout(new BorderLayout(0, 0));
		shell.setLocation(Display.getCurrent().getClientArea().width / 2 - shell.getShell().getSize().x/2, Display.getCurrent() 
                .getClientArea().height / 2 - shell.getSize().y/2);
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setToolTipText("");
		composite_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
		composite_1.setLayoutData(BorderLayout.CENTER);
		
		Label lblNewLabel_1 = new Label(composite_1, SWT.NONE);
		lblNewLabel_1.setAlignment(SWT.RIGHT);
		lblNewLabel_1.setBounds(20, 20, 80, 23);
		lblNewLabel_1.setText("DB type：");
		
		Label lblNewLabel_2 = new Label(composite_1, SWT.NONE);
		lblNewLabel_2.setAlignment(SWT.RIGHT);
		lblNewLabel_2.setBounds(20, 50, 80, 23);
		lblNewLabel_2.setText("URL：");
		
		Label lblNewLabel_3 = new Label(composite_1, SWT.NONE);
		lblNewLabel_3.setAlignment(SWT.RIGHT);
		lblNewLabel_3.setText("username：");
		lblNewLabel_3.setBounds(20, 80, 80, 23);
		
		Label lblNewLabel_4 = new Label(composite_1, SWT.NONE);
		lblNewLabel_4.setAlignment(SWT.RIGHT);
		lblNewLabel_4.setBounds(20, 110, 80, 23);
		lblNewLabel_4.setText("password：");
		
		Label lblNewLabel_5 = new Label(composite_1, SWT.NONE);
		lblNewLabel_5.setAlignment(SWT.RIGHT);
		lblNewLabel_5.setBounds(20, 140, 80, 23);
		lblNewLabel_5.setText("Schema：");
		
		text_1 = new Text(composite_1, SWT.BORDER);
		text_1.setBounds(110, 50, 240, 23);
		
		text_2 = new Text(composite_1, SWT.BORDER);
		text_2.setBounds(110, 80, 240, 23);
		
		text_3 = new Text(composite_1, SWT.BORDER | SWT.PASSWORD);
		text_3.setBounds(110, 110, 240, 23);
		
		text_4 = new Text(composite_1, SWT.BORDER);
		text_4.setBounds(110, 140, 240, 23);
		
		combo = new Combo(composite_1, SWT.READ_ONLY);
		combo.setBounds(110, 20, 240, 25);
		combo.setItems(new String[] {"oracle", "mysql"});
		
		Composite composite_2 = new Composite(shell, SWT.NONE);
		composite_2.setLayoutData(BorderLayout.SOUTH);
		
		Button btnNewButton = new Button(composite_2, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dbConnect("connect");
			}
		});
		btnNewButton.setBounds(60, 5, 80, 27);
		btnNewButton.setText("Test");
		
		Button btnNewButton_1 = new Button(composite_2, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dbConnect("save");
			}
		});
		btnNewButton_1.setBounds(145, 5, 80, 27);
		btnNewButton_1.setText("save");
		
		Button btnNewButton_2 = new Button(composite_2, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnNewButton_2.setBounds(230, 5, 80, 27);
		btnNewButton_2.setText("cancel");
		
		Label lblNewLabel_6 = new Label(composite_2, SWT.NONE);
		lblNewLabel_6.setBounds(10, 40, 434, 5);
		
		setValue();
	}
	
	//给赋予默认值
	private void setValue(){
		String dbTypeVal = store.getString("dbType");
		String urlVal = store.getString("url");
		String userNameVal = store.getString("user");
		String passwordVal = store.getString("password");
		String schemaVal = store.getString("schema");
		
		combo.setText(dbTypeVal);
		text_1.setText(urlVal);
		text_2.setText(userNameVal);
		text_3.setText(passwordVal);
		text_4.setText(schemaVal);
		
	}
	
	private void dbConnect(String type){
		//测试链接
		boolean flag = false;
		String dbTypeVal = combo.getText();
		String urlVal = text_1.getText();
		String userVal = text_2.getText();
		String pwdVal = text_3.getText();
		String schemaVal = text_4.getText();
		
		if(isNull(dbTypeVal) || isNull(urlVal) || isNull(userVal)
				||isNull(pwdVal) ||isNull(schemaVal)){
			MessageDialog.openError(shell,"Message","Parameters can't be null");
			return;
		}
		if("save".equals(type)){
			store.setValue("dbType", dbTypeVal);
			store.setValue("url", urlVal);
			store.setValue("user", userVal);
			store.setValue("password", pwdVal);
			store.setValue("schema", schemaVal);
			shell.dispose();
			return;
		}
		try{
			flag = DbUtil.isConnection(dbTypeVal,urlVal,userVal,pwdVal);
		}catch(Exception ex){
			ex.printStackTrace();
			MessageDialog.openError(shell,"提示信息",ex.getMessage());
			return;
		}
		if(flag){
			MessageDialog.openInformation(shell,"Message","success");
		}else{
			MessageDialog.openError(shell,"Message","fail");
		}
	}
	
	private boolean isNull(String str) {
		if (str == null || str.length() <= 0) {
			return true;
		}
		return false;
	}
}
