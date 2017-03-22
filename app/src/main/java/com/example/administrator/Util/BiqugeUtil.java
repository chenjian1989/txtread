package com.example.administrator.Util;

import android.text.TextUtils;

import com.example.administrator.entity.HomeTxtEntity;
import com.example.administrator.entity.SearchEntity;
import com.example.administrator.entity.TxtEntity;
import com.example.administrator.txtread.App;

import java.util.ArrayList;
import java.util.List;

public class BiqugeUtil implements Ihtml {

    @Override
    public TxtEntity jiexishuju(String str, String url) {
        TxtEntity txtEntity = new TxtEntity();
        txtEntity.setUrl(url);
        if (!TextUtils.isEmpty(str)) {
            try {
                // 章节的标题
                String title = App.getInstance().substring(str, "<h1>", "</h1>");
                txtEntity.setTitle(title);
                // 上一章地址
                String prev = App.getInstance().substring(str, "id=\"pager_prev\" href=\"", "\" target=\"_top\"");
                if (prev != null && prev.contains("html")) {
                    txtEntity.setPrev(url.substring(0, url.lastIndexOf("/") + 1) + prev);
                }
                // 下一章地址
                String next = App.getInstance().substring(str, "id=\"pager_next\" href=\"", "\" target=\"_top\"");
                if (next != null && next.contains("html")) {
                    txtEntity.setNext(url.substring(0, url.lastIndexOf("/") + 1) + next);
                }
                // 内容数据
                str = App.getInstance().substring(str, "<div id=\"content\">", "</div>");
                int index = str.indexOf("<script");
                if (index > 0) {
                    str = str.substring(0, index);
                }
                str = str.replace("<br/>", "\n").replace("&lt;", "").replace("&gt;", "").replace("&nbsp;", "");
                str = "    " + title + "\n" + str;
                txtEntity.setData(str);
                //LogUtil.e("BiqugeUtil转码后的str: " + str);
            } catch (Exception e) {
                e.getStackTrace();
                txtEntity.setError("解析异常" + e.getMessage());
                LogUtil.e("BiqugeUtil--getHttp() ERROR: " + e.getMessage());
            }
        } else {
            txtEntity.setError("获取的数据为空!");
        }
        return txtEntity;
    }

    @Override
    public HomeTxtEntity jiexihome(String data, String url, boolean isZhangjie) {
        HomeTxtEntity homeTxtEntity = new HomeTxtEntity();
        homeTxtEntity.setHomeUrl(url);
        homeTxtEntity.setName(App.getInstance().substring(data, "<h1>", "</h1>"));
        data = data.substring(data.indexOf("<h1>"));
        homeTxtEntity.setAuthor(App.getInstance().substring(data, "<p>", "</p>").replace("\n", "").replace("\r", "").replace(" ", "")
                .replace("&nbsp;", ""));
        homeTxtEntity.setNewest(newest(data));
        String temp = url.substring(url.indexOf("www"));
        String urls = "http://" + temp.substring(0, temp.indexOf("/")) + App.getInstance().substring(data, "\" src=\"", "\" width=\"120\"");
        homeTxtEntity.setFengmianUrl(urls);
        if(isZhangjie){
            homeTxtEntity.setChapters(ergodiclist(data, url));
        }
        return homeTxtEntity;
    }

    private String newest(String data) {
        data = data.substring(data.indexOf("<p>最新更新：<a href=\""));
        return "最新更新：" + App.getInstance().substring(data, "\">", "</a></p>");
    }

    private ArrayList<String> ergodiclist(String data, String url) {
        ArrayList<String> datalist = new ArrayList<>();
        boolean isErgodic = true;
        while (isErgodic) {
            int index = data.indexOf("<dd> <a style=\"\" href=\"");
            if (index >= 0) {
                String str = App.getInstance().substring(data, "<dd> <a style=\"\" href=\"", "</a></dd>");
                String temp = url.substring(url.indexOf("www"));
                String urls = "http://" + temp.substring(0, temp.indexOf("/")) + str.substring(0, str.indexOf("\">"));
                String t = str.substring(str.indexOf("\">") + 2);
                datalist.add(urls + ";" + t);
                index = data.indexOf("</a></dd>");
                data = data.substring(index + 9);
            } else {
                isErgodic = false;
            }
        }
        return datalist;
    }

    @Override
    public List<SearchEntity> jiexiSearch(String data) {
        List<SearchEntity> searchEntities = new ArrayList<>();
        String bq = "<div class=\"result-item result-game-item\">";
        while (data.contains(bq)) {
            try {
                searchEntities.add(jiexiDangeSearch(App.getInstance().substring(data, bq, bq)));
            } catch (Exception e) {
                e.getStackTrace();
            }
            data = data.substring(data.indexOf(bq) + bq.length());
        }
        return searchEntities;
    }

    private SearchEntity jiexiDangeSearch(String data) {
        SearchEntity searchEntity = new SearchEntity();
        searchEntity.setFengmianUrl(App.getInstance().substring(data, "<img src=\"", "\" alt=\""));
        searchEntity.setHomeUrl(App.getInstance().substring(data, "onclick=\"window.location='", "'\" class=\"").replace("m.","www."));
        searchEntity.setName(App.getInstance().substring(data, "<h3 class=\"result-item-title result-game-item-title\">", "</h3>").replace("<em>",
                "").replace("</em>","").replace(" ","").replace("\r","").replace("\n",""));
        String str = "<span class=\"result-game-item-info-tag-title preBold\">作者：</span>";
        data = data.substring(data.indexOf(str) + str.length());
        searchEntity.setAuthor("作者：" + App.getInstance().substring(data, "<span>", "</span>").replace(" ","").replace("<em>", "")
                .replace("</em>","").replace("\r","").replace("\n",""));
        str = "<span class=\"result-game-item-info-tag-title\">最新章节：</span>";
        data = data.substring(data.indexOf(str) + str.length());
        searchEntity.setNewest("最新章节：" + App.getInstance().substring(data, ");\">", "</span>").replace(" ","").replace("\r","").replace("\n",""));
        return searchEntity;
    }
}
