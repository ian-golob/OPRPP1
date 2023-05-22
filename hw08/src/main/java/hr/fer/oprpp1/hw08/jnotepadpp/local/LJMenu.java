package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

public class LJMenu extends JMenu {
    private static final long serialVersionUID = 42343951L;

    private final String key;

    private final ILocalizationProvider provider;

    private final ILocalizationListener listener = this::updateText;

    public LJMenu(String key, ILocalizationProvider provider){
        this.key = key;
        this.provider = provider;

        provider.addLocalizationListener(listener);
        updateText();
    }

    public void updateText(){
        String newLabel = provider.getString(key);
        setText(newLabel);
    }
}
