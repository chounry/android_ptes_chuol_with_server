package com.group6.choul.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.group6.choul.R;
import com.group6.choul.models.ChatModel;

import java.util.List;

public class ListChatAdapter extends BaseAdapter {

    private Context context;
    private List<ChatModel> modeList;

    public ListChatAdapter(Context context , List<ChatModel> modeList){
        this.context = context;
        this.modeList= modeList;
    }


    @Override
    public int getCount() {
        return modeList.size();
    }

    @Override
    public Object getItem(int position) {
        ChatModel model = modeList.get(position);
        return modeList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v =View.inflate(context, R.layout.item_each_chat,null);


        ImageView imageView  = v.findViewById(R.id.profile_image);
        TextView textviewName = v.findViewById(R.id.userName);
        TextView textviewTime = v.findViewById(R.id.chatTime);
        TextView titleTv = v.findViewById(R.id.title_tv);
        TextView msgTv = v.findViewById(R.id.msg_tv);
        ImageView imgView  = v.findViewById(R.id.imgViewProduct);

        ChatModel obj = modeList.get(position);

        imageView.setImageResource(obj.getImg_id());
        textviewName.setText(obj.getName());
        titleTv.setText(obj.getTitle());
        textviewTime.setText(obj.getTime());
        msgTv.setText(obj.getMessage());
        imgView.setImageResource(obj.getImgProfile());

        return v;
    }

}
