package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Benas on 9/19/2016.
 */
public class InsertFactActiivty extends Activity {


    private EditText title_fact_admin;
    private EditText body_fact_admin;
    private EditText source_fact_admin;
    private EditText url_fact_admin;
    private ImageView image_fact_admin;
    private String filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fact_admin);
        super.onCreate(savedInstanceState);
        title_fact_admin = (EditText) findViewById(R.id.title_fact_admin);
        body_fact_admin = (EditText) findViewById(R.id.body_fact_admin);
        source_fact_admin = (EditText) findViewById(R.id.source_fact_admin);
        url_fact_admin = (EditText) findViewById(R.id.url_fact_admin);
        image_fact_admin = (ImageView) findViewById(R.id.image_fact_admin);

    }


    public void add_fact(View view) {
        String title = title_fact_admin.getText().toString();
        String body = body_fact_admin.getText().toString();
        String url = url_fact_admin.getText().toString();
        String source = source_fact_admin.getText().toString();

        if(!CheckingUtils.isNetworkConnected(this)){
            CheckingUtils.createErrorBox("Įjunkite internetą", this);
            return;
        }else{

            new ServerManager(this, "ADD_FACT").execute("ADD_FACT", title, body, url, source, filePath);

        }

    }

    public void add_photo(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            switch (resultCode){
                case Activity.RESULT_OK:

                    Uri selectedImage = data.getData();
                    filePath = getPath(selectedImage);

                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    image_fact_admin.setImageBitmap(bitmap);

                case Activity.RESULT_CANCELED:

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
