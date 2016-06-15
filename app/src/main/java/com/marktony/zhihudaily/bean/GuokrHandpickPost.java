package com.marktony.zhihudaily.bean;

/**
 * Created by lizhaotailang on 2016/6/14.
 */
public class GuokrHandpickPost {

    private String title;
    private String headlineImg;
    private String id;

    public GuokrHandpickPost(String id,String title,String headline_img) {

        this.title = title;
        this.headlineImg = headline_img;
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public String getHeadlineImg() {
        return headlineImg;
    }

    public String getTitle() {
        return title;
    }
}
