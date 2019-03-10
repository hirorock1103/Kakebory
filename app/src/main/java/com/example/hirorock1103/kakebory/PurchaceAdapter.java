package com.example.hirorock1103.kakebory;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

public class PurchaceAdapter extends RecyclerView.Adapter<PurchaceViewHolder> {

    private List<JoinedCategoryItem> list;
    private Context context;
    private FragmentManager fragmentManager;

    public PurchaceAdapterListener listener;

    public interface PurchaceAdapterListener{
        public void PurchaceAdapterNoticeResult();
    }

    PurchaceAdapter(List<JoinedCategoryItem> list){
        this.list = list;
    }

    public void setContext(Context context){
        this.context = context;
        listener = (PurchaceAdapterListener)context;
    }
    public void setFragmentManager(FragmentManager manager){
        fragmentManager = manager;
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
        //itemViewHolder.price.setText(String.valueOf(list.get(i).getItem().getPurchaseItemPrice()));

        itemViewHolder.price.setText(Common.format_3keta(list.get(i).getItem().getPurchaseItemPrice()) + "円");
        String type = list.get(i).getCategory().getCategoryType() == 1 ? "収入" : "支出";
        itemViewHolder.category_name.setText(list.get(i).getCategory().getCategoryTitle() + "(" + type + ")");
        if( list.get(i).getCategory().getColorCode() != "" && list.get(i).getCategory().getColorCode() != null){
            itemViewHolder.category_name.setBackgroundColor(Color.parseColor(list.get(i).getCategory().getColorCode()));
        }
        Date date = Common.convertStringToDate(list.get(i).getItem().getCreatedate(), Common.dateFormat3);
        //itemViewHolder.detail.setText("購入日時:"+Common.convertDateToString(date, "hh時mm分"));
        itemViewHolder.detail.setText("購入日時:"+Common.convertDateToString(date, Common.dateFormat3));

        if(list.get(i).getItem().getPurchaseItemTitle().equals("")){
            itemViewHolder.comment.setText("no comment");
        }else{
            itemViewHolder.comment.setText(list.get(i).getItem().getPurchaseItemTitle());
        }

        final int itemId = list.get(i).getItem().getPurchaseItemId();
        StringBuilder builder = new StringBuilder();
        builder.append( list.get(i).getCategory().getCategoryTitle() );
        builder.append("(" + list.get(i).getItem().getPurchaseItemPrice() + "円)");
        final String title = builder.toString();
        final int categoryId = list.get(i).getCategory().getCategoryId();
        itemViewHolder.layout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                menu.setHeaderTitle(title);

                menu.add("edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        //open edit dialog
                        DialogRecordKakeibo dialogRecordKakeibo = new DialogRecordKakeibo();
                        Bundle bundle = new Bundle();
                        bundle.putInt("categoryId", categoryId);
                        bundle.putInt("itemId", itemId);
                        dialogRecordKakeibo.setArguments(bundle);
                        dialogRecordKakeibo.show(fragmentManager,"dialog");

                        return true;
                    }
                });

                menu.add("delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        Common.log("delete item id : " + itemId);
                        KakaiboManager manager = new KakaiboManager(context);
                        manager.deleteItemByItemId(itemId);

                        listener.PurchaceAdapterNoticeResult();

                        return true;
                    }
                });

            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
