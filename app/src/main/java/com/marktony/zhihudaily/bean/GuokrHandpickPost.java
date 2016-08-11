package com.marktony.zhihudaily.bean;

/**
 * Created by lizhaotailang on 2016/6/14.
 */
public class GuokrHandpickPost {

    private String title;
    private String headlineImg;
    private String id;
    private String summary;

    public GuokrHandpickPost(String id,String title,String headline_img, String summary) {

        this.title = title;
        this.headlineImg = headline_img;
        this.id = id;
        this.summary = summary;

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

    public String getSummary() {
        return summary;
    }
}
