package com.lizhizhan.appstore.protocol;

import com.lizhizhan.appstore.domain.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lizhizhan on 2016/11/1.
 */

public class HomeDetailProtocol extends BaseProtocol<AppInfo> {

    private String packageName;

    public HomeDetailProtocol(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getKey() {
        return "detail";
    }

    @Override
    public String getParms() {
        return "&packageName=" + packageName;
    }

    @Override
    public AppInfo parseData(String result) {
        //使用JSONObject 遇到{}就是 JSONObject。遇到[]就是ARRAY
        try {
            JSONObject jo = new JSONObject(result);
            AppInfo appInfo = new AppInfo();
            appInfo.des = jo.getString("des");
            appInfo.downloadUrl = jo.getString("downloadUrl");
            appInfo.iconUrl = jo.getString("iconUrl");
            appInfo.name = jo.getString("name");
            appInfo.packageName = jo.getString("packageName");
            appInfo.size = jo.getLong("size");
            appInfo.stars = (float) jo.getDouble("stars");
            appInfo.id = jo.getString("id");
            appInfo.author = jo.getString("author");
            appInfo.date = jo.getString("date");
            appInfo.downloadNum = jo.getString("downloadNum");
            appInfo.version = jo.getString("version");

            JSONArray ja = jo.getJSONArray("safe");
            ArrayList<AppInfo.SafeInfo> safe = new ArrayList<AppInfo.SafeInfo>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo1 = ja.getJSONObject(i);
                AppInfo.SafeInfo safeInfo = new AppInfo.SafeInfo();
                safeInfo.safeDes = jo1.getString("safeDes");
                safeInfo.safeDesUrl = jo1.getString("safeUrl");
                safeInfo.safeUrl = jo1.getString("safeUrl");
                safe.add(safeInfo);
            }
            appInfo.safe = safe;
            JSONArray ja1 = jo.getJSONArray("screen");
            ArrayList<String> screen = new ArrayList<String>();
            for (int i = 0; i < ja1.length(); i++) {
                String pic = ja1.getString(i);
                screen.add(pic);
            }
            appInfo.screen = screen;
            return appInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
