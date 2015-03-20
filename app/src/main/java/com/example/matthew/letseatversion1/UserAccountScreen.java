package com.example.matthew.letseatversion1;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
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


public class UserAccountScreen extends ActionBarActivity {

    String serverResponse;
    String newUserID;
    String newUserPassword;
    String newCity;
    String newLastname;
    String newFirstname;
    TextView user;
    TextView firstName;
    TextView lastName;
    TextView city;
    String[] tokens;

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

    //when the search button is clicked it pulls the selected info from the spinner and sends that to the next activity
    public void searchButtonClick(View view) {
        Intent intent = new Intent(this, ResturantScreen.class);
        Bundle myBundle = new Bundle();
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        myBundle.putString("type", mySpinner.getSelectedItem().toString());
        intent.putExtras(myBundle);
        startActivity(intent);
    }

    //button to edit account
    public void editAccountButtonClick (View view) {
        new HttpRequest().execute("http://www.csce.uark.edu/~mrs018/EditInfo.php");
    }

    //button to delete the account
    public void deleteAccountButtonClick(View view) {
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

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

                Intent intent = new Intent(context, LoginScreen.class);
                startActivity(intent);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            username = newUserID;
            password = newUserPassword;
            city = newCity;
            firstname = newFirstname;
            lastname = newLastname;
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



