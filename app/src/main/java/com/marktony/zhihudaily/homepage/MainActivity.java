package com.marktony.zhihudaily.homepage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.about.AboutPreferenceActivity;
import com.marktony.zhihudaily.app.App;
import com.marktony.zhihudaily.service.CacheService;
import com.marktony.zhihudaily.settings.SettingsPreferenceActivity;
import com.marktony.zhihudaily.util.Theme;


public class MainActivity extends AppCompatActivity implements MainFragment.OnViewPagerCreated {

    private ViewGroup viewGroup;
    private ImageView imageView;
    private MainFragment fragment;

    private final long ANIMTION_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(App.getThemeResources());
        setContentView(R.layout.activity_main);

        Theme.setStatusBarColor(this);

        addFragment();
        initViews();

        startService(new Intent(this, CacheService.class));

    }

    private void addFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment != null){
            fragmentTransaction.remove(fragment);
        }
        fragment = MainFragment.newInstance();
        fragmentTransaction.add(R.id.layout_fragment, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_change_theme) {
            changeTheme();
            save();
            Theme.setStatusBarColor(this);

        } else if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this,SettingsPreferenceActivity.class));
        } else if (id == R.id.action_about) {
            startActivity(new Intent(MainActivity.this,AboutPreferenceActivity.class));
        }

        return true;
    }

    private void initViews() {
        viewGroup = (ViewGroup) findViewById(R.id.layout_fragment);
        imageView = (ImageView) findViewById(R.id.imageview);
    }

    /**
     * 改变主题
     */
    private void changeTheme(){
        setDrawableCahe();
        setTheme();
        getState();
    }

    // 屏幕方向改变时调用的方法，拦截屏幕切换
    // manifest文件中给activity设置了相应的参数
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 获取布局的DrawableCache给ImageView覆盖Fragment
     */
    private void setDrawableCahe() {
        //设置false清除缓存
        viewGroup.setDrawingCacheEnabled(false);
        //设置true之后可以获取Bitmap
        viewGroup.setDrawingCacheEnabled(true);
        imageView.setImageBitmap(viewGroup.getDrawingCache());
        imageView.setAlpha(1f);
        imageView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置主题
     */
    private void setTheme() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.my_theme, typedValue, true);
        switch (typedValue.data){

            case Theme.DAY_THEME:
                App.setThemeValue(Theme.NIGHT_THEME);
                setTheme(Theme.RESOURCES_NIGHT_THEME);
                break;
            case Theme.NIGHT_THEME:
                App.setThemeValue(Theme.DAY_THEME);
                setTheme(Theme.RESOURCES_DAY_THEME);
                break;
        }
    }

    /**
     * 主题选择的本地存储
     */
    private void save() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_settings",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme", App.getThemeValue());
        editor.commit();
    }

    /**
     * 获取当前fragment状态
     */
    public void getState() {
        addFragment();
    }

    /**
     * ImageView的动画
     * @param view
     */
    private void startAnimation(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f).setDuration(ANIMTION_TIME);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float n = (float) animation.getAnimatedValue();
                view.setAlpha(1f - n);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imageView.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }

    @Override
    public void viewPagerCreated() {
        startAnimation(imageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (CacheService.class.getName().equals(service.service.getClassName())) {
                stopService(new Intent(this, CacheService.class));
            }
        }
    }
}
