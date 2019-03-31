package io.github.junxworks.tools.dialog;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.internal.Workbench;

import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import swing2swt.layout.BorderLayout;

@SuppressWarnings("restriction")
public class ChooseDialog extends Dialog {

	private String DaoClassName;
	private Text text;
	private Table table;
	private IJavaElement jElement;
	
	public ChooseDialog(Shell parentShell) {
		super(parentShell);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new BorderLayout(0,0));
		
		Group group1 = new Group(composite, SWT.NONE);
		group1.setLayout(new GridLayout(1, true));
		group1.setLayoutData(BorderLayout.CENTER);
		
		table = new Table(group1, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,1,50));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		table.addMouseListener(new MouseListener(){

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				final Point point = new Point(e.x, e.y);
				final TableItem tableItem = table.getItem(point);
				if (tableItem == null)
					return;
				okPressed();
				
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(200);
		tblclmnNewColumn.setText("组件类名");
		tblclmnNewColumn.setAlignment(SWT.LEFT);
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(200);
		tblclmnNewColumn_1.setText("组件包名");
		tblclmnNewColumn_1.setAlignment(SWT.LEFT);
		
		final TableViewer tv = new TableViewer(table);
		tv.setContentProvider(new ContentProvider());
		tv.setLabelProvider(new LabelProvider());
		//tv.setInput(new DaoClassModel());
		
		Group group = new Group(composite, SWT.NONE);
		group.setLayout(new GridLayout(1, true));
		group.setLayoutData(BorderLayout.NORTH);
		final DaoFilte filte = new DaoFilte(tv);
		text = new Text(group, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false, 1, 1));
		text.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if(tv.getInput() == null)
					tv.setInput(new DaoClassModel());
				String value = text.getText();
				filte.setMatch(value);
			}

			
		});
		
		return composite;
	}

	private class DaoClassModel{
		
		public IJavaElement[] getElements() {
			IJavaProject jProject;
			if(jElement != null)
				jProject = jElement.getJavaProject();
			else{
				ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
				ISelection selection = selectionService.getSelection();
				Object element = ((IStructuredSelection) selection).getFirstElement();
				jProject = ((IJavaElement) element).getJavaProject();
			}
			
			List<IJavaElement> elements = new ArrayList<IJavaElement>();
			getJavaElements(jProject,elements);
			IJavaElement[] rtn = new IJavaElement[elements.size()];
			for(int i = 0; i < elements.size();i++){
				rtn[i] = elements.get(i);
			}
			return rtn;
		}

		
	}

	private void getJavaElements(IJavaElement element,List<IJavaElement> elements){
		try{
			if(element.getElementType() != IJavaElement.COMPILATION_UNIT){
				IJavaElement child[];
				if(element.getElementType() == IJavaElement.JAVA_PROJECT){
					child = ((IJavaProject)element).getChildren();
				}else if( element.getElementType() == IJavaElement.PACKAGE_FRAGMENT_ROOT){
					if(element instanceof JarPackageFragmentRoot)
						return;
					child = ((IPackageFragmentRoot)element).getChildren();
				}
				else if( element.getElementType() == IJavaElement.PACKAGE_FRAGMENT){
					child = ((IPackageFragment)element).getChildren();
				}else if(element.getElementType() == IJavaElement.JAVA_MODEL)
					child = ((IJavaModel)element).getChildren();
				else
					return;
				for(IJavaElement e : child){
					getJavaElements(e,elements);
				}
			}else
				elements.add(element);	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	private class LabelProvider implements ITableLabelProvider{

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			IJavaElement java = (IJavaElement)element;
			switch(columnIndex){
				case 0:
					return java.getElementName().substring(0,java.getElementName().lastIndexOf("."));
				case 1:
					return java.getParent().getElementName();
					
			}
			return null;
		}

	}
	
	private class ContentProvider implements IStructuredContentProvider{

		
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			
			
			return ((DaoClassModel)inputElement).getElements();
		}
		
	}
	@Override
	protected void okPressed() {
		TableItem[] ti = table.getSelection();
		if(ti == null || ti.length == 0){
			MessageDialog.openInformation(getParentShell(), "warning", "请选择注入的组件类");
			return;
		}
		DaoClassName = ti[0].getText(1) + "." + ti[0].getText(0);
		super.okPressed();
	}



	public String getDaoClassName() {
		return DaoClassName;
	}



	@Override
	protected void configureShell(Shell newShell) {
		// TODO Auto-generated method stub
		super.configureShell(newShell);
		newShell.setText("选择注入类");
	}



	public void setjElement(IJavaElement jElement) {
		this.jElement = jElement;
	}

}
