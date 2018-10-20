package com.example.hirorock1103.kakebory;

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
        holder.category_name.setText(list.get(i).getCategory().getCategoryTitle());
        holder.price.setText(String.valueOf(list.get(i).getTotal()) + "円");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
