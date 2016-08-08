package org.gradle.common.util;

import java.util.Locale;

import org.gradle.common.enums.Territory;

public final class LocaleUtils {
	public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	
	public static Locale getLocale(Territory territory) {
        return getLocale(territory.name());
    }
    
    public static Locale getLocale(String territory) {
        if(territory == null) {
            return DEFAULT_LOCALE;
        }
            
        Locale locale = null;
        switch (territory) {
        case "TW":
            locale = new Locale("zh", "TW");
            break;
        case "HK":
            locale = new Locale("zh", "HK");
            break;
        case "SG":
            locale = DEFAULT_LOCALE;
            break;
        case "ID":
            locale = new Locale("in", "ID");
            break;
        default:
            locale = DEFAULT_LOCALE;
        }

        return locale;
    }
    
    public static Locale specifyLocale(Locale locale) {
        if("en".equals(locale.getLanguage())) {
            return DEFAULT_LOCALE;
        } else if ("zh".equals(locale.getLanguage()) && "TW".equals(locale.getCountry())) {
            return locale;
        } else if ("zh".equals(locale.getLanguage()) && "SG".equals(locale.getCountry())) {
            return DEFAULT_LOCALE;
        } else if("in".equals(locale.getLanguage())) {
            return new Locale("in", "ID");
        } else {
            return DEFAULT_LOCALE;
        }
    }
}
