package com.lizhizhan.appstore.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.lizhizhan.appstore.manager.ThreadManager;
import com.lizhizhan.appstore.ui.holder.BaseHolder;
import com.lizhizhan.appstore.ui.holder.MoreHolder;
import com.lizhizhan.appstore.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by lizhizhan on 2016/10/24.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    private ArrayList<T> data;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_MORE = 0;

    public MyBaseAdapter(ArrayList<T> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size() + 1;
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //返回布局类型个数
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //返回当前应该展示哪种布局
    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {
            //返回加载更多的
            return TYPE_MORE;
        } else {
            //返回正常的
            return getInnerType(position);
        }
    }

    /**
     * 子类可以重写方法，返回View的类型。
     * 默认的正常的
     * 正常的或者定制的。
     *
     * @param position
     * @return
     */
    public int getInnerType(int position) {
        return TYPE_NORMAL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder;
        if (convertView == null) {
            //1.加载布局文件
            //2.findviewbyid
            //3.setTag
            if (getItemViewType(position) == TYPE_MORE) {
                //判断是否是加载更多
                holder = new MoreHolder(hasMore());
            } else {
                holder = getHolder(position);//子类返回对象
            }
        } else {
            holder = (BaseHolder) convertView.getTag();
        }

        if (getItemViewType(position) != TYPE_MORE) {
            //通过setData再refreshView
            holder.setData(getItem(position));
        } else {
            //加载更多布局,布局展示，开始加载数据
            //有更多的数据加载更多。
            MoreHolder moreHolder = (MoreHolder) holder;
            if (moreHolder.getData() == MoreHolder.STATE_MORE) {
                loadMore(moreHolder);
            }

        }
        return holder.getmRootView();
    }

    /**
     * 默认有，子类可以重写的
     *
     * @return true, false
     */
    public boolean hasMore() {
        return true;
    }


    public abstract BaseHolder<T> getHolder(int position);

    private boolean isLoadMore = false;

    /**
     * 加载更多数据
     */
    public void loadMore(final MoreHolder holder) {
        if (!isLoadMore) {
            isLoadMore = true;
            //            new Thread() {
            //                @Override
            //                public void run() {
            //                    final ArrayList<T> onLoadMore = onLoadMore();
            //                    UIUtils.RunOnUiThread(new Runnable() {
            //                        @Override
            //                        public void run() {
            //                            if (onLoadMore != null) {
            //                                if (onLoadMore.size() < 20) {
            //                                    holder.setData(MoreHolder.STATE_NONE);
            //                                    Toast.makeText(UIUtils.getContext(), "没有更多数据了", Toast.LENGTH_LONG).show();
            //                                } else {
            //                                    holder.setData(MoreHolder.STATE_MORE);
            //                                }
            //                                //当前集合添加新数据，刷新界面
            //                                data.addAll(onLoadMore);
            //                                MyBaseAdapter.this.notifyDataSetChanged();
            //
            //                            } else {
            //                                //加载更多失败
            //                                holder.setData(MoreHolder.STATE_ERROR);
            //                            }
            //                            isLoadMore = false;
            //                        }
            //                    });
            //
            //                }
            //            }.start();
            ThreadManager.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<T> onLoadMore = onLoadMore();
                    UIUtils.RunOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (onLoadMore != null) {
                                if (onLoadMore.size() < 20) {
                                    holder.setData(MoreHolder.STATE_NONE);
                                    Toast.makeText(UIUtils.getContext(), "没有更多数据了", Toast.LENGTH_LONG).show();
                                } else {
                                    holder.setData(MoreHolder.STATE_MORE);
                                }
                                //当前集合添加新数据，刷新界面
                                data.addAll(onLoadMore);
                                MyBaseAdapter.this.notifyDataSetChanged();

                            } else {
                                //加载更多失败
                                holder.setData(MoreHolder.STATE_ERROR);
                            }
                            isLoadMore = false;
                        }
                    });
                }
            });
        }

    }

    public int getListSize() {
        return data.size();
    }

    public abstract ArrayList<T> onLoadMore();
}
