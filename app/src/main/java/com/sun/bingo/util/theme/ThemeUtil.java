package com.sun.bingo.util.theme;

import android.app.Activity;
import android.content.Context;

import com.sun.bingo.R;
import com.sun.bingo.sharedpreferences.SettingsSharedPreferences;

import de.devland.esperandro.Esperandro;

/**
 * Created by sunfusheng on 15/8/10.
 */
public class ThemeUtil {

    public static void changeTheme(Activity activity, ColorEnum theme) {
        if (activity == null)
            return;
        int style;
        switch (theme) {
            case RED:
                style = R.style.RedTheme;
                break;
            case PINK:
                style = R.style.PinkTheme;
                break;
            case PURPLE:
                style = R.style.PurpleTheme;
                break;
            case INDIGO:
                style = R.style.IndigoTheme;
                break;
            case BLUE:
                style = R.style.BlueTheme;
                break;
            case CYAN:
                style = R.style.CyanTheme;
                break;
            case TEAL:
                style = R.style.TealTheme;
                break;
            case GREEN:
                style = R.style.GreenTheme;
                break;
            case ORANGE:
                style = R.style.OrangeTheme;
                break;
            case YELLOW:
                style = R.style.YellowTheme;
                break;
            case BROWN:
                style = R.style.BrownTheme;
                break;
            case GREY:
                style = R.style.GreyTheme;
                break;
            default:
                style = R.style.BlueTheme;
                break;
        }
        activity.setTheme(style);
    }

    public static void changeTheme(Activity activity) {
        changeTheme(activity, getCurrentColorEnum(activity));
    }

    public static ColorEnum getCurrentColorEnum(Context context){
        int value = Esperandro.getPreferences(SettingsSharedPreferences.class, context).themeValue();
        return ColorEnum.mapValueToTheme(value);
    }

}
