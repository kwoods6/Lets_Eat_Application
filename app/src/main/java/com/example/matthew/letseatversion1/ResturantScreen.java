package com.example.matthew.letseatversion1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class ResturantScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturant_screen);
        ListView listView = (ListView) findViewById(R.id.restaurantView);
        String type = getIntent().getExtras().getString("type");
        String[] locations;
        if(type.equalsIgnoreCase("Italian")){
                locations = getResources().getStringArray(R.array.Italian);
        }
        else if(type.equalsIgnoreCase("Mexican"))
        {
            locations = getResources().getStringArray(R.array.Mexican);
        }
        else
        {
            locations = getResources().getStringArray(R.array.Chinese);
        }
        ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locations);
        listView.setAdapter(listadapter);
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
}
