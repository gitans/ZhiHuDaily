package com.marktony.zhihudaily.bean;

/**
 * Created by lizhaotailang on 2016/6/3.
 */
public class FanfouDailyPost {

    private String avatarUrl;
    private String author;
    private String content;
    private String time;

    public FanfouDailyPost(String avatarUrl,String author,String content,String time){
        this.author = author;
        this.avatarUrl = avatarUrl;
        this.content = content;
        this.time = time;
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
}
