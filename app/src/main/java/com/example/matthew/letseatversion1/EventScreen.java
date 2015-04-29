package com.example.matthew.letseatversion1;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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

import java.util.ArrayList;


public class EventScreen extends ActionBarActivity {

    Context context = this;
    String Username;
    String Friendnumber;
    String Friends;
    String DateAndTime;
    String Location;
    String serverResponse;
    String[] tokens;
    String newUserPassword;
    ArrayList<String> buddies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_screen);

        buddies = new ArrayList<String>();

        if(getIntent().getExtras() != null) {

            serverResponse = getIntent().getExtras().getString("serverResponse");

            //tokens the string into usefull info
            tokens = serverResponse.split("\"");
            Username = tokens[3];
            newUserPassword = tokens[7];
        }

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

    public void activate(){
        String message = "Enter the username of your friend";
        AlertDialog alert;
        final EditText input = new EditText(this);
        alert = new AlertDialog.Builder(EventScreen.this)
                .setTitle("Update Status")
                .setMessage(message)
                .setView(input)
                .setPositiveButton("Add Additional Friend", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        if (!value.equals("")) {
                            buddies.add(value.toString());
                            activate();
                        }
                    }
                }).setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Editable value = input.getText();
                        if(!value.equals(""))
                            buddies.add(value.toString());
                        String output = "";
                        String formatted = "";
                        for(int i = 0; i<buddies.size(); i++)
                        {
                            output += buddies.get(i) + ";";
                            formatted+= buddies.get(i) + "\n";
                        }
                        output = output.substring(0,output.length() - 1);
                        Friends = output;
                        TextView text = (TextView) findViewById(R.id.txtInvitees);
                        text.setText("Invitees:\n" + formatted);
                        //Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
                        buddies.clear();
                    }
                }).show();
    }



    public void startInviteButtonClick(View view) {


        //EditText numberOfFriends = (EditText)findViewById(R.id.numberOfUsers);
        EditText dateAndTime = (EditText)findViewById(R.id.dateAndTime);
        EditText location = (EditText)findViewById(R.id.location);

        //Friendnumber = numberOfFriends.getText().toString();
        DateAndTime = dateAndTime.getText().toString();
        Location = location.getText().toString();

        //NumberOfFriends = Integer.parseInt(Friendnumber);


        activate();




        /*for(int i = 0; i < NumberOfFriends; i++)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("please enter the " + (i + 1) + " th/rd/nd/st user name");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_VARIATION_NORMAL);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Friends += input.getText().toString();
                    Friends += ";";
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }*/


    }

    public void startEventButtonClick(View view){

       /* new AlertDialog.Builder(context).setTitle("freinds variable")
                .setMessage(Friends)
                .setIcon(android.R.drawable.ic_dialog_alert).show();*/

        new HttpRequest().execute("http://www.csce.uark.edu/~mrs018/CreateEvent.php");


    }






    class HttpRequest extends AsyncTask<String,String,String>
    {
        //holder strings that are used to pass info from LoginScreen class to HttpRequest class
        String username;
        String friends;
        String dateandtime;
        String location;


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            new AlertDialog.Builder(context).setTitle("response from server")
                    .setMessage(result)
                    .setIcon(android.R.drawable.ic_dialog_alert).show();
            Intent intent = new Intent(getBaseContext(), SelectPreferencesScreen.class);
            intent.putExtra("serverResponse", serverResponse);
            intent.putExtra("passingInviter" , Username);
            startActivity(intent);
            finish();
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

             username = Username;
             friends = Friends;
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
                nameValuePairs.add(new BasicNameValuePair("friends", friends));
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
