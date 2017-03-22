package com.example.administrator.Util;

public interface HttpCallback {
    void httpSuccess(String data, String url);
    void httpError(String des);
}
