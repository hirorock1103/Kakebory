package com.example.hirorock1103.kakebory;

import android.content.ContentValues;
import android.content.Context;
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

    public void deleteAll(){
        super.deleteCategoryAll();
        super.deleteItemAll();
    }

    public void MakeTestData(){

        //data delete
        super.deleteCategoryAll();
        super.deleteItemAll();

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

        int catId0 = super.addCategory(sample0);
        int catId1 = super.addCategory(sample1);
        int catId2 = super.addCategory(sample2);

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

        super.addPurchaceItem(sample3);
        super.addPurchaceItem(sample4);


        //前日のデータ
        PurchaseItem item = new PurchaseItem();
        item.setCategoryId(catId2);
        item.setPurchaseItemTitle("テスト");
        item.setPurchaseItemPrice(500);
        item.setCreatedate(Common.getDate(-3,Common.dateFormat2));

        super.addPurchaceItemTest(item, -3);

        item.setCategoryId(catId2);
        item.setPurchaseItemTitle("テスト");
        item.setPurchaseItemPrice(400);
        item.setCreatedate(Common.getDate(-7,Common.dateFormat2));

        super.addPurchaceItemTest(item, -7);



    }


    public void switchShow(int selectedItemId, int mode){

        super.switchShow(selectedItemId, mode);

    }


    public PriceList getPriceList(){
        //PriceList list = new PriceList();
        return super.getPriceList();
    }

    public List<JoinedCategoryItem> getJoinedCategoryItem(){
        return super.getJoinedItemWidthCategory();
    }


    public List<Category> getCategory(){
        return super.getCategory();
    }

    public List<PurchaseItem> getPurchaseItem(){
        return super.getPurchaseItem();
    }

    public Category getCategoryById(int categoryId){
        return super.getCategoryById(categoryId);
    }

    public int updateCategory(Category category, int targetId){
        return super.updateCategory(category, targetId);
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
