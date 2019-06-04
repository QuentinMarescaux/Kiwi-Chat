package com.example.chat2i.Views;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chat2i.Models.Message;
import com.example.chat2i.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private final TextView author;
    private final TextView content;

    public MessageViewHolder(View itemView) {
        super(itemView);
        author = itemView.findViewById(R.id.message_author);
        content = itemView.findViewById(R.id.message_content);
    }

    public void updateWithMessage(Message message) {
        this.author.setText(message.getAuteur() + " : ");
        this.content.setText(message.getContenu());
    }
}
