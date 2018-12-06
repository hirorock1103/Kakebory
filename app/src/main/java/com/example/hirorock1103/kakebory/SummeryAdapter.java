package com.example.hirorock1103.kakebory;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class SummeryAdapter extends RecyclerView.Adapter<SummeryViewHolder> {

    List<KakaiboManager.MonthSummery> list;

    SummeryAdapter(List<KakaiboManager.MonthSummery> list){
        this.list = list;
    }

    public void setList(List<KakaiboManager.MonthSummery> list){
        this.list = list;
    }

    @NonNull
    @Override
    public SummeryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.summery_row,viewGroup,false);
        SummeryViewHolder holder = new SummeryViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SummeryViewHolder holder, int i) {

        if(list.get(i).getCategory().getCategoryType() == 1){
            holder.title1_comment.setText("今月は");
            holder.title2_comment.setText("として");
            holder.title3_comment.setText("得ました。");
        }else{
            holder.title1_comment.setText("今月は");
            holder.title2_comment.setText("として");
            holder.title3_comment.setText("使いました。");
        }
        String type = list.get(i).getCategory().getCategoryType() == 1 ? "収入" : "支出";
        holder.category_name.setText(list.get(i).getCategory().getCategoryTitle() + "("+type+")");
        holder.category_name.setBackgroundColor(Color.parseColor(list.get(i).getCategory().getColorCode()));
        holder.price.setText( Common.format_3keta(list.get(i).getTotal()) + "円");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
