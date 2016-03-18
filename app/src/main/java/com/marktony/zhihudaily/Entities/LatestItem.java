package com.marktony.zhihudaily.Entities;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/3/18.
 */
public class LatestItem {

    private String title;
    private List<String> images = new ArrayList<String>();
    private String type;
    private String id;

    public LatestItem(String title,List<String> images,String type,String id){
        this.title = title;
        this.images = images;
        this.type = type;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public List<String> getImages() {
        return images;
    }

    public String getFirstImg(){
        if (images.isEmpty())
            return null;
        return images.get(0);
    }

}
