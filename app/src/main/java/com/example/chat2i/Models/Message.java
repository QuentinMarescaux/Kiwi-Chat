package com.example.chat2i.Models;

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
