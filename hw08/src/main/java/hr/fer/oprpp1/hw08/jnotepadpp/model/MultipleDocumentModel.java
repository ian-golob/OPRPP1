package hr.fer.oprpp1.hw08.jnotepadpp.model;

import hr.fer.oprpp1.hw08.jnotepadpp.exception.DocumentOpenedException;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;

public interface MultipleDocumentModel extends Iterable<SingleDocumentModel>{
    JComponent getVisualComponent();
    SingleDocumentModel createNewDocument();
    SingleDocumentModel getCurrentDocument();
    SingleDocumentModel loadDocument(Path path);
    void saveDocument(SingleDocumentModel model, Path newPath) throws IOException, DocumentOpenedException;
    void closeDocument(SingleDocumentModel model);
    void addMultipleDocumentListener(MultipleDocumentListener l);
    void removeMultipleDocumentListener(MultipleDocumentListener l);
    int getNumberOfDocuments();
    SingleDocumentModel getDocument(int index);
    SingleDocumentModel findForPath(Path path); //null, if no such model exists
    int getIndexOfDocument(SingleDocumentModel doc); //-1 if not present
}
