package com.example.hirorock1103.kakebory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class Fragment1 extends Fragment {

    private String targetYm;

    private RecyclerView list_view;
    private PurchaceAdapter adapter;
    private TextView textView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main1, container, false);

        KakaiboManager manager = new KakaiboManager(getContext());

        //get data as list
        List<JoinedCategoryItem> list = manager.getJoinedCategoryItem();

        list_view = view.findViewById(R.id.list_view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        adapter = new PurchaceAdapter(list);
        adapter.setContext(getContext());
        adapter.setFragmentManager(getFragmentManager());

        DividerItemDecoration divider = new DividerItemDecoration(getContext(), 1);
        list_view.setHasFixedSize(true);
        list_view.setClipToOutline(true);
        list_view.addItemDecoration(divider);
        list_view.setLayoutManager(llm);
        list_view.setAdapter(adapter);

        textView = view.findViewById(R.id.summary);

        //get data as summery
        PriceList priceList = manager.getPriceList();
        textView.setText("収支 ¥" +  Common.format_3keta(priceList.getTodaysList()) + " /収入 ¥" + Common.format_3keta(priceList.getIncome_today_total())+ " /支出 ¥" + Common.format_3keta(priceList.getExpense_today_total()));
        return view;

    }


    public void notifyInsert(){
        KakaiboManager manager = new KakaiboManager(getContext());
        List<JoinedCategoryItem> list = manager.getJoinedCategoryItem();
        PriceList priceList = manager.getPriceList();

        adapter.setItem(list);
        adapter.notifyItemInserted(0);
        textView.setText("収支 ¥" +  Common.format_3keta(priceList.getTodaysList()) + " /収入 ¥" + Common.format_3keta(priceList.getIncome_today_total())+ " /支出 ¥" + Common.format_3keta(priceList.getExpense_today_total()));
    }

    public void notifyNew(){

        KakaiboManager manager = new KakaiboManager(getContext());
        List<JoinedCategoryItem> list = manager.getJoinedCategoryItem();
        PriceList priceList = manager.getPriceList();

        adapter.setItem(list);
        adapter.notifyDataSetChanged();
        textView.setText("収支 ¥" +  Common.format_3keta(priceList.getTodaysList()) + " /収入 ¥" + Common.format_3keta(priceList.getIncome_today_total())+ " /支出 ¥" + Common.format_3keta(priceList.getExpense_today_total()));
    }






}
