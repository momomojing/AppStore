package com.lizhizhan.appstore.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.domain.SubjectInfo;
import com.lizhizhan.appstore.http.HttpHelper;
import com.lizhizhan.appstore.utils.BitmapHelper;
import com.lizhizhan.appstore.utils.UIUtils;

/**
 * 专题的Holder
 * Created by lizhizhan on 2016/10/28.
 */

public class SubjetHolder extends BaseHolder<SubjectInfo> {

    private ImageView ivSbPic;
    private TextView tvSbTitle;
    private BitmapUtils bitmapUtils;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_subject);
        ivSbPic = (ImageView) view.findViewById(R.id.iv_sb_pic);
        tvSbTitle = (TextView) view.findViewById(R.id.tv_sb_title);
        bitmapUtils = BitmapHelper.getmBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(SubjectInfo data) {
        tvSbTitle.setText(data.des);
        bitmapUtils.display(ivSbPic, HttpHelper.URL + "image?name=" + data.url);
    }
}
