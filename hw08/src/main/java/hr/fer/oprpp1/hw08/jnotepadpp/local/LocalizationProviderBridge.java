package hr.fer.oprpp1.hw08.jnotepadpp.local;

public class LocalizationProviderBridge extends AbstractLocalizationProvider {

    private final ILocalizationProvider parent;

    private final ILocalizationListener listener = this::fire;

    private boolean connected = false;

    private String currentLanguage = "";

    public LocalizationProviderBridge(ILocalizationProvider parent) {
        this.parent = parent;
    }

    @Override
    public String getString(String key) {
        return parent.getString(key);
    }

    @Override
    public String getLanguage() {
        return currentLanguage;
    }

    public void connect(){
        if(!connected){
            parent.addLocalizationListener(listener);
            connected = true;

            String parentLanguage = parent.getLanguage();
            if(!parentLanguage.equals(currentLanguage)){
                currentLanguage = parentLanguage;

                fire();
            }
        }
    }

    public void disconnect(){
        if(connected){
            parent.removeLocalizationListener(listener);

            connected = false;
        }
    }
}
