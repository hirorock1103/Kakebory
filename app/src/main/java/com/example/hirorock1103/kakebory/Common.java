package com.example.hirorock1103.kakebory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Common {

    public static String dateFormat1 = "yyyy/MM/dd";
    public static String dateFormat2 = "yyyy/MM/dd hh:mm:ss";
    public static String dateFormat3 = "yyyy-MM-dd hh:mm:ss";
    public static String dateFormat4 = "MM-dd";

    public static String getDate(int dayCount, String format){

        String date = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, dayCount);
        date = sdf.format(cal.getTime());

        return date;
    }

    public static Date convertStringToDate(String datestr, String format){

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(datestr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static String convertDateToString(Date dateInstance, String format){

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(dateInstance);

    }

    public static void log(String comment){
        Log.i("INFO_TEST", comment);
    }

    public static void toast(Context context, String comment){
        Toast.makeText(context,comment,Toast.LENGTH_SHORT).show();
    }

    public static Bitmap getDrawableByStringPath(String path, Context context){

        Uri uri = Uri.parse(path);

        Common.log("uri:" + uri.getPath());

        InputStream inputStream;

        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap image = BitmapFactory.decodeStream(inputStream);
            return image;
        }catch(FileNotFoundException e){
            e.printStackTrace();
            Common.log(e.getMessage());
        }

        return null;


    }

    //3桁区切り
    public static String format_3keta(int number){
        return String.format("%,d",number);
    }




}
