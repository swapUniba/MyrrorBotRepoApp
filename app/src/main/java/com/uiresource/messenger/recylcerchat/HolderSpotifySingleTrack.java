package com.uiresource.messenger.recylcerchat;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.uiresource.messenger.R;


public class HolderSpotifySingleTrack extends RecyclerView.ViewHolder {

    private TextView chatText;

    public HolderSpotifySingleTrack(View v) {
        super(v);
        chatText = (TextView) v.findViewById(R.id.tv_chat_text);


    }



    public TextView getChatText() {
        return chatText;
    }

    public void setChatText(TextView chatText) {
        this.chatText = chatText;
    }
}