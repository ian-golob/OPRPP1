package hr.fer.oprpp1.hw08.jnotepadpp.local;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FormLocalizationProvider extends LocalizationProviderBridge {

    public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame){
        super(parent);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                connect();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                disconnect();
            }
        });
    }

}
