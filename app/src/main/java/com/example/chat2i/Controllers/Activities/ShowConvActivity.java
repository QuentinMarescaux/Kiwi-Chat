package com.example.chat2i.Controllers.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.support.annotation.NonNull;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat2i.Models.Message;
import com.example.chat2i.R;
import com.example.chat2i.Utils.GlobalState;
import com.example.chat2i.Utils.VolleyCallback;
import com.example.chat2i.Views.MessageAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShowConvActivity extends DefaultActivity implements View.OnClickListener {

    private int idConv;
    private int idLastMessage = 0;

    private GlobalState gs;

    private RecyclerView recyclerView;
    private LinearLayout msgLayout;
    private Button btnOK;
    private EditText edtMsg;

    private List<Message> messages;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gs = (GlobalState) getApplication();
        setContentView(R.layout.activity_show_conversation);

        Bundle bdl = getIntent().getExtras();
        idConv = bdl.getInt("idConversation");

        gs.log("idConv : " + Integer.toString(idConv));

        // On configure le recyclerView
        this.messages = new ArrayList<>();
        this.adapter = new MessageAdapter(this.messages);
        this.recyclerView = (RecyclerView) findViewById(R.id.messages_recycler_view);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // On récupère la liste des messages périodiquement
        // action=getMessages&idConv=<ID>

        // NB : l'API fournit une route
        // qui permet d'indiquer le dernier message dont on dispose
        // action=getMessages&idConv=<ID>&idLastMessage=<NUMERO>

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                retrieveMessages();
                handler.postDelayed(this, 5000);
            }
        }, 0);

        btnOK = findViewById(R.id.conversation_btnOK);
        btnOK.setOnClickListener(this);

        edtMsg = findViewById(R.id.conversation_edtMessage);
        edtMsg.setOnClickListener(this);
    }

    public void retrieveMessages() {
        String qs = "action=getMessages&idConv=" + idConv;
        qs += "&idLastMessage=" + idLastMessage;

        gs.volleyJsonObjectRequest(qs,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        gs.log(response.toString());
                        displayMessages(response);
                    }
                });
    }

    public void displayMessages(JSONObject response) {
        try {
            // parcours des messages
            JSONArray messages = response.getJSONArray("messages");
            List<Message> messageList = new ArrayList<Message>();

            //Utilisation de Gson pour transformer le Json en objet Message
            Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
            messageList = new Gson().fromJson(String.valueOf(messages), listType);

            if (!messageList.isEmpty())
                updateUI(messageList);

            /*
            for(Message m : messageList){
                TextView tv2 = new TextView(this);
                tv2.setText("[" + m.getAuteur() + "]" + m.getContenu());
                // tv2.setTextColor(Color.parseColor(m.getCouleur()));
                msgLayout.addView(tv2);
            }
            */

            // mise à jour du numéro du dernier message
            idLastMessage = Integer.parseInt(response.getString("idLastMessage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateUI(List<Message> messages) {
        this.messages.addAll(messages);
        this.adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(this.messages.size() - 1);
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.conversation_edtMessage :
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(messages.size() - 1);
                    }
                }, 100);
                break;
            case R.id.conversation_btnOK :
                String msg = edtMsg.getText().toString();
                String qs = "action=setMessage&idConv=" + idConv +"&contenu=" + msg;

                gs.volleyJsonObjectRequest(qs,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                gs.log("retour de la requete posterMessage");
                                gs.log(response.toString());
                            }
                        });

                edtMsg.setText("");
                break;
        }
    }
}
