package com.example.chat2i;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.NonNull;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
