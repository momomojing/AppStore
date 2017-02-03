package com.lizhizhan.appstore.ui.holder;

import android.animation.ValueAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lizhizhan.appstore.R;
import com.lizhizhan.appstore.domain.AppInfo;
import com.lizhizhan.appstore.http.HttpHelper;
import com.lizhizhan.appstore.utils.BitmapHelper;
import com.lizhizhan.appstore.utils.UIUtils;

import java.util.ArrayList;

import static com.lizhizhan.appstore.R.id.rl_des_root;

/**
 * 运用详情页，安全描述模块
 * Created by lizhizhan on 2016/11/1.
 */

public class DetailSafeHolder extends BaseHolder<AppInfo> {
    ImageView[] mSaveIcon;//安全图标
    ImageView[] mDesIcon;//描述图标
    TextView[] mSaveDes;//安全描述
    LinearLayout[] mSaveDesBar;//安全描述的条目

    private BitmapUtils bitmapUtils;
    private LinearLayout llDesRoot;
    private RelativeLayout rlDesRoot;
    private int mDesHeight;
    private LinearLayout.LayoutParams llDesRootParams;
    private ImageView ivArrow;

    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.layout_detail_safeinfo);

        mSaveIcon = new ImageView[4];
        mSaveIcon[0] = (ImageView) view.findViewById(R.id.iv_safe1);
        mSaveIcon[1] = (ImageView) view.findViewById(R.id.iv_safe2);
        mSaveIcon[2] = (ImageView) view.findViewById(R.id.iv_safe3);
        mSaveIcon[3] = (ImageView) view.findViewById(R.id.iv_safe4);

        mDesIcon = new ImageView[4];
        mDesIcon[0] = (ImageView) view.findViewById(R.id.iv_des1);
        mDesIcon[1] = (ImageView) view.findViewById(R.id.iv_des2);
        mDesIcon[2] = (ImageView) view.findViewById(R.id.iv_des3);
        mDesIcon[3] = (ImageView) view.findViewById(R.id.iv_des4);

        mSaveDes = new TextView[4];
        mSaveDes[0] = (TextView) view.findViewById(R.id.tv_des1);
        mSaveDes[1] = (TextView) view.findViewById(R.id.tv_des2);
        mSaveDes[2] = (TextView) view.findViewById(R.id.tv_des3);
        mSaveDes[3] = (TextView) view.findViewById(R.id.tv_des4);

        mSaveDesBar = new LinearLayout[4];
        mSaveDesBar[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
        mSaveDesBar[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
        mSaveDesBar[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
        mSaveDesBar[3] = (LinearLayout) view.findViewById(R.id.ll_des4);

        llDesRoot = (LinearLayout) view.findViewById(R.id.ll_des_root);
        rlDesRoot = (RelativeLayout) view.findViewById(rl_des_root);
        ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);

        rlDesRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        bitmapUtils = BitmapHelper.getmBitmapUtils();
        return view;
    }

    /**
     * 打开关闭安全描述信息
     */
    private boolean isOpen = false;

    private void toggle() {
        ValueAnimator valueAnimator = null;
        if (isOpen) {
            //开的话。关闭，设为false
            isOpen = false;
            valueAnimator = ValueAnimator.ofInt(mDesHeight, 0);
            ivArrow.setImageResource(R.drawable.arrow_down);
        } else {
            isOpen = true;
            valueAnimator = ValueAnimator.ofInt(0, mDesHeight);
            ivArrow.setImageResource(R.drawable.arrow_up);
        }
        //动画更新的监听
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取最新的高度
                Integer height = (Integer) animation.getAnimatedValue();
                llDesRootParams.height = height;
                llDesRoot.setLayoutParams(llDesRootParams);
            }
        });
        valueAnimator.setDuration(200);//动画时间
        valueAnimator.start();//开始动画

    }

    @Override
    public void refreshView(AppInfo data) {
        ArrayList<AppInfo.SafeInfo> safe = data.safe;
        for (int i = 0; i < 4; i++) {
            if (i < safe.size()) {
                AppInfo.SafeInfo safeInfo = safe.get(i);
                bitmapUtils.display(mSaveIcon[i], HttpHelper.URL + "image?name=" + safeInfo.safeUrl);
                bitmapUtils.display(mDesIcon[i], HttpHelper.URL + "image?name=" + safeInfo.safeDesUrl);
                mSaveDes[i].setText(safeInfo.safeDes);

            } else {
                //越界的话，顺便把多余的布局隐藏
                mSaveIcon[i].setVisibility(View.GONE);
                mSaveDesBar[i].setVisibility(View.GONE);

            }
        }
        //获取安全描述的完整高度，传0，0。表示自己不操作。让底层操作
        llDesRoot.measure(0, 0);
        //获取测量后的值
        mDesHeight = llDesRoot.getMeasuredHeight();
        //刚进来默认隐藏的。
        llDesRootParams = (LinearLayout.LayoutParams) llDesRoot.getLayoutParams();
        llDesRootParams.height = 0;
        llDesRoot.setLayoutParams(llDesRootParams);
    }
}
