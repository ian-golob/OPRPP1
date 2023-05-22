package hr.fer.oprpp1.hw08.jnotepadpp.model;

import hr.fer.oprpp1.hw08.jnotepadpp.exception.DocumentOpenedException;
import hr.fer.oprpp1.hw08.jnotepadpp.icons.IconLoader;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {

    private final List<SingleDocumentModel> documents = new ArrayList<>();

    private SingleDocumentModel currentDocument;

    private final List<MultipleDocumentListener> listeners = new ArrayList<>();

    private final ImageIcon unmodifiedDocumentIcon;

    private final ImageIcon modifiedDocumentIcon;

    public DefaultMultipleDocumentModel(){
        unmodifiedDocumentIcon = IconLoader.load("greenDisk.png");
        modifiedDocumentIcon = IconLoader.load("redDisk.png");
    }

    @Override
    public JComponent getVisualComponent() {
        return this;
    }

    @Override
    public SingleDocumentModel createNewDocument() {
        SingleDocumentModel newDocument = addNewDocument(null, "");
        addTab(
                "(" + LocalizationProvider.getInstance().getString("unnamed") + ")",
                unmodifiedDocumentIcon,
                new JScrollPane(newDocument.getTextComponent()),
                "(" + LocalizationProvider.getInstance().getString("unnamed") + ")"
        );
        setSelectedIndex(documents.size()-1);

        return newDocument;
    }

    @Override
    public SingleDocumentModel getCurrentDocument() {
        return currentDocument;
    }


    @Override
    public SingleDocumentModel loadDocument(Path path) {
        if(path == null){
            throw new IllegalArgumentException();
        }

        for(int i = 0; i < documents.size(); i++){
            if(documents.get(i).getFilePath() != null && documents.get(i).getFilePath().equals(path)){
                setSelectedIndex(i);
                return currentDocument;
            }
        }

        byte[] okteti;
        try {
            okteti = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String content = new String(okteti, StandardCharsets.UTF_8);

        SingleDocumentModel newDocument = addNewDocument(path, content);
        addTab(
                path.getFileName().toString(),
                unmodifiedDocumentIcon,
                new JScrollPane(newDocument.getTextComponent()),
                path.toAbsolutePath().toString()
        );
        setSelectedIndex(documents.size()-1);

        return newDocument;
    }


    private SingleDocumentModel addNewDocument(Path path, String content){
        SingleDocumentModel newDocument = new DefaultSingleDocumentModel(path, content);

        documents.add(newDocument);
        newDocument.addSingleDocumentListener(new SingleDocumentListener() {
            @Override
            public void documentModifyStatusUpdated(SingleDocumentModel model) {
                int index = documents.indexOf(model);

                if(model.isModified()){
                    setIconAt(index, modifiedDocumentIcon);
                } else {
                    setIconAt(index, unmodifiedDocumentIcon);
                }
            }

            @Override
            public void documentFilePathUpdated(SingleDocumentModel model) {
                int index = documents.indexOf(model);
                setTitleAt(index, model.getFilePath().getFileName().toString());
                setToolTipTextAt(index, model.getFilePath().toAbsolutePath().toString());
            }
        });

        listeners.forEach(l -> l.documentAdded(newDocument));

        return newDocument;
    }

    @Override
    public void saveDocument(SingleDocumentModel model, Path newPath) throws IOException, DocumentOpenedException {
        Path path = newPath;

        for (SingleDocumentModel docModel : documents) {
            Path docPath = docModel.getFilePath();
            if (!docModel.equals(model) && docPath != null && docPath.equals(newPath)) {
                throw new DocumentOpenedException();
            }
        }

        if(path==null) {
            path = model.getFilePath();
        }

        byte[] data = model.getTextComponent().getText().getBytes(StandardCharsets.UTF_8);

        Files.write(path, data);

        model.setModified(false);
        model.setFilePath(path);
    }

    @Override
    public void closeDocument(SingleDocumentModel model) {
        int index = documents.indexOf(model);
        documents.remove(model);

        if(documents.size() == 0){
            currentDocument = null;
        } else {
            if(index == 0) {
                setSelectedIndex(0);
            } else {
                setSelectedIndex(index-1);
            }
        }

        removeTabAt(index);

        listeners.forEach(l -> l.documentRemoved(model));

        if(documents.size() == 0){
            listeners.forEach(l -> l.currentDocumentChanged(model, null));
        }
    }

    @Override
    public void addMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeMultipleDocumentListener(MultipleDocumentListener l) {
        listeners.remove(l);
    }

    @Override
    public int getNumberOfDocuments() {
        return documents.size();
    }

    @Override
    public SingleDocumentModel getDocument(int index) {
        return documents.get(index);
    }

    @Override
    public SingleDocumentModel findForPath(Path path) {
        if(path == null){
            throw new IllegalArgumentException();
        }

        for(SingleDocumentModel documentModel: documents){
            if(documentModel.getFilePath() != null && documentModel.getFilePath().equals(path)){
                return documentModel;
            }
        }
        return null;
    }

    @Override
    public int getIndexOfDocument(SingleDocumentModel doc) {
        for(int i = 0; i < documents.size(); i++){
            if(documents.get(i).equals(doc)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public Iterator<SingleDocumentModel> iterator() {
        return documents.iterator();
    }

    @Override
    public void setSelectedIndex(int index) {
        super.setSelectedIndex(index);

        SingleDocumentModel oldCurrentDocument = currentDocument;
        currentDocument = documents.get(index);
        currentDocument.getTextComponent().requestFocus();
        listeners.forEach(l -> l.currentDocumentChanged(oldCurrentDocument, currentDocument));
    }
}
