package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;

public abstract class LocalizableAction extends AbstractAction {

    private static final long serialVersionUID = 1234951L;

    private final String key;

    private final ILocalizationProvider provider;

    private final ILocalizationListener listener = this::updateName;

    public LocalizableAction(String key, ILocalizationProvider provider){
        this.key = key;
        this.provider = provider;

        provider.addLocalizationListener(listener);
        updateName();
    }

    public void updateName(){
        String newName = provider.getString(key);
        String oldName = (String) getValue(NAME);

        if(!newName.equals(oldName)){
            putValue(NAME, newName);

            firePropertyChange(NAME, oldName, newName);
        }
    }
}
