package com.marktony.zhihudaily.bean;

/**
 * Created by lizhaotailang on 2016/6/3.
 */
public class FanfouDailyPost {

    private String avatarUrl;
    private String author;
    private String content;
    private String time;
    private String imgUrl;

    public FanfouDailyPost(String avatarUrl,String author,String content,String time,String imgUrl){
        this.author = author;
        this.avatarUrl = avatarUrl;
        this.content = content;
        this.time = time;
        this.imgUrl = imgUrl;
    }

    public String getAuthor() {
        return author;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
