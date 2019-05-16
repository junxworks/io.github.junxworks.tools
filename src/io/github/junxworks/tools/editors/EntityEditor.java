package io.github.junxworks.tools.editors;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.mysql.jdbc.StringUtils;

import io.github.junxworks.tools.StringUtil;
import io.github.junxworks.tools.utils.TextEditHelper;
import swing2swt.layout.BorderLayout;

/**
 * An example showing how to create a multi-page editor.
 * This example has 3 pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
@SuppressWarnings("restriction")
public class EntityEditor extends MultiPageEditorPart implements IResourceChangeListener {

	private CompilationUnitEditor editor;
	
	private SourceType st;

	private Text text_1;

	private Table table;
	
	private Combo combo;

	public EntityEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	private void createSourcePage() {
		try {
			editor = new CompilationUnitEditor();//java编辑器
			int index = addPage(editor, getEditorInput());
			setPageText(index, "source");
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
		}
	}

	private void createPropertyEditPage() {
		
		final IEditorInput input = getEditorInput();
		IJavaElement f = (IJavaElement) input.getAdapter(IJavaElement.class);
		final CompilationUnit cu = (CompilationUnit) f;

		try {
			IJavaElement[] ie = cu.getChildren();
			for (IJavaElement i : ie) {
				if (i instanceof SourceType) {
					st = (SourceType) i;
					break;
					
				}
				
			}
		} catch (JavaModelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new BorderLayout(0, 0));
		Group group = new Group(composite, SWT.NONE);
		group.setText("实体属性");
		group.setLayout(new GridLayout(8, true));
		group.setLayoutData(BorderLayout.NORTH);

		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblNewLabel.setText("实体类名:");
		lblNewLabel.setAlignment(SWT.RIGHT);

		text_1 = new Text(group, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		String name = input.getName();
		text_1.setText(name.substring(0, name.lastIndexOf(".")));
		text_1.setEnabled(false);
		
		Label lblNewLabel2 = new Label(group, SWT.NONE);
		lblNewLabel2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		lblNewLabel2.setText("数据元:");
		lblNewLabel2.setAlignment(SWT.RIGHT);
		
		combo = new Combo(group, SWT.READ_ONLY | SWT.ARROW_DOWN);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		combo.setItems(new String[]{"MONGODB","OTHER"});
		try {
			String db = st.getAnnotations()[0].getElementName();
			if("Document".equals(db))
				db = "MONGODB";
			else 
				db = "OTHER";
			
			combo.setText(db);
		} catch (JavaModelException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		combo.setEnabled(false);
		
		Group group1 = new Group(composite, SWT.NONE);
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
				try{
					TableItem[] items = table.getItems();
					
					for (TableItem item : items) {
						if (item.getChecked()){
							
							if(!StringUtil.isNull(item.getText(1))){
								
								st.getField(item.getText(1)).delete(true, null);
								st.getMethod("set" + item.getText(1).substring(0,1).toUpperCase() + (item.getText(1).length() > 1 ? item.getText(1).substring(1) : ""), new String[]{typeMatch(item.getText(2))}).delete(true, null);
								st.getMethod("get" + item.getText(1).substring(0,1).toUpperCase() + (item.getText(1).length() > 1 ? item.getText(1).substring(1) : ""), null).delete(true, null);
								
							}
							
							item.dispose();
						}
							

					}
				}catch(Exception e1){
					e1.printStackTrace();
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

		});

		table = new Table(group1, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION | SWT.MULTI);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 80));
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
		TableColumn tblclmnNewColumn_9 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_9.setWidth(0);
		tblclmnNewColumn_9.setAlignment(SWT.CENTER);
		TableColumn tblclmnNewColumn_10 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_10.setWidth(0);
		tblclmnNewColumn_10.setAlignment(SWT.CENTER);
//		TableColumn tblclmnNewColumn_11 = new TableColumn(table, SWT.NONE);
//		tblclmnNewColumn_11.setWidth(150);
//		tblclmnNewColumn_11.setText("描述信息");
//		tblclmnNewColumn_11.setAlignment(SWT.CENTER);
		
		TableItem ti;
		IField[] fields = null;
		try {
			fields = st.getFields();
		} catch (JavaModelException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		IField s;
		for(int i = 0; i < fields.length; i++){
			s = fields[i];
			ti = new TableItem(table, SWT.NONE);
			ti.setText(1, s.getElementName());
			ti.setText(9, s.getElementName());
			ti.setText(2, s.toString().substring(0, s.toString().indexOf(" ")));
			ti.setText(10, s.toString().substring(0, s.toString().indexOf(" ")));
			ti.setText(3, s.getAnnotation("Id").exists() ? "true" : "false");
			ti.setText(6, s.getAnnotation("GeneratedValue").exists() ? "true" : "false");
			//ti.setText(11, );
			
			try {
				IMemberValuePair member[] = s.getAnnotation("Column").getMemberValuePairs();
				String memberName = null;
				if(member != null && member.length > 0){
					for(IMemberValuePair mem : member){
						memberName = mem.getMemberName();
						if("unique".equals(memberName))
							ti.setText(4,mem.getValue().toString());
						else if("nullable".equals(memberName))
							ti.setText(5,mem.getValue().toString());
						else if("length".equals(memberName))
							ti.setText(7,mem.getValue().toString());
						else if("precision".equals(memberName))
							ti.setText(8,mem.getValue().toString());
					}
					if(StringUtil.isNull(ti.getText(4)))
						ti.setText(4,"false");
					if(StringUtil.isNull(ti.getText(5)))
						ti.setText(5,"false");
				}
				
			} catch (JavaModelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
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
									freshDoc(tableItem);
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
								freshDoc(tableItem);
							}
						});
						
						break;
					}
				}

			}
			
			private void freshDoc(TableItem tableItem){
				try {
					if(StringUtil.isNull(tableItem.getText(1)))
						return;
					
					String field = "\n\n";
//					if(!StringUtil.isNull(tableItem.getText(11)))
//						field += "/* " + tableItem.getText(11) + " */\n";
					//String repField = "";
					if("true".equals(tableItem.getText(3))){
						field += "@Id\n";
						//repField += "@Id\n";
					}
					if("true".equals(tableItem.getText(6))){
						field += "@GeneratedValue(strategy = GenerationType.AUTO)\n";
						//repField += "	@GeneratedValue(strategy = GenerationType.AUTO)\n";
					}	
					field += "@Column(name=\"" + tableItem.getText(1).toLowerCase() + "\", unique=" +tableItem.getText(4)
						  + ", nullable=" + tableItem.getText(5);
