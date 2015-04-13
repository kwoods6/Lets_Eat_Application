package com.example.matthew.letseatversion1;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;


public class EventScreen extends ActionBarActivity {

    Context context = this;
    String Username = "";
    String Friend1 = "";
    String Friend2 = "";
    String Friend3 = "";
    String Friend4 = "";
    String Friend5 = "";
    String DateAndTime = "";
    String Location = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_screen);

        Username = getIntent().getExtras().getString("passingUserName");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_screen, menu);
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
        //CreateEvent.php
    }

    public void  passingAccountInfo(String result){
        Bundle bundle = new Bundle();
        bundle.putString("serverResponse", result);
        Intent intent = new Intent(getBaseContext(), UserAccountScreen.class);
        intent.putExtra("serverResponse", result);
        startActivity(intent);


    }

    public void startCreatingAccountButtonClick(View view) {


        EditText friend1 = (EditText)findViewById(R.id.inviteUser1);
        EditText friend2 = (EditText)findViewById(R.id.inviteUser2);
        EditText friend3 = (EditText)findViewById(R.id.inviteUser3);
        EditText friend4 = (EditText)findViewById(R.id.inviteUser4);
        EditText friend5 = (EditText)findViewById(R.id.inviteUser5);
        EditText dateAndTime = (EditText)findViewById(R.id.dateAndTime);
        EditText location = (EditText)findViewById(R.id.location);

        Friend1 = friend1.getText().toString();
        Friend2 = friend2.getText().toString();
        Friend3 = friend3.getText().toString();
        Friend4 = friend4.getText().toString();
        Friend5 = friend5.getText().toString();
        DateAndTime = dateAndTime.getText().toString();
        Location = location.getText().toString();

        new HttpRequest().execute("http://www.csce.uark.edu/~mrs018/CreateEvent.php");

    }



    class HttpRequest extends AsyncTask<String,String,String>
    {
        //holder strings that are used to pass info from LoginScreen class to HttpRequest class
        String username;
        String friend1;
        String friend2;
        String friend3;
        String friend4;
        String friend5;
        String dateandtime;
        String location;


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

             username = Username;
             friend1 = Friend1;
             friend2 = Friend2;
             friend3 = Friend3;
             friend4 = Friend4;
             friend5 = Friend5;
             dateandtime = DateAndTime;
             location = Location;


            super.onPreExecute();
        }

        @Override
        //this passing info to and from the app and server
        //it is done on a separate thread so that it does not interfere with other task while the info is transferred
        protected String doInBackground(String... params) {
            try
            {
                //makes name value pairs to be passed to server
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("friend1", friend1));
                nameValuePairs.add(new BasicNameValuePair("friend2", friend2));
                nameValuePairs.add(new BasicNameValuePair("friend3", friend3));
                nameValuePairs.add(new BasicNameValuePair("friend4", friend4));
                nameValuePairs.add(new BasicNameValuePair("friend5", friend5));
                nameValuePairs.add(new BasicNameValuePair("dateandtime", dateandtime));
                nameValuePairs.add(new BasicNameValuePair("location", location));

                //makes a httpclient and sends the info to the server
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost method = new HttpPost(params[0]);
                method.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(method);
                HttpEntity entity = response.getEntity();

                //keep the stream open until all the data has been passed back
                if(entity != null){
                    return EntityUtils.toString(entity);
                }
                else{
                    return "No string.";
                }
            }
            catch(Exception e){
                return "Network problem";
            }

        }
    }

}
