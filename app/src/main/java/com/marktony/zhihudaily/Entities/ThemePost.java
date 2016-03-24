package com.marktony.zhihudaily.Entities;

/**
 * Created by lizhaotailang on 2016/3/24.
 * 主题日报列表单项类
 */
public class ThemePost {

    private String id;
    private String[] images;
    private String title;

    public ThemePost(String id,String[] images,String title){
        this.id = id;
        this.images = images;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String[] getImages() {

        if (images == null)
            return null;
        return images;
    }

    public String getFirstImg(){
        if (images == null)
            return null;
        return images[0];
    }
}
