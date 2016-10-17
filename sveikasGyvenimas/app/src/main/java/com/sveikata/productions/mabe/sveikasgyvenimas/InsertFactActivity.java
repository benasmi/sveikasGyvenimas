package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by Benas on 9/19/2016.
 */
public class InsertFactActivity extends Activity {


    private EditText title_fact_admin;
    private EditText body_fact_admin;
    private EditText source_fact_admin;
    private EditText url_fact_admin;
    private ImageView image_fact_admin;
    private String filePath;
    private String img_height;
    private boolean failedToLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CheckingUtils.changeNotifBarColor("#2980b9", getWindow());

        filePath = "";
        setContentView(R.layout.fact_admin);
        super.onCreate(savedInstanceState);
        title_fact_admin = (EditText) findViewById(R.id.title_fact_admin);
        body_fact_admin = (EditText) findViewById(R.id.body_fact_admin);
        source_fact_admin = (EditText) findViewById(R.id.source_fact_admin);
        url_fact_admin = (EditText) findViewById(R.id.url_fact_admin);
        image_fact_admin = (ImageView) findViewById(R.id.image_fact_admin);

        image_fact_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });



        url_fact_admin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    url_fact_admin.setTextColor(Color.BLACK);
                    image_fact_admin.setImageResource(R.drawable.add_file);
                    filePath=null;
            }

            @Override
            public void afterTextChanged(Editable s) {
                Glide.with(InsertFactActivity.this)
                        .load(s.toString())
                        .asBitmap()
                        .animate(R.anim.slide_in_left)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                failedToLoad = false;
                                image_fact_admin.setImageBitmap(resource);
                                filePath = null;
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                failedToLoad = true;
                            }
                        });
            }
        });

    }

    public void add_fact(View view) {

        Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);

        String title = title_fact_admin.getText().toString();
        String body = body_fact_admin.getText().toString();

        String url = "";
        url = url_fact_admin.getText().toString();
        String source = source_fact_admin.getText().toString();

        if(title.equals("")){
            title_fact_admin.setError("Sukurkite antraštę");
            title_fact_admin.startAnimation(shake);
            CheckingUtils.vibrate(this, 100);
            return;
        }
        if(body.equals("")){
            body_fact_admin.setError("Negalite įdėti fakto be jokios informacijos");
            body_fact_admin.startAnimation(shake);
            CheckingUtils.vibrate(this, 100);
            return;

        }

        if(!CheckingUtils.isNetworkConnected(this)){
            CheckingUtils.createErrorBox("Įjunkite internetą", this, R.style.FactsDialogStyle);
            CheckingUtils.vibrate(this, 100);
            return;
        }

        if(failedToLoad){
            url_fact_admin.setError("Netinkamas paveiksliuko URL!");
            image_fact_admin.setImageResource(R.drawable.add_file);
            url_fact_admin.startAnimation(shake);
            return;
        }

        AskQuestionsActivity.addFAQData = true;
        InterestingFactsActivity.addFactsFirstTime = true;
        HealthyLifeActivity.addData = true;
        PlayActivity.shouldAddInfo = true;
        new ServerManager(this, "ADD_FACT").execute("ADD_FACT", title, body, url, source, filePath, img_height);



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            switch (resultCode){

                case RESULT_OK:

                    url_fact_admin.setTextColor(Color.parseColor("#bdc3c7"));
                    url_fact_admin.setText("");
                    Uri selectedImage = data.getData();
                    filePath = getPath(selectedImage);

                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    image_fact_admin.setImageBitmap(CheckingUtils.scaleBitmap(bitmap));
                    break;


                case RESULT_CANCELED:
                    image_fact_admin.setImageResource(R.drawable.add_file);
                    return;
            }

        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }
}
