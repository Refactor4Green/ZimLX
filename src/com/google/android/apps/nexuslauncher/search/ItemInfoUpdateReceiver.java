package com.google.android.apps.nexuslauncher.search;

import android.content.SharedPreferences;

import com.android.launcher3.ItemInfoWithIcon;
import com.android.launcher3.Launcher;
import com.android.launcher3.LauncherCallbacks;
import com.android.launcher3.icons.IconCache;

public class ItemInfoUpdateReceiver implements IconCache.ItemInfoUpdateReceiver, SharedPreferences.OnSharedPreferenceChangeListener {
    private final LauncherCallbacks mCallbacks;
    private final int eD;
    private final Launcher mLauncher;

    public ItemInfoUpdateReceiver(final Launcher launcher, final LauncherCallbacks callbacks) {
        this.mLauncher = launcher;
        this.mCallbacks = callbacks;
        this.eD = launcher.getDeviceProfile().inv.numColumns;
    }

    public void di() {
    }

    public void onCreate() {
        mLauncher.getSharedPrefs().registerOnSharedPreferenceChangeListener(this);
    }

    public void onDestroy() {
        mLauncher.getSharedPrefs().unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String s) {
        if ("reflection_last_predictions".equals(s) || "pref_show_predictions".equals(s)) {
            this.di();
        }
    }

    public void reapplyItemInfo(final ItemInfoWithIcon itemInfoWithIcon) {
    }
}
