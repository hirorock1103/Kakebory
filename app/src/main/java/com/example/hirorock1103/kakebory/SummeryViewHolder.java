package com.example.hirorock1103.kakebory;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class SummeryViewHolder extends RecyclerView.ViewHolder{

    public TextView category_name;
    public TextView price;

    public SummeryViewHolder(@NonNull View itemView) {
        super(itemView);
        category_name = itemView.findViewById(R.id.category_name);
        price = itemView.findViewById(R.id.price);
    }
}
