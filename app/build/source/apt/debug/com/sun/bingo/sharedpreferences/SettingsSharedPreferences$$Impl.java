package com.sun.bingo.sharedpreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

public class SettingsSharedPreferences$$Impl
    implements SettingsSharedPreferences, de.devland.esperandro.SharedPreferenceActions {

  private SharedPreferences preferences;

  public SettingsSharedPreferences$$Impl(Context context) {
    this.preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
  }

  @Override
  public boolean isReceivePush() {
    return preferences.getBoolean("isReceivePush", false);
  }

  @Override
  @SuppressLint("NewApi")
  public void isReceivePush(boolean isReceivePush) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
      preferences.edit().putBoolean("isReceivePush", isReceivePush).commit();
    } else {
      preferences.edit().putBoolean("isReceivePush", isReceivePush).apply();
    }
  }

  @Override
  public SharedPreferences get() {
    return preferences;
  }

  @Override
  public boolean contains(String key) {
    return preferences.contains(key);
  }

  @Override
  @SuppressLint("NewApi")
  public void remove(String key) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
      preferences.edit().remove(key).commit();
    } else {
      preferences.edit().remove(key).apply();
    }
  }

  @Override
  public void registerOnChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener listener) {
    preferences.registerOnSharedPreferenceChangeListener(listener);
  }

  @Override
  public void unregisterOnChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener listener) {
    preferences.unregisterOnSharedPreferenceChangeListener(listener);
  }

  @Override
  @SuppressLint("NewApi")
  public void clear() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
      preferences.edit().clear().commit();
    } else {
      preferences.edit().clear().apply();
    }
  }

  @Override
  @SuppressLint("NewApi")
  public void clearDefined() {
    SharedPreferences.Editor editor = preferences.edit();
    editor.remove("isReceivePush");
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
      editor.commit();
    } else {
      editor.apply();
    }
  }

  @Override
  public void initDefaults() {
    this.isReceivePush(this.isReceivePush());
  }

}
