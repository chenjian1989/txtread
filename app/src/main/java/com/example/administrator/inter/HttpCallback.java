package com.example.administrator.inter;

public interface HttpCallback {
    void httpSuccess(String data, String url);
    void httpError(String des);
}
