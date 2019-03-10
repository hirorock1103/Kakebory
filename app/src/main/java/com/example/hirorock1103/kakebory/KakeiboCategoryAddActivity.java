package com.example.hirorock1103.kakebory;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

public class KakeiboCategoryAddActivity extends AppCompatActivity {

    Button bt_ok;
    Button bt_image_get;
    EditText et_category_name;
    ImageView image_area;
    ConstraintLayout mainLayout;
    String imagePath = "";
    String[] colorCode = {"#ff00ff", "#ff0000", "#000000", "#00ffff"};
    String selectedColor = "";
    byte[] iconByteImage;

    KakaiboManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_add);
        mainLayout = findViewById(R.id.main_layout);
        bt_image_get = findViewById(R.id.bt_image_get);
        image_area = findViewById(R.id.image_area);
        bt_ok = findViewById(R.id.regsiter);
        et_category_name = findViewById(R.id.edit_title);
        manager = new KakaiboManager(this);


        Fragment3 fragment = new Fragment3();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.commit();


        setView();

    }

    private void setView(){

        bt_image_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                File picDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String path = picDirectory.getPath();

                Uri data = Uri.parse(path);
                photoPickerIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoPickerIntent, 10);

            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //登録
                if(et_category_name.getText().toString().equals("") == true){
                    Common.toast(KakeiboCategoryAddActivity.this, "カテゴリ名が空白です。");
                    return;
                }

                if(imagePath.equals("")){
                    Common.toast(KakeiboCategoryAddActivity.this, "画像が選択されていません");
                    return;
                }

                if(iconByteImage == null){
                    Common.toast(KakeiboCategoryAddActivity.this, "画像[byte]が選択されていません");
                    return;
                }

                if(selectedColor.equals("")){
                    Common.toast(KakeiboCategoryAddActivity.this, "色が選択されていません");
                    return;
                }

                // create Category Object
                Category category = new Category();
                category.setCategoryTitle(et_category_name.getText().toString());
                category.setResorceImgPath(imagePath);
                category.setCategoryShowStatus(0);
                category.setIcomImage(iconByteImage);
                category.setCategoryType(0);
                if(selectedColor != ""){
                    category.setColorCode(selectedColor);
                }
                //add
                int resId = manager.addCategory(category);
                if(resId > 0){
                    Common.toast(KakeiboCategoryAddActivity.this,"登録完了");
                }
            }
        });


    }


}
