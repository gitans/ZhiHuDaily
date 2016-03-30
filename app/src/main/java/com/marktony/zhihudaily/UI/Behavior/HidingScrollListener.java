package com.marktony.zhihudaily.UI.Behavior;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by lizhaotailang on 2016/3/31.
 */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    /**
     *
     * @param recyclerView recyclerView
     * @param dx 横向移动距离
     * @param dy 纵向移动距离
     *           准确的说是两个滚动事件之间的距离，而不是总的滚动距离
     *           1.计算出滚动的总距离（deltas相加），但是只在Toolbar隐藏且上滚或者Toolbar未隐藏且下滚的时候，因为我们只关心这两种情况
     *           2.如果总的滚动距离超多了一定值（这个值取决于你自己的设定，越大，需要滑动的距离越长才能显示或者隐藏），我们就根据其方向显示或者隐藏Toolbar（dy>0意味着下滚，dy<0意味着上滚）。
     *           3.实际显示和隐藏的操作我们并没有定义在scroll listener类中，而是定义了两个抽象方法。
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        // 是否有findFirstVisibleItemPosition方法取决layoutmanager
        int firstItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        //show views if first item is first visible position and views are hidden
        if (firstItem == 0){
            if (!controlsVisible){
                onShow();
                controlsVisible = true;
            }
        } else {
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow();
                controlsVisible = true;
                scrolledDistance = 0;
            }
            if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                scrolledDistance += dy;
            }
        }

    }
    public abstract void onHide();
    public abstract void onShow();

}
