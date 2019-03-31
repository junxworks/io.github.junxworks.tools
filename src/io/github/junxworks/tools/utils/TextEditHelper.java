package io.github.junxworks.tools.utils;
 import java.util.Map;
 import org.eclipse.jface.text.BadLocationException;
 import org.eclipse.jface.text.DocumentRewriteSession;
 import org.eclipse.jface.text.DocumentRewriteSessionType;
 import org.eclipse.jface.text.IDocument;
 import org.eclipse.jface.text.IDocumentExtension4;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.TextUtilities;
 import org.eclipse.text.edits.MalformedTreeException;
 import org.eclipse.text.edits.TextEdit;
 
 public class TextEditHelper
 {
   private TextEdit textEdit;
   private IDocument document;
   private DocumentRewriteSession rewriteSession;
 
   public TextEditHelper(IDocument document, TextEdit textEdit)
   {
     this.document = document;
     this.textEdit = textEdit;
   }
 
   public void apply() throws MalformedTreeException, BadLocationException {
     apply(3);
   }
 
   public void apply(int style) throws MalformedTreeException, BadLocationException {
     Map stateData = null;
     try {
       stateData = startSequentialRewriteMode();
       this.textEdit.apply(this.document, style);
     } finally {
       stopSequentialRewriteMode(stateData);
     }
   }
 
   private Map<?, ?> startSequentialRewriteMode() {
     Map stateData = null;
     if ((this.document instanceof IDocumentExtension4)) {
       IDocumentExtension4 extension = (IDocumentExtension4)this.document;
       this.rewriteSession = extension.startRewriteSession(DocumentRewriteSessionType.SEQUENTIAL);
     }
     else {
       stateData = TextUtilities.removeDocumentPartitioners(this.document);
     }
 
     return stateData;
   }
 
   private void stopSequentialRewriteMode(Map<?, ?> stateData) {
     if ((this.document instanceof IDocumentExtension4)) {
       IDocumentExtension4 extension = (IDocumentExtension4)this.document;
       extension.stopRewriteSession(this.rewriteSession);
     }
     else {
       TextUtilities.addDocumentPartitioners(this.document, (Map<String, ? extends IDocumentPartitioner>) stateData);
     }
   }
 }
