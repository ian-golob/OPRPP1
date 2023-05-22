package hr.fer.oprpp1.hw08.jnotepadpp.model;

import hr.fer.oprpp1.hw08.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LJLabel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowListener;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class StatusBarModel extends JPanel implements MultipleDocumentListener {

    private final FormLocalizationProvider flp;

    private final JStatusBarLabel length;

    private final JStatusBarLabel lineNumber;

    private final JStatusBarLabel column;

    private final JStatusBarLabel selected;

    private final JClock clock;

    private final DocumentListener documentListener = new DocumentListener() {
        private void updateLength(DocumentEvent e) {
            length.setValue(e.getDocument().getLength());
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateLength(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateLength(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateLength(e);
        }
    };

    private final CaretListener caretListener = new CaretListener() {
        @Override
        public void caretUpdate(CaretEvent e) {
            JTextArea source = (JTextArea) e.getSource();

            try {
                lineNumber.setValue(source.getLineOfOffset(e.getDot()) + 1);
                column.setValue(e.getDot() - source.getLineStartOffset(source.getLineOfOffset(e.getDot())) + 1);
            } catch (BadLocationException ex) {
                lineNumber.clearValue();
                column.clearValue();
            }

            int selectedLength = Math.abs(e.getDot() - e.getMark());
            if(selectedLength != 0){
                selected.setValue(selectedLength);
            } else {
                selected.clearValue();
            }
        }
    };

    public StatusBarModel(FormLocalizationProvider flp) {
        this.flp = flp;

        length = new JStatusBarLabel("statusBarLength", flp);
        lineNumber = new JStatusBarLabel("statusBarLineNumber", flp);
        column = new JStatusBarLabel("statusBarColumn", flp);
        selected = new JStatusBarLabel("statusBarSelected", flp);
        clock = new JClock();

        initGui();
    }

    public JPanel getVisualComponent(){
        return this;
    }

    private void initGui() {
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        setBackground(Color.GRAY);
        setLayout(new GridLayout(1, 3, 2, 0));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        add(leftPanel);
        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(middlePanel);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(rightPanel);

        leftPanel.add(length);

        middlePanel.add(lineNumber);
        middlePanel.add(column);
        middlePanel.add(selected);

        rightPanel.add(clock);
    }

    @Override
    public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
        if(previousModel != null){
            previousModel.getTextComponent().removeCaretListener(caretListener);
            previousModel.getTextComponent().getDocument().removeDocumentListener(documentListener);
        }

        clearAllValues();
        if(currentModel != null){
            length.setValue(currentModel.getTextComponent().getDocument().getLength());

            currentModel.getTextComponent().addCaretListener(caretListener);
            currentModel.getTextComponent().getDocument().addDocumentListener(documentListener);
        }
    }

    private void clearAllValues() {
        length.clearValue();
        lineNumber.clearValue();
        column.clearValue();
        selected.clearValue();
    }

    @Override
    public void documentAdded(SingleDocumentModel model) {

    }

    @Override
    public void documentRemoved(SingleDocumentModel model) {

    }

    public static class JStatusBarLabel extends LJLabel {
        private Long value;

        public JStatusBarLabel(String key, ILocalizationProvider provider) {
            super(key, provider);

            value = null;
            updateLabel();
        }

        public void setValue(long value){
            this.value = value;
            updateLabel();
        }

        public void clearValue(){
            value = null;
            updateLabel();
        }

        @Override
        public void updateLabel() {
            super.updateLabel();
            if(value == null){
                setText(getText() + ": -");
            }else {
                setText(getText() + ": " + value);
            }
        }
    }

    public static class JClock extends JLabel{

        private volatile boolean stop = false;

        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        public JClock(){
            updateTime();

            Thread t = new Thread(()->{
                while(true) {
                    try {
                        Thread.sleep(500);
                    } catch(Exception ex) {}
                    if(stop) break;
                    SwingUtilities.invokeLater(this::updateTime);
                }
            });
            t.setDaemon(true);
            t.start();
        }
        private void updateTime() {
            setText(formatter.format(LocalDateTime.now()));
        }

        public void stop(){
            stop = true;
        }

    }

    public void stopClock(){
        clock.stop();
    }

}
