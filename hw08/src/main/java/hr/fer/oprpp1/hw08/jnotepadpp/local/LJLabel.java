package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

public class LJLabel extends JLabel {

    private static final long serialVersionUID = 4234951L;

    private final String key;

    private final ILocalizationProvider provider;

    private final ILocalizationListener listener = this::updateLabel;

    public LJLabel(String key, ILocalizationProvider provider){
        this.key = key;
        this.provider = provider;

        provider.addLocalizationListener(listener);
        updateLabel();
    }

    public void updateLabel(){
        String newLabel = provider.getString(key);
        setText(newLabel);
    }

}
