package com.lizhizhan.appstore.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.domain.CategoryInfo;
import com.lizhizhan.appstore.http.HttpHelper;
import com.lizhizhan.appstore.utils.BitmapHelper;
import com.lizhizhan.appstore.utils.UIUtils;

/**
 * Created by lizhizhan on 2016/10/31.
 */

public class CategoryHolder extends BaseHolder<CategoryInfo> implements View.OnClickListener {

    private ImageView ivIcon1;
    private ImageView ivIcon2;
    private ImageView ivIcon3;
    private TextView tvName1;
    private TextView tvName2;
    private TextView tvName3;
    private BitmapUtils bitmapUtils;
    private LinearLayout llGrid1, llGrid2, llGrid3;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_category);
        ivIcon1 = (ImageView) view.findViewById(R.id.iv_icon1);
        ivIcon2 = (ImageView) view.findViewById(R.id.iv_icon2);
        ivIcon3 = (ImageView) view.findViewById(R.id.iv_icon3);

        tvName1 = (TextView) view.findViewById(R.id.tv_name1);
        tvName2 = (TextView) view.findViewById(R.id.tv_name2);
        tvName3 = (TextView) view.findViewById(R.id.tv_name3);

        llGrid1 = (LinearLayout) view.findViewById(R.id.ll_grid1);
        llGrid2 = (LinearLayout) view.findViewById(R.id.ll_grid2);
        llGrid3 = (LinearLayout) view.findViewById(R.id.ll_grid3);

        llGrid1.setOnClickListener(this);
        llGrid2.setOnClickListener(this);
        llGrid3.setOnClickListener(this);

        bitmapUtils = BitmapHelper.getmBitmapUtils();
        return view;
    }

    @Override
    public void refreshView(CategoryInfo data) {
        tvName1.setText(data.name1);
        tvName2.setText(data.name2);
        tvName3.setText(data.name3);
        bitmapUtils.display(ivIcon1, HttpHelper.URL + "image?name=" + data.url1);
        bitmapUtils.display(ivIcon2, HttpHelper.URL + "image?name=" + data.url2);
        bitmapUtils.display(ivIcon3, HttpHelper.URL + "image?name=" + data.url3);
    }

    @Override
    public void onClick(View v) {
        CategoryInfo data = getData();
        switch (v.getId()) {
            case R.id.ll_grid1:
                Toast.makeText(UIUtils.getContext(), data.name1, Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_grid2:
                Toast.makeText(UIUtils.getContext(), data.name2, Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_grid3:
                Toast.makeText(UIUtils.getContext(), data.name3, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
