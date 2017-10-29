package com.test.culogicproductlisting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.culogicproductlisting.R;

public class SplashActivity extends AppCompatActivity {
    private TextView tvLogo;
    private ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvLogo = (TextView) findViewById(R.id.tvLogo);
        ivLogo = (ImageView) findViewById(R.id.imageView);
        runAnimationForText();
    }

    private void runAnimationForText() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.right_to_left);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);

                // close this activity
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ivLogo.startAnimation(animation);
    }


}
