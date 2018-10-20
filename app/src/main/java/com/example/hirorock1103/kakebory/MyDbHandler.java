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
    public final static String DBNAME = "Kakeibory.db";
    //Category
    public static final String TABLE_CATEGORY = "Category";
    public static final String CATEGORY_COLUMN_ID = "categoryId";
    public static final String CATEGORY_COLUMN_NAME = "categoryTitle";
    public static final String CATEGORY_COLUMN_COLORCODE = "categoryColorCode";
    public static final String CATEGORY_COLUMN_TYPE = "categoryType";
    public static final String CATEGORY_COLUMN_SHOWSTATUS = "categoryShowStatus";
    public static final String CATEGORY_IMAGE_PATH = "resorceImgPath";
    public static final String CATEGORY_ICON_IMAGE = "iconImage";
    public static final String CATEGORY_COLUMN_CREATEDATE = "categoryCreatedate";

    //item
    public static final String TABLE_PURCHACEITEM = "PurchaceItem";
    public static final String PURCHACEITEM_COLUMN_ID = "purchaseItemId";
    public static final String PURCHACEITEM_COLUMN_NAME = "purchaseItemTitle";
    public static final String PURCHACEITEM_COLUMN_PRICE = "purchaseItemPrice";
    public static final String PURCHACEITEM_COLUMN_CATEGORY_ID = "categoryId";
    public static final String PURCHACEITEM_COLUMN_STATUS = "status";
    public static final String PURCHACEITEM_COLUMN_CREATEDATE = "purchaceItemCreatedate";


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

    //Category
    public List<Category> getCategory(){

        List<Category> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY +
                " WHERE " + CATEGORY_COLUMN_SHOWSTATUS + " = 0 " +
                " ORDER BY " + CATEGORY_COLUMN_ID + " DESC ";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){

            Category category = new Category();
            category.setCategoryId(c.getInt(c.getColumnIndex(CATEGORY_COLUMN_ID)));
            category.setCategoryTitle(c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME)));
            category.setResorceImgPath(c.getString(c.getColumnIndex(CATEGORY_IMAGE_PATH)));
            category.setColorCode(c.getString(c.getColumnIndex(CATEGORY_COLUMN_COLORCODE)));
            category.setCategoryType(c.getInt(c.getColumnIndex(CATEGORY_COLUMN_TYPE)));
            byte[] data = c.getBlob(c.getColumnIndex(CATEGORY_ICON_IMAGE));
            //category.setIcomImage(c.getBlob(c.getColumnIndex(CATEGORY_ICON_IMAGE)));
            for(int i = 0; i < data.length; i++){
                if(i > 10){break;}
                Common.log(String.valueOf(data[i]));
            }
            category.setIcomImage(data);


            category.setCreatedate(c.getString(c.getColumnIndex(CATEGORY_COLUMN_CREATEDATE)));

            list.add(category);

            c.moveToNext();
        }


        return list;

    }

    public int addCategory(Category category) {

        long id = 0;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_NAME, category.getCategoryTitle());
        values.put(CATEGORY_IMAGE_PATH, category.getResorceImgPath());
        values.put(CATEGORY_COLUMN_COLORCODE, category.getColorCode());
        values.put(CATEGORY_COLUMN_TYPE, category.getCategoryType());
        values.put(CATEGORY_COLUMN_SHOWSTATUS, category.getCategoryShowStatus());
        values.put(CATEGORY_ICON_IMAGE, category.getIcomImage());
        values.put(CATEGORY_COLUMN_CREATEDATE, Common.getDate(0, Common.dateFormat2));

        id = db.insert(TABLE_CATEGORY, null, values);

        return (int)id;

    }

    public void deleteCategoryAll(){
        String query = "DELETE FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }


    //Item
    //Category
    public List<PurchaseItem> getPurchaseItem(){

        List<PurchaseItem> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PURCHACEITEM +
                " ORDER BY " + PURCHACEITEM_COLUMN_ID + " DESC ";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){

            PurchaseItem item = new PurchaseItem();

            item.setPurchaseItemId(c.getInt(c.getColumnIndex(PURCHACEITEM_COLUMN_ID)));
            item.setPurchaseItemTitle(c.getString(c.getColumnIndex(PURCHACEITEM_COLUMN_NAME)));
            item.setPurchaseItemPrice(c.getInt(c.getColumnIndex(PURCHACEITEM_COLUMN_PRICE)));
            item.setCategoryId(c.getInt(c.getColumnIndex(PURCHACEITEM_COLUMN_CATEGORY_ID)));
            item.setStatus(c.getInt(c.getColumnIndex(PURCHACEITEM_COLUMN_STATUS)));
            item.setCreatedate(c.getString(c.getColumnIndex(PURCHACEITEM_COLUMN_CREATEDATE)));

            list.add(item);

            c.moveToNext();
        }


        return list;

    }

    public int addPurchaceItem(PurchaseItem item) {

        long id = 0;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PURCHACEITEM_COLUMN_NAME, item.getPurchaseItemTitle());
        values.put(PURCHACEITEM_COLUMN_PRICE, item.getPurchaseItemPrice());
        values.put(PURCHACEITEM_COLUMN_CATEGORY_ID, item.getCategoryId());
        values.put(PURCHACEITEM_COLUMN_STATUS, item.getStatus());
        values.put(PURCHACEITEM_COLUMN_CREATEDATE, Common.getDate(0, Common.dateFormat3));
        Common.log("kakunin:" + Common.getDate(0,Common.dateFormat3));

        id = db.insert(TABLE_PURCHACEITEM, null, values);

        return (int)id;

    }

    //test
    public int addPurchaceItemTest(PurchaseItem item, int dayCount){

        long id = 0;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PURCHACEITEM_COLUMN_NAME, item.getPurchaseItemTitle());
        values.put(PURCHACEITEM_COLUMN_PRICE, item.getPurchaseItemPrice());
        values.put(PURCHACEITEM_COLUMN_CATEGORY_ID, item.getCategoryId());
        values.put(PURCHACEITEM_COLUMN_STATUS, item.getStatus());
        values.put(PURCHACEITEM_COLUMN_CREATEDATE, Common.getDate(dayCount, Common.dateFormat3));

        id = db.insert(TABLE_PURCHACEITEM, null, values);

        return (int)id;
    }



    public void deleteItemAll(){
        String query = "DELETE FROM " + TABLE_PURCHACEITEM;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

    public List<KakaiboManager.MonthSummery>getMonthSummery(){

        List<KakaiboManager.MonthSummery> list = new ArrayList<>();
        String query = "SELECT *," +
                "SUM(" + PURCHACEITEM_COLUMN_PRICE + ") as total, " +
                "strftime('%Y-%m'," + PURCHACEITEM_COLUMN_CREATEDATE + ") as target " +
                " FROM " + TABLE_PURCHACEITEM +
                " INNER JOIN " + TABLE_CATEGORY +
                " ON " + TABLE_PURCHACEITEM + "." + PURCHACEITEM_COLUMN_CATEGORY_ID +
                " = " + TABLE_CATEGORY + "." + CATEGORY_COLUMN_ID +
                " WHERE strftime('%Y%m'," + PURCHACEITEM_COLUMN_CREATEDATE + ")=strftime('%Y%m','"+ Common.getDate(0,Common.dateFormat3) +"')" +
                " GROUP BY " + TABLE_CATEGORY + "." +CATEGORY_COLUMN_ID +
                " ORDER BY total DESC ";

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){

            KakaiboManager.MonthSummery summery = new KakaiboManager.MonthSummery();

            Category category = new Category();
            category.setCategoryId(c.getInt(c.getColumnIndex(CATEGORY_COLUMN_ID)));
            category.setCategoryTitle(c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME)));
            category.setColorCode(c.getString(c.getColumnIndex(CATEGORY_COLUMN_COLORCODE)));
            category.setCreatedate(c.getString(c.getColumnIndex(CATEGORY_COLUMN_CREATEDATE)));

            int total = c.getInt(c.getColumnIndex("total"));

            summery.setCategory(category);
            summery.setTotal(total);
            summery.setTarget(c.getString(c.getColumnIndex("target")));

            list.add(summery);

            c.moveToNext();
        }



        return list;



    }


    public List<JoinedCategoryItem> getJoinedItemWidthCategory(){

        List<JoinedCategoryItem> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT *" +
                " FROM " + TABLE_PURCHACEITEM +
                " LEFT JOIN " + TABLE_CATEGORY +
                " ON " + TABLE_PURCHACEITEM + "." + PURCHACEITEM_COLUMN_CATEGORY_ID +
                " = " + TABLE_CATEGORY + "." + CATEGORY_COLUMN_ID +
                " WHERE strftime('%Y%m%d'," + PURCHACEITEM_COLUMN_CREATEDATE + ")=strftime('%Y%m%d','"+ Common.getDate(0,Common.dateFormat3) +"')"+
                " ORDER BY " + TABLE_PURCHACEITEM + "." + PURCHACEITEM_COLUMN_ID + " DESC ";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        try{
            while(!c.isAfterLast()){

                JoinedCategoryItem join = new JoinedCategoryItem();

                //item
                PurchaseItem item = new PurchaseItem();
                item.setPurchaseItemId(c.getInt(c.getColumnIndex(PURCHACEITEM_COLUMN_ID)));
                item.setPurchaseItemTitle(c.getString(c.getColumnIndex(PURCHACEITEM_COLUMN_NAME)));
                item.setPurchaseItemPrice(c.getInt(c.getColumnIndex(PURCHACEITEM_COLUMN_PRICE)));
                item.setCategoryId(c.getInt(c.getColumnIndex(PURCHACEITEM_COLUMN_CATEGORY_ID)));
                item.setStatus(c.getInt(c.getColumnIndex(PURCHACEITEM_COLUMN_STATUS)));
                item.setCreatedate(c.getString(c.getColumnIndex(PURCHACEITEM_COLUMN_CREATEDATE)));

                //category
                Category category = new Category();
                category.setCategoryId(c.getInt(c.getColumnIndex(CATEGORY_COLUMN_ID)));
                category.setCategoryTitle(c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME)));
                category.setResorceImgPath(c.getString(c.getColumnIndex(CATEGORY_IMAGE_PATH)));
                category.setColorCode(c.getString(c.getColumnIndex(CATEGORY_COLUMN_COLORCODE)));
                category.setCategoryType(c.getInt(c.getColumnIndex(CATEGORY_COLUMN_TYPE)));
                category.setCreatedate(c.getString(c.getColumnIndex(CATEGORY_COLUMN_CREATEDATE)));

                join.setItem(item);
                join.setCategory(category);

                list.add(join);

                c.moveToNext();
            }
        }catch(Exception e){
            Common.log(e.getMessage());
        }

        return list;

    }


    //price
    public PriceList getPriceList(){

        PriceList list = new PriceList();

        SQLiteDatabase db = getWritableDatabase();

        List<PurchaseItem> testList = this.getPurchaseItem();
        for (PurchaseItem item : testList){
            Common.log("\n\nid:" + item.getPurchaseItemId()) ;
            Common.log("\ntitle:" + item.getPurchaseItemTitle()) ;
            Common.log("\nprice:" + item.getPurchaseItemPrice()) ;
            Common.log("\ncategory id:" + item.getCategoryId()) ;
            Common.log("\nstatus:" + item.getStatus()) ;
            Common.log("\ncreatedate:" + item.getCreatedate()) ;
        }

        String query1 = "SELECT strftime('%Y%m%d',date('now')) as datenow," +
                "strftime('%Y%m%d'," + PURCHACEITEM_COLUMN_CREATEDATE + ") as datecreate,"+
                "SUM(" + PURCHACEITEM_COLUMN_PRICE + ") as todayTotal " +
                " FROM  " + TABLE_PURCHACEITEM +
                " WHERE " + PURCHACEITEM_COLUMN_STATUS + " = 0 " +
                " AND strftime('%Y%m%d'," + PURCHACEITEM_COLUMN_CREATEDATE + ")=strftime('%Y%m%d','"+ Common.getDate(0,Common.dateFormat3) +"')";

        String query2 = "SELECT strftime('%Y%m',date('now')) as datenow," +
                "strftime('%Y%m'," + PURCHACEITEM_COLUMN_CREATEDATE + ") as datecreate,"+
                PURCHACEITEM_COLUMN_CREATEDATE + " as confirmdata, " +
                "SUM(" + PURCHACEITEM_COLUMN_PRICE + ") as monthTotal" +
                " FROM  " + TABLE_PURCHACEITEM +
                " WHERE " + PURCHACEITEM_COLUMN_STATUS + " = 0 " +
                " AND strftime('%Y%m'," + PURCHACEITEM_COLUMN_CREATEDATE + ")=strftime('%Y%m','"+ Common.getDate(0,Common.dateFormat3) +"')";

        //date_format
        Cursor c = db.rawQuery(query1,null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            Common.log("datenow:" + c.getString(c.getColumnIndex("datenow")));
            Common.log("datecreate:" + c.getString(c.getColumnIndex("datecreate")));
            Common.log("■today:" + c.getInt(c.getColumnIndex("todayTotal")));
            list.setTodaysList(c.getInt(c.getColumnIndex("todayTotal")));
            c.moveToNext();
        }

        c = db.rawQuery(query2, null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            Common.log("datenow:" + c.getString(c.getColumnIndex("datenow")));
            Common.log("datecreate:" + c.getString(c.getColumnIndex("datecreate")));
            Common.log("confirmdata:" + c.getString(c.getColumnIndex("confirmdata")));
            Common.log("■month:" + c.getInt(c.getColumnIndex("monthTotal")));
            list.setMonthlist(c.getInt(c.getColumnIndex("monthTotal")));
            c.moveToNext();
        }

        return list;


    }


    public void switchShow(int categoryId, int mode){

        String query = "";
        if(mode == KakaiboManager.SHOW ){
            query = "UPDATE " + TABLE_CATEGORY + " SET " + CATEGORY_COLUMN_SHOWSTATUS + "=" + KakaiboManager.NOTSHOW +
                    " WHERE " + CATEGORY_COLUMN_ID + " = " + categoryId;

        }else if(mode == KakaiboManager.NOTSHOW){
            query = "UPDATE " + TABLE_CATEGORY + " SET " + CATEGORY_COLUMN_SHOWSTATUS + "=" + KakaiboManager.NOTSHOW +
                    " WHERE " + CATEGORY_COLUMN_ID + " = " + categoryId;
        }

        if(query != ""){

            //run query
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(query);

        }else{

            // wrong mode

        }



    }




}
