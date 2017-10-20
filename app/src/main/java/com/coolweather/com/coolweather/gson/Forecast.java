package com.coolweather.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 非皇 on 2017/10/6.
 */
/*
"daily_forecast": [ //天气预报，国内7天，国际10天
        {
        "date": "2015-07-02",  //预报日期
        "cond": { //天气状况

        "txt_d": "晴",  //白天天气状况描述

        },

        "tmp": { //温度
        "max": "34",  //最高温度
        "min": "18" //最低温度
        }
        },
        ...... //这是个json数组，后面还有跟这个类似的东西
        }
*/
public class Forecast {
    public String date;
    @SerializedName("tmp")
    public Temperature temperature;
    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}
