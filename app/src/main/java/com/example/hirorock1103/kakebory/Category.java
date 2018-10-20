package com.example.hirorock1103.kakebory;

public class Category {

    private int categoryId;
    private String categoryTitle;
    private String colorCode;
    private int categoryType;//０:支出,1:収入
    private int categoryShowStatus;//0:表示 1:非表示
    private String resorceImgPath;
    private byte[] icomImage;
    private String categoryCreatedate;


    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCreatedate() {
        return categoryCreatedate;
    }

    public void setCreatedate(String createdate) {
        this.categoryCreatedate = createdate;
    }

    public String getResorceImgPath() {
        return resorceImgPath;
    }

    public void setResorceImgPath(String resorceImgPath) {
        this.resorceImgPath = resorceImgPath;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public byte[] getIcomImage() {
        return icomImage;
    }

    public void setIcomImage(byte[] icomImage) {
        this.icomImage = icomImage;
    }

    public String getCategoryCreatedate() {
        return categoryCreatedate;
    }

    public void setCategoryCreatedate(String categoryCreatedate) {
        this.categoryCreatedate = categoryCreatedate;
    }

    public int getCategoryShowStatus() {
        return categoryShowStatus;
    }

    public void setCategoryShowStatus(int showStatus) {
        this.categoryShowStatus = showStatus;
    }
}
