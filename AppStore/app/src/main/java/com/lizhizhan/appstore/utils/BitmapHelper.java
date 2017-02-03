package com.lizhizhan.appstore.utils;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by lizhizhan on 2016/10/28.
 */

public class BitmapHelper {
    private static BitmapUtils mBitmapUtils=null;
    public static BitmapUtils getmBitmapUtils(){
        if (mBitmapUtils==null){
            synchronized (BitmapHelper.class){
                if (mBitmapUtils==null){
                    mBitmapUtils=new BitmapUtils(UIUtils.getContext());
                }
            }
        }
        return mBitmapUtils;
    }
}
