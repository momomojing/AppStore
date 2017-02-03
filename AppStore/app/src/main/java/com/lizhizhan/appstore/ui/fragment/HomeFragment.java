package com.lizhizhan.appstore.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.lizhizhan.appstore.domain.AppInfo;
import com.lizhizhan.appstore.protocol.HomeProtocol;
import com.lizhizhan.appstore.ui.activity.HomeDetailActivity;
import com.lizhizhan.appstore.ui.adapter.MyBaseAdapter;
import com.lizhizhan.appstore.ui.holder.BaseHolder;
import com.lizhizhan.appstore.ui.holder.HomeHeadHolder;
import com.lizhizhan.appstore.ui.holder.HomeHolder;
import com.lizhizhan.appstore.ui.view.LoadingPage;
import com.lizhizhan.appstore.ui.view.MyListView;
import com.lizhizhan.appstore.utils.UIUtils;

import java.util.ArrayList;

/**
 * 首页
 * Created by lizhizhan on 2016/10/21.
 */

public class HomeFragment extends BaseFragment {
    private ArrayList<AppInfo> data;
    private ArrayList<String> picList;

    @Override
    public View OnCreatSuccessView() {
        MyListView listView = new MyListView(UIUtils.getContext());
        //增加头布局，展示轮播条
        HomeHeadHolder headHolder = new HomeHeadHolder();
        listView.addHeaderView(headHolder.getmRootView());
        if (picList != null) {
            //设置轮播条数据
            headHolder.setData(picList);
        }
        listView.setAdapter(new HomeAdapter(data));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //减去一条头布局
                AppInfo appInfo = data.get(position - 1);
                if (appInfo != null) {
                    String packageName = appInfo.packageName;
                    Intent intent = new Intent(UIUtils.getContext(), HomeDetailActivity.class);
                    intent.putExtra("packageName", packageName);
                    startActivity(intent);
                }
            }
        });


        return listView;
    }

    /**
     * 运行在子线程，直接请求网络获取数据
     *
     * @return
     */
    public LoadingPage.ResultState onLoad() {

        HomeProtocol homeProtocol = new HomeProtocol();
        data = homeProtocol.getData(0);//加载第一页数据
        picList = homeProtocol.getPicList();
        return check(data);//校检数据并返回
    }


    class HomeAdapter extends MyBaseAdapter<AppInfo> {

        public HomeAdapter(ArrayList<AppInfo> data) {
            super(data);
        }

        public BaseHolder<AppInfo> getHolder(int position) {
            return new HomeHolder();
        }

        @Override
        public ArrayList<AppInfo> onLoadMore() {
            HomeProtocol homeProtocol = new HomeProtocol();
            ArrayList<AppInfo> moreData = homeProtocol.getData(getListSize());
            return moreData;
        }
    }
}
