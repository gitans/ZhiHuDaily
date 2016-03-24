package com.marktony.zhihudaily.Entities;

/**
 * Created by lizhaotailang on 2016/3/23.
 * 主题列表
 */
public class ThemeList {

    private String id;
    private String thumbnail;   //图片地址
    private String description;     //描述
    private String name;    //名称

    public ThemeList(String id, String thumbnail, String description, String name){
        this.id = id;
        this.thumbnail = thumbnail;
        this.description = description;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
