package com.coolweather.com.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 非皇 on 2018/1/13.
 */
/*"suggestion": { //生活指数，仅限国内城市
        "comf": { //舒适度指数
        "txt": "白天天气多云，同时会感到有些热，不很舒适。" //详细描述
        },
        "cw": { //洗车指数
        "txt": "较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
        },
        "sport": { //运动指数
        "txt": "天气较好，户外运动请注意防晒。推荐您进行室内运动。"
        }
        }*/
public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("cw")
    public CarWash carWash;
    public Sport sport;

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
