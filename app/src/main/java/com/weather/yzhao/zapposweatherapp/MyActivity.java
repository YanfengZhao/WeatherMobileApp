package com.weather.yzhao.zapposweatherapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MyActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void searchCurrWeather(View view){
        EditText zipCode = (EditText)findViewById(R.id.zipCode);
        String zipCodeStr = zipCode.getText().toString();
        Intent intent = new Intent(this, CurrentWeather.class);
        intent.putExtra("Code", zipCodeStr+",USA");
        startActivity(intent);
    }

    public void useCurrLoc(View view){
        Intent intent = new Intent(this, YourLocation.class);
        startActivity(intent);
    }
}
