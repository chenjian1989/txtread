package com.example.administrator.entity;

public class SearchEntity {

    private String name;

    private String author;

    // 最新章节
    private String newest;

    private String homeUrl;

    private String fengmianUrl;

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
