package com.example.hirorock1103.kakebory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ActivityCategoryAdd extends AppCompatActivity {

    Button bt_ok;
    Button bt_image_get;
    EditText et_category_name;
    ImageView image_area;
    ConstraintLayout mainLayout;
    LinearLayout colorPickArea;
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

                if(iconByteImage == null){
                    Common.toast(ActivityCategoryAdd.this, "画像[byte]が選択されていません");
                    return;
                }

                if(selectedColor.equals("")){
                    Common.toast(ActivityCategoryAdd.this, "色が選択されていません");
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
                    Common.toast(ActivityCategoryAdd.this,"登録完了");
                }
            }
        });


        for(int i = 0; i < colorCode.length; i++){
            Button bt = new Button(this);
            final String color = colorCode[i];
            bt.setTextSize(4);
            bt.setText(color);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedColor = color;
                    Common.toast(ActivityCategoryAdd.this, "color:" + selectedColor);
                }
            });
            bt.setBackground(getDrawable(R.drawable.circle01));
            Drawable aaa = bt.getBackground();
            aaa.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
            colorPickArea.addView(bt);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){

            if(requestCode == 10){

                Uri imageUri = data.getData();
                InputStream inputStream;
                BitmapFactory.Options imageOptions;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    //画像サイズ情報
                    imageOptions = new BitmapFactory.Options();
                    imageOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(inputStream,null,imageOptions);
                    inputStream.close();

                    //再度読み込み
                    inputStream = getContentResolver().openInputStream(imageUri);
                    int width = imageOptions.outWidth;

                    Common.log("width:" + imageOptions.outWidth);
                    Common.log("height:" + imageOptions.outHeight);

                    int p = 1;
                    while(width > 50){
                        //縮小率を決める
                        p *= 2;
                        width /= p;
                    }

                    Common.log("縮小率："+ p);
                    Common.log("width："+ width);

                    Bitmap imageBitmap;
                    if(p > 1){
                        //縮小
                        imageOptions = new BitmapFactory.Options();
                        imageOptions.inSampleSize = p;
                        imageBitmap = BitmapFactory.decodeStream(inputStream, null,imageOptions);

                    }else{
                        //等倍
                        imageBitmap = BitmapFactory.decodeStream(inputStream, null,null);
                    }

                    inputStream.close();


                    //bitmapをblob保存用に変換
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    iconByteImage = stream.toByteArray();
                    stream.close();

                    //確認用の画像
                    Bitmap img = BitmapFactory.decodeByteArray(iconByteImage, 0, iconByteImage.length);
                    image_area.setImageBitmap(img);

                    Common.log("byte" + iconByteImage.length);

                }catch(FileNotFoundException e){
                    e.printStackTrace();
                    Common.log(e.getMessage());
                }catch(IOException e){
                    e.printStackTrace();
                    Common.log(e.getMessage());
                }

                imagePath = imageUri.getPath();

            }

        }else{

        }
    }
}
