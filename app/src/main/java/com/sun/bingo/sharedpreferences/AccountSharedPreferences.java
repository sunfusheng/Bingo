package com.sun.bingo.sharedpreferences;

import de.devland.esperandro.SharedPreferenceActions;
import de.devland.esperandro.SharedPreferenceMode;
import de.devland.esperandro.annotations.SharedPreferences;

/**
 * Created by sunfusheng on 2015/6/25.
 */
@SharedPreferences(name = "account", mode = SharedPreferenceMode.PRIVATE)
public interface AccountSharedPreferences extends SharedPreferenceActions {

    String uid();
    void uid(String uid);

    String access_token();
    void access_token(String access_token);

    String refresh_token();
    void refresh_token(String refresh_token);

    long expires_in();
    void expires_in(long expires_in);

}
