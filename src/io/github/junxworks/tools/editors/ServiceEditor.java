package io.github.junxworks.tools.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.ImportContainer;
import org.eclipse.jdt.internal.core.ImportDeclaration;
import org.eclipse.jdt.internal.core.SourceField;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
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

import io.github.junxworks.tools.Constants;
import io.github.junxworks.tools.StringUtil;
import io.github.junxworks.tools.dialog.ChooseDialog;
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
public class ServiceEditor extends MultiPageEditorPart implements IResourceChangeListener {

	private CompilationUnitEditor editor;

	private Text text_1;

	private Text text_2;

	private Combo combo;

	private Combo combo_1;

	private Combo combo_2;
	
	private Combo combo_3;

	private Table table;

	public ServiceEditor() {
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
		final IJavaElement f = (IJavaElement) input.getAdapter(IJavaElement.class);
		final CompilationUnit cu = (CompilationUnit) f;
		SourceType st = null;
		ImportContainer importCon = null;
		final List<SourceField> fields = new ArrayList<SourceField>();
		
		String pool = "";
		String propagation = null;
		String propagation_type = null;
		String dataReader = null;
		String dataWriter = null;

		class FocusEventListener implements FocusListener {

			private String annotionName;
			private Combo comb;
			private Text text;
			private String preValue;
			public FocusEventListener(String annotionName,int comp){
				this.annotionName = annotionName;
				switch(comp){
					case 1:
						text = text_2;
						break;
					case 2:
						comb = combo;
						break;
					case 3:
						comb = combo_1;
						break;
					case 4:
						comb = combo_2;
						break;
					case 5:
						comb = combo_3;
						break;
				}
			}
			@Override
			public void focusGained(org.eclipse.swt.events.FocusEvent e) {
				// TODO Auto-generated method stub
				if(text != null)
					preValue = text.getText();
				else
					preValue = comb.getText();
			}

			@Override
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
				// TODO Auto-generated method stub
				
				try{
					String curValue;
					if(text != null)
						curValue = text.getText();
					else
						curValue = comb.getText();
					if(preValue.equals(curValue))
						return;
					IJavaElement[] ie = cu.getChildren();
					SourceType st = null;
					for (IJavaElement i : ie) {
						if (i instanceof SourceType){
							st = (SourceType) i;
							break;
						}
					}
					IAnnotation ia = st.getAnnotation(annotionName);
					ISourceRange sr = null;
					if (ia != null) {
						 sr = ia.getSourceRange();
					}
					String swkzc = combo.getText().equals("SERVICE") ? "PROPAGATION_TYPE_SERVICE" : "PROPAGATION_TYPE_COMP";
					int trans = combo_1.getSelectionIndex();
					String transaction = null;
					switch(trans){
						case 0:
							transaction = "PROPAGATION_REQUIRED";
							break;
						case 1:
							transaction = "PROPAGATION_SUPPORTS";
							break;
						case 2:
							transaction = "PROPAGATION_MANDATORY";
							break;
						case 3:
							transaction = "PROPAGATION_REQUIRES_NEW";
							break;
						case 4:
							transaction = "PROPAGATION_NOT_SUPPORTED";
							break;
						case 5:
							transaction = "PROPAGATION_NEVER";
							break;
						case 6:
							transaction = "PROPAGATION_NONE";
							break;
							
					}
					String pool = text_2.getText();
					String read = "DATA_READER_JSON";
					String write = "DATA_WRITER_JSON";
					String annotionValue = "@ServiceProperty(propagation_type=ServiceProperty."+ swkzc +",dataReader=Constants."+ read +",dataWriter=Constants."+write+")";
					if("Transaction".equals(annotionName))
						annotionValue = "@Transaction(pool = \""+pool+"\", propagation = Propagation."+transaction+")";
					IDocument doc=editor.getDocumentProvider().getDocument(input);
					doc.replace(sr.getOffset(), sr.getLength(), annotionValue);

				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			
		}
		try {
			IJavaElement[] ie = cu.getChildren();
			for (IJavaElement i : ie) {
				if (i instanceof SourceType) {
					st = (SourceType) i;
					IAnnotation ia = st.getAnnotation(Constants.ANNO_SVC_PROPERTY);
					IMemberValuePair[] imps = ia.getMemberValuePairs();
					for (IMemberValuePair imp : imps) {
						String val = String.valueOf(imp.getValue());
						switch (imp.getMemberName()) {
							case Constants.SVC_PROPERTY_PROPAGATION_TYPE:
								propagation_type = val;
								break;
							case Constants.SVC_PROPERTY_DATAREADER:
								dataReader = val;
								break;
							case Constants.SVC_PROPERTY_DATAWRITER:
								dataWriter = val;
								break;
							default:
								break;
						}
					}
					ia = st.getAnnotation(Constants.ANNO_SVC_TRANSACTION);
					imps = ia.getMemberValuePairs();
					for (IMemberValuePair imp : imps) {
						String val = String.valueOf(imp.getValue());
						switch (imp.getMemberName()) {
							case Constants.SVC_PROPERTY_POOL:
								pool = val;
								break;
							case Constants.SVC_PROPERTY_PROPAGATION:
								propagation = val;
								break;
							default:
								break;
						}
					}
					for(IJavaElement e : st.getChildren()){
						if(e instanceof SourceField)
							fields.add((SourceField)e);
							
					}
				}else if(i instanceof ImportContainer){
					importCon = (ImportContainer)i;
				}
			}
		} catch (JavaModelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new BorderLayout(0, 0));
		Group group = new Group(composite, SWT.NONE);
		group.setText("服务属性");
		group.setLayout(new GridLayout(4, true));
		group.setLayoutData(BorderLayout.NORTH);

		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("服务类名：");

		text_1 = new Text(group, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 1, 1));
		String name = input.getName();
		text_1.setText(name.substring(0, name.lastIndexOf(".")));
		text_1.setEnabled(false);

		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("事务控制层：");

		combo = new Combo(group, SWT.READ_ONLY | SWT.ARROW_DOWN);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo.setItems(new String[] { "SERVICE", "COMP" });
		if (propagation_type != null && propagation_type.contains("SERVICE")) {
			combo.select(0);
		} else {
			combo.select(1);
		}
		combo.addFocusListener(new FocusEventListener("ServiceProperty",2));
		
		Label lblNewLabel_2 = new Label(group, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("事务传播：");

		combo_1 = new Combo(group, SWT.READ_ONLY | SWT.ARROW_DOWN);
		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		String[] names = new String[] { "REQUIRED", "SUPPORTS", "MANDATORY", "REQUIRES_NEW", "NOT_SUPPORTED", "NEVER", "NONE" };
		combo_1.setItems(names);
		if (propagation != null) {
			for (int i = 0; i < names.length; i++) {
				if (propagation.contains(names[i]))
					combo_1.select(i);
			}
		}
		combo_1.addFocusListener(new FocusEventListener("Transaction",3) );
		
		Label lblNewLabel_3 = new Label(group, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("连接池：");

		text_2 = new Text(group, SWT.BORDER);
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 1, 1));
		text_2.setText(pool);
		text_2.setToolTipText("连接池必填");
		text_2.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(StringUtil.isNull(text_2.getText())){
					MessageDialog.openWarning(getSite().getShell(), "WARNING", "请输入连接池！");
					text_2.forceFocus();
				}
			}
			
		});
		text_2.addFocusListener(new FocusEventListener("Transaction",1) );
		
		Label lblNewLabel_4 = new Label(group, SWT.NONE);
		lblNewLabel_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_4.setText("反序列化器：");

		combo_2 = new Combo(group, SWT.READ_ONLY | SWT.ARROW_DOWN);
		combo_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo_2.setItems(new String[] { "Json" });
		combo_2.select(0);
		combo_2.addFocusListener(new FocusEventListener("ServiceProperty",4));
		
		Label lblNewLabel_5 = new Label(group, SWT.NONE);
		lblNewLabel_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_5.setText("序列化器：");

		combo_3 = new Combo(group, SWT.READ_ONLY | SWT.ARROW_DOWN);
		combo_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		combo_3.setItems(new String[] { "Json" });
		combo_3.select(0);
		combo_3.addFocusListener(new FocusEventListener("ServiceProperty",5));
		
		Group group1 = new Group(composite, SWT.NONE);
		group1.setText("DAO注入");
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

			@SuppressWarnings("null")
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				ImportContainer importCon = null;
				try {
					for (IJavaElement i : cu.getChildren()) {
						if(i instanceof ImportContainer){
							importCon = (ImportContainer)i;
						}
					}
				} catch (JavaModelException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				TableItem[] items = table.getItems();
				String packgeName;
				for (TableItem item : items) {
					if (item.getChecked()){
						for(SourceField s :fields){
							try {
								
								if(s.getElementName().equals(item.getText(1))){
									packgeName = item.getText(2);
									s.delete(true, null);
									if(importCon != null)
										importCon.getImport(packgeName).delete(true, null);
									fields.remove(s);

									break;
								}
							} catch (JavaModelException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						}
						
						item.dispose();
					}
						

				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

		});

		table = new Table(group1, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION | SWT.MULTI);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 50));
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

		TableItem ti;
		String className;
		IJavaElement[] importDec = null;
		try {
			importDec = importCon.getChildren();
		} catch (JavaModelException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		for(SourceField s : fields){
			ti = new TableItem(table, SWT.NONE);
			ti.setText(1, s.getElementName());
			className = s.toString().substring(0, s.toString().indexOf(" "));
			for(int i = 0;i < importDec.length;i++){
				if(((ImportDeclaration)importDec[i]).getElementName().contains("." + className)){
					ti.setText(2,((ImportDeclaration)importDec[i]).getElementName());
					break;
				}	
			}
			try {
				ti.setText(3, s.getAnnotation(Constants.SVC_FILED_INJECT).getMemberValuePairs()[0].getValue().toString());
			} catch (JavaModelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		table.addListener(SWT.MouseDoubleClick, new Listener() {
			int editColumnIndex = -1;

			private void freshDoc(String preValue,String prePackage,TableItem tableItem){
				try {
					if(StringUtil.isNull(tableItem.getText(1)) || StringUtil.isNull(tableItem.getText(2))
							|| StringUtil.isNull(tableItem.getText(3)))
						return;
					ImportContainer importCon = null;
					SourceType st = null;
					for (IJavaElement i : cu.getChildren()) {
						if(i instanceof ImportContainer){
							importCon = (ImportContainer)i;
						}else if(i instanceof SourceType)
							st = (SourceType) i;
					}
					
					for(SourceField s :fields){
						if(preValue.equals(s.getElementName())){
							s.delete(true, null);
							fields.remove(s);
							importCon.getImport(prePackage).delete(true, null);
							break;
						}
					}
					
					String beanName =  tableItem.getText(3);
					String pakName = tableItem.getText(2);
					fields.add((SourceField)(st.createField("@inject(bean=\"" +beanName+ "\")\nprivate " + pakName.substring(pakName.lastIndexOf(".") + 1)+ " " + tableItem.getText(1) + ";", null, false, null)));
					
					if(!importCon.getImport(pakName).exists()){
						ISourceRange sr = cu.getImportContainer().getSourceRange();
						IDocument doc = ServiceEditor.this.editor.getDocumentProvider().getDocument(input);
						TextEdit te = new InsertEdit(sr.getOffset()+sr.getLength(), "\nimport "+pakName+";\n");
						new TextEditHelper(doc, te).apply();

					}
					
				} catch (JavaModelException | MalformedTreeException | BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			@Override
			public void handleEvent(Event e) {

				final Point point = new Point(e.x, e.y);
				final TableItem tableItem = table.getItem(point);
				if (tableItem == null)
					return;
				final String preValue = tableItem.getText(1);
				final String prePackage = tableItem.getText(2);
				for (int i = 0; i < 4; i++) {
					final Rectangle r = tableItem.getBounds(i);
					if (r.contains(point)) {
						if(i == 2 || i == 3){
							ChooseDialog dialog = new ChooseDialog(getSite().getShell());
							dialog.setjElement(f);
							if(dialog.open() != 0)
								return;
							String DaoClassName = dialog.getDaoClassName();
							tableItem.setText(i, DaoClassName);
							freshDoc(preValue,prePackage,tableItem);	
						}else{
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
									freshDoc(preValue,prePackage,tableItem);	
								}
							});
						}
						
					}
				}

			}
		});
		int index = addPage(composite);
		setPageText(index, "edit");
		
		
	}

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
