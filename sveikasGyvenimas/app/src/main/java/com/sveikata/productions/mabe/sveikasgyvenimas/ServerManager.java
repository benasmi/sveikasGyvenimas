package com.sveikata.productions.mabe.sveikasgyvenimas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.android.internal.http.multipart.MultipartEntity;
import com.facebook.TestUserManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by Benas on 9/7/2016.+-
 */
public class ServerManager extends AsyncTask<String, Void, Void> {

    private String method_type;
    private String dialogType;
    private int response;
    private SharedPreferences sharedPreferences;
    private Context context;

    private String username_login;
    private String password_login;

    private ProgressDialog progressDialog = null;

    public static String SERVER_ADRESS_REGISTER = "http://dvp.lt/android/register.php";
    public static String SERVER_ADRESS_LOGIN = "http://dvp.lt/android/login.php";
    public static String SERVER_ADRESS_FETCH_SCHEDULE = "http://dvp.lt/android/fetch_schedule.php";
    public static String SERVER_ADRESS_FETCH_USER_DATA = "http://dvp.lt/android/fetch_user_data.php";
    public static String SERVER_ADRESS_INSERT_EVENT_DATA = "http://dvp.lt/android/insert_event.php";
    public static String SERVER_ADRESS_NOTIFICATION = "http://dvp.lt/android/notification.php";
    public static String SERVER_ADRESS_UPDATE_TOKEN = "http://dvp.lt/android/update_token.php";
    public static String SERVER_ADRESS_DELETE_EVENT ="http://dvp.lt/android/delete_event.php";
    public static String SERVER_ADRESS_LOGOUT ="http://dvp.lt/android/delete_token.php";
    public static String SERVER_ADDRESS_ADD_FACT ="http://dvp.lt/android/add_fact.php";



    public ServerManager(Context context, String dialogType){
        this.context=context;
        this.dialogType = dialogType;



    }
    @Override
    protected void onPreExecute() {
        if(dialogType.equals("LOGIN")){
            progressDialog = CheckingUtils.progressDialog(context,"Jungiamasi...Palaukite kelias sekundes");
        }
        if(dialogType.equals("REGISTRATION")){
            progressDialog = CheckingUtils.progressDialog(context, "Duomenys perkeliami į serverį");
        }
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String...params) {
        method_type = params[0];

        if(method_type.equals("REGISTRATION")){

            String name = params[1];
            String last_name = params[2];
            String username = params[3];
            String password = params[4];
            String mail = params[5];
            String gender = params[6];
            String years = params[7];
            String type = params[8];
            String token = params[9];


            response = register(name,last_name,username,password,mail,gender,years,type, token);

            if(!type.equals("regular")) {
                context.startActivity(new Intent(context, TabActivityLoader.class));
                ((Activity) context).finish();
            }

        }
        if(method_type.equals("LOGIN")) {

            username_login = params[1];
            password_login = params[2];
            String device_id = params[3];

            response = login(username_login,password_login, device_id);
        }
        if(method_type.equals("INSERT_EVENT")){

            String username = params[1];
            String password = params[2];
            String event_location = params[3];
            String date = params[4];
            double latitude = Double.parseDouble(params[5]);
            double longtitude = Double.parseDouble(params[6]);
            String name = params[7];
            String description = params[8];



            insert_event_data(username, password,event_location,date,latitude,longtitude, name, description);

        }
        if(method_type.equals("SEND_NOTIFICATION")){
            String message = params[1];
            String description = params[2];

            sendNotifcation(message,description);
        }
        if(method_type.equals("DELETE_EVENT")){
            String username = params[1];
            String password = params[2];
            String name = params[3];
            String description = params[4];

            delete_event(username,password,name,description);

        }
        if(method_type.equals("LOGOUT")){
            String username = params[1];
            String password = params[2];

            logout(username, password);

        }
        if(method_type.equals("ADD_FACT")){
            String fact_title = params[1];
            String fact_body = params[2];
            String fact_source = params[3];
            String fact_image_path = params[4];
            String fact_image_url = params[5];


            insertFact(fact_title, fact_body, fact_source, fact_image_url, fact_image_path);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {


        if (method_type.equals("REGISTRATION")) {
            progressDialog.cancel();
            switch (response) {
                case 0:
                    new AlertDialog.Builder(context)
                            .setMessage("Toks vartotojo vardas arba paštas jau egzistuoja")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    break;
                case 1:
                    context.startActivity(new Intent(context, ActivateAccountActivity.class));
                    break;

                case 3:
                    CheckingUtils.createErrorBox("Norint prisijungti, reikia interneto", context);
                    break;
            }
        }

        if(method_type.equals("LOGIN")){
            progressDialog.cancel();
            switch (response) {
                case 0:
                    sharedPreferences = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username_login);
                    editor.putString("password", password_login);
                    editor.commit();
                    new fetchData().execute();
                    break;
                case 1:
                    CheckingUtils.createErrorBox("Wrong username or password", context);
                    break;

                case 2:
                    context.startActivity(new Intent(context, ActivateAccountActivity.class));
                    break;

                case 3:
                    CheckingUtils.createErrorBox("You need internet connection to do that", context);
                    break;
            }

         }
        if(method_type.equals("LOGOUT")){
            sharedPreferences = context.getSharedPreferences("DataPrefs", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", "");
            editor.putString("password", "");
            editor.commit();
            HealthyLifeActivity.addData=true;
            AskQuestionsActivity.addFAQData = true;

            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("isAnimDisabled", true);
            context.startActivity(intent);
        }
        if(method_type.equals("SEND_NOTIFICATION")){
            CheckingUtils.createErrorBox("Sėkmingai išsiųsta",context);
        }
        super.onPostExecute(aVoid);
        }




    public int register(String name, String last_name, String username, String password, String mail, String gender, String age, String type, String token){

        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADRESS_REGISTER);

        //JSON object.
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("name", name);
            jsonObject.putOpt("last_name", last_name);
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);
            jsonObject.putOpt("mail", mail);
            jsonObject.putOpt("gender", gender);
            jsonObject.putOpt("age", age);
            jsonObject.putOpt("type", type);
            jsonObject.putOpt("device_id", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EntityBuilder entity = EntityBuilder.create();
        entity.setText(jsonObject.toString());
        httpPost.setEntity(entity.build());

        JSONObject responseObject = null;


        try {
            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            System.err.println(responseBody);
            responseObject = new JSONObject(responseBody);

        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            return responseObject.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }



    }

    public void startFetchingData(){
        new fetchData().execute();
    }

    private int insertFact(String title, String body, String source, String url, String path){
        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost( ServerManager.SERVER_ADDRESS_ADD_FACT);

            String username = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("username", "");

            //JSON object.
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);
            jsonObject.putOpt("title", title);
            jsonObject.putOpt("body", body);
            jsonObject.putOpt("source", source);

            MultipartEntityBuilder entity = MultipartEntityBuilder.create();

            if(url.isEmpty()) {
                entity.addPart("picture", new FileBody(new File(path)));
            }else{
                jsonObject.putOpt("url", url);
            }

            entity.addPart("json", new StringBody(jsonObject.toString()));

            httpPost.setEntity(entity.build());

            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONObject responseObject = new JSONObject(responseBody);

            return responseObject.getInt("code");
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    private int login(String username, String password, String device_id) {

        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADRESS_LOGIN);


        //JSON object.
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);
            jsonObject.putOpt("device_id", device_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        EntityBuilder entity = EntityBuilder.create();
        entity.setText(jsonObject.toString());
        httpPost.setEntity(entity.build());

        JSONObject responseObject = null;

        try {
            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            System.err.println(responseBody);
            responseObject = new JSONObject(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return responseObject.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }


    }

    public void sendNotifcation(String message, String description){
        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADRESS_NOTIFICATION);


        //JSON object.
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("message", message);
            jsonObject.putOpt("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        ContentType type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        entity.addTextBody("json", jsonObject.toString(), type);
        httpPost.setEntity(entity.build());

        JSONObject responseObject = null;

        try {
            //Getting response
            HttpResponse response = httpClient.execute(httpPost);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void delete_event(String username, String password, String name, String description){

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADRESS_DELETE_EVENT);

        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);
            jsonObject.putOpt("name", name);
            jsonObject.putOpt("description", description);

        }catch (Exception e){

        }

        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        ContentType type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        entity.addTextBody("json", jsonObject.toString(), type);
        httpPost.setEntity(entity.build());

        JSONObject responseObject = null;

        try {
            HttpResponse response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void logout (String username, String password){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADRESS_LOGOUT);

        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);


        }catch (Exception e){

        }

