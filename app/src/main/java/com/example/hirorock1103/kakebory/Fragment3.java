package com.example.hirorock1103.kakebory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class Fragment3 extends Fragment {


    private EditText edit_title;
    private Button getImageBtn;
    private String imagePath;
    private ImageView image_area;
    private byte[] iconByteImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_dialog_add_category, container, false);

        image_area = view.findViewById(R.id.image_area);
        edit_title = view.findViewById(R.id.edit_title);
        getImageBtn = view.findViewById(R.id.bt_image_get);

        getImageBtn.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Common.log("resultCode:" + resultCode);

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
