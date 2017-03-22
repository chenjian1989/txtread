package com.example.administrator.entity;

import java.util.ArrayList;

public class HomeTxtEntity {

    private String name;

    private String author;

    // 最新章节
    private String newest;

    // 章节列表-- 储存的数据中 又以;(分号)隔开 分为 章节地址 章节名称
    private ArrayList<String> chapters;

    private String homeUrl;

    private String fengmianUrl;

    public HomeTxtEntity(){
    }

    public HomeTxtEntity(String name_txt, String author_txt, String newest_txt){
        name = name_txt;
        author = author_txt;
        newest = newest_txt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNewest() {
        return newest;
    }

    public void setNewest(String newest) {
        this.newest = newest;
    }

    public ArrayList<String> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<String> chapters) {
        this.chapters = chapters;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public String getFengmianUrl() {
        return fengmianUrl;
    }

    public void setFengmianUrl(String fengmianUrl) {
        this.fengmianUrl = fengmianUrl;
    }
}
