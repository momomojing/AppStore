package com.lizhizhan.appstore.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.domain.AppInfo;
import com.lizhizhan.appstore.protocol.HomeDetailProtocol;
import com.lizhizhan.appstore.ui.holder.DetailAppinfoHolder;
import com.lizhizhan.appstore.ui.holder.DetailDesInfoHolder;
import com.lizhizhan.appstore.ui.holder.DetailDownloadHolder;
import com.lizhizhan.appstore.ui.holder.DetailSafeHolder;
import com.lizhizhan.appstore.ui.holder.ScreenHolder;
import com.lizhizhan.appstore.ui.view.LoadingPage;
import com.lizhizhan.appstore.utils.UIUtils;

import butterknife.ButterKnife;

/**
 * Created by lizhizhan on 2016/11/1.
 */

public class HomeDetailActivity extends BaseActivity {

    FrameLayout flDetailAppinfo;
    private LoadingPage loadingPage;
    private String packageName;
    private AppInfo data;
    private FrameLayout flDetailSafeinfo;
    private HorizontalScrollView hsv;
    private FrameLayout flDetailDesinfo;
    private FrameLayout flDetailDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingPage = new LoadingPage(this) {
            @Override
            public View onCreatSuccessView() {
                return HomeDetailActivity.this.OnCreatSuccessView();
            }

            @Override
            public ResultState initData() {
                return HomeDetailActivity.this.initData();
            }
        };
        //        开始加载网络数据
        setContentView(loadingPage);
        ButterKnife.bind(this);
        packageName = getIntent().getStringExtra("packageName");
        loadingPage.LoadData();
        initActionBar();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //切换抽屉
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {
        //        标题栏图标的显示
        ActionBar actionBar = getSupportActionBar();
        //        actionBar.setLogo(R.drawable.ic_launcher);
        //        actionBar.setDisplayUseLogoEnabled(true);//显示图标
        //        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);//显示标题
        actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键
        //        actionBar.hide();//把整个actionbar都隐藏掉
    }

    public View OnCreatSuccessView() {
        //初始化成功布局
        View view = UIUtils.inflate(R.layout.page_home_detail);
        //初始化运用信息模块
        flDetailAppinfo = (FrameLayout) view.findViewById(R.id.fl_detail_appinfo);
        //动态的给布局模块添加数据
        DetailAppinfoHolder detailAppinfoHolder = new DetailAppinfoHolder();
        View detailAppinfoView = detailAppinfoHolder.getmRootView();
        detailAppinfoHolder.setData(data);
        flDetailAppinfo.addView(detailAppinfoView);
        //初始化安全信息模块
        flDetailSafeinfo = (FrameLayout) view.findViewById(R.id.fl_detail_safeinfo);
        DetailSafeHolder detailSafeHolder = new DetailSafeHolder();
        View detailSafeView = detailSafeHolder.getmRootView();
        detailSafeHolder.setData(data);
        flDetailSafeinfo.addView(detailSafeView);
        //初始化运用截图模块
        hsv = (HorizontalScrollView) view.findViewById(R.id.hsv);
        ScreenHolder screenHolder = new ScreenHolder();
        View screenView = screenHolder.getmRootView();
        screenHolder.setData(data);
        hsv.addView(screenView);
        //初始化运用详细描述
        flDetailDesinfo = (FrameLayout) view.findViewById(R.id.fl_detail_desinfo);
        DetailDesInfoHolder detailDesInfoHolder = new DetailDesInfoHolder();
        View desInfoView = detailDesInfoHolder.getmRootView();
        detailDesInfoHolder.setData(data);
        flDetailDesinfo.addView(desInfoView);
        //初始化底端下载
        flDetailDownload = (FrameLayout) view.findViewById(R.id.fl_detail_download);
        DetailDownloadHolder detailDownloadHolder = new DetailDownloadHolder();
        View detailDownView = detailDownloadHolder.getmRootView();
        flDetailDownload.addView(detailDownView);
        detailDownloadHolder.setData(data);
        return view;
    }

    public LoadingPage.ResultState initData() {
        HomeDetailProtocol homeDetailProtocol = new HomeDetailProtocol(packageName);
        data = homeDetailProtocol.getData(0);
        if (data != null) {
            return LoadingPage.ResultState.STATE_SUCCESS;
        } else {
            return LoadingPage.ResultState.STATE_ERROR;
        }

    }
}
