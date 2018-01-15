package com.coolweather.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 非皇 on 2018/1/13.
 */
/*"now": { //实况天气
        "cond": { //天气状况
        "txt": "晴" //天气状况描述
        },
        "tmp": "32",  //温度
        }*/
public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String info;
    }
}