//					repField += "	@Column(name=\"" + tableItem.getText(1).toUpperCase() + "\", unique=" +tableItem.getText(4)
//					  + ", nullable=" + tableItem.getText(5)+ ", length=" + tableItem.getText(7);
					
					if(!StringUtil.isNull(tableItem.getText(7))){
						field += ", length=" + tableItem.getText(7);
						//repField += ", precision=" + tableItem.getText(8);
					}
					if(!StringUtil.isNull(tableItem.getText(8))){
						field += ", precision=" + tableItem.getText(8);
						//repField += ", precision=" + tableItem.getText(8);
					}
						
					field += ")\nprivate " + tableItem.getText(2) + " " + tableItem.getText(1).toLowerCase() + ";\n";
					//repField += ")\n	private " + tableItem.getText(2) + " " + tableItem.getText(1) + ";";
					String setMethodStr = getSetMethodStr(tableItem);
					String getMethodStr = getGetMethodStr(tableItem);
//					String setRepMethodStr = getRepSetMethodStr(tableItem);
//					String getRepMethodStr = getRepGetMethodStr(tableItem);
					
					IField fld = st.getField(tableItem.getText(9));
					if(fld.exists()){
						//替换有问题
						/*IDocument doc=editor.getDocumentProvider().getDocument(input);
						doc.replace(fld.getSourceRange().getOffset(), fld.getSourceRange().getLength(), repField);
						IMethod method = st.getMethod("set" + tableItem.getText(9).substring(0,1).toUpperCase() + (tableItem.getText(9).length() > 1 ? tableItem.getText(9).substring(1) : ""), new String[]{typeMatch(tableItem.getText(10))});
						doc.replace(method.getSourceRange().getOffset(),method.getSourceRange().getLength(),setRepMethodStr);
						method = st.getMethod("get" + tableItem.getText(9).substring(0,1).toUpperCase() + (tableItem.getText(9).length() > 1 ? tableItem.getText(9).substring(1) : ""), null);
						doc.replace(method.getSourceRange().getOffset(),method.getSourceRange().getLength(),getRepMethodStr);*/
						fld.delete(true, null);
						st.getMethod("set" + tableItem.getText(9).substring(0,1).toUpperCase() + (tableItem.getText(9).length() > 1 ? tableItem.getText(9).toLowerCase().substring(1) : ""), new String[]{typeMatch(tableItem.getText(10))}).delete(true, null);
						st.getMethod("get" + tableItem.getText(9).substring(0,1).toUpperCase() + (tableItem.getText(9).length() > 1 ? tableItem.getText(9).toLowerCase().substring(1) : ""), null).delete(true, null);
						
					}
					/*else{
						st.createField(field , null, false, null);
						// 添加属性set方法
						st.createMethod(setMethodStr, null, true, null);
						// 添加属性get方法
						st.createMethod(getMethodStr, null, true, null);
					}*/
					st.createField(field , null, false, null);
					// 添加属性set方法
					st.createMethod(setMethodStr, null, true, null);
					// 添加属性get方法
					st.createMethod(getMethodStr, null, true, null);
					
					ISourceRange sr = cu.getImportContainer().getSourceRange();
					IDocument doc = EntityEditor.this.editor.getDocumentProvider().getDocument(input);
					TextEdit te = null;
					if("Date".equals(tableItem.getText(2)) && !cu.getImportContainer().getImport("java.util.Date").exists()){
						
						te = new InsertEdit(sr.getOffset()+sr.getLength(), "\nimport java.util.Date;\n");
						
					}else if("Timestamp".equals(tableItem.getText(2)) && !cu.getImportContainer().getImport("java.sql.Timestamp").exists()){
						te = new InsertEdit(sr.getOffset()+sr.getLength(), "\nimport java.sql.Timestamp;\n");
						
					}else if("BigDecimal".equals(tableItem.getText(2)) && !cu.getImportContainer().getImport("java.math.BigDecimal").exists()){
						te = new InsertEdit(sr.getOffset()+sr.getLength(), "\nimport java.math.BigDecimal;\n");
						
					}
					if(te != null)
						new TextEditHelper(doc, te).apply();
					tableItem.setText(9,tableItem.getText(1));
					tableItem.setText(10,tableItem.getText(2));
				} catch (JavaModelException | MalformedTreeException | BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			/**
			 * 获取属性get方法字符串
			 * 
			 * @param tColumn
			 * @return
			 */
			private String getGetMethodStr(TableItem tableItem) {
				
				String name = tableItem.getText(1).toLowerCase();
				
				return "public " + tableItem.getText(2) + " get" + name.substring(0, 1).toUpperCase() + name.substring(1) 
						+ "() {\n	return this."
						+ tableItem.getText(1)+ ";\n}\n";
			}

			private String getRepGetMethodStr(TableItem tableItem) {
				
				String name = tableItem.getText(1).toLowerCase();
				
				return "	public " + tableItem.getText(2) + " get" + name.substring(0, 1).toUpperCase() + name.substring(1) 
						+ "() {\n		return this."
						+ tableItem.getText(1)+ ";\n	}";
			}
			/**
			 * 获取属性set方法字符串
			 * 
			 * @param tColumn
			 * @return
			 */
			private String getSetMethodStr(TableItem tableItem) {
				String name = tableItem.getText(1).toLowerCase();
				String setStr = "public void" + " set" + name.substring(0, 1).toUpperCase() + name.substring(1)
							+ "("+ tableItem.getText(2) + " " + name + ") {\n	this."
						+ name + " = " + name + ";\n}\n";
				return setStr;
			}
			private String getRepSetMethodStr(TableItem tableItem) {
				String name = tableItem.getText(1).toLowerCase();
				String setStr = "public void" + " set" + name.substring(0, 1).toUpperCase() + name.substring(1)
							+ "("+ tableItem.getText(2) + " " + name + ") {\n		this."
						+ name + " = " + name + ";\n	}\n";
				return setStr;
			}
		});
		int index = addPage(composite);
		setPageText(index, "edit");
		
		
	}

	private static String typeMatch(String Type){
		
		if("int".equals(Type))
			return "I";
		else if("long".equals(Type))
			return "J";
		else if("float".equals(Type))
			return "F";
		else if("double".equals(Type))
			return "D";
		else if("boolean".equals(Type))
			return "Z";
		else if("Date".equals(Type))
			return "QDate;";
		else if("Timestamp".equals(Type))
			return "QTimestamp;";
		else if("String".equals(Type))
			return "QString;";
		else if("BigDecimal".equals(Type))
			return "QBigDecimal;";
		else if("Integer".equals(Type))
			return "QInteger;";
		else if("Long".equals(Type))
			return "QLong;";
		else if("Float".equals(Type))
			return "QFloat;";
		else if("Double".equals(Type))
			return "QDouble;";
		else if("Boolean".equals(Type))
			return "QBoolean;";
		else 
			return "";
	}
	@SuppressWarnings("deprecation")
	protected void createPages() {
		createSourcePage();
		createPropertyEditPage();
		this.setTitle(editor.getTitle());
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}

	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 1) {

		}
	}

	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) editor.getEditorInput()).getFile().getProject().equals(event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

	@Override
	public void doSaveAs() {

	}
	
}
