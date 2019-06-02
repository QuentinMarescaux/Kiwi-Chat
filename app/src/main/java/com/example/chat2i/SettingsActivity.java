package com.example.chat2i;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;

public class SettingsActivity extends PreferenceActivity {

    // A déclarer dans le MANIFESTE !
    // Cf. <activity android:name=".SettingsActivity"></activity>
    // dans la balise <application>


    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Utiliser des fragments plutot qu'une activité 'préférences'
        //noinspection deprecation
        addPreferencesFromResource(R.xml.preferences);
    }
}
