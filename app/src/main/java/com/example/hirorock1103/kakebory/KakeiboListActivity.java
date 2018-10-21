package com.example.hirorock1103.kakebory;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class KakeiboListActivity extends AppCompatActivity implements DialogRecordKakeibo.CommunicateListener,DialogCategory.ActivityNoticeListner {

    private static final int COLUMN = 4;//アイコン縦表示個数
    private KakaiboManager manager;//各種db操作
    private List<JoinedCategoryItem> joinList;//支出とカテゴリJoinデータ
    private CategoryViewAdapter categoryAdapter;
    private RecyclerView listView;
    private PriceList priceList;//Summery(今日と今月の合計)

    /**
     * tab関連
     */
    private MyPagerAdapter viewPageAdapter;//タブ
    private Fragment1 fragment1;//tabView1
    private Fragment2 fragment2;//tabView2

    //暫定対応中
    private int selectedViewTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakeibo_list);

        manager = new KakaiboManager(this);

        //カテゴリをリスト表示
        List<Category> list = manager.getCategory();
        listView = findViewById(R.id.recycler_view);
        CategoryViewAdapter categoryAdapter = new CategoryViewAdapter(list);
        GridLayoutManager layoutManager = new GridLayoutManager(this,COLUMN);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(categoryAdapter);

        //一覧取得
        joinList = manager.getJoinedCategoryItem();

        //priceList
        priceList = manager.getPriceList();

        //tab
        TabLayout tabLayout = findViewById(R.id.tablayout);
        viewPageAdapter = new MyPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);


        //floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog("addCategory", 0);
            }
        });
    }

    public void openDialog(String mode, int categoryId){

        switch (mode){
            case "addCategory":
                DialogCategory dialog = new DialogCategory();
                dialog.show(getSupportFragmentManager(), mode);
                break;
            case "price":
                DialogRecordKakeibo dialog2 = new DialogRecordKakeibo();
                Bundle args = new Bundle();
                args.putInt("categoryId",categoryId);
                dialog2.setArguments(args);
                dialog2.show(getSupportFragmentManager(),mode);
                break;
            case "editCategory":
                DialogCategory dialog3 = new DialogCategory();
                Bundle args2 = new Bundle();
                args2.putInt("categoryId",categoryId);
                dialog3.setArguments(args2);
                dialog3.show(getSupportFragmentManager(), mode);
                break;
        }


    }

    @Override
    public void getInsertResult(int insertId) {

        if(insertId > 0){
            //Common.toast(this, "OK!!" + insertId);
            //view更新
            joinList = manager.getJoinedCategoryItem();
            //合計
            priceList = manager.getPriceList();
            fragment1.notify(joinList, priceList);
            fragment2.notify(manager.getMonthSummery(),priceList);
        }

    }

    @Override
    public void noticeResultActivity(int insertId) {

        if(insertId > 0){

            //view update
            List<Category> list = manager.getCategory();
            categoryAdapter = new CategoryViewAdapter(list);
            GridLayoutManager layoutManager = new GridLayoutManager(this,COLUMN);
            listView.setHasFixedSize(true);
            listView.setLayoutManager(layoutManager);
            listView.setAdapter(categoryAdapter);

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

        public void setItem(List<Category> list){
            this.list = list;
        }

        @Override
        public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int i) {

            //get byte image data
            byte[] img = list.get(i).getIcomImage();
            if(img != null){
                //convert byte image to bitmap
                holder.image.setImageBitmap(BitmapFactory.decodeByteArray(list.get(i).getIcomImage(),0,list.get(i).getIcomImage().length));
            }else{
                //if there is no image data, set image from drawable
                holder.image.setImageDrawable(getDrawable(android.R.drawable.ic_dialog_map));
            }

            //set Tag for noticing which item is clicked
            holder.image.setTag(list.get(i).getCategoryId());

            //set Animation
            final Animation myAnim = AnimationUtils.loadAnimation(KakeiboListActivity.this,R.anim.bounce );
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            myAnim.setInterpolator(interpolator);
            holder.image.setAnimation(myAnim);

            //add long click menu to view
            registerForContextMenu(holder.image);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {


                    int categoryId = Integer.parseInt(v.getTag().toString());

                    //opendialog
                    openDialog("price", categoryId);

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

    /////open ContextMenu when item is Long clicked///////
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.option_menu, menu);

        //set selected item
        selectedViewTag = Integer.parseInt(v.getTag().toString());

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.option1:
                //open edit dialog
                openDialog("editCategory", selectedViewTag);

                return true;
            case R.id.option2:
                //set not show
                manager.switchShow(selectedViewTag,KakaiboManager.NOTSHOW);

                //view update
                List<Category> list = manager.getCategory();
                categoryAdapter = new CategoryViewAdapter(list);
                GridLayoutManager layoutManager = new GridLayoutManager(this,COLUMN);
                listView.setHasFixedSize(true);
                listView.setLayoutManager(layoutManager);
                listView.setAdapter(categoryAdapter);

                //非表示後のview更新
                Common.toast(KakeiboListActivity.this, "itemを非表示に設定しました！");

                return true;
            default:
                return super.onContextItemSelected(item);
        }


    }

    //bounce animation
    public class MyBounceInterpolator implements android.view.animation.Interpolator{

        private double amplitude;
        private double frequency;

        MyBounceInterpolator(double amplitude, double frequency){
            this.amplitude = amplitude;
            this.frequency = frequency;
        }

        @Override
        public float getInterpolation(float time) {
            return (float)(-1 * Math.pow(Math.E, -time/amplitude) * Math.cos(frequency * time) + 1);
        }
    }

    //ViewPager
    public class MyPagerAdapter extends FragmentPagerAdapter{

        private String[] tabTitles = {"本日","今月"};


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
                    fragment1 = new Fragment1();
                    return fragment1;

                case 1:
                    fragment2 = new Fragment2();
                    return fragment2;

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