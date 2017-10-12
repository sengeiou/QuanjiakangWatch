package com.quanjiakan.util.language;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by Administrator on 2017/10/12.
 */

public class LanguageSwitcher {
    private static final String KEY = "LanguageSwitcherSharePreferenceKey";

    public static final int LANGUAGE_SIMPLIFIED_CHINESE = 1;
    public static final int LANGUAGE_TRADITIONAL_CHINESE = 2;
    public static final int LANGUAGE_ENGLISH = 3;
    public static final int LANGUAGE_ENGLISH_US = 4;
    public static final int LANGUAGE_ENGLISH_UK = 5;
    public static final int LANGUAGE_JAPANESE = 6;
    public static final int LANGUAGE_KOREAN = 7;
    public static final int LANGUAGE_GERMANY = 8;
    public static final int LANGUAGE_FRENCH = 9;
    public static final int LANGUAGE_ITALIAN = 10;
    public static final int LANGUAGE_SYSTEM = 11;


    public static void applyLanguageChange(int languageType, Context context,ApplyLanguageSetting setting){

        setCurrentLanguageConfiguration(languageType,context);

        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        switch (languageType){
            case LANGUAGE_SIMPLIFIED_CHINESE:
                config.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case LANGUAGE_TRADITIONAL_CHINESE:
                config.locale = Locale.TRADITIONAL_CHINESE;
                break;
            case LANGUAGE_ENGLISH:
                config.locale = Locale.ENGLISH;
                break;
            case LANGUAGE_ENGLISH_US:
                config.locale = Locale.US;
                break;
            case LANGUAGE_ENGLISH_UK:
                config.locale = Locale.UK;
                break;
            case LANGUAGE_JAPANESE:
                config.locale = Locale.JAPANESE;
                break;
            case LANGUAGE_KOREAN:
                config.locale = Locale.KOREAN;
                break;
            case LANGUAGE_GERMANY:
                config.locale = Locale.GERMANY;
                break;
            case LANGUAGE_FRENCH:
                config.locale = Locale.FRENCH;
                break;
            case LANGUAGE_ITALIAN:
                config.locale = Locale.ITALIAN;
                break;
            default:
                config.locale = Locale.getDefault();
                if(config.locale!=Locale.SIMPLIFIED_CHINESE ||
                        config.locale!=Locale.TRADITIONAL_CHINESE ||
                        config.locale!=Locale.ENGLISH ||
                        config.locale!=Locale.US ||
                        config.locale!=Locale.UK ||
                        config.locale!=Locale.JAPANESE ||
                        config.locale!=Locale.KOREAN ||
                        config.locale!=Locale.GERMANY ||
                        config.locale!=Locale.FRENCH ||
                        config.locale!=Locale.ITALIAN){
                    config.locale = Locale.ENGLISH;
                }
                break;
        }
        resources.updateConfiguration(config, dm);

        if(setting!=null){
            setting.refreshViewValues();
        }
    }

    public static void setCurrentLanguageConfiguration(int languageType, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LanguageSwitcher.class.getSimpleName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY, languageType);
        editor.commit();
    }

    public static int getCurrentLanguageConfiguration(Context context){
        int currentLanguage = context.getSharedPreferences(LanguageSwitcher.class.getSimpleName(),Context.MODE_PRIVATE).getInt(KEY,LANGUAGE_SYSTEM);
        return currentLanguage;
    }
}
