package com.echo.fans.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.echo.fans.data.model.Token;
import com.echo.fans.data.DataManager;

import org.zeroturnaround.zip.ZipUtil;

import java.io.File;

import com.echo.fans.App;
import echo.com.fans.R;
import com.echo.fans.fragment.ActivateFragment;
import com.echo.fans.fragment.GenerateFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_CITY = 100;
    private String currentCity = "北京";
    private TextView currentCityTextView;

    private SharedPreferences sharedPreferences;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    private boolean unpacked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        currentCityTextView = (TextView) findViewById(R.id.currentCityTextView);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, positionOffset, true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String deviceId = sharedPreferences.getString("deviceId", "");
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Secure.getString(getContentResolver(),
                    Secure.ANDROID_ID);
            sharedPreferences.edit().putString("deviceId", deviceId).commit();
        }

        unpacked = sharedPreferences.getBoolean("unpacked", false);
        if (!unpacked) {
            unpackCityList();
        }
        checkActivateStatus();
    }

    private void checkActivateStatus() {
        String key = sharedPreferences.getString("key", "");
        if (TextUtils.isEmpty(key)) {
            // do nothing
        } else {
            DataManager.getInstance().checkKey(key, new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if (response.body().getCode() == 0) {
                        App.getInstance().isActivated = true;
                    }

                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                }
            });
        }
    }

    private void unpackCityList() {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                boolean result;
                try {
                    File dir = new File(getFilesDir() + File.separator + "city");
                    if (dir.exists()) {
                        deleteDir(dir);
                    }
                    ZipUtil.unpack(getResources().openRawResource(R.raw.city), getFilesDir());
                    result = true;
                } catch (Throwable e) {
                    result = false;
                }
                return result;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    sharedPreferences.edit().putBoolean("unpacked", true).commit();
                }
            }
        }.execute();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = GenerateFragment.newInstance();
                    break;
                case 1:
                    fragment = ActivateFragment.newInstance();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
}