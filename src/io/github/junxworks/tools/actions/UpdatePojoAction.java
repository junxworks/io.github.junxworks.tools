package io.github.junxworks.tools.actions;

import io.github.junxworks.tools.pojo.db.utils.BeanEditAction;

import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.Workbench;

public class UpdatePojoAction implements IObjectActionDelegate {

	private static final String update_pojo = "io.github.junxworks.tools.actions.UpdatePojoAction";

	public UpdatePojoAction() {
		super();
	}

	public void run(IAction action) {
		String actionId = action.getId();
		switch (actionId) {
			case update_pojo:
				ISelectionService selectionService = Workbench.getInstance().getActiveWorkbenchWindow().getSelectionService();
				ISelection selection = selectionService.getSelection();
				if (selection instanceof IStructuredSelection) {
					Object element = ((IStructuredSelection) selection).getFirstElement();
					if (element instanceof CompilationUnit) {
						CompilationUnit je = (CompilationUnit) element;
						
						try {
							BeanEditAction.editBean(je);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {

	}

}
