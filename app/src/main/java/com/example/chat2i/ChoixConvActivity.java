package com.example.chat2i;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.List;

public class ChoixConvActivity extends AppCompatActivity implements View.OnClickListener {

    private GlobalState gs;
    private Button btnOK;
    private Spinner sp;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gs = (GlobalState) getApplication();
        setContentView(R.layout.activity_choix_conversation);

        sp = (Spinner) findViewById(R.id.choixConversation_choixConv);

        // Au démarrage de l'activité, réaliser une requete
        //  pour récupérer les conversations
        String qs = "action=getConversations";

        // On se sert des services offerts par librairie Volley
        //  pour effectuer nos requêtes
        gs.volleyJsonObjectRequest(qs,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        createConversationList(response);
                    }
                });

        btnOK = findViewById(R.id.choixConversation_btnOK);
        btnOK.setOnClickListener(this);
    }


    public void createConversationList(JSONObject response) {
        gs.alerter("coucou");
        JSONArray convs = null;
        List<Conversation> conversationList = new ArrayList<Conversation>();
        try {
            if (response.getString("action").contentEquals("getConversations")) {
                gs.alerter(response.toString());

                /**
                 * AVEC GSON
                 */
                //on récupère les conversations dans le JSON
                convs = response.getJSONArray("conversations");

                //on récupère le type avec TypeToken. Elle va permettre à la librairie de connaitre
                //le type de retour de notre list car il ne peut pas être déterminé à l'execution
                Type listType = new TypeToken<ArrayList<Conversation>>(){}.getType();
                //on crée le Gson qui va transformer le JSONArray en objet Conversation qui sera placé dans une List<Conversation>
                conversationList = new Gson().fromJson(String.valueOf(convs), listType);

                //parcours de la List<Conversation> pour afficher comme précédemment les conversations
                for(Conversation c : conversationList){
                    gs.alerter("Conv " + c.getId()  + " / theme = " + c.getTheme() + " / active ?" + c.getActive());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //gs.alerter(listeConvs.toString());
        gs.alerter(conversationList.toString());

        // On peut maintenant appuyer sur le bouton
        btnOK.setEnabled(true);
        sp.setAdapter(new MyCustomAdapter(this,
                R.layout.spinner_item,
                (ArrayList<Conversation>) conversationList));
    }

    @Override
    public void onClick(@NonNull View v) {
        // lors du clic sur le bouton OK,
        // récupérer l'id de la conversation sélectionnée
        // démarrer l'activité d'affichage des messages

        // NB : il faudrait être sur qu'on ne clique pas sur le bouton
        // tant qu'on a pas fini de charger la liste des conversations
        // On indique que le bouton est désactivé au départ.

        Conversation convSelected = (Conversation) sp.getSelectedItem();
        gs.alerter("Conv sélectionnée : " + convSelected.getTheme()
                        + " id=" + convSelected.getId());

        // On crée un Intent pour changer d'activité
        Intent toShowConv = new Intent(this,ShowConvActivity.class);
        Bundle bdl = new Bundle();
        bdl.putInt("idConversation",convSelected.getId());
        toShowConv.putExtras(bdl);
        startActivity(toShowConv);
    }

    public class MyCustomAdapter extends ArrayAdapter<Conversation> {

        private int layoutId;
        private ArrayList<Conversation> dataConvs;

        public MyCustomAdapter(@NonNull Context context,
                               int itemLayoutId,
                               @NonNull ArrayList<Conversation> objects) {
            super(context, itemLayoutId, objects);
            layoutId = itemLayoutId;
            dataConvs = objects;
        }



        @Override
        @NonNull
        public View getDropDownView(@NonNull int position, View convertView, ViewGroup parent) {
            //return getCustomView(position, convertView, parent);
            LayoutInflater inflater = getLayoutInflater();
            View item = inflater.inflate(layoutId, parent, false);
            Conversation nextC = dataConvs.get(position);

            TextView label = (TextView) item.findViewById(R.id.spinner_theme);
            label.setText(nextC.getTheme());

            ImageView icon = (ImageView) item.findViewById(R.id.spinner_icon);

            if (nextC.getActive()) {
                icon.setImageResource(R.drawable.icon36);
            } else {
                icon.setImageResource(R.drawable.icongray36);
            }

            return item;
        }

        @Override
        @NonNull
        public View getView(@NonNull  int position, View convertView, ViewGroup parent) {
            //return getCustomView(position, convertView, parent);
            LayoutInflater inflater = getLayoutInflater();
            View item = inflater.inflate(layoutId, parent, false);
            Conversation nextC = dataConvs.get(position);

            TextView label = (TextView) item.findViewById(R.id.spinner_theme);
            label.setText(nextC.getTheme());

            ImageView icon = (ImageView) item.findViewById(R.id.spinner_icon);

            if (nextC.getActive()) {
                icon.setImageResource(R.drawable.icon);
            } else {
                icon.setImageResource(R.drawable.icongray);
            }

            return item;
        }
    }

}
