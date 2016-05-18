package com.marktony.zhihudaily.Entities;

/**
 * Created by lizhaotailang on 2016/5/18.
 */
public class Comment {

    private String avatarUrl;
    private String author;
    private String comment;
    private String time;
    private String likes;

    public Comment(String avatarUrl,String author,String comment,String time,String likes){
        this.avatarUrl = avatarUrl;
        this.author = author;
        this.comment = comment;
        this.time = time;
        this.likes = likes;
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

    public String getLikes() {
        return likes;
    }

    public String getTime() {
        return time;
    }
}
