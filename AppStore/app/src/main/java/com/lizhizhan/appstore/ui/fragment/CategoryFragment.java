package com.lizhizhan.appstore.ui.fragment;

import android.view.View;

import com.lizhizhan.appstore.domain.CategoryInfo;
import com.lizhizhan.appstore.protocol.CategoryProtocol;
import com.lizhizhan.appstore.ui.adapter.MyBaseAdapter;
import com.lizhizhan.appstore.ui.holder.BaseHolder;
import com.lizhizhan.appstore.ui.holder.CategoryHolder;
import com.lizhizhan.appstore.ui.holder.TitleHolder;
import com.lizhizhan.appstore.ui.view.LoadingPage;
import com.lizhizhan.appstore.ui.view.MyListView;
import com.lizhizhan.appstore.utils.UIUtils;

import java.util.ArrayList;

/**
 * 首页
 * Created by lizhizhan on 2016/10/21.
 */

public class CategoryFragment extends BaseFragment {

    private ArrayList<CategoryInfo> data;

    @Override
    public View OnCreatSuccessView() {
        MyListView listView = new MyListView(UIUtils.getContext());
        listView.setAdapter(new CatagoryAdapter(data));
        return listView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        CategoryProtocol protocol = new CategoryProtocol();
        data = protocol.getData(0);
        return check(data);
    }

    class CatagoryAdapter extends MyBaseAdapter<CategoryInfo> {

        public CatagoryAdapter(ArrayList<CategoryInfo> data) {
            super(data);
        }

        @Override
        public boolean hasMore() {
            return false;
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getInnerType(int position) {
            CategoryInfo info = data.get(position);
            if (info.isTitle) {
                //返回标题类型
                return super.getInnerType(position) + 1;
            } else {
                return super.getInnerType(position);
            }

        }

        @Override
        public BaseHolder<CategoryInfo> getHolder(int position) {
            CategoryInfo info = data.get(position);
            if (info.isTitle) {
                //返回标题的Holder
                return new TitleHolder();
            } else {
                //返回正常显示的Holder
                return new CategoryHolder();
            }
        }

        @Override
        public ArrayList<CategoryInfo> onLoadMore() {
            return null;
        }
    }
}
