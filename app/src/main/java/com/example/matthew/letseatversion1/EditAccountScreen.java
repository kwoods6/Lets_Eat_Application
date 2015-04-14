package com.example.matthew.letseatversion1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

//This is for a git test
//This is for test number 2


public class EditAccountScreen extends ActionBarActivity {

    //holder strings
    //String newUserID;
    //String newUserPassword;
    //String newCity;
    //String newLastname;
    //String newFirstname;

    String editUserID;
    String editUserPassword;
    String editCity;
    String editLastname;
    String editFirstname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account_screen);

        //pulls the response from the server and textfield ids
        editUserID = getIntent().getExtras().getString("passingUserName");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_creation_screen, menu);
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




    public void confirmEditAccountClick(View view) {

        /*new AlertDialog.Builder(context).setTitle("response from server")
                .setMessage(editUserID)
                .setIcon(android.R.drawable.ic_dialog_alert).show();*/

        //pulls the id for each text edit field
        EditText editTextPassword = (EditText) findViewById(R.id.editText);
        EditText editTextCity = (EditText) findViewById(R.id.editText4);
        EditText editTextFirstname = (EditText) findViewById(R.id.editText2);
        EditText editTextLastname = (EditText) findViewById(R.id.editText3);

        //makes the holder string equal to what is in the text edit
        editUserPassword = editTextPassword.getText().toString();
        editCity = editTextCity.getText().toString();
        editFirstname = editTextFirstname.getText().toString();
        editLastname = editTextLastname.getText().toString();

        //makes a new http class and executes the url
        new HttpRequest().execute("http://www.csce.uark.edu/~mrs018/EditInfo.php");

    }




    //this variable is used to make new intents when in another class
    Context context = this;


    //all httprequests work the same way
    class HttpRequest extends AsyncTask<String,String,String>
    {
        //holder strings that are used to pass info from accountCreationScreen class to HttpRequest class
        String username;
        String password;
        String city;
        String firstname;
        String lastname;

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            /*new AlertDialog.Builder(context).setTitle("response from server")
                    .setMessage(result)
                    .setIcon(android.R.drawable.ic_dialog_alert).show();*/

            Intent intent = new Intent(context, LoginScreen.class);
            startActivity(intent);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            username = editUserID;
            password = editUserPassword;
            city = editCity;
            firstname = editFirstname;
            lastname = editLastname;
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try
            {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("hometown", city));
                nameValuePairs.add(new BasicNameValuePair("firstname", firstname));
                nameValuePairs.add(new BasicNameValuePair("lastname", lastname));
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