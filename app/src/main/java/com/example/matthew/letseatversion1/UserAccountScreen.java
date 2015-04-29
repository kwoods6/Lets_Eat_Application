package com.example.matthew.letseatversion1;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

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
import java.util.concurrent.TimeUnit;


public class UserAccountScreen extends ActionBarActivity {

    String serverResponse;
    String checkInviteServerResponse = "[]";
    String newUserID;
    String newUserPassword;
    String newCity;
    String newLastname;
    String newFirstname;
    String whatWeAreDoing;
    boolean invitation = false;
    String localString;
    String termString;
    String Inviter;
    String dateandtime;
    String location;
    String infoToPass;
    TextView user;
    TextView firstName;
    TextView lastName;
    TextView city;
    String[] tokens;
    String[] eventTokens;
    int arrayLength;
    ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_screen);

        //pulls the response from the server and textfield ids
        if(getIntent().getExtras() != null) {
            serverResponse = getIntent().getExtras().getString("serverResponse");
            user = (TextView) findViewById(R.id.finalUser);
            firstName = (TextView) findViewById(R.id.finalFirstname);
            lastName = (TextView) findViewById(R.id.finalLastname);
            city = (TextView) findViewById(R.id.finalCity);


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
        Intent intent = new Intent(getBaseContext(),EventScreen.class);
        intent.putExtra("serverResponse", serverResponse);
        startActivity(intent);

    }

    public void checkEventButtonClick(View view){

        //new CheckEvents().execute("http://www.csce.uark.edu/~mrs018/CheckEventInvites.php");
        //new CheckEvents().execute("http://www.csce.uark.edu/~mrs018/CheckEventInvites.php");
        Intent eventInbox = new Intent(getApplicationContext(), eventinbox.class);
        eventInbox.putExtra("passingUsername", newUserID);
        eventInbox.putExtra("serverResponse", serverResponse);
        startActivity(eventInbox);
    }

    public void eventCheckFinished(){


        if(invitation == true){
            final Intent intent = new Intent(getBaseContext(), SelectPreferencesScreen.class);
            intent.putExtra("serverResponse", serverResponse);
            intent.putExtra("passingInviter", Inviter);
            invitation = false;

            arrayLength = eventTokens.length;
            new AlertDialog.Builder(context).setTitle("arrayLength var")
                    .setMessage(arrayLength)
                    .setIcon(android.R.drawable.ic_dialog_alert).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("you have an invite from " + Inviter + " \n at location " + location + "\n on " + dateandtime + "\n please accept or decline");

            // Set up the input

            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(intent);
                    //SendInPreferences.php username preferences inviter
                }
            });

            builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new declineInvite().execute("http://www.csce.uark.edu/~mrs018/SendInPreferences.php");
                }
            });
            builder.show();
        }
        else{
            new AlertDialog.Builder(context).setTitle("response from server")
                    .setMessage("no invites currently")
                    .show();
        }
    }

    public void quickSearch(View view){
        EditText term = (EditText)findViewById(R.id.quickType);
        EditText local = (EditText)findViewById(R.id.quickLocation);

        termString = term.getText().toString();
        localString = local.getText().toString();


        whatWeAreDoing = "quickSearch";
        new HttpRequest().execute("http://uaf59309.ddns.uark.edu/yelprequest.php");

    }

    //button to edit account
    public void editAccountButtonClick (View view) {

        Bundle bundle = new Bundle();
        bundle.putString("passingUserName", newUserID);
        Intent intent = new Intent(getBaseContext(), EditAccountScreen.class);
        intent.putExtra("passingUserName", newUserID);

        startActivity(intent);
    }

    //button to delete the account
    public void deleteAccountButtonClick(View view) {
        whatWeAreDoing = "deleteAccount";
        new HttpRequest().execute("http://www.csce.uark.edu/~mrs018/Delete.php");
    }
    public void logout(View view)
    {
        Intent intent = new Intent(UserAccountScreen.this, LoginScreen.class);
        startActivity(intent);
        finish();
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
                    new AlertDialog.Builder(context).setTitle("quick search results")
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
                if(WhatWeAreDoing.equalsIgnoreCase("quickSearch")) {
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

    class CheckEvents extends AsyncTask<String,String,String>
    {
        //holder strings that are used to pass info from LoginScreen class to HttpRequest class
        String username;


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            /*try
            {
                JSONObject OBJ = new JSONObject(result);
                JSONArray arr = OBJ.getJSONArray();
                String inviters = "";
                    for(int i = 0; i < arr.length(); i++)
                {
                    JSONObject invite = arr.getJSONObject(i);
                    String inviter = invite.getString("inviter");
                    inviters+= inviter + ";";
                }
                Toast toast = Toast.makeText(getApplicationContext(),inviters, Toast.LENGTH_LONG);
            }
            catch (Exception e)
            {

            }*/
            checkInviteServerResponse = result;
            /*new AlertDialog.Builder(context).setTitle("checkInviteServerResponse var")
                    .setMessage(checkInviteServerResponse)
                    .setIcon(android.R.drawable.ic_dialog_alert).show();*/
            if(!checkInviteServerResponse.equalsIgnoreCase("null")) {
                eventTokens = checkInviteServerResponse.split("\"");
                Inviter = eventTokens[3];
                dateandtime = eventTokens[7];
                location = eventTokens[11];
                invitation = true;

                new AlertDialog.Builder(context).setTitle("blahh")
                        .setMessage(eventTokens[3])
                        .setIcon(android.R.drawable.ic_dialog_alert).show();


                //list.add(checkInviteServerResponse);
            }
            checkInviteServerResponse = "null";
            eventCheckFinished();

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            username = newUserID;



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

    class declineInvite extends AsyncTask<String,String,String>
    {
        //holder strings that are used to pass info from LoginScreen class to HttpRequest class
        String username;
        String inviter;
        String preferences;


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);


        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            username = newUserID;
            inviter = Inviter;
            preferences = "000000000";


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
                nameValuePairs.add(new BasicNameValuePair("inviter", inviter));
                nameValuePairs.add(new BasicNameValuePair("preferences", username));
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




