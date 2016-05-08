package com.marktony.zhihudaily.UI.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.UI.Fragments.HotPostFragment;
import com.marktony.zhihudaily.UI.Fragments.ThemeFragment;
import com.marktony.zhihudaily.UI.Fragments.LatestFragment;
import com.marktony.zhihudaily.Utils.NetworkState;
import com.marktony.zhihudaily.Utils.UtilFunctions;

import java.io.File;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    // 提示用户是否联网
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        UtilFunctions.setTheme(MainActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        dialog = new MaterialDialog.Builder(MainActivity.this)
                .title(R.string.point)
                .content(R.string.no_network_connected)
                .positiveText(R.string.go_to_set)
                .negativeText(R.string.got_it)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        if ( !NetworkState.networkConneted(MainActivity.this)){
            dialog.show();
        }

        navigationView.setCheckedItem(R.id.nav_home);
        LatestFragment fragment = new LatestFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        if (id == R.id.action_copy_right) {
            MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                    .title(R.string.action_copy_right)
                    .neutralText(R.string.got_it)
                    .content(R.string.copy_right)
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }).build();

            dialog.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //toolbar的标题应该根据不同的id来判断
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            changeFragment(new LatestFragment());
            toolbar.setTitle(getString(R.string.app_name));

        } else if (id == R.id.nav_theme_post) {

            changeFragment(new ThemeFragment());
            toolbar.setTitle(item.getTitle());

        } else if (id == R.id.nav_hot_post) {

            changeFragment(new HotPostFragment());
            toolbar.setTitle(item.getTitle());

        } else if (id == R.id.nav_change_theme){

            if (UtilFunctions.getThemeState(MainActivity.this) == 0){
                UtilFunctions.setThemeState(MainActivity.this,1);
            } else {
                UtilFunctions.setThemeState(MainActivity.this,0);
            }

            this.finish();
            this.startActivity(this.getIntent());


        } else if (id == R.id.nav_settings) {

            startActivity(new Intent(MainActivity.this,SettingsActivity.class));

        } else if (id == R.id.nav_about) {

            startActivity(new Intent(MainActivity.this,AboutActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    //改变fragment
    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        deleteDir(getCacheDir());
    }

    // 屏幕方向改变时调用的方法，拦截屏幕切换
    // manifest文件中给activity设置了相应的参数
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 递归删除应用下的缓存
     * @param dir 需要删除的文件或者文件目录
     * @return 文件是否删除
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

}
