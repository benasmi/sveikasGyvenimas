package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class StartingActivity extends AppCompatActivity {

    private ImageView heart_IV;
    private SharedPreferences loginPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        loginPrefs = getSharedPreferences("DataPrefs", MODE_PRIVATE);

        heart_IV = (ImageView) findViewById(R.id.heart);
        Animation pulse = AnimationUtils.loadAnimation(StartingActivity.this, R.anim.pulse);
        heart_IV.startAnimation(pulse);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.sveikata.productions.mabe.sveikasgyvenimas",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("TEST", "HASH: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(!loginPrefs.getString("username", "").isEmpty() && CheckingUtils.isNetworkConnected(this)) {
            new ServerManager(this, "LOGIN").startFetchingData(0, false);

        }else{
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    Intent intent = new Intent(StartingActivity.this, LoginActivity.class);
                    intent.putExtra("isAnimDisabled", true);
                    startActivity(intent);
                }
            }, 2100L);
        }
        }


}