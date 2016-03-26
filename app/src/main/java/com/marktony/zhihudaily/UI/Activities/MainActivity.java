package com.marktony.zhihudaily.UI.Activities;

import android.content.Intent;
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
import com.marktony.zhihudaily.UI.Fragments.PageFragment;
import com.marktony.zhihudaily.UI.Fragments.ThemeFragment;
import com.marktony.zhihudaily.UI.Fragments.LatestFragment;
import com.marktony.zhihudaily.Utils.NetworkState;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    // 提示用户是否联网
    private MaterialDialog dialog;

    public Map<String,PageFragment> map = new HashMap<String,PageFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        dialog = new MaterialDialog.Builder(MainActivity.this)
                .title("提示")
                .content("当前没有网络连接...")
                .positiveText("去设置")
                .negativeText("知道了")
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

        if (id == R.id.action_settings) {
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

        } else if (id == R.id.nav_manage) {



        } else if (id == R.id.nav_night_mode) {



        } else if (id == R.id.nav_about) {



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

}
