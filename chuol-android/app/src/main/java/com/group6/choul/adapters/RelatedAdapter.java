package com.group6.choul.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group6.choul.R;
import com.group6.choul.models.RelatedModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RelatedAdapter extends RecyclerView.Adapter<RelatedAdapter.MyViewHolder> {

    private List<RelatedModel> modelList;
    private Context context;

    public RelatedAdapter(List<RelatedModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public RelatedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_raleted_view, viewGroup,false );
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.textViewTitle.setText(modelList.get(i).getTitle());
        myViewHolder.textViewDescription.setText(modelList.get(i).getDescriptino());
        Picasso.get().load(modelList.get(i).getImg()).into(myViewHolder.img);

    }

    @Override
    public int getItemCount() {
        return this.modelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDescription;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.related_title);
            textViewDescription = itemView.findViewById(R.id.related_description);
            img = itemView.findViewById(R.id.house_related_img);

        }
    }

}
