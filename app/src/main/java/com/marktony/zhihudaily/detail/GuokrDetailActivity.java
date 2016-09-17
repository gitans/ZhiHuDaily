package com.marktony.zhihudaily.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.app.App;

/**
 * 2016.6.15 黎赵太郎
 * 果壳文章阅读
 */
public class GuokrDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(App.getThemeResources());
        setContentView(R.layout.frame);

        GuokrDetailFragment fragment = GuokrDetailFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();

        new GuokrDetailPresenter(this, fragment);

    }

}
