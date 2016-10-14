
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
import android.widget.Toast;

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
    private SharedPreferences userData;
    private Context context;

    private String username_login;
    private String password_login;

    private String username_register;
    private String password_register;

    private OnFinishListener onfinishlistener;

    private ProgressDialog progressDialog = null;

    public static final String SERVER_ADRESS_REGISTER = "http://dvp.lt/android/register.php";
    public static final String SERVER_ADRESS_LOGIN = "http://dvp.lt/android/login.php";
    public static final String SERVER_ADRESS_FETCH_SCHEDULE = "http://dvp.lt/android/fetch_schedule.php";
    public static final String SERVER_ADRESS_FETCH_USER_DATA = "http://dvp.lt/android/fetch_user_data.php";
    public static final String SERVER_ADRESS_INSERT_EVENT_DATA = "http://dvp.lt/android/insert_event.php";
    public static final String SERVER_ADRESS_NOTIFICATION = "http://dvp.lt/android/notification.php";
    public static final String SERVER_ADRESS_UPDATE_TOKEN = "http://dvp.lt/android/update_token.php";
    public static final String SERVER_ADRESS_DELETE_EVENT ="http://dvp.lt/android/delete_event.php";
    public static final String SERVER_ADRESS_DELETE_FACT ="http://dvp.lt/android/delete_fact.php";
    public static final String SERVER_ADRESS_LOGOUT ="http://dvp.lt/android/delete_token.php";
    public static final String SERVER_ADDRESS_ADD_FACT ="http://dvp.lt/android/add_fact.php";
    public static final String SERVER_ADDRESS_FETCH_FACTS= "http://dvp.lt/android/fetch_facts.php";
    public static final String SERVER_ADDRESS_SEND_CHALLENGE= "http://dvp.lt/android/insert_challenge.php";
    public static final String SERVER_ADDRESS_ACCEPT_CHALLENGE= "http://dvp.lt/android/accept_challenge.php";
    public static final String SERVER_ADDRESS_DECLINE_CHALLENGE= "http://dvp.lt/android/decline_challenge.php";
    public static final String SERVER_ADRESS_FETCH_CHALLENGES = "http://dvp.lt/android/fetch_challenges.php";
    public static final String SERVER_ADRESS_I_FAILED_CHALLENGE = "http://dvp.lt/android/failed_challenge.php";


    public ServerManager(Context context, String dialogType){
        this.context=context;
        this.dialogType = dialogType;
        this.userData = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);

    }
    @Override
    protected void onPreExecute() {
        if(dialogType.equals("LOGIN")){
            progressDialog = CheckingUtils.progressDialog(context,"1...2...3... Net nespėjai perskaityti, kad čia parašyta", R.style.ScheduleDialogStyle);
        }
        if(dialogType.equals("REGISTRATION")){
            progressDialog = CheckingUtils.progressDialog(context, "Tik išsaugom tavo duomenis", R.style.ScheduleDialogStyle);
        }
        if(dialogType.equals("ADD_FACT")){
            Toast.makeText(context, "Pridedame faktą..." , Toast.LENGTH_LONG).show();
        }
        super.onPreExecute();
    }

    public void setOnFinishListener(OnFinishListener listener){
        this.onfinishlistener = listener;
    }

    @Override
    protected Void doInBackground(String...params) {

        method_type = params[0];

        if(method_type.equals("REGISTRATION")){

            String name = params[1];
            String last_name = params[2];
            username_register = params[3];
            password_register = params[4];
            String mail = params[5];
            String gender = params[6];
            String years = params[7];
            String type = params[8];
            String token = params[9];


            response = register(name,last_name,username_register,password_register,mail,gender,years,type, token);



        }
        if(method_type.equals("LOGIN")) {

            username_login = params[1];
            password_login = params[2];
            String device_id = params[3];

            response = login(username_login,password_login, device_id);
        }

        if(method_type.equals("ACCEPT_CHALLENGE")) {

            username_login = params[1];
            password_login = params[2];

            response = accept_challenge(username_login, password_login);
        }

        if(method_type.equals("DECLINE_CHALLENGE")) {

            username_login = params[1];
            password_login = params[2];

            response = decline_challenge(username_login, password_login);
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
        if(method_type.equals("DELETE_FACT")){
            String username = params[1];
            String password = params[2];
            String title = params[3];
            String url = params[4];
            String source = params[5];
            String body = params[6];

            delete_fact(username, password, title,url, source, body);

        }
        if(method_type.equals("LOGOUT")){
            String username = params[1];
            String password = params[2];

            logout(username, password);

        }
        if(method_type.equals("ADD_FACT")){
            String fact_title = params[1];
            String fact_body = params[2];
            String fact_image_url = params[3];
            String fact_source = params[4];
            String fact_image_path = params[5];
            String username = userData.getString("username", "");
            String password = userData.getString("password", "");

            insertFact(fact_title, fact_body, fact_source, fact_image_url, fact_image_path, username, password);
        }
        if(method_type.equals("SEND_CHALLENGE")){
            String challenge = params[1];
            String mail = params[2];
            String time = params[3];
            String title = params[4];
            String username = params[5];
            String password = params[6];

            response = send_challenge(username, password,challenge, title, time, mail);
        }

        if(method_type.equals("I_FAILED_CHALLENGE")){
            String username = params[1];
            String password = params[2];

            failed_challenge(username, password);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {


        if (method_type.equals("REGISTRATION")) {
            progressDialog.cancel();
            switch (response) {
                case 0:
                    CheckingUtils.createErrorBox(" Su tokiu vartotojo vardu arba El.Paštu jau turi paskyra :?", context, R.style.ScheduleDialogStyle);
                    break;
                case 1:
                    context.startActivity(new Intent(context, LoginActivity.class));
                    break;

                case 3:
                    CheckingUtils.createErrorBox("Nejaugi gaila mobilių?", context, R.style.ScheduleDialogStyle);
                    break;
                case 4:

                    sharedPreferences = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username_register);
                    editor.putString("password", password_register);
                    editor.commit();
                    new fetchData(0, true).execute();
                    break;


            }
        }
        if(method_type.equals("SEND_CHALLENGE")){
            switch (response){

                case 0:
                    PlayActivity.shouldAddInfo=true;
                    AskQuestionsActivity.addFAQData=true;
                    HealthyLifeActivity.addData=true;
                    InterestingFactsActivity.addFactsFirstTime =true;
                    startFetchingData(1, false);
                    Toast.makeText(context, "Iššūkis išsiųstas sėkmingai.", Toast.LENGTH_LONG).show();
                    break;

                case 1:
                    CheckingUtils.createBoxSuggestToFriend("Toks vartotojas neegzistuoja, pasiūlyk draugui prisiregistruoti!", context, R.style.PlayDialogStyle);
                    break;

                case 2:
                    CheckingUtils.createErrorBox("Šis vartotojas jau vykdo iššūkį, bandyk kitą kartą!", context, R.style.PlayDialogStyle);
                    PlayActivity.shouldAddInfo=true;
                    AskQuestionsActivity.addFAQData=true;
                    HealthyLifeActivity.addData=true;
                    InterestingFactsActivity.addFactsFirstTime =true;
                    startFetchingData(1, true);
                    break;

            }
        }


        if(method_type.equals("ACCEPT_CHALLENGE")){
            CheckingUtils.createErrorBox("Iššūkis priimtas!", context, R.style.PlayDialogStyle);
            startFetchingData(1, true);
        }
        if(method_type.equals("DECLINE_CHALLENGE")){
            CheckingUtils.createErrorBox("Iššūkis atmestas :(", context, R.style.PlayDialogStyle);
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
                    new fetchData(0, true).execute();

                    break;
                case 1:
                    CheckingUtils.createErrorBox("Uhhh...Leisiu pabandyti dar kartą", context,  R.style.ScheduleDialogStyle);
                    break;

                case 2:
                    CheckingUtils.createErrorBox("Sveikinu prisiregistravus, tačiau tau reikia nueiti į paštą ir aktyvuoti savo paskyrą", context, R.style.ScheduleDialogStyle);
                    break;

                case 3:
                    CheckingUtils.createErrorBox("Pamaitink mūsų serverį WIFI ryšiu arba mobiliais", context, R.style.ScheduleDialogStyle);
                    break;
            }

        }

        if(method_type.equals("LOGOUT")){
            sharedPreferences = context.getSharedPreferences("DataPrefs", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", "");
            editor.putString("password", "");
            editor.putBoolean("revoke", true);
            editor.commit();


            HealthyLifeActivity.addData=true;
            AskQuestionsActivity.addFAQData = true;
            InterestingFactsActivity.addFactsFirstTime = true;
            PlayActivity.shouldAddInfo = true;


            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("isAnimDisabled", true);
            context.startActivity(intent);

        }
        if(method_type.equals("SEND_NOTIFICATION")){
            CheckingUtils.createErrorBox("Sėkmingai išsiųsta",context, R.style.ScheduleDialogStyle);
        }
        if(method_type.equals("ADD_FACT")){
            startFetchingData(2, true);
//            context.startActivity(new Intent(context, TabActivityLoader.class).putExtra("Tab", 2));
        }


        if(onfinishlistener!=null){
            onfinishlistener.onFinish();

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

        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        ContentType type_content = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        entity.addTextBody("json", jsonObject.toString(), type_content);
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

    public int accept_challenge(String username, String password){

        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_ACCEPT_CHALLENGE);


        //JSON object.
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);
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

        return 0;



    }

    public int decline_challenge(String username, String password){

        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_DECLINE_CHALLENGE);

        //JSON object.
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);
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

        return 0;



    }

    public void startFetchingData(int tabAfterwards, boolean shouldCreateProgressDialog){
        new fetchData(tabAfterwards, shouldCreateProgressDialog).execute();
    }

    private int insertFact(String title, String body, String source, String url, String path, String username, String password){
        try {
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost( ServerManager.SERVER_ADDRESS_ADD_FACT);

            //JSON object.
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);
            jsonObject.putOpt("title", title);
            jsonObject.putOpt("body", body);
            jsonObject.putOpt("source", source);


            ContentType type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();

            if(url.isEmpty() && !path.isEmpty()) {
                entity.addPart("picture", new FileBody(new File(path)));
            }
            if(path.isEmpty() && !url.isEmpty()){
                jsonObject.putOpt("url", url);
            }

            entity.addPart("json", new StringBody(jsonObject.toString(), type));

            httpPost.setEntity(entity.build());

            //Getting response
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());


            return 0;
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


        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        ContentType type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        entity.addTextBody("json", jsonObject.toString(), type);
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
    private int send_challenge(String username, String password, String challenge,String title, String time, String mail) {

        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_SEND_CHALLENGE);


        //JSON object.
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);
            jsonObject.putOpt("challenge", challenge);
            jsonObject.putOpt("time", time);
            jsonObject.putOpt("title", title);
            jsonObject.putOpt("mail", mail);
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
    public void delete_fact(String username, String password, String title, String url, String source, String body){

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADRESS_DELETE_FACT);

        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);
            jsonObject.putOpt("title", title);
            jsonObject.putOpt("url", url);
            jsonObject.putOpt("source", source);
            jsonObject.putOpt("body", body);

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


    public void failed_challenge(String username, String password){

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADRESS_I_FAILED_CHALLENGE);

        JSONObject jsonObject = new JSONObject();

        try{

            jsonObject.putOpt("username", username);
            jsonObject.putOpt("password", password);

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

    public void fetchFactData(){

        SharedPreferences loginPrefs = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
        String username =loginPrefs.getString("username","");
        String password = loginPrefs.getString("password","");


        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS_FETCH_FACTS);



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
            SharedPreferences sharedPreferences = context.getSharedPreferences("FactData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("fact_data", jsonArray.toString()).commit();

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

            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            ContentType type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            entity.addTextBody("json", jsonObject.toString(), type);
            httpPost.setEntity(entity.build());

            response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(responseBody);


            Log.i("TEST", responseBody);
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_data", jsonArray.toString()).commit();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void fetchChallenges(){




        SharedPreferences loginPrefs = context.getSharedPreferences("DataPrefs", Context.MODE_PRIVATE);
        String username =loginPrefs.getString("username","");



        //Connect to mysql.
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVER_ADRESS_FETCH_CHALLENGES);

        //Getting response
        HttpResponse response = null;
        try {

            //JSON object.
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOpt("username", username);

            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            ContentType type = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            entity.addTextBody("json", jsonObject.toString(), type);
            httpPost.setEntity(entity.build());

            response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(responseBody);


            Log.i("TEST", responseBody);
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("challenge_data", jsonArray.toString()).commit();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    class fetchData extends AsyncTask<Void,Void,Void>{

        int tabAfterwards = 0;
        private ProgressDialog progressDialog;
        boolean shouldCreateDialog;

        public fetchData(int tabAfterwards, boolean shouldCreateDialog) {
            this.tabAfterwards = tabAfterwards;
            this.shouldCreateDialog = shouldCreateDialog;
        }

        @Override
        protected void onPreExecute() {
            if(shouldCreateDialog) {
                progressDialog = CheckingUtils.progressDialog(context, "Sshhh...Tuoj užkrausim", R.style.CasualStyle);
            }

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            fetchScheduleData();
            fetchUserData();
            fetchFactData();
            fetchChallenges();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(shouldCreateDialog){
                progressDialog.cancel();
            }

            if(onfinishlistener == null){
                context.startActivity(new Intent(context, TabActivityLoader.class).putExtra("Tab", tabAfterwards));
                ((Activity) context).finish();
            }else{
                onfinishlistener.onFinish();
            }


            super.onPostExecute(aVoid);
        }
    }

}



