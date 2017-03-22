package com.example.administrator.Util;

import com.example.administrator.entity.HomeTxtEntity;
import com.example.administrator.entity.SearchEntity;
import com.example.administrator.entity.TxtEntity;

import java.util.List;

public interface Ihtml {

    TxtEntity jiexishuju(String data, String url);

    HomeTxtEntity jiexihome(String data, String url, boolean iszhangjie);

    List<SearchEntity> jiexiSearch(String data);
}
