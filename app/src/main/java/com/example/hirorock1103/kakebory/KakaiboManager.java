package com.example.hirorock1103.kakebory;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class KakaiboManager extends MyDbHandler {

    private Context context;

    KakaiboManager(Context context){
        super(context);
        this.context = context;

    }

    public void MakeTestData(){

        //data delete
        super.deleteCategoryAll();
        super.deleteItemAll();

        Category sample0 = new Category();

        sample0.setCategoryTitle("カテゴリなし");
        sample0.setResorceImgPath("/external/images/media/21352");
        sample0.setColorCode("#000000");
        sample0.setCreatedate(Common.getDate(0,Common.dateFormat2));


        Category sample1 = new Category();

        sample1.setCategoryTitle("カフェ");
        sample1.setResorceImgPath("/external/images/media/21352");
        sample1.setColorCode("#AAAAAA");
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


}
