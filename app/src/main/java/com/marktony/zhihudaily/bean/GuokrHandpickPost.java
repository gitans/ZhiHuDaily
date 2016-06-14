package com.marktony.zhihudaily.bean;

/**
 * Created by lizhaotailang on 2016/6/14.
 */
public class GuokrHandpickPost {

    private String title;
    private String headline_img;
    private String id;

    public GuokrHandpickPost(String id,String title,String headline_img) {

        this.title = title;
        this.headline_img = headline_img;
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public String getHeadline_img() {
        return headline_img;
    }

    public String getTitle() {
        return title;
    }
}
