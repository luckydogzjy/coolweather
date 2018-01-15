package com.coolweather.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 非皇 on 2018/1/13.
 */

/*
"basic": { //基本信息
        "city": "北京",  //城市名称
        "id": "CN101010100",  //城市ID
        "update": { //更新时间
        "loc": "2015-07-02 14:44",  //当地时间
        }
*/
public class Basic {
     /*由于用GSON解析出来的变量名不够友好
     我们需要使用SerializedName来让GSON解析出来的变量名与实体类定义出来的变量名一一对应
     也可以说是让JSON字段与Java字段建立映射关系*/
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }

}
