package io.github.junxworks.tools.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.actions.FormatAllAction;
import org.eclipse.jdt.ui.actions.OrganizeImportsAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import io.github.junxworks.tools.JunxworksPlugin;

public class EditorUtils {
	public static void insertContent(String content) {
		IEditorPart editor = getEditor();
		insert(editor, content);
	}

	public static IEditorPart getEditor() {
		IEditorPart part = JunxworksPlugin.getWorkbenchPage().getActiveEditor();
		if (part == null) {
			return null;
		}

		if ((part instanceof IEditorPart)) {
			return part;
		}

		return null;
	}

	private static void insert(IEditorPart editorPart, String content) {
		if (editorPart == null)
			return;
		if ((editorPart instanceof ITextEditor)) {
			ITextEditor editor = (ITextEditor) editorPart;
			if (editor.isEditable()) {
				IDocumentProvider docprovider = editor.getDocumentProvider();
				ISelectionProvider selprovider = editor.getSelectionProvider();

				if ((docprovider != null) && (selprovider != null)) {
					IDocument document = docprovider.getDocument(editorPart.getEditorInput());
					ISelection selection = selprovider.getSelection();
					if ((document != null) && (selection != null) && ((selection instanceof ITextSelection))) {
						ITextSelection textSel = (ITextSelection) selection;
						try {
							doInsert(editorPart, editor, document, textSel, content);

							if ((editor instanceof JavaEditor)) {
								OrganizeImportsAction orgnizeImportAction = new OrganizeImportsAction((JavaEditor) editor);
								orgnizeImportAction.run(textSel);
								FormatAllAction fc = new FormatAllAction(editor.getSite());
								fc.runOnMultiple(new ICompilationUnit[] { JavaUI.getWorkingCopyManager().getWorkingCopy(editor.getEditorInput()) });
							}
						} catch (Exception t) {
							editor.getSite().getShell().getDisplay().beep();
						}
					}
				}
			}
		}
	}

	private static void doInsert(IEditorPart part, ITextEditor textEditor, IDocument document, ITextSelection textSelection, String content) throws BadLocationException {
		if ((content != null) && ((content.length() > 0) || (textSelection.getLength() > 0))) {
			String preferredEOL = null;
			if ((document instanceof IDocumentExtension4)) {
				preferredEOL = ((IDocumentExtension4) document).getDefaultLineDelimiter();
			} else {
				Method getLineDelimiter = null;
				try {
					getLineDelimiter = document.getClass().getMethod("getLineDelimiter", new Class[0]);
				} catch (NoSuchMethodException e) {
				}

				if (getLineDelimiter != null) {
					try {
						preferredEOL = (String) getLineDelimiter.invoke(document, new Object[0]);
					} catch (IllegalAccessException e) {
					} catch (InvocationTargetException e) {
					}

				}

			}

			if (preferredEOL == null) {
				preferredEOL = System.getProperty("line.separator");
			}
			if ((!"\n".equals(preferredEOL)) && (preferredEOL != null))
				;
			document.replace(textSelection.getOffset(), textSelection.getLength(), content);
		}
	}
}
