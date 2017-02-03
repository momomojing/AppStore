package com.lizhizhan.appstore.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.domain.CategoryInfo;
import com.lizhizhan.appstore.utils.UIUtils;

/**
 * Created by lizhizhan on 2016/10/31.
 */

public class TitleHolder extends BaseHolder<CategoryInfo> {

    private TextView tvTitle;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_title);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    public void refreshView(CategoryInfo data) {
        tvTitle.setText(data.title);
    }
}
