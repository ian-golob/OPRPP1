package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider {

    private static final LocalizationProvider instance = new LocalizationProvider();

    private String language;

    private ResourceBundle bundle;

    private LocalizationProvider(){
        language = "en";
        updateResourceBundle();
    }
    public static LocalizationProvider getInstance(){
        return instance;
    }
    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }

    @Override
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language){
        this.language = language;
        updateResourceBundle();

        fire();
    }

    private void updateResourceBundle() {
        Locale locale = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle("locale", locale);
    }

    public static List<String> getSupportedLanguages() {
        return List.of("en", "hr", "de");
    }
}
