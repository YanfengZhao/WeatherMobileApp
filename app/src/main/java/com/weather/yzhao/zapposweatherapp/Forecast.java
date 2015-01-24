package com.weather.yzhao.zapposweatherapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Forecast extends ActionBarActivity {
    Context context = this;
    public String request_uri = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    private AsyncHttpClient httpClient = new AsyncHttpClient();
    private String TAG  = "Forecast Weather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        final String code = getIntent().getStringExtra("Code");
        if(code.length()<11) {
            System.out.println(code);
            request_uri += "q=" + code + "&mode=json&units=metric&cnt=5"; // handle zipcode
        }
        else{
            request_uri += code + "&mode=json&units=metric&cnt=5"; // handle coordinate
        }

        final TextView currCity = (TextView) this.findViewById(R.id.currCity);

        final TextView weatherCondition1 = (TextView) this.findViewById(R.id.weatherCondition1);
        final TextView maxTemperature1 = (TextView) this.findViewById(R.id.maxTemperature1);
        final TextView minTemperature1 = (TextView) this.findViewById(R.id.minTemperature1);
        final TextView day1 = (TextView) this.findViewById(R.id.day1);

        final TextView weatherCondition2 = (TextView) this.findViewById(R.id.weatherCondition2);
        final TextView maxTemperature2 = (TextView) this.findViewById(R.id.maxTemperature2);
        final TextView minTemperature2 = (TextView) this.findViewById(R.id.minTemperature2);
        final TextView day2 = (TextView) this.findViewById(R.id.day2);

        final TextView weatherCondition3 = (TextView) this.findViewById(R.id.weatherCondition3);
        final TextView maxTemperature3 = (TextView) this.findViewById(R.id.maxTemperature3);
        final TextView minTemperature3 = (TextView) this.findViewById(R.id.minTemperature3);
        final TextView day3 = (TextView) this.findViewById(R.id.day3);

        final TextView weatherCondition4 = (TextView) this.findViewById(R.id.weatherCondition4);
        final TextView maxTemperature4 = (TextView) this.findViewById(R.id.maxTemperature4);
        final TextView minTemperature4 = (TextView) this.findViewById(R.id.minTemperature4);
        final TextView day4 = (TextView) this.findViewById(R.id.day4);

        final TextView weatherCondition5 = (TextView) this.findViewById(R.id.weatherCondition5);
        final TextView maxTemperature5 = (TextView) this.findViewById(R.id.maxTemperature5);
        final TextView minTemperature5 = (TextView) this.findViewById(R.id.minTemperature5);
        final TextView day5 = (TextView) this.findViewById(R.id.day5);

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
                        ArrayList<String> maxTempArr = new ArrayList<String>();
                        ArrayList<String> minTempArr = new ArrayList<String>();
                        ArrayList<String> weatherConditionArr = new ArrayList<String>();

                        // get city
                        String city = jObject.getString("city");
                        List<String> strings = Arrays.asList(city.replaceAll("^.*?name\":\"", "").split("\".*?(name\":\"|$)"));
                        String getCity = strings.get(0);
                        currCity.setText(getCity);
                        currCity.setText("5 day forecast for " + new String(getCity));

                        // traverse each day
                        JSONArray jsonArray = jObject.getJSONArray("list");
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject day = jsonArray.getJSONObject(i);

                            // get weather condition
                            String weather = day.getString("weather");
                            List<String> wCondition = Arrays.asList(weather.replaceAll("^.*?description\":\"", "").split("\".*?(description\":\"|$)"));
                            String currWeather = wCondition.get(0);
                            weatherConditionArr.add(currWeather);

                            // get temperature
                            String temp = day.getString("temp");
                            JSONObject temperatures = new JSONObject(temp);
                            maxTempArr.add(tempConversion(temperatures.getString("max")));
                            minTempArr.add(tempConversion(temperatures.getString("min")));
                        }

                        System.out.println(weatherConditionArr);
                        System.out.println(maxTempArr);
                        System.out.println(minTempArr);


                        day1.setText("Day 1");
                        weatherCondition1.setText(weatherConditionArr.get(0));
                        minTemperature1.setText("Minimum Temperature: " + minTempArr.get(0) + " degrees fahrenheit");
                        maxTemperature1.setText("Maximum Temperature: " + maxTempArr.get(0) + " degrees fahrenheit");

                        day2.setText("Day 2");
                        weatherCondition2.setText(weatherConditionArr.get(1));
                        minTemperature2.setText("Minimum Temperature: " + minTempArr.get(1) + " degrees fahrenheit");
                        maxTemperature2.setText("Maximum Temperature: " + maxTempArr.get(1) + " degrees fahrenheit");

                        day3.setText("Day 3");
                        weatherCondition3.setText(weatherConditionArr.get(2));
                        minTemperature3.setText("Minimum Temperature: " + minTempArr.get(2) + " degrees fahrenheit");
                        maxTemperature3.setText("Maximum Temperature: " + maxTempArr.get(2) + " degrees fahrenheit");

                        day4.setText("Day 4");
                        weatherCondition4.setText(weatherConditionArr.get(3));
                        minTemperature4.setText("Minimum Temperature: " + minTempArr.get(3) + " degrees fahrenheit");
                        maxTemperature4.setText("Maximum Temperature: " + maxTempArr.get(3) + " degrees fahrenheit");

                        day5.setText("Day 5");
                        weatherCondition5.setText(weatherConditionArr.get(4));
                        minTemperature5.setText("Minimum Temperature: " + minTempArr.get(4) + " degrees fahrenheit");
                        maxTemperature5.setText("Maximum Temperature: " + maxTempArr.get(4) + " degrees fahrenheit");
                    }
                }
                catch(JSONException j){
                    System.out.println("Invalid Zip Code Error");
                }
            }

            public String tempConversion(String CTemp){
                double value = Double.parseDouble(CTemp);
                double FValue = (value)* 1.8000 + 32.00;
                return String.valueOf(FValue).substring(0,2);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e(TAG, "There was a problem in retrieving the url : " + e.toString());
                Toast.makeText(context, "There was a problem in retrieving the url", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.forecast, menu);
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

    public void useCurrLoc(View view){
        Intent intent = new Intent(this, YourLocation.class);
        startActivity(intent);
    }
}
