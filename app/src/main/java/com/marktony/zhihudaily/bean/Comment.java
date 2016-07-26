package com.marktony.zhihudaily.bean;

/**
 * Created by lizhaotailang on 2016/5/18.
 */
public class Comment {

    private String avatarUrl;
    private String author;
    private String comment;
    private String time;

    public Comment(String avatarUrl,String author,String comment,String time ){
        this.avatarUrl = avatarUrl;
        this.author = author;
        this.comment = comment;
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }
}
