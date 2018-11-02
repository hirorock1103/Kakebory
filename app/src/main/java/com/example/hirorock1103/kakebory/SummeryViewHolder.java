package com.example.hirorock1103.kakebory;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class SummeryViewHolder extends RecyclerView.ViewHolder{

    public TextView category_name;
    public TextView title1_comment;
    public TextView title2_comment;
    public TextView title3_comment;
    public TextView price;

    public SummeryViewHolder(@NonNull View itemView) {
        super(itemView);
        category_name = itemView.findViewById(R.id.category_name);
        title1_comment = itemView.findViewById(R.id.title1_comment);
        title2_comment = itemView.findViewById(R.id.title2_comment);
        title3_comment = itemView.findViewById(R.id.title3_comment);
        price = itemView.findViewById(R.id.price);
    }
}
