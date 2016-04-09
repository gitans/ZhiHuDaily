package com.marktony.zhihudaily.UI.Activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.marktony.zhihudaily.R;

import java.lang.reflect.Field;

/**
 * Created by lizhaotailang on 2016/4/9.
 * 用于滑动返回
 */
public class BaseSwipeBackActivity extends AppCompatActivity implements SlidingPaneLayout.PanelSlideListener {

    private SlidingPaneLayout mSlidingPaneLayout;
    private FrameLayout container;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mSlidingPaneLayout = new SlidingPaneLayout(this);

        // 通过反射来改变SlidingPanelLayout的值
        try {
            // mOverhangSize属性，意思就是左菜单离右边屏幕边缘的距离
            Field f_overHang = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
            f_overHang.setAccessible(true);
            // 这个mOverhangSize值为菜单到右边屏幕的最短距离，默认
            //是32dp，现在给它改成0
            f_overHang.set(mSlidingPaneLayout,0);

            mSlidingPaneLayout.setPanelSlideListener(this);
            mSlidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);

        // 添加两个view,这是左侧菜单，因为Activity是透明的，这里就不用设置了
        View leftView = new View(this);
        // 设置全屏
        leftView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 添加到SlidingPaneLayout中
        mSlidingPaneLayout.addView(leftView,0);

        // 内容布局，用来存放Activity布局用的
        container = new FrameLayout(this);
        // 内容布局不应该是透明，这里加了白色背景
        // mContainerFl.setBackgroundColor(getResources().getColor(android.R.color.white));
        // 全屏幕显示
        container.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        // 添加到SlidingPaneLayout中
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mSlidingPaneLayout.addView(container,1);

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(getLayoutInflater().inflate(layoutResID,null));
    }

    @Override
    public void setContentView(View view) {
        setContentView(view,new  ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(mSlidingPaneLayout, params);

        container.removeAllViews();
        container.addView(view,params);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(View panel) {

        // 菜单打开后，结束掉这个activity
        onBackPressed();
        this.overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    public void onPanelClosed(View panel) {

    }
}
