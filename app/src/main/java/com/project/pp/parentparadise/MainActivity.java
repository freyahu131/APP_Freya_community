package com.project.pp.parentparadise;

import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpActionBar();
        initDrawer();
        initContent();
    }

    //幾乎不會用這個生命週期方法，因為同步化規定要寫在這邊
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // home icon will keep still without calling syncState()
        actionBarDrawerToggle.syncState();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //可以放按鈕(<-)
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //ActionBarDrawerToggle監聽器與同步化按鈕(其實也可不加監聽器，為了同步化按鈕才加的)
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.text_Open, R.string.text_Close);

        //檢查手機有沒有新功能setDrawerListener屬於舊寫法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
        } else {
            drawerLayout.setDrawerListener(actionBarDrawerToggle);
        }

        NavigationView view_start = (NavigationView) findViewById(R.id.navigation_start);
        view_start.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.item_Home:
                        initContent();
                        break;
                    case R.id.item_activities:
                        fragment = new ActivitiesFragment();
                        switchFragment(fragment);
                        setTitle("親子活動");
                        break;
                    case R.id.item_Share:
                        fragment = new ShareFragment();
                        switchFragment(fragment);
                        setTitle("分享");
                        break;
                    case R.id.item_Community:
                        fragment = new CommunityFragment();
                        switchFragment(fragment);
                        setTitle("社群");
                        break;
                    case R.id.item_Favorite:
                        fragment = new FavoriteFragment();
                        switchFragment(fragment);
                        setTitle("我的收藏");
                        break;
                    default:
                        initContent();
                        break;
                }
                return true;
            }
        });
    }

    private void initContent() {
        Fragment fragment = new HomeFragment();
        switchFragment(fragment);
        setTitle("首頁");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment fragment) {
        //想要異動fragment就需要FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction異動的method
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        //commit切換，一定要做
        fragmentTransaction.commit();
    }
}

