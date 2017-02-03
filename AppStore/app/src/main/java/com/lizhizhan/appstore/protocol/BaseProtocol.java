package com.lizhizhan.appstore.protocol;

import com.lizhizhan.appstore.http.HttpHelper;
import com.lizhizhan.appstore.utils.IOUtils;
import com.lizhizhan.appstore.utils.StringUtils;
import com.lizhizhan.appstore.utils.UIUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 访问网络的基类
 * Created by lizhizhan on 2016/10/26.
 */

public abstract class BaseProtocol<T> {


    public T getData(int index) {
        //先判断是否有缓存，有缓存的话就加载缓存
        String result = getCache(index);
        if (StringUtils.isEmpty(result)) {
            //没有缓存就从网络加载
            result = getDataFromServer(index);
        }
        //开始解析 result!=null
        if (result != null) {
            T data = parseData(result);
            return data;
        }

        return null;
    }

    /**
     * 从网络加载数据
     *
     * @param index 表示从哪一条开始加载20条数据 ，用于分页
     */
    private String getDataFromServer(int index) {
        HttpHelper.HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey() + "?index=" + index + getParms());
        if (httpResult != null) {
            String result = httpResult.getString();
            //            System.out.println("访问及结果"+result);
            if (!StringUtils.isEmpty(result)) {
                setCache(index, result);
            }
            return result;
        }
        return null;
    }

    public abstract String getKey();

    public abstract String getParms();

    //写缓存
    public void setCache(int index, String json) {
        File cacheDir = UIUtils.getContext().getCacheDir();//此运用的缓存文件夹
        //生成缓存文件,cacheDir存储文件的目录
        File cacheFlie = new File(cacheDir, getKey() + "?index=" + index + getParms());
        FileWriter writer = null;
        try {
            writer = new FileWriter(cacheFlie);
            long deadLine = System.currentTimeMillis() + 30 * 60 * 1000;
            //第一行写入缓存时间
            writer.write(deadLine + "\n");
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }

    }

    public String getCache(int index) {
        File cacheDir = UIUtils.getContext().getCacheDir();//此运用的缓存文件夹
        //生成缓存文件,cacheDir存储文件的目录
        File cacheFlie = new File(cacheDir, getKey() + "?index=" + index + getParms());
        //判断缓存是否存在
        if (cacheFlie.exists()) {
            //判断缓存是否有效
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFlie));
                String deadLine = reader.readLine();
                long deadTime = Long.parseLong(deadLine);
                long currentTimeMillis = System.currentTimeMillis();
                //当前时间小于过期时间
                if (currentTimeMillis < deadTime) {
                    StringBuffer sb = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }

    /**
     * 解析数据，子类调用
     *
     * @param result
     * @return
     */
    public abstract T parseData(String result);
}
