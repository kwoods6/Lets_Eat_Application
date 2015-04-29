package com.example.matthew.letseatversion1;

import android.app.AlertDialog;
import android.content.Context;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;



/**
 * A login screen that offers login via email/password.
 */
public class LoginScreen extends ActionBarActivity {


    //string to hold the info the user puts in the text edit boxs
    String loginFromUser;
    String passwordFromUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
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


    /** Called when the user clicks the Send button */
    public void loginButtonClick(View view) {

        //pulls the id for each text edit field
        EditText editTextEmail = (EditText)findViewById(R.id.email);
        EditText editTextPassword = (EditText)findViewById(R.id.password);

        //makes the holder string equal to what is in the text edit
        loginFromUser = editTextEmail.getText().toString();
        passwordFromUser = editTextPassword.getText().toString();

        //makes a new http class and executes the url
        new HttpRequest().execute("http://www.csce.uark.edu/~mrs018/Login.php");
    }


    //when the create account button is click this method a new intent
    //and starts a new activity accountCreationScreen
    public void startCreatingAccountButtonClick(View view) {
        Intent intent = new Intent(this, accountCreationScreen.class);
        startActivity(intent);
        finish();
    }


//this variable is used to make new intents when in another class
Context context = this;

    //this methos is called to pass info to the account screen
    //it bundles the returned JSON object as a string
    public void  passingAccountInfo(String result){
        Bundle bundle = new Bundle();
        bundle.putString("serverResponse", result);
        Intent intent = new Intent(getBaseContext(), UserAccountScreen.class);
        intent.putExtra("serverResponse", result);
        startActivity(intent);
        finish();


    }


class HttpRequest extends AsyncTask<String,String,String>
{
    //holder strings that are used to pass info from LoginScreen class to HttpRequest class
    String username;
    String password;

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        //this checks the result the server sent back to see if the info to login
        //was valid
        if(result.equalsIgnoreCase("Please fill out all form entries") == true) {
            new AlertDialog.Builder(context).setTitle("response from server")
                       .setMessage(result)
                       .setIcon(android.R.drawable.ic_dialog_alert).show();
        }
        else if(result.equalsIgnoreCase("Username or Password Incorrect") == true){
            new AlertDialog.Builder(context).setTitle("response from server")
                       .setMessage(result)
                       .setIcon(android.R.drawable.ic_dialog_alert).show();
        }
        else{
            passingAccountInfo(result);
        }

    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        username = loginFromUser;
        password = passwordFromUser;
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
            nameValuePairs.add(new BasicNameValuePair("password", password));

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