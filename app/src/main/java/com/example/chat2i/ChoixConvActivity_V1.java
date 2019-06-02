package com.example.chat2i;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ChoixConvActivity_V1 extends AppCompatActivity {

    GlobalState gs;

    class JSONAsyncTask extends AsyncTask<String, Void, JSONObject> {
        // Params, Progress, Result

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("L4-SI-Logs","onPreExecute");
        }

        @Override
        protected JSONObject doInBackground(String... qs) {
            // String... qs est une ellipse:
            // permet de récupérer des arguments passés sous forme de liste arg1, arg2, arg3...
            // dans un tableau
            // pas d'interaction avec l'UI Thread ici
            Log.i("L4-SI-Logs","doInBackground");
            String res = ChoixConvActivity_V1.this.gs.requete(qs[0]);

            JSONObject ob = null;
            try {
                // TODO: interpréter le résultat sous forme d'objet JSON
                ob = new JSONObject(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ob; // TODO: renvoyer des JSONObject et pas des String
        }

        protected void onPostExecute(JSONObject result) {
            Log.i("L4-SI-Logs","onPostExecute");
            if (result != null ) {
                Log.i("L4-SI-Logs", result.toString());
                ChoixConvActivity_V1.this.gs.alerter(result.toString());

                // TODO: Vérifier la connexion ("connecte":true)
                try {
                    if (result.getBoolean("connecte")) {
                        // TODO: Changer d'activité vers choixConversation

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_conversation);
        gs = (GlobalState) getApplication();

        String qs = "action=getConversations";
        JSONAsyncTask js = new JSONAsyncTask();
        js.execute(qs);
    }
}
