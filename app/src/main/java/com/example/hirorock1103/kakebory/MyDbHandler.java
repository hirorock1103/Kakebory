package com.example.hirorock1103.kakebory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static com.example.hirorock1103.kakebory.KakaiboManager.NOTSHOW;
import static com.example.hirorock1103.kakebory.KakaiboManager.SHOW;

public class MyDbHandler extends SQLiteOpenHelper {

    private final static int version = 7;
    protected final static String DBNAME = "Kakeibory.db";
    //Category
    protected static final String TABLE_CATEGORY = "Category";
    protected static final String CATEGORY_COLUMN_ID = "categoryId";
    protected static final String CATEGORY_COLUMN_NAME = "categoryTitle";
    protected static final String CATEGORY_COLUMN_COLORCODE = "categoryColorCode";
    protected static final String CATEGORY_COLUMN_TYPE = "categoryType";
    protected static final String CATEGORY_COLUMN_SHOWSTATUS = "categoryShowStatus";
    protected static final String CATEGORY_IMAGE_PATH = "resorceImgPath";
    protected static final String CATEGORY_ICON_IMAGE = "iconImage";
    protected static final String CATEGORY_COLUMN_CREATEDATE = "categoryCreatedate";

    //item
    protected static final String TABLE_PURCHACEITEM = "PurchaceItem";
    protected static final String PURCHACEITEM_COLUMN_ID = "purchaseItemId";
    protected static final String PURCHACEITEM_COLUMN_NAME = "purchaseItemTitle";
    protected static final String PURCHACEITEM_COLUMN_PRICE = "purchaseItemPrice";
    protected static final String PURCHACEITEM_COLUMN_CATEGORY_ID = "categoryId";
    protected static final String PURCHACEITEM_COLUMN_STATUS = "status";
    protected static final String PURCHACEITEM_COLUMN_CREATEDATE = "purchaceItemCreatedate";


    public MyDbHandler(Context context) {
        super(context, DBNAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_CATEGORY + "(" +
                CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_COLUMN_NAME + " text, " +
                CATEGORY_IMAGE_PATH + " text, " +
                CATEGORY_COLUMN_COLORCODE + " text, " +
                CATEGORY_COLUMN_TYPE + " INTEGER, " +
                CATEGORY_COLUMN_SHOWSTATUS + " INTEGER default 0, " +
                CATEGORY_ICON_IMAGE + " BLOB, " +
                CATEGORY_COLUMN_CREATEDATE + " text " +
                ");";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_PURCHACEITEM + "(" +
                PURCHACEITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PURCHACEITEM_COLUMN_NAME + " text, " +
                PURCHACEITEM_COLUMN_CATEGORY_ID + " INTEGER, " +
                PURCHACEITEM_COLUMN_PRICE + " INTEGER, " +
                PURCHACEITEM_COLUMN_STATUS + " INTEGER, " +
                PURCHACEITEM_COLUMN_CREATEDATE + " text " +
                ");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = "DROP TABLE IF EXISTS " + TABLE_CATEGORY;
        db.execSQL(query);

        query = "DROP TABLE IF EXISTS " + TABLE_PURCHACEITEM;
        db.execSQL(query);

        onCreate(db);

    }

}
