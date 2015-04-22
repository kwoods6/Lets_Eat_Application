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
import android.widget.CheckBox;

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


public class SelectPreferencesScreen extends ActionBarActivity {

    String PreferenceString;
    String UserID;
    String Inviter;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_preferences_screen);


            UserID = getIntent().getExtras().getString("passingUserName");
            Inviter = getIntent().getExtras().getString("passingInviter");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_preferences_screen, menu);
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

    public void selectPreferencesButtonClick(View view){

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        CheckBox checkBox5 = (CheckBox) findViewById(R.id.checkBox5);
        CheckBox checkBox6 = (CheckBox) findViewById(R.id.checkBox6);
        CheckBox checkBox7 = (CheckBox) findViewById(R.id.checkBox7);
        CheckBox checkBox8 = (CheckBox) findViewById(R.id.checkBox8);
        CheckBox checkBox9 = (CheckBox) findViewById(R.id.checkBox9);

        if(checkBox9.isChecked() ){

            PreferenceString = "1";
        }
        else{
            PreferenceString = "0";
        }
        if(checkBox8.isChecked() ){

            PreferenceString = "1";
        }
        else{
            PreferenceString = "0";
        }
        if(checkBox7.isChecked()){

            PreferenceString += "1";
        }
        else{
            PreferenceString += "0";
        }
        if(checkBox6.isChecked()){

            PreferenceString += "1";
        }
        else{
            PreferenceString += "0";
        }
        if(checkBox5.isChecked()){

            PreferenceString += "1";
        }
        else{
            PreferenceString += "0";
        }
        if(checkBox4.isChecked()){

            PreferenceString += "1";
        }
        else{
            PreferenceString += "0";
        }
        if(checkBox3.isChecked()){

            PreferenceString += "1";
        }
        else{
            PreferenceString += "0";
        }
        if(checkBox2.isChecked()){

            PreferenceString += "1";
        }
        else{
            PreferenceString += "0";
        }
        if(checkBox.isChecked()){

            PreferenceString += "1";
        }
        else{
            PreferenceString += "0";
        }

        new SendPreferences().execute("http://www.csce.uark.edu/~mrs018/SendInPreferences.php");

        /*new AlertDialog.Builder(this).setTitle("variable")
                .setMessage(PreferenceString)
                .show();*/







        Intent intent = new Intent(context, UserAccountScreen.class);
        //intent.putExtra("serverResponse", UserID);
        startActivity(intent);

    }
/*public void bullshit()
{
    Intent intent = new Intent(context, UserAccountScreen.class);
    startActivity(intent);
}*/


    class SendPreferences extends AsyncTask<String,String,String>
    {
        //holder strings that are used to pass info from LoginScreen class to HttpRequest class
        String username;
        String inviter;
        String preferences;
        //Context mattsucks;



        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
           // bullshit();


        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            username = UserID;
            inviter = Inviter;
            preferences = PreferenceString;
            //mattsucks = context;

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
