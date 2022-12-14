package com.kh_kerbabian.savememoney.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.kh_kerbabian.savememoney.R;

public class SettingsFragment extends PreferenceFragmentCompat {


    private SharedPreferences sharedPref;
    String settingsDarkTheme = "xmlDarkTheme";
    String settingsNotification = "xmlNotification";
    String settingsVibration = "xmlVibration";





    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext().getApplicationContext());
        int[] values = new int[2];
        values[0] = sharedPref.getBoolean(settingsDarkTheme, false) == true ? 1 : 0;
        values[1] = sharedPref.getBoolean(settingsNotification, false) == true ? 1 : 0;
        if( sharedPref.getBoolean(settingsDarkTheme, false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext().getApplicationContext());
        int[] values = new int[2];
        values[0] = sharedPref.getBoolean(settingsDarkTheme, false) == true ? 1 : 0;
        values[1] = sharedPref.getBoolean(settingsNotification, false) == true ? 1 : 0;
        if( sharedPref.getBoolean(settingsDarkTheme, false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);




    }



}