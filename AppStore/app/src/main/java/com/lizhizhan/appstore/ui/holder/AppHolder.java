package com.lizhizhan.appstore.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.domain.AppInfo;
import com.lizhizhan.appstore.http.HttpHelper;
import com.lizhizhan.appstore.utils.BitmapHelper;
import com.lizhizhan.appstore.utils.UIUtils;

/**
 * 运用的Holder
 * Created by lizhizhan on 2016/10/28.
 */

public class AppHolder extends BaseHolder<AppInfo> {

    private TextView tvAppName;
    private TextView tvSize;
    private ImageView ivIcon;
    private RatingBar rbStar;
    private TextView tvDec;
    private BitmapUtils bitmapUtils;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_home);
        tvAppName = (TextView) view.findViewById(R.id.tv_appName);
        tvSize = (TextView) view.findViewById(R.id.tv_size);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        rbStar = (RatingBar) view.findViewById(R.id.rb_star);
        tvDec = (TextView) view.findViewById(R.id.tv_dec);
        bitmapUtils = BitmapHelper.getmBitmapUtils();

        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        tvAppName.setText(data.name);
        tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
        tvDec.setText(data.des);
        bitmapUtils.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);
        rbStar.setRating(data.stars);
    }
}
