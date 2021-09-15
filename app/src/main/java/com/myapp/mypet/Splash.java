package com.myapp.mypet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.mypet.controller.apresentation.Fragment;
import com.myapp.mypet.controller.sign.Sign;

public class Splash extends AppCompatActivity {

    private ImageView imageView = null;
    private LinearLayout imageView_Container = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView_Container = findViewById(R.id.imagePaw_Container);

        Animation animation = new TranslateAnimation(0, 0,0, 400);
        animation.setDuration(3000);
        animation.setRepeatCount(Animation.ABSOLUTE);
        imageView_Container.startAnimation(animation);

        String key = "already_open_first";

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView_Container.setVisibility(View.INVISIBLE);

                if (sharedPref.getBoolean(key, false)) {
                    sign();
                } else {
                    editor.putBoolean(key, true);
                    editor.apply();
                    apresentation();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void apresentation()
    {
        Intent intent = new Intent(this, Fragment.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void sign()
    {
        Intent intent = new Intent(this, Sign.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}