package com.lizhizhan.appstore.ui.fragment;

import android.view.View;

import com.lizhizhan.appstore.domain.AppInfo;
import com.lizhizhan.appstore.protocol.AppProtocol;
import com.lizhizhan.appstore.ui.adapter.MyBaseAdapter;
import com.lizhizhan.appstore.ui.holder.AppHolder;
import com.lizhizhan.appstore.ui.holder.BaseHolder;
import com.lizhizhan.appstore.ui.view.LoadingPage;
import com.lizhizhan.appstore.ui.view.MyListView;
import com.lizhizhan.appstore.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by lizhizhan on 2016/10/21.
 */

public class AppFragment extends BaseFragment {
    private ArrayList<AppInfo> data;

    @Override
    public View OnCreatSuccessView() {
        MyListView listView = new MyListView(UIUtils.getContext());
        listView.setAdapter(new AppAdapter(data));
        return listView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        AppProtocol appProtocol = new AppProtocol();
        data = appProtocol.getData(0);
        return check(data);
    }

    class AppAdapter extends MyBaseAdapter<AppInfo> {

        public AppAdapter(ArrayList<AppInfo> data) {
            super(data);
        }

        @Override
        public BaseHolder<AppInfo> getHolder(int position) {
            return new AppHolder();
        }

        @Override
        public ArrayList<AppInfo> onLoadMore() {
            AppProtocol appProtocol = new AppProtocol();
            ArrayList<AppInfo> moreData = appProtocol.getData(getListSize());
            return moreData;
        }
    }
}
