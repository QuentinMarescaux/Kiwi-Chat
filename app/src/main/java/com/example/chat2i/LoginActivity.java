package com.example.chat2i;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.support.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends DefaultActivity implements View.OnClickListener {

    EditText champLogin;
    EditText champPass;
    CheckBox champRemember;
    Button btnOK;

    class JSONAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            gs.log("[LoginActivity] onPreExecute");
        }

        @Override
        @NonNull
        protected JSONObject doInBackground(@Nullable String... qs) {
            // String... qs est une ellipse:
            // permet de récupérer des arguments passés sous forme de liste arg1, arg2, arg3...
            // dans un tableau
            // pas d'interaction avec l'UI Thread ici
            gs.log("[LoginActivity] doInBackground");
            String res = LoginActivity.this.gs.requete(qs[0]);

            JSONObject ob = null;
            try {
                ob = new JSONObject(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ob;
        }

        protected void onPostExecute(@Nullable JSONObject result) {
            gs.log("[LoginActivity] onPostExecute");
            if (result != null ) {
                gs.log(result.toString());

                try {
                    if (result.getBoolean("connecte")) {
                        LoginActivity.this.savePrefs();
                        Intent toChoixConv = new Intent(LoginActivity.this, ChoixConvActivity.class);
                        startActivity(toChoixConv);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gs.log("[LoginActivity] onCreate");

        setContentView(R.layout.activity_login);

        champLogin = findViewById(R.id.login_edtLogin);
        champPass = findViewById(R.id.login_edtPasse);
        champRemember = findViewById(R.id.login_cbRemember);
        btnOK = findViewById(R.id.login_btnOK);

        champRemember.setOnClickListener(this);
        btnOK.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        gs.log("[LoginActivity] onStart");

        // Récupérer les préférences, si la case 'remember' est cochée, on complète le formulaire
        // autres champs des préférences : urlData, login, passe
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean("remember",true)) {
            String login = prefs.getString("login", "");
            String passe = prefs.getString("passe", "");
            champLogin.setText(login);
            champPass.setText(passe);
            champRemember.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        gs.log("[LoginActivity] onResume");

        // Verif Réseau, si dispo, on active le bouton
        btnOK.setEnabled(gs.verifReseau());
    }

    private void savePrefs() {
        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();

        if (champRemember.isChecked()) {
            editor.putBoolean("remember", true);
            editor.putString("login", champLogin.getText().toString());
            editor.putString("passe", champPass.getText().toString());
        } else {
            editor.putBoolean("remember", false);
            editor.putString("login", "");
            editor.putString("passe", "");
        }
        editor.commit();
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.login_cbRemember : // Clic sur case à cocher
                savePrefs();
                break;
            case R.id.login_btnOK :
                String login = champLogin.getText().toString();
                String passe = champPass.getText().toString();

                String qs = "action=connexion&login=" + login + "&passe=" +passe;

                JSONAsyncTask js = new JSONAsyncTask();
                js.execute(qs);
                break;
        }
    }
}
