package com.example.writeitdown.ModelClasses;

public class PermanentList {
    String mTitles;
    int mImages, mBgColors, mAddBtn,mDeleteBtn;

    public PermanentList(String mTitles, int mImages, int mBgColors) {
        this.mTitles = mTitles;
        this.mImages = mImages;
        this.mBgColors = mBgColors;
    }


    public PermanentList(String mTitles, int mBgColors) {
        this.mTitles = mTitles;
        this.mBgColors = mBgColors;
    }
    public PermanentList() {
    }

    public PermanentList(String mTitles, int mImages, int mBgColors, int mAddBtn, int mDeleteBtn) {
        this.mTitles = mTitles;
        this.mImages = mImages;
        this.mBgColors = mBgColors;
        this.mAddBtn = mAddBtn;
        this.mDeleteBtn = mDeleteBtn;
    }

    public PermanentList(String mTitles, int mImages, int mBgColors, int mAddBtn) {
        this.mTitles = mTitles;
        this.mImages = mImages;
        this.mBgColors = mBgColors;
        this.mAddBtn = mAddBtn;
    }

    public int getmAddBtn() {
        return mAddBtn;
    }

    public void setmAddBtn(int mAddBtn) {
        this.mAddBtn = mAddBtn;
    }

    public int getmDeleteBtn() {
        return mDeleteBtn;
    }

    public void setmDeleteBtn(int mDeleteBtn) {
        this.mDeleteBtn = mDeleteBtn;
    }

    public String getmTitles() {
        return mTitles;
    }

    public void setmTitles(String mTitles) {
        this.mTitles = mTitles;
    }

    public int getmImages() {
        return mImages;
    }

    public void setmImages(int mImages) {
        this.mImages = mImages;
    }

    public int getmBgColors() {
        return mBgColors;
    }

    public void setmBgColors(int mBgColors) {
        this.mBgColors = mBgColors;
    }
}
