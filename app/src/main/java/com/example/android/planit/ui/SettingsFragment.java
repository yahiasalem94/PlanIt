package com.example.android.planit.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.android.planit.R;
import com.example.android.planit.utils.LocaleManager;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();
    private String english;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        english = getResources().getStringArray(R.array.settings_list_preference_values)[0];
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.app_preferences);
        setListSummary(getResources().getString(R.string.language_preference));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        Log.d(TAG, sharedPreferences.getString( ) + " " + key);
        if (key.equals(getResources().getString(R.string.language_preference))) {
            // Set summary to be the user-description for the selected value
            ListPreference pref = findPreference(key);
            setListSummary(key);
           if (pref.getValue().equals(english)) {
               setNewLocale((AppCompatActivity)getActivity(), LocaleManager.ENGLISH);
           } else {
               setNewLocale((AppCompatActivity)getActivity(), LocaleManager.DUTCH);
           }

        }
    }

    private void setListSummary(String key) {
        ListPreference pref = findPreference(key);
        pref.setSummary(pref.getEntry());
    }

    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(getActivity(), language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }


}
