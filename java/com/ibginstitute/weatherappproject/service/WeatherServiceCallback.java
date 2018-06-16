package com.ibginstitute.weatherappproject.service;

import com.ibginstitute.weatherappproject.data.Channel;

public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);


}
