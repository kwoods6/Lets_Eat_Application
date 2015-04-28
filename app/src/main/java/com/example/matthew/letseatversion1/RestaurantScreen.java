package com.example.matthew.letseatversion1;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;


public class RestaurantScreen extends ActionBarActivity {
    String  whatWeAreDoing;
    String preferences; //= "105230134";
    String restaurant;
    String username;
    String inviter;
    String serverResponse;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_screen);
        
        preferences = getIntent().getExtras().getString("preferences");
        username = getIntent().getExtras().getString("username");
        inviter = getIntent().getExtras().getString("inviter");
        serverResponse = getIntent().getExtras().getString("serverResponse");
        location = getIntent().getExtras().getString("location");
        for(int i = 0; i<9-preferences.length();i++)
        {
            preferences = "0" + preferences;
        }
        char largest = preferences.charAt(0);
        int positionOfLargest = 0;

        for(int i = 0; i < 8; i++){
            if(largest < preferences.charAt(i)){
                largest = preferences.charAt(i);
                positionOfLargest++;
            }
            else if(largest == preferences.charAt(i)){
                int random = 1;
                Random dice = new Random();
                random = dice.nextInt(10);
                if(random > 5){
                    largest = preferences.charAt(i);
                    positionOfLargest++;
                }
            }
        }

        //if position is equal to somethign set string equal to the resturaunt
        if(positionOfLargest == 0){
            restaurant = "vegetarian";
        }
        else if(positionOfLargest == 1){
            restaurant = "gluten free";
        }
        else if(positionOfLargest == 2){
            restaurant = "vegan";
        }
        else if(positionOfLargest == 3){
            restaurant = "korean";
        }
        else if(positionOfLargest == 4){
            restaurant = "chinese";
        }
        else if(positionOfLargest == 5){
            restaurant = "mexican";
        }
        else if(positionOfLargest == 6){
            restaurant = "italian";
        }
        else if(positionOfLargest == 7){
            restaurant = "french";
        }
        else{
            restaurant = "american";
        }

        new HttpRequestRest().execute("http://uaf59309.ddns.uark.edu/yelprequest.php");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resturant_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    class HttpRequestRest extends AsyncTask<String,String,String>
    {

        String username;
        String password;
        String city;
        String firstname;
        String lastname;
        String WhatWeAreDoing;
        String Local;
        String Term;

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

                try {

                    JSONObject jobject = new JSONObject(result);
                    String list = "";
                    JSONArray jarray = jobject.getJSONArray("businesses");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject json_data = jarray.getJSONObject(i);
                        String restaurantName = json_data.getString("name");
                        String rating = json_data.getString("rating");
                        String phone = json_data.getString("display_phone");
                        JSONObject address = (JSONObject) json_data.getJSONObject("location");
                        JSONArray display_address = address.getJSONArray("display_address");
                        String finaladdress = display_address.getString(0) + " " + display_address.getString(1);
                        String realAddress = address.getString("address");
                        String formattedString = restaurantName + "\n" + rating + "\n" + phone + "\n" + finaladdress + "\n\n";
                        list += formattedString;
                    }
                    TextView text = (TextView) findViewById(R.id.textView2);
                    text.setText(list);
                    //new AlertDialog.Builder(getBaseContext()).setTitle("test results")
                         //   .setMessage(list)
                         //   .setIcon(android.R.drawable.ic_dialog_alert).show();

                } catch (Exception e) {
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
            }



        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            Term = restaurant;
            Local = location;
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("term", Term));
                nameValuePairs.add(new BasicNameValuePair("location", Local));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost method = new HttpPost(params[0]);
                method.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(method);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                } else {
                    return "No string.";
                }
            } catch (Exception e) {
                return "Network problem";
            }
        }

        }
    }

