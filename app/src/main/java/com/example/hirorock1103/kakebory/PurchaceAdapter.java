package com.example.hirorock1103.kakebory;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

public class PurchaceAdapter extends RecyclerView.Adapter<PurchaceViewHolder> {

    private List<JoinedCategoryItem> list;

    PurchaceAdapter(List<JoinedCategoryItem> list){
        this.list = list;
    }

    public void setItem(List<JoinedCategoryItem> list){
        this.list = list;
    }

    @NonNull
    @Override
    public PurchaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.purchace_row,viewGroup,false);
        PurchaceViewHolder holder = new PurchaceViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull PurchaceViewHolder itemViewHolder, int i) {

        itemViewHolder.category_name.setText(list.get(i).getCategory().getCategoryTitle());
        itemViewHolder.price.setText(String.valueOf(list.get(i).getItem().getPurchaseItemPrice()));

        itemViewHolder.price.setText(String.valueOf(list.get(i).getItem().getPurchaseItemPrice()) + "円");
        itemViewHolder.category_name.setText(list.get(i).getCategory().getCategoryTitle());
        if( list.get(i).getCategory().getColorCode() != "" && list.get(i).getCategory().getColorCode() != null){
            itemViewHolder.category_name.setBackgroundColor(Color.parseColor(list.get(i).getCategory().getColorCode()));
        }
        Date date = Common.convertStringToDate(list.get(i).getItem().getCreatedate(), Common.dateFormat3);
        //itemViewHolder.detail.setText("購入日時:"+Common.convertDateToString(date, "hh時mm分"));
        itemViewHolder.detail.setText("購入日時:"+Common.convertDateToString(date, Common.dateFormat3));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
