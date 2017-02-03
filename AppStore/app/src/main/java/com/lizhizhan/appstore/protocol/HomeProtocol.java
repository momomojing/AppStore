package com.lizhizhan.appstore.protocol;

import com.lizhizhan.appstore.domain.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 首页网络数据解析
 * Created by lizhizhan on 2016/10/27.
 */

public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>> {

    private ArrayList<String> pictures;

    @Override
    public String getKey() {
        return "home";
    }

    @Override
    public String getParms() {
        return "";
    }

    @Override
    public ArrayList<AppInfo> parseData(String result) {
        try {
            //使用JSONObject 遇到{}就是 JSONObject。遇到[]就是ARRAY
            JSONObject jo = new JSONObject(result);
            //解析运用列表数据
            JSONArray ja = jo.getJSONArray("list");
            ArrayList<AppInfo> appInfosList = new ArrayList<AppInfo>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo1 = ja.getJSONObject(i);
                AppInfo appInfo = new AppInfo();
                appInfo.des = jo1.getString("des");
                appInfo.downloadUrl = jo1.getString("downloadUrl");
                appInfo.iconUrl = jo1.getString("iconUrl");
                appInfo.name = jo1.getString("name");
                appInfo.id = jo1.getString("id");
                appInfo.packageName = jo1.getString("packageName");
                appInfo.size = jo1.getLong("size");
                appInfo.stars = (float) jo1.getDouble("stars");
                //                System.out.println(appInfo.toString());
                appInfosList.add(appInfo);
            }
            //轮播条图片的数据
            JSONArray ja1 = jo.getJSONArray("picture");
            pictures = new ArrayList<String>();
            for (int i = 0; i < ja1.length(); i++) {
                String pic = ja1.getString(i);
                pictures.add(pic);
            }

            return appInfosList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getPicList() {
        return pictures;
    }
}
