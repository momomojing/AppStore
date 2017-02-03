package com.lizhizhan.appstore.protocol;

import com.lizhizhan.appstore.domain.SubjectInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 专题网络请求
 * Created by lizhizhan on 2016/10/28.
 */

public class SubjectProtocol extends BaseProtocol<ArrayList<SubjectInfo>> {
    @Override
    public String getKey() {
        return "subject";
    }

    @Override
    public String getParms() {
        return "";
    }

    @Override
    public ArrayList<SubjectInfo> parseData(String result) {
        try {
            JSONArray ja = new JSONArray(result);
            ArrayList<SubjectInfo> list = new ArrayList<SubjectInfo>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                SubjectInfo subjectInfo = new SubjectInfo();
                subjectInfo.des = jo.getString("des");
                subjectInfo.url = jo.getString("url");
                list.add(subjectInfo);
            }
            return list;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
