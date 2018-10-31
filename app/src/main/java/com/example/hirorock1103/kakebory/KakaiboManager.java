package com.example.hirorock1103.kakebory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class KakaiboManager extends MyDbHandler {

    private Context context;

    public static final int SHOW = 1;
    public static final int NOTSHOW = 2;


    KakaiboManager(Context context){
        super(context);
        this.context = context;
    }


    /**
     * カテゴリを追加
     * @param category
     * @return
     */
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

    /**
     * カテゴリとItem削除
     */
    public void deleteAll(){
        deleteCategoryAll();
        deleteItemAll();
    }

    /**
     * カテゴリ削除
     */
    public void deleteCategoryAll(){
        String query = "DELETE FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

    /**
     * Item削除
     */
    public void deleteItemAll(){
        String query = "DELETE FROM " + TABLE_PURCHACEITEM;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

    /**
     * 購入レコードを追加
     * @param item
     * @return
     */
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

    /**
     * テストデータ作成用
     */
    public void MakeTestData(){

        //data delete
        deleteCategoryAll();
        deleteItemAll();

        Category sample0 = new Category();

        sample0.setCategoryTitle("カテゴリなし");
        sample0.setResorceImgPath("/external/images/media/21352");
        sample0.setColorCode("#000000");
        sample0.setCategoryShowStatus(0);
        sample0.setCreatedate(Common.getDate(0,Common.dateFormat2));


        Category sample1 = new Category();

        sample1.setCategoryTitle("カフェ");
        sample1.setResorceImgPath("/external/images/media/21352");
        sample1.setColorCode("#AAAAAA");
        sample0.setCategoryShowStatus(1);
        sample1.setCreatedate(Common.getDate(0,Common.dateFormat2));

        Category sample2 = new Category();

        sample2.setCategoryTitle("昼食");
        sample2.setResorceImgPath("/external/images/media/21352");
        sample2.setColorCode("#FF0000");
        sample2.setCreatedate(Common.getDate(0,Common.dateFormat2));

        int catId0 = addCategory(sample0);
        int catId1 = addCategory(sample1);
        int catId2 = addCategory(sample2);

        PurchaseItem sample3 = new PurchaseItem();

        sample3.setPurchaseItemTitle("コーヒー");
        sample3.setPurchaseItemPrice(100);
        sample3.setCategoryId(catId1);
        sample3.setCreatedate(Common.getDate(0,Common.dateFormat2));

        PurchaseItem sample4 = new PurchaseItem();

        sample4.setPurchaseItemTitle("カフェオレ");
        sample4.setPurchaseItemPrice(180);
        sample4.setCategoryId(catId2);
        sample4.setCreatedate(Common.getDate(0,Common.dateFormat2));

        addPurchaceItem(sample3);
        addPurchaceItem(sample4);


        //前日のデータ
        PurchaseItem item = new PurchaseItem();
        item.setCategoryId(catId2);
        item.setPurchaseItemTitle("テスト");
        item.setPurchaseItemPrice(500);
        item.setCreatedate(Common.getDate(-3,Common.dateFormat2));

        addPurchaceItemTest(item, -3);

        item.setCategoryId(catId2);
        item.setPurchaseItemTitle("テスト");
        item.setPurchaseItemPrice(400);
        item.setCreatedate(Common.getDate(-7,Common.dateFormat2));

        addPurchaceItemTest(item, -7);



    }

    /**
     * テスト用
     * @param item
     * @param dayCount
     * @return
     */
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


    /**
     *月ごとのグルーピング
     * @return
     */
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


    /**
     * 購入金額リストを取得
     * @return　PriceList
     */
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


    /**
     * カテゴリとItemのJoinデータを取得する
     * @return
     */
    public List<JoinedCategoryItem> getJoinedCategoryItem(){
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


    /**
     * カテゴリを取得する
     * @return
     */
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

    /**
     * 購入リストを取得する
     * @return
     */
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
    /**
     * IDからカテゴリを取得する
     * @param categoryId
     * @return
     */
    public Category getCategoryById(int categoryId){


        String query = "SELECT * FROM "+ TABLE_CATEGORY +
                " WHERE " + CATEGORY_COLUMN_ID + " = " + categoryId;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        Category category = new Category();

        while(!c.isAfterLast()){
            category.setCategoryId(c.getInt(c.getColumnIndex(CATEGORY_COLUMN_ID)));
            category.setCategoryTitle(c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME)));
            category.setColorCode(c.getString(c.getColumnIndex(CATEGORY_COLUMN_COLORCODE)));
            category.setCategoryShowStatus(c.getInt(c.getColumnIndex(CATEGORY_COLUMN_SHOWSTATUS)));
            category.setCategoryType(c.getInt(c.getColumnIndex(CATEGORY_COLUMN_TYPE)));
            category.setIcomImage(c.getBlob(c.getColumnIndex(CATEGORY_ICON_IMAGE)));
            category.setCreatedate(c.getString(c.getColumnIndex(CATEGORY_COLUMN_CREATEDATE)));
            c.moveToNext();
        }


        return category;

    }

    /**
     * カテゴリをUPDATE
     * @param category
     * @param targetId
     * @return
     */
    public int updateCategory(Category category, int targetId){
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_NAME, category.getCategoryTitle());
        values.put(CATEGORY_ICON_IMAGE, category.getIcomImage());
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_CATEGORY, values, CATEGORY_COLUMN_ID + " = " + targetId, null);

        return targetId;
    }


    public static class MonthSummery{
        private Category category;
        private String target;
        private int total;

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }
    }


}
