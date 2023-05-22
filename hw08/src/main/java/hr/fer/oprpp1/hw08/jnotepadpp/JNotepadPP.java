package hr.fer.oprpp1.hw08.jnotepadpp;

import hr.fer.oprpp1.hw08.jnotepadpp.exception.DocumentOpenedException;
import hr.fer.oprpp1.hw08.jnotepadpp.local.*;
import hr.fer.oprpp1.hw08.jnotepadpp.model.DefaultMultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.model.SingleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.model.StatusBarModel;

import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.Collator;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JNotepadPP extends JFrame {

    private final FormLocalizationProvider flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);;

    private final DefaultMultipleDocumentModel mdm = new DefaultMultipleDocumentModel();;

    private final StatusBarModel sbm = new StatusBarModel(flp);

    public JNotepadPP(){
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(500, 500);
        setTitle("JNotepad++");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitAction.actionPerformed(null);
            }
        });

        mdm.addMultipleDocumentListener(new MultipleDocumentListener() {
            @Override
            public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
                if(currentModel != null){
                    setTitle(mdm.getTitleAt(mdm.getSelectedIndex()) + " - JNotepad++");
                } else {
                    setTitle("JNotepad++");
                }

                if(previousModel != null){
                    previousModel.getTextComponent().removeCaretListener(selectionOnlyActionListener);
                }

                if(currentModel != null){
                    currentModel.getTextComponent().addCaretListener(selectionOnlyActionListener);
                }
            }

            @Override
            public void documentAdded(SingleDocumentModel model) {

            }

            @Override
            public void documentRemoved(SingleDocumentModel model) {

            }
        });

        mdm.addMultipleDocumentListener(sbm);

        flp.addLocalizationListener(() -> {
            Iterator<SingleDocumentModel> iterator = mdm.iterator();

            while (iterator.hasNext()) {
                SingleDocumentModel model = iterator.next();

                if (model.getFilePath() == null) {
                    int docIndex = mdm.getIndexOfDocument(model);

                    mdm.setTitleAt(docIndex, "(" + flp.getString("unnamed") + ")");
                    mdm.setSelectedIndex(mdm.getSelectedIndex());
                }
            }
        });

        initActionValues();

        initGui();
    }

    private void initGui() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        initHotkeys();

        createMenus();

        createToolbar();

        createCenter();
    }

    private final Action createBlankDocumentAction = new LocalizableAction("new", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            mdm.createNewDocument();
        }
    };

    private final Action openFileAction = new LocalizableAction("open", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle(flp.getString("openFile"));
            if(fc.showOpenDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
                return;
            }
            File fileName = fc.getSelectedFile();
            Path filePath = fileName.toPath();

            mdm.loadDocument(filePath);
        }
    };

    private final Action saveFileAction = new LocalizableAction("save", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel currentModel = mdm.getCurrentDocument();

            if(currentModel == null){
                showOpenDocumentFirstDialog();
                return;
            }

            Path path = currentModel.getFilePath();

            if(path==null) {
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle(flp.getString("saveDocument"));
                if(jfc.showSaveDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
                    return;
                }
                path = jfc.getSelectedFile().toPath();
            }

            saveDocument(currentModel, path);
        }
    };

    private final Action saveFileAsAction = new LocalizableAction("saveAs", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel currentModel = mdm.getCurrentDocument();

            if(currentModel == null){
                showOpenDocumentFirstDialog();
                return;
            }

            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle(flp.getString("saveDocumentAs"));
            if(jfc.showSaveDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
                return;
            }
            Path path = jfc.getSelectedFile().toPath();

            saveDocument(currentModel, path);
        }
    };

    private final Action closeDocumentAction = new LocalizableAction("close", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel currentModel = mdm.getCurrentDocument();

            if(currentModel == null){
                showOpenDocumentFirstDialog();
                return;
            }

            if(currentModel.isModified()){

                int chosen = showUnsavedChangesDialog(mdm.getTitleAt(mdm.getIndexOfDocument(currentModel)));

                if(chosen == 2 || chosen == JOptionPane.CLOSED_OPTION){
                    return;
                }

                if(chosen == 0){
                    Path path = currentModel.getFilePath();

                    if(path==null) {
                        JFileChooser jfc = new JFileChooser();
                        jfc.setDialogTitle(flp.getString("saveDocument"));
                        if(jfc.showSaveDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
                            return;
                        }
                        path = jfc.getSelectedFile().toPath();
                    }

                    try {
                        mdm.saveDocument(currentModel, path);
                    } catch (IOException e1) {
                        showFileSaveErrorDialog(mdm.getTitleAt(mdm.getIndexOfDocument(currentModel)));
                        return;
                    } catch (DocumentOpenedException e1) {
                        showDocumentOpenedErrorDialog(mdm.getTitleAt(mdm.getIndexOfDocument(currentModel)));
                        return;
                    }
                }
            }

            mdm.closeDocument(currentModel);
        }
    };

    private final Action exitAction = new LocalizableAction("exit", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Iterator<SingleDocumentModel> iterator = mdm.iterator();
            boolean modifiedExists = false;
            while(iterator.hasNext()){
                if(iterator.next().isModified()) {
                    modifiedExists = true;
                    break;
                }
            }

            if(modifiedExists){
                while(mdm.getCurrentDocument() != null){
                    SingleDocumentModel currentModel = mdm.getCurrentDocument();

                    if(currentModel.isModified()){
                        int chosen = showUnsavedChangesDialog(mdm.getTitleAt(mdm.getIndexOfDocument(currentModel)));

                        if(chosen == 2 || chosen == JOptionPane.CLOSED_OPTION){
                            return;
                        }

                        if(chosen == 0){
                            Path path = currentModel.getFilePath();

                            if(path==null) {
                                JFileChooser jfc = new JFileChooser();
                                jfc.setDialogTitle(flp.getString("saveDocument"));
                                if(jfc.showSaveDialog(JNotepadPP.this)!=JFileChooser.APPROVE_OPTION) {
                                    return;
                                }
                                path = jfc.getSelectedFile().toPath();
                            }

                            try {
                                mdm.saveDocument(currentModel, path);
                            } catch (IOException e1) {
                                showFileSaveErrorDialog(path.toAbsolutePath().toString());
                                return;
                            } catch (DocumentOpenedException e1) {
                                showDocumentOpenedErrorDialog(path.toString());
                                return;
                            }
                        }
                    }

                    mdm.closeDocument(currentModel);
                }
            }

            sbm.stopClock();
            dispose();
        }
    };

    private final Action cutAction = new LocalizableAction("cut", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel currentModel = mdm.getCurrentDocument();

            if(currentModel != null){
                currentModel.getTextComponent().cut();
            }
        }
    };

    private final Action copyAction = new LocalizableAction("copy", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel currentModel = mdm.getCurrentDocument();

            if(currentModel != null){
                currentModel.getTextComponent().copy();
            }
        }
    };

    private final Action pasteAction = new LocalizableAction("paste", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel currentModel = mdm.getCurrentDocument();

            if(currentModel != null){
                currentModel.getTextComponent().paste();
            }
        }
    };

    private final Action statisticalInfoAction = new LocalizableAction("stats", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            SingleDocumentModel currentModel = mdm.getCurrentDocument();
            if(currentModel == null){
                showOpenDocumentFirstDialog();
            } else {
                JTextArea textArea = currentModel.getTextComponent();
                long characterNumber = textArea.getDocument().getLength();
                long nonBlankCharacters = textArea.getText().chars().filter(c -> !Character.isWhitespace(c)).count();
                long lineNumber = textArea.getLineCount();

                String info = flp.getString("yourDocumentHas") + " " +
                        characterNumber + " " + flp.getString("characters") + ", " +
                        nonBlankCharacters +  " " + flp.getString("nonBlankCharactersAnd") + " " +
                        lineNumber + " " + flp.getString("lines") + ".";

                JOptionPane.showMessageDialog(
                        JNotepadPP.this,
                        info,
                        flp.getString("info"),
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    };

    private final Action changeCaseToUppercaseAction = new LocalizableAction("toUppercase", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            doSelectionOperation(text -> text.toUpperCase(Locale.of(flp.getLanguage())));
        }
    };

    private final Action changeCaseToLowercaseAction = new LocalizableAction("toLowercase", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            doSelectionOperation(text -> text.toLowerCase(Locale.of(flp.getLanguage())));
        }
    };

    private final Action changeCaseInvertCaseAction = new LocalizableAction("invertCase", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            doSelectionOperation(text -> text.chars()
                    .map(c -> {
                        if(Character.isLowerCase(c)){
                            return Character.toUpperCase(c);
                        } else if (Character.isUpperCase(c)){
                            return Character.toLowerCase(c);
                        } else {
                            return c;
                        }
                    })
                    .mapToObj(Character::toString)
                    .collect(Collectors.joining())
            );
        }
    };

    private final Action sortAscendingAction = new LocalizableAction("sortAscending", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Comparator<Object> comparator = Collator.getInstance(Locale.of(flp.getLanguage()));

            doSelectedLinesOperation(s ->
                    s.lines()
                            .sorted(comparator)
                            .map(l -> l + "\n")
                            .collect(Collectors.joining())
            );
        }
    };

    private final Action sortDescendingAction = new LocalizableAction("sortDescending", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            Comparator<Object> comparator = Collator.getInstance(Locale.of(flp.getLanguage())).reversed();

            doSelectedLinesOperation(s ->
                    s.lines()
                    .sorted(comparator)
                    .map(l -> l + "\n")
                    .collect(Collectors.joining())
            );
        }
    };

    private final Action uniqueAction = new LocalizableAction("unique", flp) {
        @Override
        public void actionPerformed(ActionEvent e) {
            doSelectedLinesOperation(s ->
                    s.lines()
                            .distinct()
                            .map(l -> l + "\n")
                            .collect(Collectors.joining())
            );
        }
    };

    private void doSelectedLinesOperation(Function<String, String> function){
        SingleDocumentModel currentModel = mdm.getCurrentDocument();

        if(currentModel != null){
            try {
                JTextArea textArea = currentModel.getTextComponent();
                Caret caret = currentModel.getTextComponent().getCaret();
                Document document = textArea.getDocument();

                int from = Math.min(caret.getDot(), caret.getMark());
                from = textArea.getLineStartOffset(textArea.getLineOfOffset(from));

                int to = Math.max(caret.getDot(), caret.getMark());
                to = textArea.getLineEndOffset(textArea.getLineOfOffset(to));

                String text = document.getText(from, to - from);

                text = function.apply(text);

                document.remove(from, to - from);
                textArea.insert(text, from);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void doSelectionOperation(Function<String, String> function){
        SingleDocumentModel currentModel = mdm.getCurrentDocument();

        if(currentModel != null){
            Caret caret = currentModel.getTextComponent().getCaret();
            int from = Math.min(caret.getDot(), caret.getMark());
            int to = Math.max(caret.getDot(), caret.getMark());

            Document document = currentModel.getTextComponent().getDocument();

            try {
                String text = document.getText(from, to - from);

                text = function.apply(text);

                document.remove(from, to - from);
                currentModel.getTextComponent().insert(text, from);

            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private final List<Action> selectionOnlyActions = List.of(
            changeCaseToUppercaseAction, changeCaseToLowercaseAction, changeCaseInvertCaseAction,
            sortAscendingAction, sortDescendingAction, uniqueAction
    );

    private final CaretListener selectionOnlyActionListener = e -> {
        if(e.getDot() == e.getMark()){
            selectionOnlyActions.forEach(action -> action.setEnabled(false));
        } else {
            selectionOnlyActions.forEach(action -> action.setEnabled(true));
        }
    };

    private void showOpenDocumentFirstDialog() {
        JOptionPane.showMessageDialog(
                JNotepadPP.this,
                flp.getString("openDocumentFirst"),
                flp.getString("error"),
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void saveDocument(SingleDocumentModel model, Path path){
        try {
            mdm.saveDocument(model, path);
        } catch (IOException e) {
            showFileSaveErrorDialog(path.toAbsolutePath().toString());
        } catch (DocumentOpenedException e) {
            showDocumentOpenedErrorDialog(path.toString());
        }
    }

    private int showUnsavedChangesDialog(String fileName){
        String yes = flp.getString("yes");
        String no = flp.getString("no");
        String cancel = flp.getString("cancel");

        String[] options = {yes, no, cancel};
        return JOptionPane.showOptionDialog(
                JNotepadPP.this,
                flp.getString("saveModifiedDocument") + " \"" + fileName + "\"?",
                flp.getString("saveModifiedDocumentTitle"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    private void showFileSaveErrorDialog(String fileName){
        JOptionPane.showMessageDialog(
                JNotepadPP.this,
                flp.getString("saveDocumentError") +
                        fileName,
                flp.getString("error"),
                JOptionPane.ERROR_MESSAGE);
    }

    private void showDocumentOpenedErrorDialog(String documentName){
        JOptionPane.showMessageDialog(
                JNotepadPP.this,
                flp.getString("documentAlreadyOpened") + ": \"" +
                        documentName + "\". " +
                        flp.getString("tryClosingDocument"),
                flp.getString("documentOpenedErrorTitle"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void initActionValues() {
        createBlankDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control pressed N"));
        openFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control pressed O"));
        saveFileAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control pressed S"));
        saveFileAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt control pressed S"));
        closeDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control pressed Q"));
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control alt pressed Q"));

        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control pressed X"));
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control pressed C"));
        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control pressed V"));

        statisticalInfoAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control pressed I"));

        changeCaseToUppercaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift pressed U"));
        changeCaseToLowercaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift pressed L"));
        changeCaseInvertCaseAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift pressed I"));

        sortAscendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift pressed A"));
        sortDescendingAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift pressed D"));

        uniqueAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift pressed Q"));

        selectionOnlyActions.forEach(action -> action.setEnabled(false));
    }
    private void initHotkeys() {

        Map<String, Action> keyActionMap = new HashMap<>();

        keyActionMap.put("createBlankDocument", createBlankDocumentAction);
        keyActionMap.put("openFile", openFileAction);
        keyActionMap.put("saveFile", saveFileAction);
        keyActionMap.put("saveFileAs", saveFileAsAction);
        keyActionMap.put("closeDocument", closeDocumentAction);
        keyActionMap.put("exit", exitAction);
        keyActionMap.put("info", statisticalInfoAction);
        keyActionMap.put("changeToUppercase", changeCaseToUppercaseAction);
        keyActionMap.put("changeToLowercase", changeCaseToLowercaseAction);
        keyActionMap.put("invertCase", changeCaseInvertCaseAction);
        keyActionMap.put("sortAscending", sortAscendingAction);
        keyActionMap.put("sortDescending", sortDescendingAction);
        keyActionMap.put("unique", uniqueAction);

        InputMap imap = mdm.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap amap = mdm.getActionMap();

        for(var entry: keyActionMap.entrySet()){
            imap.put((KeyStroke) entry.getValue().getValue(Action.ACCELERATOR_KEY), entry.getKey());
            amap.put(entry.getKey(), entry.getValue());
        }
    }

    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new LJMenu("fileMenu", flp);
        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(createBlankDocumentAction));
        fileMenu.add(new JMenuItem(openFileAction));
        fileMenu.add(new JMenuItem(saveFileAction));
        fileMenu.add(new JMenuItem(saveFileAsAction));
        fileMenu.add(new JMenuItem(closeDocumentAction));

        JMenu editMenu = new LJMenu("editMenu", flp);
        menuBar.add(editMenu);

        editMenu.add(new JMenuItem(cutAction));
        editMenu.add(new JMenuItem(copyAction));
        editMenu.add(new JMenuItem(pasteAction));

        JMenu toolsMenu = new LJMenu("toolsMenu", flp);
        menuBar.add(toolsMenu);

        JMenu changeCaseSubmenu = new LJMenu("changeCaseSubmenu", flp);
        toolsMenu.add(changeCaseSubmenu);

        changeCaseSubmenu.add(changeCaseToUppercaseAction);
        changeCaseSubmenu.add(changeCaseToLowercaseAction);
        changeCaseSubmenu.add(changeCaseInvertCaseAction);

        JMenu sortSubmenu = new LJMenu("sortSubmenu", flp);
        toolsMenu.add(sortSubmenu);

        sortSubmenu.add(sortAscendingAction);
        sortSubmenu.add(sortDescendingAction);

        toolsMenu.add(uniqueAction);

        JMenu statsMenu = new LJMenu("statsMenu", flp);
        menuBar.add(statsMenu);

        statsMenu.add(new JMenuItem(statisticalInfoAction));

        JMenu languagesMenu = new LJMenu("languagesMenu", flp);
        menuBar.add(languagesMenu);

        LocalizationProvider.getSupportedLanguages().forEach(language ->
                languagesMenu.add(new LocalizableAction(language, flp) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LocalizationProvider.getInstance().setLanguage(language);
                    }
                })
        );
    }

    private void createToolbar() {
        JToolBar toolBar = new JToolBar();
        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        toolBar.add(createBlankDocumentAction);
        toolBar.add(openFileAction);
        toolBar.add(saveFileAction);
        toolBar.add(cutAction);
        toolBar.add(copyAction);
        toolBar.add(pasteAction);
    }

    private void createCenter() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        centerPanel.add(mdm.getVisualComponent(), BorderLayout.CENTER);

        JPanel statusBarPanel = sbm.getVisualComponent();
        centerPanel.add(statusBarPanel, BorderLayout.PAGE_END);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JNotepadPP().setVisible(true);
        });
    }

}
