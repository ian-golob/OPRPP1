package hr.fer.oprpp1.hw08.jnotepadpp.model;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DefaultSingleDocumentModel implements SingleDocumentModel {

    private Path filePath;

    private boolean modified;

    private final JTextArea textComponent;

    List<SingleDocumentListener> listeners = new ArrayList<>();

    public DefaultSingleDocumentModel(Path filePath, String textContent){
        this.filePath = filePath;
        modified = false;

        textComponent = new JTextArea();
        textComponent.setText(textContent);

        textComponent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setModified(true);
            }
        });

    }

    @Override
    public JTextArea getTextComponent() {
        return textComponent;
    }

    @Override
    public Path getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(Path path) {
        if(path == null){
            throw new IllegalArgumentException();
        }

        if(filePath == null || !filePath.equals(path)){
            filePath = path;

            listeners.forEach(listener -> {
                listener.documentFilePathUpdated(this);
            });
        }
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean modified) {
        if(this.modified != modified){
            this.modified = modified;

            listeners.forEach(listener -> {
                listener.documentModifyStatusUpdated(this);
            });
        }
    }

    @Override
    public void addSingleDocumentListener(SingleDocumentListener l) {
        listeners.add(l);
    }

    @Override
    public void removeSingleDocumentListener(SingleDocumentListener l) {
        listeners.remove(l);
    }

}
