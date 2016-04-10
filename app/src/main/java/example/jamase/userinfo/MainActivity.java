package example.jamase.userinfo;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.support.v4.app.FragmentPagerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    TabLayout mtablayout;
    ViewPager mviewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorcoltitle));//设置还没收缩时状态下字体颜色


//        mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorAccent));//设置收缩后Toolbar上字体的颜色
        mCollapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorpurple));
        mCollapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));
//        mCollapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);
        mCollapsingToolbarLayout.setExpandedTitleGravity(Gravity.CENTER_HORIZONTAL);
        int px = DipToPixels(getApplicationContext(), 60);
        mCollapsingToolbarLayout.setExpandedTitleMargin(0, px, 0, 0);
//        mCollapsingToolbarLayout.setCollapsedTitleGravity(Gravity.BOTTOM);

        BlurredLayout layout = (BlurredLayout) findViewById(R.id.blured);
        layout.setBlurRadius(0, 50);
        mtablayout = (TabLayout) findViewById(R.id.tabs);
        mviewpager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mviewpager);
        mtablayout.setupWithViewPager(mviewpager);


    }

    public int DipToPixels(Context context, int dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);
        return valuePixels;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new UserInfoFragement(), "最近动态");
        adapter.addFragment(new UserInfoFragement2(), "个人资料");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
