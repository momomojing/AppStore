package com.lizhizhan.appstore.protocol;

import com.lizhizhan.appstore.domain.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 运用的网络请求
 * Created by lizhizhan on 2016/10/28.
 */

public class AppProtocol extends BaseProtocol<ArrayList<AppInfo>> {
    @Override
    public String getKey() {
        return "app";
    }

    @Override
    public String getParms() {
        return "";
    }

    @Override
    public ArrayList<AppInfo> parseData(String result) {
        try {
            JSONArray ja = new JSONArray(result);
            ArrayList<AppInfo> list = new ArrayList<AppInfo>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                AppInfo appInfo = new AppInfo();
                appInfo.des = jo.getString("des");
                appInfo.downloadUrl = jo.getString("downloadUrl");
                appInfo.iconUrl = jo.getString("iconUrl");
                appInfo.id = jo.getString("id");
                appInfo.name = jo.getString("name");
                appInfo.packageName = jo.getString("packageName");
                appInfo.size = jo.getLong("size");
                appInfo.stars = (float) jo.getDouble("stars");
                list.add(appInfo);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
