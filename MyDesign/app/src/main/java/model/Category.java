package model;

import android.content.Context;

public class Category {
    public String id, title;
    int imgs;
    Context mContext;
    private String fullResolutionUrlLocal;

    public Category(Context mContext) {
        this.mContext = mContext;
    }

    public Category(Context mContext, String id, String title, int imgs) {
        this.mContext = mContext;
        this.id = id;
        this.title = title;
        this.imgs = imgs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        // Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(),this.imgs);
        return this.imgs;
    }

    public void setImg(int imgs) {
        this.imgs = imgs;
    }


}
