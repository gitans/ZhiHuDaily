package com.marktony.zhihudaily.Entities;

/**
 * Created by lizhaotailang on 2016/3/22.
 */
public class ShortComments {

    private String author;
    private String id;
    private String content;
    private String likes;
    private String time;
    private String avatar;

    public ShortComments(String author,String id,String content,String likes,String time,String avatar){
        this.author = author;
        this.id = id;
        this.content = content;
        this.likes = likes;
        this.time = time;
        this.avatar = avatar;
    }

    public String getAuthor() {
        return author;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getLikes() {
        return likes;
    }

    public String getTime() {
        return time;
    }

}
