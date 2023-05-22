package hr.fer.oprpp1.hw08.jnotepadpp.local;

public interface ILocalizationProvider {

    /**
     * Returns the localization of the given key string.
     * @param key Localization key.
     * @return Localization of the given key string.
     */
    String getString(String key);

    /**
     * Returns the current localization language.
     * @return The current localization language.
     */
    String getLanguage();

    /**
     * Adds the given localization listener
     * to the internal list of localization listeners.
     * @param listener The localization listener.
     */
    void addLocalizationListener(ILocalizationListener listener);

    /**
     * Removes the given localization listener
     * from the internal list of localization listeners.
     * @param listener The localization listener.
     */
    void removeLocalizationListener(ILocalizationListener listener);
}
