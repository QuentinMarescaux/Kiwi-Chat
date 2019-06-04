package com.example.chat2i.Controllers.Activities;

import android.preference.PreferenceActivity;

import com.example.chat2i.Controllers.Fragments.SettingsFragment;
import com.example.chat2i.R;

import java.util.List;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preferences_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SettingsFragment.class.getName().equals(fragmentName);
    }
}
