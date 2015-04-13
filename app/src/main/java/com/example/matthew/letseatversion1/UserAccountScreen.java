package com.example.matthew.letseatversion1;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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


public class UserAccountScreen extends ActionBarActivity {

    String serverResponse;
    String newUserID;
    String newUserPassword;
    String newCity;
    String newLastname;
    String newFirstname;
    String whatWeAreDoing;
    String localString;
    String termString;
    TextView user;
    TextView firstName;
    TextView lastName;
    TextView city;
    String[] tokens;
    String[] eventTokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_screen);

        //pulls the response from the server and textfield ids
        serverResponse = getIntent().getExtras().getString("serverResponse");
        user=(TextView) findViewById(R.id.finalUser);
        firstName=(TextView) findViewById(R.id.finalFirstname);
        lastName=(TextView) findViewById(R.id.finalLastname);
        city=(TextView) findViewById(R.id.finalCity);


        //tokens the string into usefull info
        tokens = serverResponse.split("\"");

        newUserID = tokens[3];
        newUserPassword = tokens[7];

        //sets the textboxs equal to the appropriate strings
        user.setText(tokens[3]);
        firstName.setText(tokens[11]);
        lastName.setText(tokens[15]);
        city.setText(tokens[19]);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_account_screen, menu);
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


    //when the search button is clicked it bundles the username and sends that to the next activity
    public void createEventButtonClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("passingUserName", tokens[3]);
        Intent intent = new Intent(getBaseContext(), EventScreen.class);
        intent.putExtra("passingUserName", tokens[3]);
        startActivity(intent);

    }

    public void checkEventButtonClick(View view){
        whatWeAreDoing = "checkEvent";
        new HttpRequest().execute("http://www.csce.uark.edu/~mrs018/CheckEventInvites.php");

    }

    //button to edit account
    public void editAccountButtonClick (View view) {

        EditText term = (EditText)findViewById(R.id.quickType);
        EditText local = (EditText)findViewById(R.id.quickLocation);

        termString = term.getText().toString();
        localString = local.getText().toString();


        whatWeAreDoing = "editAccount";
        new HttpRequest().execute("http://uaf59309.ddns.uark.edu/yelprequest.php");
    }

    //button to delete the account
    public void deleteAccountButtonClick(View view) {
        whatWeAreDoing = "deleteAccount";
        new HttpRequest().execute("http://www.csce.uark.edu/~mrs018/Delete.php");
    }

    //this variable is used to make new intents when in another class
    Context context = this;


    //same http request class as the login screen
    class HttpRequest extends AsyncTask<String,String,String>
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

            if( WhatWeAreDoing.equalsIgnoreCase("deleteAccount")) {
                Intent intent = new Intent(context, LoginScreen.class);
                startActivity(intent);
            }
            else if(WhatWeAreDoing.equalsIgnoreCase("checkEvent")) {
                new AlertDialog.Builder(context).setTitle("response from server")
                        .setMessage(result)
                        .setIcon(android.R.drawable.ic_dialog_alert).show();;
            }
            else {
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
                    new AlertDialog.Builder(context).setTitle("response from server")
                            .setMessage(list)
                            .setIcon(android.R.drawable.ic_dialog_alert).show();

                } catch (Exception e) {
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            username = newUserID;
            password = newUserPassword;
            city = newCity;
            firstname = newFirstname;
            lastname = newLastname;
            WhatWeAreDoing = whatWeAreDoing;
            Local = localString;
            Term = termString;
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                if( WhatWeAreDoing.equalsIgnoreCase("deleteAccount")) {
                    nameValuePairs.add(new BasicNameValuePair("username", username));
                    nameValuePairs.add(new BasicNameValuePair("password", password));
                    nameValuePairs.add(new BasicNameValuePair("hometown", city));
                    nameValuePairs.add(new BasicNameValuePair("firstname", firstname));
                    nameValuePairs.add(new BasicNameValuePair("lastname", lastname));
                }
                if(WhatWeAreDoing.equalsIgnoreCase("checkEvent") || WhatWeAreDoing.equalsIgnoreCase("editAccount")) {
                    nameValuePairs.add(new BasicNameValuePair("term", Term));
                    nameValuePairs.add(new BasicNameValuePair("location", Local));
                }
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost method = new HttpPost(params[0]);
                method.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpclient.execute(method);
                HttpEntity entity = response.getEntity();
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



