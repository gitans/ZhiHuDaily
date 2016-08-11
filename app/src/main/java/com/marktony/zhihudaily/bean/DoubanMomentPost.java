package com.marktony.zhihudaily.bean;

/**
 * Created by Lizhaotailang on 2016/8/11.
 */

public class DoubanMomentPost {

    private int id;
    private String title;
    private String abs;
    private String thumb;

    public DoubanMomentPost(int id, String title, String abs, String thumb) {
        this.id = id;
        this.title = title;
        this.abs = abs;
        this.thumb = thumb;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAbs() {
        return abs;
    }

    public String getThumb() {
        return thumb;
    }
}
