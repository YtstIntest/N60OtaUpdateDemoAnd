package com.example.n60otaupdatedemoand;

import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.library.utils.CustomViewPager;
import com.example.n60otaupdatedemoand.Ui.Home.Fragment.HomeFragemnt;
import com.example.n60otaupdatedemoand.Ui.Mine.Fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.bottomNavigationBar)
    BottomNavigationBar bottomNavigationBar;

    private List<Fragment> fragments;
    private HomeFragemnt homeFragemnt;
    private SettingFragment settingFragment;
    private ViewPageAdapter viewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initBottomBar();
        initFragment();
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPageAdapter.setFragments(fragments);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(viewPageAdapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setScroll(false);
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        homeFragemnt = new HomeFragemnt();
        settingFragment = new SettingFragment();

        fragments.add(homeFragemnt);
        fragments.add(settingFragment);


    }

    private void initBottomBar() {
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.home, "主页").setActiveColorResource(R.color.main_blue));
        bottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.mine, "我的").setActiveColorResource(R.color.main_blue));
        bottomNavigationBar.initialise();
        bottomNavigationBar.setTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
