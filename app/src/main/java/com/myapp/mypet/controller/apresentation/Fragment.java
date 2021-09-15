package com.myapp.mypet.controller.apresentation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.myapp.mypet.R;
import com.myapp.mypet.controller.sign.Sign;

public class Fragment extends AppCompatActivity {

    private static final int NUM_PAGES = 3;

    private ViewPager2 viewPager;

    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_apresentation);

        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new MyPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        getSupportFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                String result = bundle.getString("bundleKey");

                if (result.equalsIgnoreCase("result")) {

                }
            }
        });

        if (ContextCompat.checkSelfPermission(Fragment.this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Fragment.this,
                    Manifest.permission.READ_SMS)) {
            } else {

                ActivityCompat.requestPermissions(Fragment.this,
                        new String[]{Manifest.permission.READ_SMS}, 1);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            //does not return to splash
            //super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }

    }

    public void load(View view) {
        startActivity();
    }

    private void startActivity()
    {
        Intent intent = new Intent(this, Sign.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private class MyPagerAdapter extends FragmentStateAdapter {

        public MyPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public androidx.fragment.app.Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
                    return new FragmentA();
                }
                case 1: {
                    return new FragmentB();
                }
                case 2: {
                    return new FragmentC();
                }
                default:
                    return new FragmentA();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

    }

}
