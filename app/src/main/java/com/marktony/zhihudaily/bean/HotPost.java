package com.marktony.zhihudaily.bean;

/**
 * Created by lizhaotailang on 2016/3/26.
 */
public class HotPost {

    private String news_id;
    private String url;
    private String title;
    private String thumbnail;

    public HotPost(String news_id,String url,String title,String thumbnail){
        this.news_id = news_id;
        this.url = url;
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getNews_id() {
        return news_id;
    }

    public String getUrl() {
        return url;
    }
}
