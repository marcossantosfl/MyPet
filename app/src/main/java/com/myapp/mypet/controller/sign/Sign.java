package com.myapp.mypet.controller.sign;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.mypet.R;

public class Sign extends AppCompatActivity {

    LinearLayout signIn = null;
    LinearLayout signUp = null;
    ProgressBar progressBar = null;
    TextView signInText = null;
    TextView signUpText = null;

    private final static int width = 50;
    private final static int height = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        progressBar = findViewById(R.id.progressBar);
        signInText = findViewById(R.id.signInText);
        signUpText = findViewById(R.id.signUpText);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        signIn.setVisibility(View.VISIBLE);
        signUp.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }


    public void loadSignIn(View view)
    {
        animateButton(signIn, signUp, signInText, R.color.yellow);
    }

    public void loadSignUp(View view)
    {
        animateButton(signUp, signIn, signUpText, R.color.blue);
    }

    private void animateButton(LinearLayout sign, LinearLayout signGone, TextView signInTextGone, int color)
    {
        ValueAnimator animator = ValueAnimator.ofInt(sign.getMeasuredWidth(), sign.getMeasuredHeight(),width,height);

        animator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationStart(Animator animation) {
                signGone.setVisibility(View.GONE);
                sign.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminateTintList(ColorStateList.valueOf(getColor(color)));
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                load();
            }
        });

        animator.setDuration(500);
        animator.start();
    }

    public void load()
    {
        Intent intent = new Intent(this, FragmentSignIn.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}