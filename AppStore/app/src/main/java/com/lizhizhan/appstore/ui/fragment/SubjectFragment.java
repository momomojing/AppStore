package com.lizhizhan.appstore.ui.fragment;

import android.view.View;

import com.lizhizhan.appstore.domain.SubjectInfo;
import com.lizhizhan.appstore.protocol.SubjectProtocol;
import com.lizhizhan.appstore.ui.adapter.MyBaseAdapter;
import com.lizhizhan.appstore.ui.holder.BaseHolder;
import com.lizhizhan.appstore.ui.holder.SubjetHolder;
import com.lizhizhan.appstore.ui.view.LoadingPage;
import com.lizhizhan.appstore.ui.view.MyListView;
import com.lizhizhan.appstore.utils.UIUtils;

import java.util.ArrayList;

/**
 * 专题
 * Created by lizhizhan on 2016/10/21.
 */

public class SubjectFragment extends BaseFragment {

    private ArrayList<SubjectInfo> data;

    @Override
    public View OnCreatSuccessView() {
        MyListView myListView = new MyListView(UIUtils.getContext());
        myListView.setAdapter(new SubjectAdaptet(data));
        return myListView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        SubjectProtocol subjectProtocol = new SubjectProtocol();
        data = subjectProtocol.getData(0);
        return check(data);
    }

    class SubjectAdaptet extends MyBaseAdapter<SubjectInfo> {

        public SubjectAdaptet(ArrayList<SubjectInfo> data) {
            super(data);
        }

        @Override
        public BaseHolder<SubjectInfo> getHolder(int position) {
            return new SubjetHolder();
        }

        @Override
        public ArrayList<SubjectInfo> onLoadMore() {
            SubjectProtocol subjectProtocol = new SubjectProtocol();
            ArrayList<SubjectInfo> moreData = subjectProtocol.getData(getListSize());
            return moreData;
        }
    }
}
