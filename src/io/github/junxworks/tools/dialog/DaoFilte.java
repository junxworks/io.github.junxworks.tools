package io.github.junxworks.tools.dialog;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.internal.misc.StringMatcher;

@SuppressWarnings("restriction")
public class DaoFilte extends ViewerFilter {

	private StringMatcher match;
	
	private TableViewer viewer;
	
	public DaoFilte(TableViewer viewer){
		this.viewer = viewer;
	}
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		
		String value = ((IJavaElement)element).getElementName();
		return match.match(value.substring(0, value.indexOf(".")));
	}

	public StringMatcher getMatch() {
		return match;
	}

	public void setMatch(String filtStr) {
		boolean filtering = match != null;
		if (filtStr != null && filtStr.trim().length() > 0) {
		
			match = new StringMatcher("*" + filtStr + "*", true, false);
			if (!filtering)
				viewer.addFilter(this);
			else
	            viewer.refresh();;
		}
		else {
			if (filtering){
				match = null;
				viewer.removeFilter(this);
				
			}
		}
	}
	
	
}
