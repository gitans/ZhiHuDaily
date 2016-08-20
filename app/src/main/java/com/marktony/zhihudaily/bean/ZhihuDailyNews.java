package com.marktony.zhihudaily.bean;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/8/20.
 */

public class ZhihuDailyNews {

    private String date;
    private ArrayList<ZhihuDailyQuestion> stories;
    private ArrayList<TopStories> top_stories;
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
    public ArrayList<TopStories> getTop_stories() {
        return top_stories;
    }
    public void setTop_stories(ArrayList<TopStories> top_stories) {
        this.top_stories = top_stories;
    }

    public class TopStories {
        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        public String getImage() {
            return image;
        }
        public void setImage(String image) {
            this.image = image;
        }
        public int getType() {
            return type;
        }
        public void setType(int type) {
            this.type = type;
        }
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getGa_prefix() {
            return ga_prefix;
        }
        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
    }

}
