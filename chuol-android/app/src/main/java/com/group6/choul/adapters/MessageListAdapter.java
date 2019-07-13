package com.group6.choul.adapters;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.group6.choul.R;
import com.group6.choul.models.MessageModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageListAdapter extends BaseAdapter {
    private Context context;
    private List<MessageModel> modelList;

    public MessageListAdapter(Context context, List<MessageModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    public void add(MessageModel message) {
        this.modelList.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return this.modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessageModel m = modelList.get(position);
        View v = View.inflate(this.context,R.layout.item_respsone_msg,null);

        if(!m.isBelongsToCurrentUser()) {
            // if the message is a receive message
            v = View.inflate(this.context, R.layout.item_recieve_msg,  null);
            ImageView imgView = v.findViewById(R.id.profile_image);
            Picasso.get().load(m.getMemberData().getImg()).into(imgView);
        }
        TextView messageView = v.findViewById(R.id.msgTv);
        messageView.setText(m.getMessage());

        return v;
    }
}
