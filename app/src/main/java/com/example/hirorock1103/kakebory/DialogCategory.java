package com.example.hirorock1103.kakebory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class DialogCategory extends AppCompatDialogFragment {

    private EditText edit_title;
    private Button getImageBtn;
    private String imagePath;
    private ImageView image_area;
    private CardView cardview;
    private RadioGroup radioGroup;
    private byte[] iconByteImage;
    private KakaiboManager manager;
    private ActivityNoticeListner listner;

    //use when edit mode
    private boolean editMode = false;
    private int editCategoryId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_add_category, null);
        manager = new KakaiboManager(getActivity());

        image_area = view.findViewById(R.id.image_area);
        cardview = view.findViewById(R.id.cardview);
        cardview.setVisibility(View.INVISIBLE);
        edit_title = view.findViewById(R.id.edit_title);
        radioGroup = view.findViewById(R.id.RadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Common.log("radioGroup.getCheckedRadioButtonId():" + i);
            }
        });

        getImageBtn = view.findViewById(R.id.bt_image_get);
        getImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set intent
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                //image file directory
                File picDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String path = picDirectory.getPath();

                //uri
                Uri data = Uri.parse(path);
                photoPickerIntent.setDataAndType(data, "image/*");
                startActivityForResult(photoPickerIntent, 10);


            }
        });

        try{
            //edit
            editCategoryId = getArguments().getInt("categoryId");

            if(editCategoryId > 0){

                editMode = true;

                //edit mode
                Category category = manager.getCategoryById(editCategoryId);
                edit_title.setText(category.getCategoryTitle());
                image_area.setImageBitmap(BitmapFactory.decodeByteArray(category.getIcomImage(),0,category.getIcomImage().length));
                iconByteImage = category.getIcomImage();
                cardview.setVisibility(View.VISIBLE);

            }

        }catch (Exception e){

        }

        builder.setView(view)
                .setTitle("Add Category")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Common.log("text1:" + edit_title.getText().toString());

                        //登録
                        if(edit_title.getText().toString().equals("") == true){
                            Common.toast(getActivity(), "カテゴリ名が空白です。");
                            return;
                        }

                        if(iconByteImage == null){
                            Common.toast(getActivity(), "画像[byte]が選択されていません");
                            return;
                        }


                        // create Category Object
                        Category category = new Category();
                        category.setCategoryTitle(edit_title.getText().toString());
                        category.setResorceImgPath(imagePath);
                        category.setCategoryShowStatus(0);
                        category.setIcomImage(iconByteImage);
                        category.setCategoryType(radioGroup.getCheckedRadioButtonId() == R.id.RadioButtoni2 ? 1 : 0);
                        category.setColorCode(radioGroup.getCheckedRadioButtonId() == R.id.RadioButtoni2 ? "#ef74a3" : "#FFFFA60C");

                        int resId = 0;

                        if(editMode == true){
                            //add
                            resId = manager.updateCategory(category, editCategoryId);
                        }else{
                            //add
                            resId = manager.addCategory(category);
                        }

                        if(resId > 0){
                            Common.toast(getActivity(),"登録完了");
                            //noticeResultActivity();
                            listner.noticeResultActivity(resId);
                        }else{
                            listner.noticeResultActivity(0);
                        }

                    }
                });

        return builder.create();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){

            if(requestCode == 10){


                Uri imageUri = data.getData();
                InputStream inputStream;
                BitmapFactory.Options imageOptions;

                try {
                    inputStream = getActivity().getContentResolver().openInputStream(imageUri);

                    //画像サイズ情報
                    imageOptions = new BitmapFactory.Options();
                    imageOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(inputStream,null,imageOptions);
                    inputStream.close();

                    //再度読み込み
                    inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                    int width = imageOptions.outWidth;

                    int p = 1;
                    while(width > 50){
                        //縮小率を決める
                        p *= 2;
                        width /= p;
                    }

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
                    cardview.setVisibility(View.VISIBLE);

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


    public interface ActivityNoticeListner{
        void noticeResultActivity(int insertId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            listner = (ActivityNoticeListner)context;
        }catch(ClassCastException e){

        }



    }
}
