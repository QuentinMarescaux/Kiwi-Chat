package com.example.chat2i.Controllers.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.NonNull;

import com.example.chat2i.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
