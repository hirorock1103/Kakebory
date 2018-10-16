package com.example.hirorock1103.kakebory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private Button btSetCategoryDataSample;
    private Button btSetItemDataSample;
    private Button btSetJoinedDataSampe;
    private Button btgetPriceListSample;
    private Button btAddCategory;
    private Button btMoveList;

    private LinearLayout layout;


    //manager
    private KakaiboManager manager = new KakaiboManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        KakaiboManager manager = new KakaiboManager(this);
        manager.MakeTestData();

        //
        layout = findViewById(R.id.main_layout);

        btSetCategoryDataSample = new Button(this);
        btSetCategoryDataSample.setText("btSetCategoryDataSample");
        btSetItemDataSample = new Button(this);
        btSetItemDataSample.setText("btSetItemDataSample");
        btSetJoinedDataSampe = new Button(this);
        btgetPriceListSample = new Button(this);
        btAddCategory = new Button(this);
        btMoveList = new Button(this);
        btSetJoinedDataSampe.setText("btSetJoinedDataSampe");
        btgetPriceListSample.setText("btgetPriceListSample");
        btAddCategory.setText("btAddCategory");
        btMoveList.setText("btMoveList");

        setListner();

//        layout.addView(btSetCategoryDataSample);
//        layout.addView(btSetItemDataSample);
//        layout.addView(btSetJoinedDataSampe);
//        layout.addView(btgetPriceListSample);
        layout.addView(btAddCategory);
        layout.addView(btMoveList);

        //setImage

    }


    private void setListner(){

        btSetCategoryDataSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Category> list =  manager.getCategory();
                for(Category category : list){
                    Common.log("\n\n");
                    Common.log("title:" + category.getCategoryTitle());
                    Common.log("id:" + category.getCategoryId());
                    Common.log("path:" + category.getResorceImgPath());
                    Common.log("createdate:" + category.getCreatedate());

                    ImageView image = new ImageView(TestActivity.this);
                    //image.setImageBitmap(Common.getDrawableByStringPath(category.getResorceImgPath(),TestActivity.this ));

                    layout.addView(image);

                }
            }
        });

        btSetItemDataSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PurchaseItem> list = manager.getPurchaseItem();
                for(PurchaseItem item : list){
                    Common.log("\n\n");
                    Common.log("title:" + item.getPurchaseItemTitle());
                    Common.log("id:" + item.getPurchaseItemId());
                    Common.log("price:" + item.getPurchaseItemPrice());
                    Common.log("categoryId:" + item.getCategoryId());
                    Common.log("createdate:" + item.getCreatedate());
                }
            }
        });

        btSetJoinedDataSampe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<JoinedCategoryItem> list = manager.getJoinedCategoryItem();
                for(JoinedCategoryItem join : list){
                    Common.log("\n\n");
                    Common.log("categorytitle:" + join.getCategory().getCategoryTitle());
                    Common.log("itemTitle" +join.getItem().getPurchaseItemTitle() );
                    Common.log("itemPrice" + join.getItem().getPurchaseItemPrice());
                }
            }
        });


        btgetPriceListSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PriceList list = manager.getPriceList();
                Common.log("todayTotal:" + list.getTodaysList());
                Common.log("monthTotal:" + list.getMonthlist());
            }
        });

        btAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, ActivityCategoryAdd.class);
                startActivity(intent);
            }
        });

        btMoveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, TestListActivity.class);
                startActivity(intent);
            }
        });


    }

}
