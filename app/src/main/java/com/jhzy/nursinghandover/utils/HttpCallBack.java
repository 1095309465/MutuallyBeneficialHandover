package com.jhzy.nursinghandover.utils;

/**
 * @Description: 描述
 * @AUTHOR 刘楠  Create By 2016/10/27 0027 16:31
 */
public interface HttpCallBack {
    void onLoading(long current, long total);
    void onFaile();
}
