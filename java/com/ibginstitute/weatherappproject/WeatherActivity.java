package com.ibginstitute.weatherappproject;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibginstitute.weatherappproject.data.Channel;
import com.ibginstitute.weatherappproject.data.Item;
import com.ibginstitute.weatherappproject.service.WeatherServiceCallback;
import com.ibginstitute.weatherappproject.service.YahooWeatherService;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback {

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;

    private EditText enterLocationEditText;
    private Button findWeatherButton;
    private String locationString;

    private YahooWeatherService service;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherIconImageView = (ImageView)findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView)findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView)findViewById(R.id.conditionTextView);
        locationTextView = (TextView)findViewById(R.id.locationTextView);

        enterLocationEditText = findViewById(R.id.enter_location_editText);
        findWeatherButton = findViewById(R.id.find_weather_button);

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        service.refreshWeather("Charlotte, NC");

        // This button press takes the EditText that the user entered and searches for the weather from that place.
        findWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationString = enterLocationEditText.getText().toString();
                service.refreshWeather(locationString);
            }
        });

    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();

        Item item = channel.getItem();

        int resourceID = getResources().getIdentifier(("drawable/icon_" + item.getCondition().getCode()), null, getPackageName());

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceID);

        weatherIconImageView.setImageDrawable(weatherIconDrawable);

        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(service.getLocation());
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
