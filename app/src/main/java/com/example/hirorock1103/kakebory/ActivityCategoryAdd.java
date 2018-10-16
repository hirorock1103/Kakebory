package com.example.hirorock1103.kakebory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.mbms.FileInfo;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ActivityCategoryAdd extends AppCompatActivity {

    Button bt_ok;
    Button bt_image_get;
    EditText et_category_name;
    ImageView image_area;
    ConstraintLayout mainLayout;
    LinearLayout colorPickArea;
    File[] imageFiles;
    String imagePath = "";
    String[] colorCode = {"#ff00ff", "#ff0000", "#000000", "#00ffff"};
    String selectedColor = "";

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
        colorPickArea = findViewById(R.id.color_pick);

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
                    Common.toast(ActivityCategoryAdd.this, "カテゴリ名が空白です。");
                    return;
                }

                if(imagePath.equals("")){
                    Common.toast(ActivityCategoryAdd.this, "画像が選択されていません");
                    return;
                }

                Category category = new Category();
                Common.log(et_category_name.getText().toString());

                category.setCategoryTitle(et_category_name.getText().toString());
                category.setResorceImgPath(imagePath);
                category.setCategoryType(0);
                if(selectedColor != ""){
                    category.setColorCode(selectedColor);
                }



                int resId = manager.addCategory(category);
                if(resId > 0){
                    Common.toast(ActivityCategoryAdd.this,"登録完了");
                }
            }
        });


        for(int i = 0; i < colorCode.length; i++){
            Button bt = new Button(this);
            final String color = colorCode[i];
            bt.setText(color);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedColor = color;
                    Common.toast(ActivityCategoryAdd.this, "color:" + selectedColor);
                }
            });
            bt.setBackground(getDrawable(R.drawable.ring01));
            bt.setBackgroundColor(Color.parseColor(color));
            colorPickArea.addView(bt);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){

            if(requestCode == 10){

                Uri imageUri = data.getData();
                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    image_area.setImageBitmap(image);
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                    Common.log(e.getMessage());
                }

                imagePath = imageUri.getPath();

            }

        }else{

        }
    }
}
