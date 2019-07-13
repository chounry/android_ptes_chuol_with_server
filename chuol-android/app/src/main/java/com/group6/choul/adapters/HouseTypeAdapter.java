package com.group6.choul.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group6.choul.R;
import com.group6.choul.models.HouseTypeModel;

import java.util.List;

public class HouseTypeAdapter extends RecyclerView.Adapter<HouseTypeViewHolder> {
    private Context context;
    private List<HouseTypeModel> modelsList;

    public HouseTypeAdapter(Context context, List<HouseTypeModel> modelsList) {
        this.context = context;
        this.modelsList = modelsList;
    }

    @NonNull
    @Override
    public HouseTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_house_type,null);
        return new HouseTypeViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseTypeViewHolder holder, int position) {
        holder.name_tv.setText(modelsList.get(0).getName());
    }

    @Override
    public int getItemCount() {
        return modelsList.size();
    }
}

class HouseTypeViewHolder extends RecyclerView.ViewHolder{

    TextView name_tv;

    public HouseTypeViewHolder(@NonNull View itemView) {
        super(itemView);
        name_tv = itemView.findViewById(R.id.name_tv);
    }
}
