package com.weather.yzhao.zapposweatherapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class CurrentWeather extends ActionBarActivity {
    Context context = this;
    public String request_uri = "http://api.openweathermap.org/data/2.5/weather?";
    private AsyncHttpClient httpClient = new AsyncHttpClient();
    private String TAG  = "CurrentWeather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_weather);

        String code = getIntent().getStringExtra("Code");
        if(code.length()<10) {
            request_uri += "q="+code; // handle zipcode
        }
        else{
            request_uri += code; // handle coordinate
        }
        final TextView currCity = (TextView) this.findViewById(R.id.currCity);
        final TextView zipEntered = (TextView) this.findViewById(R.id.zip);
        final TextView weatherCondition = (TextView) this.findViewById(R.id.weatherCondition);
        final TextView currTemp = (TextView) this.findViewById(R.id.currTemp);
        final TextView maxTemperature = (TextView) this.findViewById(R.id.maxTemperature);
        final TextView minTemperature = (TextView) this.findViewById(R.id.minTemperature);
        final TextView currHumidity = (TextView) this.findViewById(R.id.currHumidity);

        httpClient.get(request_uri, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    String returnCode = jObject.getString("cod");
                    if(returnCode.equals("404")){
                        currCity.setText("Invalid Zip Code. Try again with a valid Zip Code");
                    }
                    else {
                        String city = jObject.getString("name");
                        System.out.println(city);
                        currCity.setText(new String(city));

                        // get weather condition
                        String weather = jObject.getString("weather");
                        List<String> strings = Arrays.asList(weather.replaceAll("^.*?description\":\"", "").split("\".*?(description\":\"|$)"));
                        String currWeather = strings.get(0);
                        weatherCondition.setText(currWeather);

                        // get temperature
                        String temperatures = jObject.getString("main");
                        JSONObject main = new JSONObject(new String(temperatures));
                        String temp = tempConversion(main.getString("temp"));
                        String minTemp = tempConversion(main.getString("temp_min"));
                        String maxTemp = tempConversion(main.getString("temp_max"));
                        String humidity = main.getString("humidity");
                        currTemp.setText("Current Temperature: "+ temp+" degrees fahrenheit");
                        minTemperature.setText("Minimum Temperature: " + minTemp + " degrees fahrenheit");
                        maxTemperature.setText("Maximum Temperature: " + maxTemp + " degrees fahrenheit");
                        currHumidity.setText("Humidity: " + humidity + "%");
                    }
                }
                catch(JSONException j){
                    System.out.println("Invalid Zip Code Error");
                }
            }

            public String tempConversion(String KTemp){
                double value = Double.parseDouble(KTemp);
                double FValue = (value - 273.15)* 1.8000 + 32.00;
                return String.valueOf(FValue).substring(0,5);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e(TAG, "There was a problem in retrieving the url : " + e.toString());
                Toast.makeText(context,"There was a problem in retrieving the url" , Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.current_weather, menu);
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
        Intent intent = new Intent(this, CurrentWeather.class);
        String zipCodeStr = zipCode.getText().toString();
        intent.putExtra("Code", zipCodeStr + ",USA");
        startActivity(intent);
    }

    public void viewForecast(View view){
        Intent intent = new Intent(this, Forecast.class);
        String code = getIntent().getStringExtra("Code");
        intent.putExtra("Code", code);
        startActivity(intent);
    }

    public void useCurrLoc(View view){
        Intent intent = new Intent(this, YourLocation.class);
        startActivity(intent);
    }
}
