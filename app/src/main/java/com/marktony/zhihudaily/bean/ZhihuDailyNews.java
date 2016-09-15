package com.marktony.zhihudaily.bean;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/8/20.
 */

public class ZhihuDailyNews {

    private String date;
    private ArrayList<ZhihuDailyQuestion> stories;
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public ArrayList<ZhihuDailyQuestion> getStories() {
        return stories;
    }
    public void setStories(ArrayList<ZhihuDailyQuestion> stories) {
        this.stories = stories;
    }

}
