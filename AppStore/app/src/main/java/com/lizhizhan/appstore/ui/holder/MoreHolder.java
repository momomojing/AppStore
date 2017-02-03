package com.lizhizhan.appstore.ui.holder;

import android.view.View;
import android.widget.LinearLayout;

import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.utils.UIUtils;

/**
 * Created by lizhizhan on 2016/10/24.
 */

public class MoreHolder extends BaseHolder<Integer> {
    //加载更多的状态
    public static final int STATE_MORE = 0;
    public static final int STATE_ERROR = 1;
    public static final int STATE_NONE = 2;
    private LinearLayout llMore;
    private LinearLayout llError;

    /**
     *
     * @param hasMore　是否展示上拉更多
     */
    public MoreHolder(boolean hasMore) {
        if (hasMore) {
            setData(STATE_MORE);
        }else {
            setData(STATE_NONE);
        }
    }

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_more);
        llMore = (LinearLayout) view.findViewById(R.id.ll_more);
        llError = (LinearLayout) view.findViewById(R.id.ll_error);
        return view;
    }

    @Override
    public void refreshView(Integer data) {
        switch (data) {
            case STATE_MORE:
                llMore.setVisibility(View.VISIBLE);
                llError.setVisibility(View.GONE);
                break;
            case STATE_ERROR:
                llMore.setVisibility(View.GONE);
                llError.setVisibility(View.VISIBLE);
                break;
            case STATE_NONE:
                llMore.setVisibility(View.GONE);
                llError.setVisibility(View.GONE);
                break;
        }
    }
}
