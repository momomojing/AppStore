package com.lizhizhan.appstore.domain;

import java.util.ArrayList;

/**
 * 首页运用信息封装
 * Created by lizhizhan on 2016/10/27.
 */

public class AppInfo {
    public String des;
    public String downloadUrl;
    public String iconUrl;
    public String id;
    public String name;
    public String packageName;
    public long size;
    public float stars;

    //补充字段，给运用详情页使用
    public String author;
    public String date;
    public String downloadNum;
    public String version;
    public ArrayList<SafeInfo> safe;
    public ArrayList<String> screen;

    //当一个内部类是public static的时候, 和外部类没有区别
    public static class SafeInfo {
        public String safeDes;
        public String safeDesUrl;
        public String safeUrl;
    }

}
