/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.fnv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import com.android.settings.widget.NavBarItemPreference;
import com.android.settings.widget.SeekBarPreference;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class Extras extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String STATUS_BAR_AM_PM = "status_bar_am_pm";
    private static final String STATUS_BAR_CLOCK = "status_bar_show_clock";
    private static final String PREF_ENABLE = "clock_style";
    private static final String PREF_STATUSBAR_COLOR = "statusbar_background_color";
    private static final String PREF_NAV_BAR_COLOR = "interface_navbar_color";
    private static final String PREF_NAV_COLOR = "nav_button_color";
    private static final String PREF_NAV_GLOW_COLOR = "nav_button_glow_color";
    private static final String NAVIGATION_BAR_HEIGHT = "navigation_bar_height";
    private static final String NAVIGATION_BAR_HEIGHT_LANDSCAPE = "navigation_bar_height_landscape";
    private static final String NAVIGATION_BAR_WIDTH = "navigation_bar_width";

    private static final int DIALOG_NAVBAR_HEIGHT_REBOOT = 204;
    private static final int DEFAULT_NAVBAR_BG_COLOR = 0xFF000000;

    private ColorPickerPreference mStatusbarBgColor;
    private ColorPickerPreference mNavBar;
    private ColorPickerPreference mNavigationBarColor;
    private ColorPickerPreference mNavigationBarGlowColor;
    private SeekBarPreference mButtonAlpha;
    private ListPreference mStatusBarAmPm;
    private ListPreference mStatusBarClock;
    private ListPreference mNavigationBarHeight;
    private ListPreference mNavigationBarHeightLandscape;
    private ListPreference mNavigationBarWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.extras_settings);

        PreferenceScreen prefSet = getPreferenceScreen();

        mStatusBarClock = (ListPreference) prefSet.findPreference(PREF_ENABLE);
        mStatusBarClock.setOnPreferenceChangeListener(this);
        mStatusBarClock.setValue(Integer.toString(Settings.System.getInt(getActivity()
                .getContentResolver(), Settings.System.STATUS_BAR_CLOCK,
                1)));
        mStatusBarAmPm = (ListPreference) prefSet.findPreference(STATUS_BAR_AM_PM);
		mStatusBarAmPm.setOnPreferenceChangeListener(this);
		
		int statusBarAmPm = Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
		Settings.System.STATUS_BAR_AM_PM, 2);
		
		mStatusBarAmPm.setValue(String.valueOf(statusBarAmPm));
        mStatusBarAmPm.setSummary(mStatusBarAmPm.getEntry());

        mStatusbarBgColor = (ColorPickerPreference) prefSet.findPreference(PREF_STATUSBAR_COLOR);
        mStatusbarBgColor.setOnPreferenceChangeListener(this);

        mNavBar = (ColorPickerPreference) prefSet.findPreference(PREF_NAV_BAR_COLOR);
        mNavBar.setOnPreferenceChangeListener(this);

        mNavigationBarColor = (ColorPickerPreference) prefSet.findPreference(PREF_NAV_COLOR);
        mNavigationBarColor.setOnPreferenceChangeListener(this);

        mNavigationBarGlowColor = (ColorPickerPreference) prefSet.findPreference(PREF_NAV_GLOW_COLOR);
        mNavigationBarGlowColor.setOnPreferenceChangeListener(this);

        float defaultAlpha = Settings.System.getFloat(getActivity()
                .getContentResolver(), Settings.System.NAVIGATION_BAR_BUTTON_ALPHA, 0.6f);
        mButtonAlpha = (SeekBarPreference) prefSet.findPreference("button_transparency");
        mButtonAlpha.setInitValue((int) (defaultAlpha * 100));
        mButtonAlpha.setOnPreferenceChangeListener(this);

        mNavigationBarHeight = (ListPreference) prefSet.findPreference("navigation_bar_height");
        mNavigationBarHeight.setOnPreferenceChangeListener(this);

        mNavigationBarHeightLandscape = (ListPreference) prefSet.findPreference("navigation_bar_height_landscape");
        mNavigationBarHeightLandscape.setOnPreferenceChangeListener(this);

        mNavigationBarWidth = (ListPreference) prefSet.findPreference("navigation_bar_width");
        mNavigationBarWidth.setOnPreferenceChangeListener(this);
        
        //try {
        //    if (Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
        //            Settings.System.TIME_12_24) == 24) {
        //        mStatusBarAmPm.setEnabled(false);
        //        mStatusBarAmPm.setSummary(R.string.status_bar_am_pm_info);
        //    }
        //} catch (SettingNotFoundException e ) {
        //}

        //int statusBarAmPm = Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
        //        Settings.System.STATUS_BAR_AM_PM, 2);
        
        
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mStatusBarAmPm) {
            int statusBarAmPm = Integer.valueOf((String) newValue);
            int index = mStatusBarAmPm.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUS_BAR_AM_PM, statusBarAmPm);
            mStatusBarAmPm.setSummary(mStatusBarAmPm.getEntries()[index]);
            return true;
        } else if (preference == mStatusBarClock) {
            int clockStyle = Integer.parseInt((String) newValue);
            int index = mStatusBarClock.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUS_BAR_CLOCK, clockStyle);
            mStatusBarClock.setSummary(mStatusBarClock.getEntries()[index]);
            return true;
        } else if (preference == mStatusbarBgColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_BACKGROUND_COLOR, intHex);
            return true;
        } else if (preference == mNavBar) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.SYSTEMUI_NAVBAR_COLOR, intHex);
            return true;
        } else if (preference == mNavigationBarColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer.valueOf(String
                    .valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NAVIGATION_BAR_TINT, intHex);
            return true;
        } else if (preference == mNavigationBarGlowColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NAVIGATION_BAR_GLOW_TINT, intHex);
            return true;
        } else if (preference == mButtonAlpha) {
            float val = Float.parseFloat((String) newValue);
            Settings.System.putFloat(getActivity().getContentResolver(),
                    Settings.System.NAVIGATION_BAR_BUTTON_ALPHA, val / 100);
            return true;
        } else if (preference == mNavigationBarWidth) {
            String newVal = (String) newValue;
            int dp = Integer.parseInt(newVal);
            int width = mapChosenDpToPixels(dp);
            Settings.System.putInt(getContentResolver(), Settings.System.NAVIGATION_BAR_WIDTH,
                    width);
            showDialog(DIALOG_NAVBAR_HEIGHT_REBOOT);
            return true;
        } else if (preference == mNavigationBarHeight) {
            String newVal = (String) newValue;
            int dp = Integer.parseInt(newVal);
            int height = mapChosenDpToPixels(dp);
            Settings.System.putInt(getContentResolver(), Settings.System.NAVIGATION_BAR_HEIGHT,
                    height);
            showDialog(DIALOG_NAVBAR_HEIGHT_REBOOT);
            return true;
        } else if (preference == mNavigationBarHeightLandscape) {
            String newVal = (String) newValue;
            int dp = Integer.parseInt(newVal);
            int height = mapChosenDpToPixels(dp);
            Settings.System.putInt(getContentResolver(), Settings.System.NAVIGATION_BAR_HEIGHT_LANDSCAPE,
                    height);
            showDialog(DIALOG_NAVBAR_HEIGHT_REBOOT);
            return true;
        }
        return false;
    }

    @Override
    public Dialog onCreateDialog(int dialogId) {
        LayoutInflater factory = LayoutInflater.from(mContext);

        switch (dialogId) {
            case DIALOG_NAVBAR_HEIGHT_REBOOT:
                return new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.navbar_height_dialog_title))
                        .setMessage(
                                getResources().getString(R.string.navbar_height_dialog_summary))
                        .setCancelable(false)
                        .setNeutralButton(getResources().getString(R.string.navbar_height_dialog_button_later), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.navbar_height_dialog_button_reboot), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PowerManager pm = (PowerManager) getActivity()
                                        .getSystemService(Context.POWER_SERVICE);
                                pm.reboot("Rebooting with new bar height");
                            }
                        })
                        .create();
        }
        return null;
    }

    public int mapChosenDpToPixels(int dp) {
        switch (dp) {
            case 48:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_48);
            case 44:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_44);
            case 42:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_42);
            case 40:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_40);
            case 36:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_36);
            case 30:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_30);
            case 24:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_24);
            case 0:
                return getResources().getDimensionPixelSize(R.dimen.navigation_bar_0);
        }
        return -1;
    }
}