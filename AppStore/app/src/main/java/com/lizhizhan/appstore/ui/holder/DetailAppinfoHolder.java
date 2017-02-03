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
 * 运用详情页，APP基本信息,图标等
 * Created by lizhizhan on 2016/11/1.
 */

public class DetailAppinfoHolder extends BaseHolder<AppInfo> {
    ImageView ivIcon;
    TextView tvName;
    RatingBar rbStar;
    TextView tvDownloadNum;
    TextView tvVersion;
    TextView tvDate;
    TextView tvSize;
    private BitmapUtils bitmapUtils;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_appinfo);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        rbStar = (RatingBar) view.findViewById(R.id.rb_star);
        tvDownloadNum = (TextView) view.findViewById(R.id.tv_download_num);
        tvVersion = (TextView) view.findViewById(R.id.tv_version);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvSize = (TextView) view.findViewById(R.id.tv_size);
        bitmapUtils = BitmapHelper.getmBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(AppInfo data) {
        bitmapUtils.display(ivIcon, HttpHelper.URL + "image?name=" + data.iconUrl);
        tvName.setText(data.name);
        rbStar.setRating(data.stars);
        tvDownloadNum.setText("下载量:" + data.downloadNum);
        tvVersion.setText("版本号:" + data.version);
        tvDate.setText(data.date);
        tvSize.setText(Formatter.formatFileSize(UIUtils.getContext(), data.size));
    }
}