        EntityBuilder entity = EntityBuilder.create();
        entity.setText(jsonObject.toString());
        httpPost.setEntity(entity.build());

        JSONObject responseObject = null;

        try {
            HttpResponse response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public void fetchScheduleData(){




            SharedPreferences loginPrefs = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
            String username =loginPrefs.getString("username","");
            String password = loginPrefs.getString("password","");


        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADRESS_FETCH_SCHEDULE);



        //Getting response
        HttpResponse response = null;
        try {

            //JSON object.
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);

            EntityBuilder entity = EntityBuilder.create();
            entity.setText(jsonObject.toString());
            httpPost.setEntity(entity.build());

            response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(responseBody);


            Log.i("TEST", responseBody);
            SharedPreferences sharedPreferences = context.getSharedPreferences("ScheduleData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("schedule_data", jsonArray.toString()).commit();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void insert_event_data(String username, String password, String location, String date, double latitude, double longtitude, String name, String description){

        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADRESS_INSERT_EVENT_DATA);


        //Getting response
        HttpResponse response = null;
        try {

            //JSON object.
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);
            jsonObject.putOpt("description", description);
            jsonObject.putOpt("name", name);
            jsonObject.putOpt("location", location);
            jsonObject.putOpt("date", date);
            jsonObject.putOpt("latitude", latitude);
            jsonObject.putOpt("longtitude", longtitude);

            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            ContentType type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            entity.addTextBody("json", jsonObject.toString(), type);
            httpPost.setEntity(entity.build());

            response = httpClient.execute(httpPost);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void fetchUserData(){




            SharedPreferences loginPrefs = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
            String username =loginPrefs.getString("username","");
            String password = loginPrefs.getString("password","");



        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADRESS_FETCH_USER_DATA);

        //Getting response
        HttpResponse response = null;
        try {

            //JSON object.
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);

            EntityBuilder entity = EntityBuilder.create();
            entity.setText(jsonObject.toString());
            httpPost.setEntity(entity.build());

            response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(responseBody);

            System.out.println("async" + jsonArray.toString());

            SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_data", jsonArray.toString()).commit();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    class fetchData extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            fetchScheduleData();
            fetchUserData();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            context.startActivity(new Intent(context, TabActivityLoader.class));
            ((Activity) context).finish();

            super.onPostExecute(aVoid);
        }
    }

}

