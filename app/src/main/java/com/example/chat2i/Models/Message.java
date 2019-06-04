package com.example.chat2i.Models;


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


import android.support.annotation.NonNull;

public class Message {

    private int id;
    private String contenu;
    private String auteur;
    private String couleur;

    public Message(int id, String contenu, String auteur, String couleur) {
        this.id = id;
        this.contenu = contenu;
        this.auteur = auteur;
        this.couleur = couleur;
    }

    public int getId() {
        return id;
    }

    public String getContenu() {
        return contenu;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getCouleur() {
        return couleur;
    }

    @Override
    @NonNull
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", auteur='" + auteur + '\'' +
                ", couleur='" + couleur + '\'' +
                '}';
    }
}
