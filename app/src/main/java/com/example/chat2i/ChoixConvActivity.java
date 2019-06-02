package com.example.chat2i;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.List;

public class ChoixConvActivity extends RestActivity implements View.OnClickListener {

    private ListeConversations listeConvs;
    private Button btnOK;
    private Spinner sp;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_conversation);

        // Au démarrage de l'activité, réaliser une requete
        // Pour récupérer les conversations
        String qs = "action=getConversations";

        // On se sert des services offerts par RestActivity,
        // qui propose des méthodes d'envoi de requetes asynchrones
        envoiRequete(qs, "recupConversations");

        listeConvs = new ListeConversations();

        btnOK = findViewById(R.id.choixConversation_btnOK);
        btnOK.setOnClickListener(this);

        sp = (Spinner) findViewById(R.id.choixConversation_choixConv);

    }


    @Override
    public void traiteReponse(@NonNull JSONObject o, @NonNull String action) {
        if (action.contentEquals("recupConversations")) {
            gs.alerter("attention le json arrive");
            gs.alerter(o.toString());

            // On transforme notre objet JSON en une liste de "Conversations"
            // On pourrait utiliser la librairie GSON pour automatiser ce processus d'interprétation
            // des objets JSON reçus
            // Cf. poly de centrale "programmation mobile et réalité augmentée"

            // Ici, on se contente de créer une classe "Conversation" et une classe "ListeConversations"
            // On parcourt l’objet JSON pour instancier des Conversations, que l’on insère dans la liste

            /*
             * {"connecte":true,
             * "action":"getConversations",
             * "feedback":"entrez action: logout, setPasse(passe),setPseudo(pseudo), setCouleur(couleur),getConversations, getMessages(idConv,[idLastMessage]), setMessage(idConv,contenu), ...",
             * "conversations":[ {"id":"12","active":"1","theme":"Les cours en IAM"},
             *                   {"id":"2","active":"1","theme":"Ballon d'Or"}]}
             * */


            JSONArray convs = null;
            List<Conversation> conversationList = new ArrayList<Conversation>();

            try {
                /**
                 * AVEC GSON
                 */
                //on récupère les conversations dans le JSON
                convs = o.getJSONArray("conversations");

                //on récupère le type avec TypeToken. Elle va permettre à la librairie de connaitre
                //le type de retour de notre list car il ne peut pas être déterminé à l'execution
                Type listType = new TypeToken<ArrayList<Conversation>>(){}.getType();
                //on crée le Gson qui va transformer le JSONArray en objet Conversation qui sera placé dans une List<Conversation>
                conversationList = new Gson().fromJson(String.valueOf(convs), listType);

                //parcours de la List<Conversation> pour afficher comme précédemment les conversations
                for(Conversation c : conversationList){
                    gs.alerter("Conv " + c.getId()  + " / theme = " + c.getTheme() + " / active ?" + c.getActive());
                }

                /**
                 * SANS GSON
                 */
/*
                int i;
                try {
                    convs = o.getJSONArray("conversations");
                    for(i=0;i<convs.length();i++) {
                    JSONObject nextConv = (JSONObject) convs.get(i);

                    int id =Integer.parseInt(nextConv.getString("id"));
                    String theme = nextConv.getString("theme");
                    Boolean active = ((String) nextConv.getString("active")).contentEquals("1");

                    gs.alerter("Conv " + id  + " theme = " + theme + " active ?" + active);
                    Conversation c = new Conversation(id,theme,active);

                    listeConvs.addConversation(c);
                }
*/
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
    }

    private void remplirSpinner() {

        // V1 : utilisation d'un spinner
        // https://www.mkyong.com/android/android-spinner-drop-down-list-example/

        // On ne doit déclencher cette méthode que lorsque la liste des conversations
        // est complète

        // V1 : Adaptateur simple (on affiche juste les noms des conversations)
        // Cet adaptateur utilise la méthode toString() des conversations
        // Pour choisir le contenu à afficher dans les items du menu

        ArrayAdapter<Conversation> dataAdapter =
                new ArrayAdapter<Conversation>(this,
                android.R.layout.simple_spinner_item, listeConvs.getList());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(dataAdapter);

    }


    private void remplirSpinner2() {
        // V2 : Utilisation d'un adapteur customisé qui permet de définir nous-même
        // la forme des éléments à afficher

        sp.setAdapter(new MyCustomAdapter(this,
                                            R.layout.spinner_item,
                                            listeConvs.getList()));

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
