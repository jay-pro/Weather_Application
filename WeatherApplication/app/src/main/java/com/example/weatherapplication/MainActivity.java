package com.example.weatherapplication;
//Bùi Hà Nhi 18110168
import androidx.appcompat.app.AppCompatActivity;//

import android.os.AsyncTask;///////////////////////
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView cityName;
    Button searchButton;
    TextView result;

    class Weather extends AsyncTask<String,Void,String>{ //First String means URL is in String, Void means nothing, Third String means Return type will be String

        @Override
        protected String doInBackground(String... address) {
            //String... means multiple address can be send. It acts as array
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //Establish connection with address
                connection.connect();

                //retrieve data from url
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                //Retrieve data and return it as String
                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1){
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }
                return content;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public void search(View view){
        cityName = findViewById(R.id.cityName);
        searchButton = findViewById(R.id.searchButton);
        result = findViewById(R.id.result);

        String cName = cityName.getText().toString();


        String content;
        Weather weather = new Weather();
        try {
            //content = weather.execute("http://api.openweathermap.org/data/2.5/weather?q=Hanoi&appid=f727e7f338eae6eb8e7e0330eef0f6fc").get();
            content = weather.execute("http://api.openweathermap.org/data/2.5/weather?q="+cName+"&appid=f727e7f338eae6eb8e7e0330eef0f6fc").get();
            //First we will check data is retrieve successfully or not
            Log.i("content",content);

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperature = jsonObject.getString("main");//this main is not part of weather array, it's seperate variable like weather
            double visibility;


            //Log.i("weatherData",weatherData);
            //weather data is in Array
            JSONArray array = new JSONArray(weatherData);

            String main = "";
            String description = "";
            String temperature = "";

            for(int i=0; i<array.length(); i++){
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");
            }

            JSONObject mainPart = new JSONObject(mainTemperature);
            temperature = mainPart.getString("temp");

            visibility = Double.parseDouble(jsonObject.getString("visibility"));
            //By default visibility is in meter
            int visibilityInKilometer = (int) visibility/1000;

            Log.i("Temperature",temperature);

            /*
            Log.i("main",main);
            Log.i("description",description);
             */

            String resultText =
                    "Main           : " + main +
                    "\nDescription  : " + description +
                    "\nTemperature  : " + temperature +
                    "\nVisibility   : " + visibilityInKilometer + " Km";

            result.setText(resultText);

            //Now we will show this result on screen


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
