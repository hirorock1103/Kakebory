package com.example.hirorock1103.kakebory;

public class PurchaseItem {

    private int purchaseItemId;
    private String purchaseItemTitle;
    private int purchaseItemPrice;
    private int categoryId;
    private int status;
    private String purchaceItemCreatedate;

    public int getPurchaseItemId() {
        return purchaseItemId;
    }

    public void setPurchaseItemId(int purchaseItemId) {
        this.purchaseItemId = purchaseItemId;
    }

    public String getPurchaseItemTitle() {
        return purchaseItemTitle;
    }

    public void setPurchaseItemTitle(String purchaseItemTitle) {
        this.purchaseItemTitle = purchaseItemTitle;
    }

    public int getPurchaseItemPrice() {
        return purchaseItemPrice;
    }

    public void setPurchaseItemPrice(int purchaseItemPrice) {
        this.purchaseItemPrice = purchaseItemPrice;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedate() {
        return purchaceItemCreatedate;
    }

    public void setCreatedate(String createdate) {
        this.purchaceItemCreatedate = createdate;
    }
}
