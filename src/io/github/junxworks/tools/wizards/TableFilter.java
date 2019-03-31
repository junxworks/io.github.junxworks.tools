package io.github.junxworks.tools.wizards;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.internal.misc.StringMatcher;

import io.github.junxworks.tools.TableModel;

@SuppressWarnings("restriction")
public class TableFilter extends ViewerFilter {
	
	private StringMatcher match;
	
	private TableViewer viewer;
	
	public TableFilter(TableViewer viewer){
		this.viewer = viewer;
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		String value = ((TableModel)element).getTableName();
		return match.match(value);
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
	            viewer.refresh();
		}else {
			if (filtering){
				match = null;
				viewer.removeFilter(this);
			}
		}
	}
}
