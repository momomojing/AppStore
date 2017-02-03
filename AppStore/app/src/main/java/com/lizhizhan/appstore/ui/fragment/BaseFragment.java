package com.lizhizhan.appstore.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lizhizhan.appstore.ui.view.LoadingPage;
import com.lizhizhan.appstore.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by lizhizhan on 2016/10/21.
 */

public abstract class BaseFragment extends Fragment {

    private LoadingPage loadingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //        TextView view = new TextView(UIUtils.getContext());
        //        view.setText(getClass().getSimpleName());
        //        view.setTextColor(Color.BLACK);
        loadingPage = new LoadingPage(UIUtils.getContext()) {
            @Override
            public View onCreatSuccessView() {

                return BaseFragment.this.OnCreatSuccessView();
            }

            @Override
            public ResultState initData() {
                return BaseFragment.this.onLoad();
            }
        };
        return loadingPage;
    }

    public abstract View OnCreatSuccessView();

    public abstract LoadingPage.ResultState onLoad();

    //开始加载数据
    public void LoadData() {
        if (loadingPage != null) {

            loadingPage.LoadData();
        }
    }

    /**
     * 对返回的网络数据进行校检
     *
     * @param obj
     * @return
     */
    public LoadingPage.ResultState check(Object obj) {

        if (obj != null) {
            if (obj instanceof ArrayList) {
                ArrayList list = (ArrayList) obj;
                if (list.isEmpty()) {
                    return LoadingPage.ResultState.STATE_EMPTY;
                } else {
                    return LoadingPage.ResultState.STATE_SUCCESS;
                }
            }

        }
        return LoadingPage.ResultState.STATE_ERROR;
    }
}
