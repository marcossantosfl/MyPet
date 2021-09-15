package com.myapp.mypet.controller.sign;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.myapp.mypet.R;
import com.myapp.mypet.controller.apresentation.Fragment;
import com.myapp.mypet.controller.apresentation.FragmentA;
import com.myapp.mypet.controller.apresentation.FragmentB;
import com.myapp.mypet.controller.apresentation.FragmentC;
import com.myapp.mypet.model.Account;
import com.myapp.mypet.model.Pet;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FragmentSignIn extends AppCompatActivity {

    private static final int NUM_PAGES = 5;

    private int percent = 10;

    private ViewPager2 viewPager;

    private FragmentStateAdapter pagerAdapter;

    private TextView progressText = null;

    private ProgressBar progressBar = null;

    private String code = null;

    private Account account;
    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_in);

        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new MyPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false);

        progressText = findViewById(R.id.progressText);
        progressBar = findViewById(R.id.progressBar);

        progressText.setText(percent + "%");
        progressBar.setProgress(percent);

        account = new Account();
        pet = new Pet(account);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public String getCode()
    {
        return code;
    }

    public void load(View view) {
        Intent intent = new Intent(this, Sign.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void nextFragment() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
    }

    public void previousFragment() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
    }

    public void updateProgress(int value) {

        percent = value;

        ObjectAnimator object = ObjectAnimator.ofInt(progressBar, "progress", percent);

        object.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) object.getAnimatedValue();
                progressText.setText(progress + "%");
            }


        });
        object.setDuration(1000);
        object.start();
    }

    public Account getAccount() {
        return account;
    }

    public Pet getPet() {
        return pet;
    }

    private class MyPagerAdapter extends FragmentStateAdapter {

        public MyPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public androidx.fragment.app.Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
                    return new FragmentSignInName();
                }
                case 1: {
                    return new FragmentSignInPhoto();
                }
                case 2: {
                    return new FragmentSignInAge();
                }
                case 3: {
                    return new FragmentSignInPhone();
                }
                case 4: {
                    return new FragmentSignInValidate();
                }
                default:
                    return new FragmentSignInName();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }

    }

}
