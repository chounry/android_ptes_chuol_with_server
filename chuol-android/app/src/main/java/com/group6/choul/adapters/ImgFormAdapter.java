package com.group6.choul.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.group6.choul.R;
import com.group6.choul.models.ImgFormModel;

import java.util.List;

public class ImgFormAdapter extends RecyclerView.Adapter<ImgViewHolder> {
    private List<ImgFormModel> imgList;
    private Context context;

    public ImgFormAdapter(List<ImgFormModel> imgList,Context context) {
        this.imgList = imgList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_each_img, null);
        return new ImgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImgViewHolder imgViewHolder, int i) {
        imgViewHolder.mImgView.setImageBitmap(imgList.get(i).getImg());

    }

    @Override
    public int getItemCount() {
        return this.imgList.size();
    }
}


class ImgViewHolder extends RecyclerView.ViewHolder {

    ImageView mImgView;
    ImageButton mImgBtn;
    public CardView mCardView;


    public ImgViewHolder(@NonNull View itemView) {
        super(itemView);
        mImgView = itemView.findViewById(R.id.img_view);
        mImgBtn = itemView.findViewById(R.id.delete_img_btn);
//        mCardView = itemView.findViewById(R.id.m_linear_layout);
        mCardView = itemView.findViewById(R.id.m_card_view);


    }

}
