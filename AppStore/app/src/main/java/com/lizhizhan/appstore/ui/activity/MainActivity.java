package com.lizhizhan.appstore.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.ui.fragment.BaseFragment;
import com.lizhizhan.appstore.ui.fragment.FragmentFactory;
import com.lizhizhan.appstore.ui.view.PagerTab;
import com.lizhizhan.appstore.utils.UIUtils;

public class MainActivity extends BaseActivity {


    private MyAdapter adapter;
    private ViewPager vp;
    private PagerTab pagerTab;
    private ActionBarDrawerToggle toggle;

    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    private ArrayAdapter arrayAdapter;
    private DrawerLayout drawy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp = (ViewPager) findViewById(R.id.vp);
        pagerTab = (PagerTab) findViewById(R.id.pager_tab);
        drawy = (DrawerLayout) findViewById(R.id.drawy);
        adapter = new MyAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        pagerTab.setViewPager(vp);

        pagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment = FragmentFactory.creatFragment(position);
                //加载数据
                fragment.LoadData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initToolBar();
        //        initActionBar();
    }

    private void initToolBar() {
        ListView lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitle("Appstore");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#000000")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        toggle = new ActionBarDrawerToggle(this, drawy, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        toggle.syncState();
        drawy.setDrawerListener(toggle);
        //设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);
    }

    //    private void initActionBar() {
    //        //        标题栏图标的显示
    //        ActionBar actionBar = getSupportActionBar();
    //        actionBar.setLogo(R.drawable.ic_launcher);
    //        actionBar.setDisplayUseLogoEnabled(true);
    //        actionBar.setDisplayShowHomeEnabled(true);
    //        actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键
    //        actionBar.setHomeButtonEnabled(true);//Home 可点击
    //        DrawerLayout drawy = (DrawerLayout) findViewById(R.id.drawy);
    //        toggle = new ActionBarDrawerToggle(this, drawy, R.string.drawer_open, R.string.drawer_close);
    //        toggle.syncState();//开关与侧边栏同步
    //    }
    //
    //    @Override
    //    public boolean onOptionsItemSelected(MenuItem item) {
    //        switch (item.getItemId()) {
    //            case android.R.id.home:
    //                //切换抽屉
    //                toggle.onOptionsItemSelected(item);
    //        }
    //        return super.onOptionsItemSelected(item);
    //    }

    class MyAdapter extends FragmentPagerAdapter {

        private final String[] mTabNames;

        public MyAdapter(FragmentManager fm) {
            super(fm);
            mTabNames = UIUtils.getStringArray(R.array.tab_names);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }

        /**
         * 返回当前位置的Fragment
         *
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.creatFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }
    }
}
