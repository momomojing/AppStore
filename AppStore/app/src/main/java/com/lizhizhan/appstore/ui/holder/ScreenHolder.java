package com.lizhizhan.appstore.ui.holder;

import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.domain.AppInfo;
import com.lizhizhan.appstore.http.HttpHelper;
import com.lizhizhan.appstore.utils.BitmapHelper;
import com.lizhizhan.appstore.utils.UIUtils;

import java.util.ArrayList;

/**
 * 运用截图展示模块
 * Created by lizhizhan on 2016/11/2.
 */

public class ScreenHolder extends BaseHolder<AppInfo> {
    ImageView[] mScreenPic;
    private BitmapUtils bitmapUtils;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_picinfo);
        mScreenPic = new ImageView[5];
        mScreenPic[0] = (ImageView) view.findViewById(R.id.iv_pic1);
        mScreenPic[1] = (ImageView) view.findViewById(R.id.iv_pic2);
        mScreenPic[2] = (ImageView) view.findViewById(R.id.iv_pic3);
        mScreenPic[3] = (ImageView) view.findViewById(R.id.iv_pic4);
        mScreenPic[4] = (ImageView) view.findViewById(R.id.iv_pic5);
        bitmapUtils = BitmapHelper.getmBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        ArrayList<String> screen = data.screen;
        for (int i = 0; i < 5; i++) {
            if (i < screen.size()) {
                bitmapUtils.display(mScreenPic[i], HttpHelper.URL + "image?name=" + screen.get(i));
            } else {
                mScreenPic[i].setVisibility(View.GONE);
            }
        }


    }
}
