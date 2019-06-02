package com.example.chat2i;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public class ListeConversations {

    private ArrayList<Conversation> list;

    public ListeConversations() {
        this.list = new ArrayList<Conversation>();
    }

    public ArrayList<Conversation> getList() {
        return list;
    }

    public void addConversation(Conversation c) {
        list.add(c);
    }

    @Override
    @NonNull
    public String toString() {
        return "ListeConversations{" +
                "list=" + list +
                '}';
    }
}
