package com.example.chat2i;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShowConvActivity extends RestActivity implements View.OnClickListener {

    private int idConv;
    private int idLastMessage = 0;

    private LinearLayout msgLayout;
    private Button btnOK;
    private EditText edtMsg;

    @Override
    public void traiteReponse(@NonNull JSONObject o, @NonNull String action) {
        if (action.contentEquals("posterMessage")) {
            gs.alerter("retour de la requete posterMessage");
        }
        if (action.contentEquals("chargement_messages")) {
            // On a reçu des messages
            // gs.alerter(o.toString());
           /* {"connecte":true,
            "action":"getMessages",
            "feedback":"entrez action: logout, setPasse(passe),setPseudo(pseudo),
            setCouleur(couleur),getConversations,
            getMessages(idConv,[idLastMessage]),
            setMessage(idConv,contenu), ...",
            "messages":[{"id":"35",
                            "contenu":"Que pensez-vous des cours en IAM ?",
                            "auteur":"Tom",
                            "couleur":"#ff0000"}]
            ,"idLastMessage":"35"}
            */

            try {

                // parcours des messages
                JSONArray messages = o.getJSONArray("messages");
                List<Message> messageList = new ArrayList<Message>();

                //Utilisation de Gson pour transformer le Json en objet Message
                Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
                messageList = new Gson().fromJson(String.valueOf(messages), listType);

                for(Message m : messageList){
                    TextView tv2 = new TextView(this);
                    tv2.setText("[" + m.getAuteur() + "]" + m.getContenu());
                   // tv2.setTextColor(Color.parseColor(m.getCouleur()));
                    msgLayout.addView(tv2);
                }
/*
                int i;
                for(i=0;i<messages.length();i++) {
                    JSONObject msg = (JSONObject) messages.get(i);
                    String contenu =  msg.getString("contenu");
                    String auteur =  msg.getString("auteur");
                    String couleur =  msg.getString("couleur");

                    TextView tv = new TextView(this);
                    tv.setText("[" + auteur + "] " + contenu);
                    //tv.setTextColor(Color.parseColor(couleur));

                    msgLayout.addView(tv);

                }
*/
                // mise à jour du numéro du dernier message
                idLastMessage = Integer.parseInt(o.getString("idLastMessage"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_conversation);

        Bundle bdl = getIntent().getExtras();
        idConv = bdl.getInt("idConversation");

        gs.alerter(Integer.toString(idConv));

        // On récupère la liste des messages périodiquement
        // action=getMessages&idConv=<ID>

        // NB : l'API fournit une route
        // qui permet d'indiquer le dernier message dont on dispose
        // action=getMessages&idConv=<ID>&idLastMessage=<NUMERO>

        requetePeriodique(10, "chargement_messages");
        msgLayout = findViewById(R.id.conversation_svLayoutMessages);

        btnOK = findViewById(R.id.conversation_btnOK);
        btnOK.setOnClickListener(this);

        edtMsg = findViewById(R.id.conversation_edtMessage);
    }

    @NonNull
    public String urlPeriodique(@NonNull String action) {
        String qs = "";
        if (action.equals("chargement_messages")) {
            qs = "action=getMessages&idConv=" + idConv;
            qs += "&idLastMessage=" + idLastMessage;
        }

        return qs;
    }

    @Override
    public void onClick(@NonNull View v) {
        // Clic sur OK : on récupère le message
        // conversation_edtMessage

        String msg = edtMsg.getText().toString();
        String qs="action=setMessage&idConv=" + idConv +"&contenu=" + msg;

        envoiRequete(qs,"posterMessage");

        edtMsg.setText("");
    }
}
