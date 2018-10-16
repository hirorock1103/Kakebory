package com.example.hirorock1103.kakebory;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestListActivity extends AppCompatActivity {
    private static final int COLUMN = 4;
    KakaiboManager manager;
    List<JoinedCategoryItem> joinList;
    private TextView today_price_total;
    private PriceList priceList;
    private PurchaceRecyclerAdapter adapter3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        manager = new KakaiboManager(this);

        //カテゴリをリスト表示
        List<Category> list = manager.getCategory();
        RecyclerView listView = findViewById(R.id.recycler_view);
        CategoryViewAdapter adapter = new CategoryViewAdapter(list);
        GridLayoutManager layoutManager = new GridLayoutManager(this,COLUMN);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);


        //一覧取得
        joinList = manager.getJoinedCategoryItem();

        RecyclerView list_view_below = findViewById(R.id.list_view_below);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(this, 1);
        list_view_below.setHasFixedSize(true);
        list_view_below.setClipToOutline(true);
        list_view_below.addItemDecoration(divider);

        list_view_below.setLayoutManager(linearLayoutManager);
        adapter3 = new PurchaceRecyclerAdapter(joinList);
        list_view_below.setAdapter(adapter3);


        //priceList
        today_price_total = findViewById(R.id.today_price_total);
        priceList = manager.getPriceList();
        today_price_total.setText(String.valueOf(priceList.getTodaysList()));

        //tab
        TabLayout tabLayout = findViewById(R.id.tablayout);
//        tabLayout.addTab(tabLayout.newTab().setText(Common.getDate(0,Common.dateFormat4)));
//        tabLayout.addTab(tabLayout.newTab().setText("tab 2"));

        MyPagerAdapter viewPageAdapter = new MyPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPageAdapter);

        tabLayout.setupWithViewPager(viewPager);


    }

    //////Purchace(Recycle)///////


    public class PuchaceViewHolder extends RecyclerView.ViewHolder{

        private TextView categoryTitle;
        private TextView title;
        private TextView detail;

        public PuchaceViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.category_name);
            title = itemView.findViewById(R.id.price);
            detail = itemView.findViewById(R.id.detail);

        }
    }

    public class PurchaceRecyclerAdapter extends RecyclerView.Adapter<PuchaceViewHolder>{

        private List<JoinedCategoryItem> list;

        PurchaceRecyclerAdapter(List<JoinedCategoryItem> list){
            this.list = list;
        }

        public void setItem(List<JoinedCategoryItem> list){
            this.list = list;
        }

        @NonNull
        @Override
        public PuchaceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.purchace_row, viewGroup, false);
            PuchaceViewHolder vh = new PuchaceViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull PuchaceViewHolder holder, int i) {

            holder.title.setText(String.valueOf(list.get(i).getItem().getPurchaseItemPrice()) + "円");
            holder.categoryTitle.setText(list.get(i).getCategory().getCategoryTitle());
            if( list.get(i).getCategory().getColorCode() != "" && list.get(i).getCategory().getColorCode() != null){
                holder.categoryTitle.setBackgroundColor(Color.parseColor(list.get(i).getCategory().getColorCode()));
                Common.log("COLOUR:" + list.get(i).getCategory().getColorCode());
            }
            Date date = Common.convertStringToDate(list.get(i).getItem().getCreatedate(), Common.dateFormat3);
            holder.detail.setText("購入日時:"+Common.convertDateToString(date, "hh時mm分"));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }


    //////CategoryView///////

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView title;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.category_image_button);
            title = itemView.findViewById(R.id.category_title);
        }
    }

    public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

        private List<Category> list;

        CategoryViewAdapter(List<Category> list){
            this.list = list;
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
            CategoryViewHolder vh = new CategoryViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int i) {
            holder.image.setImageDrawable(getDrawable(android.R.drawable.ic_dialog_map));
            holder.image.setTag(list.get(i).getCategoryId());
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    final int categoryId = Integer.parseInt(v.getTag().toString());
                    final EditText edit_price = new EditText(TestListActivity.this);
                    edit_price.setHint(getString(R.string.dialog1_description));
                    edit_price.setInputType(InputType.TYPE_CLASS_NUMBER);

                    EditText price_detail = new EditText(TestListActivity.this);
                    price_detail.setHint(R.string.dialog1_description);

                    new AlertDialog.Builder(TestListActivity.this)
                            .setTitle(getString(R.string.dialog1_title))
                            .setMessage(getString(R.string.dialog1_description))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    PurchaseItem item = new PurchaseItem();
                                    item.setPurchaseItemPrice(Integer.parseInt(edit_price.getText().toString()));
                                    item.setPurchaseItemTitle("test_item");
                                    item.setCategoryId(categoryId);
                                    int id = manager.addPurchaceItem(item);
                                    if(id > 0){

                                        //view更新
                                        joinList = manager.getJoinedCategoryItem();
                                        //comment.setText(setComment(joinList));
                                        //p_adapter.setItem(joinList);
                                        //p_adapter.notifyDataSetChanged();

                                        adapter3.setItem(joinList);
                                        adapter3.notifyDataSetChanged();

                                        //合計
                                        today_price_total = findViewById(R.id.today_price_total);
                                        priceList = manager.getPriceList();
                                        today_price_total.setText(String.valueOf(priceList.getTodaysList()));

                                    }

                                }
                            })
                            .setView(edit_price)
                            .setCancelable(false)
                            .setNegativeButton("Cancel", null)
                            .show();

                }
            });
            holder.title.setText(list.get(i).getCategoryTitle());
            holder.title.setBackgroundColor(Color.parseColor(list.get(i).getColorCode()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }

    //ViewPager
    public class MyPagerAdapter extends FragmentPagerAdapter{

        private String[] tabTitles = {"タブ１","タブ２"};


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public String getPageTitle(int position){
            return tabTitles[position];
        }


        @Override
        public Fragment getItem(int i) {

            switch(i){
                case 0:
                    return new Fragment1();

                case 1:
                    return new Fragment2();

                    default:
                        return null;
            }

        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }


}
