package com.example.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    ArrayList<Message> chats ;
    Activity context;

    public ChatAdapter(ArrayList<Message> otherMessages, Activity ctx){
        chats = otherMessages;
        context = ctx;
    }

    public void add(Message message){
        this.chats.add(message);
        //notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return chats.size();
    }

    @Override
    public Message getItem(int i) {
        return chats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        View newView;

        LayoutInflater messageInflater = context.getLayoutInflater();//(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = getItem(i); //chats.get(i);


            if (message.getChatType()) {
                newView = messageInflater.inflate(R.layout.chat_row_send, null);
                holder.chatMsg = (TextView) newView.findViewById(R.id.chatContent);
                newView.setTag(holder);
                holder.chatMsg.setText(message.getChatMessage());
            } else {
                newView = messageInflater.inflate(R.layout.chat_row_receive, null);
                holder.chatMsg = (TextView) newView.findViewById(R.id.chatContent);
                newView.setTag(holder);
                holder.chatMsg.setText(message.getChatMessage());
            }

        return newView;
    }

    private class MessageViewHolder{
        public TextView chatMsg;
    }
}

//class MessageViewHolder{
//    //public View avatar;
//    public TextView chatMsg;
//}
