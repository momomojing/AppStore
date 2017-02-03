package com.lizhizhan.appstore.protocol;

import com.lizhizhan.appstore.domain.CategoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lizhizhan on 2016/10/31.
 */

public class CategoryProtocol extends BaseProtocol<ArrayList<CategoryInfo>> {
    @Override
    public String getKey() {
        return "category";
    }

    @Override
    public String getParms() {
        return "";
    }

    @Override
    public ArrayList<CategoryInfo> parseData(String result) {
        try {
            JSONArray ja = new JSONArray(result);
            ArrayList<CategoryInfo> list = new ArrayList<CategoryInfo>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                //初始化标题对象
                if (jo.has("title")) {
                    CategoryInfo categoryInfo = new CategoryInfo();
                    String title = jo.getString("title");
                    categoryInfo.title = title;
                    categoryInfo.isTitle = true;
                    list.add(categoryInfo);
                }
                //初始化分类对象
                if (jo.has("infos")) {
                    JSONArray ja1 = jo.getJSONArray("infos");
                    for (int j = 0; j < ja1.length(); j++) {
                        JSONObject jo1 = ja1.getJSONObject(j);
                        CategoryInfo categoryInfo = new CategoryInfo();
                        categoryInfo.name1 = jo1.getString("name1");
                        categoryInfo.name2 = jo1.getString("name2");
                        categoryInfo.name3 = jo1.getString("name3");
                        categoryInfo.url1 = jo1.getString("url1");
                        categoryInfo.url2 = jo1.getString("url2");
                        categoryInfo.url3 = jo1.getString("url3");
                        categoryInfo.isTitle = false;
                        list.add(categoryInfo);
                    }

                }
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
