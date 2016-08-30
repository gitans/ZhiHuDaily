package com.marktony.zhihudaily.inner_browser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Lizhaotailang on 2016/8/30.
 */

public class InnerBrowserFragment extends Fragment {

    public InnerBrowserFragment() {

    }

    public static InnerBrowserFragment getInstance() {
        return new InnerBrowserFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
