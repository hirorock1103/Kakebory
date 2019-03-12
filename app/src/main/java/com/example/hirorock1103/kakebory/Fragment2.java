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


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fragment2 extends Fragment {

    private String targetYm;

    private List<KakaiboManager.MonthSummery> summerylist;
    private SummeryAdapter adapter;
    private int monthTotal;
    private TextView total;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main2, container, false);

        try{
            targetYm = getArguments().getString("target");
        }catch(Exception e){
            //デフォルトは今月をセット
            targetYm = Common.convertDateToString(new Date(), Common.dateFormat5);
        }

        RecyclerView recyclerView = view.findViewById(R.id.list_view);

        summerylist = new ArrayList<>();
        KakaiboManager manager = new KakaiboManager(getContext());
        summerylist = manager.getMonthSummery(targetYm);

        adapter = new SummeryAdapter(summerylist);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        DividerItemDecoration divider = new DividerItemDecoration(getContext(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setClipToOutline(true);
        recyclerView.addItemDecoration(divider);

        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        total = view.findViewById(R.id.summary);
        PriceList priceList = manager.getPriceListByMonth(targetYm);
        monthTotal = priceList.getMonthlist();
        total.setText("収支 ¥" +  Common.format_3keta(monthTotal) + " /収入 ¥" + Common.format_3keta(priceList.getIncome_month_total())+ " /支出 ¥" + Common.format_3keta(priceList.getExpense_month_total()));
        return view;

    }


    public void notify(List<KakaiboManager.MonthSummery> list, PriceList priceList){
        adapter.setList(list);
        //adapter.notifyDataSetChanged();
        adapter.notifyItemInserted(0);
        //KakaiboManager manager = new KakaiboManager(getContext());
        //priceList = manager.getPriceList();
        //monthTotal = priceList.getMonthlist();
        total.setText("収支 ¥" +  Common.format_3keta(monthTotal) + " /収入 ¥" + Common.format_3keta(priceList.getIncome_month_total())+ " /支出 ¥" + Common.format_3keta(priceList.getExpense_month_total()));

    }

    public void notifyNew(String targetYm){
        Common.log("Fragment2:notifyNew : " + targetYm);
        KakaiboManager manager = new KakaiboManager(getContext());
        List<KakaiboManager.MonthSummery> list = manager.getMonthSummery(targetYm);
        PriceList priceList = manager.getPriceListByMonth(targetYm);
        monthTotal = priceList.getMonthlist();

        adapter.setList(list);
        adapter.notifyDataSetChanged();

        total.setText("収支 ¥" +  Common.format_3keta(monthTotal) + " /収入 ¥" + Common.format_3keta(priceList.getIncome_month_total())+ " /支出 ¥" + Common.format_3keta(priceList.getExpense_month_total()));



    }



}
