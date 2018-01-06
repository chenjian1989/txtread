package com.example.administrator.Util;

import android.text.TextUtils;

import com.example.administrator.application.App;
import com.example.administrator.entity.HomeTxtEntity;
import com.example.administrator.entity.SearchEntity;
import com.example.administrator.entity.TxtEntity;
import com.example.administrator.inter.Ihtml;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析网站地址 www.shuqizw.com
 */
public class ShuqizwUtil implements Ihtml {

    private String mUrl = "http://www.shuqizw.com";

    @Override
    public TxtEntity jiexishuju(String data, String url) {
        TxtEntity txtEntity = new TxtEntity();
        txtEntity.setUrl(url);
        if (!TextUtils.isEmpty(data)) {
            try {
                // 章节的标题
                String title = App.getInstance().substringnew(data, "<li class=\"active\">", "</li>");
                txtEntity.setTitle(title);
//                // 上一章地址
//                String prev = App.getInstance().substringnew(str, "id=\"pager_prev\" href=\"", "\" target=\"_top\"");
//                if (prev != null && prev.contains("html")) {
//                    txtEntity.setPrev(App.getInstance().substringnew(url, 0, url.lastIndexOf("/") + 1) + prev);
//                }
//                // 下一章地址
//                String next = App.getInstance().substringnew(str, "id=\"pager_next\" href=\"", "\" target=\"_top\"");
//                if (next != null && next.contains("html")) {
//                    txtEntity.setNext(App.getInstance().substringnew(url, 0, url.lastIndexOf("/") + 1) + next);
//                }
                // 内容数据
                String c = App.getInstance().substringnew(data, "id=\"htmlContent\">", "</div>");
                c = App.getInstance().substringnew(c, c.indexOf("<br><br>") + 8);
                c = c.replace("\r\n", "\n").replace("<br/>","\n").replace("<br />\r\n" +
                        "<br />\r\n","\n").replace("<br />\r\n", "\n").replace("<br />","\n").replace("&lt;", "").replace("&gt;", "").replace("&nbsp;", " ");
                c = c.replace("\n\n\n\n","\n\n").replace("\n\n\n","\n\n");
                c = c.replace("-->><p class=\"text-danger text-center mg0\">本章未完，点击下一页继续阅读</p>","");
                //c = "    " + title + "\n" + c;
                txtEntity.setData(c);
                //LogUtil.e("ShuqizwUtil转码后的str: " + c);
            } catch (Exception e) {
                e.getStackTrace();
                txtEntity.setError("解析异常" + e.getMessage());
                LogUtil.e("ShuqizwUtil--jiexishuju() ERROR: " + e.getMessage());
            }
        } else {
            txtEntity.setError("获取的数据为空!");
        }

        return txtEntity;
    }

    @Override
    public HomeTxtEntity jiexihome(String data, String url, boolean iszhangjie) {
        HomeTxtEntity homeTxtEntity = new HomeTxtEntity();
        homeTxtEntity.setHomeUrl(url);
        homeTxtEntity.setName(App.getInstance().substringnew(data, "<h1 class=\"bookTitle\">", "</h1>"));
        data = App.getInstance().substringnew(data, data.indexOf("<p class=\"booktag\">") + 18);
        homeTxtEntity.setAuthor(App.getInstance().substringnew(data, "title=\"","\">"));

        data = App.getInstance().substringnew(data, data.indexOf("最新章节") + 4);
        homeTxtEntity.setNewest(App.getInstance().substringnew(data, "\">","</a>"));
        homeTxtEntity.setFengmianUrl(App.getInstance().substringnew(data, "src=\"","\""));
        if(iszhangjie){
            homeTxtEntity.setChapters(ergodiclist(data, url));
        }
        return homeTxtEntity;
    }

    @Override
    public List<SearchEntity> jiexiSearch(String data) {
        return null;
    }

    private ArrayList<String> ergodiclist(String data, String url) {
        ArrayList<String> datalist = new ArrayList<>();
        data = App.getInstance().substringnew(data, data.indexOf("id=\"list-chapterAll\""));
        boolean isErgodic = true;
        while (isErgodic) {
            int index = data.indexOf("<dd class=\"col-md-3\"><a href=\"");
            int index2 = data.indexOf("<dd class=\"col-md-3\"><a href='");
            if (index >= 0 || index2 >= 0) {
                String str;
                if(index2 != -1 && (index2 == 0 || index > index2)){
                    str = App.getInstance().substringnew(data, "<dd class=\"col-md-3\"><a href='", "'");
                } else {
                    str = App.getInstance().substringnew(data, "<dd class=\"col-md-3\"><a href=\"", "\"");
                }
                String urls = mUrl + str;
                String t = App.getInstance().substringnew(data, "title=\"","\">");
                datalist.add(urls + ";" + t);
                index = data.indexOf("</a></dd>");
                if(index >= 0){
                    data = App.getInstance().substringnew(data, index + 9);
                }
            } else {
                isErgodic = false;
            }
        }
        return datalist;
    }
}
