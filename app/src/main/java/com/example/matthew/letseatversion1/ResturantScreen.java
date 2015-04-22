package com.example.matthew.letseatversion1;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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


public class ResturantScreen extends ActionBarActivity {
    String  whatWeAreDoing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_screen);

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

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("term", "mexican"));
                nameValuePairs.add(new BasicNameValuePair("location", "springdale"));
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

