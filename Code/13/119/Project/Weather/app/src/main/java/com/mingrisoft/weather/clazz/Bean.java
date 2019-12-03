package com.mingrisoft.weather.clazz;

import java.util.List;

/**
 * Author LYJ
 * Created on 2016/12/5.
 * Time 16:18
 */

public class Bean {

    /**
     * reason : successed!
     * result : {"data":{"pubdate":"2016-12-05","pubtime":"11:00:00","realtime":{"city_code":"101010100","city_name":"北京","date":"2016-12-05","time":"15:00:00","week":1,"moon":"十一月初七","dataUptime":1480923364,"weather":{"temperature":"5","humidity":"14","info":"多云","img":"1"},"wind":{"direct":"西北风","power":"4级","offset":null,"windspeed":null}},"life":{"date":"2016-12-5","info":{"chuanyi":["冷","天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。"],"ganmao":["易发","相对于今天将会出现大幅度降温，易发生感冒，请注意适当增加衣服，加强自我防护避免感冒。"],"kongtiao":["开启制暖空调","您将感到有些冷，可以适当开启制暖空调调节室内温度，以免着凉感冒。"],"xiche":["较适宜","较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"],"yundong":["较不宜","天气较好，但考虑风力较大，天气寒冷，推荐您进行室内运动，若在户外运动须注意保暖。"],"ziwaixian":["中等","属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"]}},"weather":[{"date":"2016-12-05","info":{"day":["0","晴","6","北风","3-4 级","07:20"],"night":["0","晴","-5","","微风","16:49"]},"week":"一","nongli":"十一月初七"},{"date":"2016-12-06","info":{"dawn":["0","晴","-5","无持续风向","微风","16:49"],"day":["1","多云","5","","微风","07:21"],"night":["1","多云","-3","","微风","16:49"]},"week":"二","nongli":"十一月初八"},{"date":"2016-12-07","info":{"dawn":["1","多云","-3","无持续风向","微风","16:49"],"day":["0","晴","8","","微风","07:22"],"night":["2","阴","-3","","微风","16:49"]},"week":"三","nongli":"十一月初九"},{"date":"2016-12-08","info":{"dawn":["2","阴","-3","无持续风向","微风","16:49"],"day":["2","阴","4","北风","4-5 级","07:23"],"night":["0","晴","-3","北风","4-5 级","16:49"]},"week":"四","nongli":"十一月初十 "},{"date":"2016-12-09","info":{"dawn":["0","晴","-3","北风","4-5 级","16:49"],"day":["0","晴","4","","微风","07:24"],"night":["0","晴","-5","","微风","16:49"]},"week":"五","nongli":"十一月十一"}],"f3h":{"temperature":[{"jg":"20161205140000","jb":"5"},{"jg":"20161205170000","jb":"3"},{"jg":"20161205200000","jb":"0"},{"jg":"20161205230000","jb":"-1"},{"jg":"20161206020000","jb":"-4"},{"jg":"20161206050000","jb":"-3"},{"jg":"20161206080000","jb":"-2"},{"jg":"20161206110000","jb":"-2"},{"jg":"20161206140000","jb":"4"}],"precipitation":[{"jg":"20161205140000","jf":"0"},{"jg":"20161205170000","jf":"0"},{"jg":"20161205200000","jf":"0"},{"jg":"20161205230000","jf":"0"},{"jg":"20161206020000","jf":"0"},{"jg":"20161206050000","jf":"0"},{"jg":"20161206080000","jf":"0"},{"jg":"20161206110000","jf":"0"},{"jg":"20161206140000","jf":"0"}]},"pm25":{"key":"Beijing","show_desc":0,"pm25":{"curPm":"39","pm25":"6","pm10":"42","level":1,"quality":"优","des":"可正常活动。"},"dateTime":"2016年12月05日15时","cityName":"北京"},"jingqu":"","jingqutq":"","date":"","isForeign":"0"}}
     * error_code : 0
     */

    private String reason;
    /**
     * data : {"pubdate":"2016-12-05","pubtime":"11:00:00","realtime":{"city_code":"101010100","city_name":"北京","date":"2016-12-05","time":"15:00:00","week":1,"moon":"十一月初七","dataUptime":1480923364,"weather":{"temperature":"5","humidity":"14","info":"多云","img":"1"},"wind":{"direct":"西北风","power":"4级","offset":null,"windspeed":null}},"life":{"date":"2016-12-5","info":{"chuanyi":["冷","天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。"],"ganmao":["易发","相对于今天将会出现大幅度降温，易发生感冒，请注意适当增加衣服，加强自我防护避免感冒。"],"kongtiao":["开启制暖空调","您将感到有些冷，可以适当开启制暖空调调节室内温度，以免着凉感冒。"],"xiche":["较适宜","较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"],"yundong":["较不宜","天气较好，但考虑风力较大，天气寒冷，推荐您进行室内运动，若在户外运动须注意保暖。"],"ziwaixian":["中等","属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"]}},"weather":[{"date":"2016-12-05","info":{"day":["0","晴","6","北风","3-4 级","07:20"],"night":["0","晴","-5","","微风","16:49"]},"week":"一","nongli":"十一月初七"},{"date":"2016-12-06","info":{"dawn":["0","晴","-5","无持续风向","微风","16:49"],"day":["1","多云","5","","微风","07:21"],"night":["1","多云","-3","","微风","16:49"]},"week":"二","nongli":"十一月初八"},{"date":"2016-12-07","info":{"dawn":["1","多云","-3","无持续风向","微风","16:49"],"day":["0","晴","8","","微风","07:22"],"night":["2","阴","-3","","微风","16:49"]},"week":"三","nongli":"十一月初九"},{"date":"2016-12-08","info":{"dawn":["2","阴","-3","无持续风向","微风","16:49"],"day":["2","阴","4","北风","4-5 级","07:23"],"night":["0","晴","-3","北风","4-5 级","16:49"]},"week":"四","nongli":"十一月初十 "},{"date":"2016-12-09","info":{"dawn":["0","晴","-3","北风","4-5 级","16:49"],"day":["0","晴","4","","微风","07:24"],"night":["0","晴","-5","","微风","16:49"]},"week":"五","nongli":"十一月十一"}],"f3h":{"temperature":[{"jg":"20161205140000","jb":"5"},{"jg":"20161205170000","jb":"3"},{"jg":"20161205200000","jb":"0"},{"jg":"20161205230000","jb":"-1"},{"jg":"20161206020000","jb":"-4"},{"jg":"20161206050000","jb":"-3"},{"jg":"20161206080000","jb":"-2"},{"jg":"20161206110000","jb":"-2"},{"jg":"20161206140000","jb":"4"}],"precipitation":[{"jg":"20161205140000","jf":"0"},{"jg":"20161205170000","jf":"0"},{"jg":"20161205200000","jf":"0"},{"jg":"20161205230000","jf":"0"},{"jg":"20161206020000","jf":"0"},{"jg":"20161206050000","jf":"0"},{"jg":"20161206080000","jf":"0"},{"jg":"20161206110000","jf":"0"},{"jg":"20161206140000","jf":"0"}]},"pm25":{"key":"Beijing","show_desc":0,"pm25":{"curPm":"39","pm25":"6","pm10":"42","level":1,"quality":"优","des":"可正常活动。"},"dateTime":"2016年12月05日15时","cityName":"北京"},"jingqu":"","jingqutq":"","date":"","isForeign":"0"}
     */

    private ResultEntity result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultEntity {
        /**
         * pubdate : 2016-12-05
         * pubtime : 11:00:00
         * realtime : {"city_code":"101010100","city_name":"北京","date":"2016-12-05","time":"15:00:00","week":1,"moon":"十一月初七","dataUptime":1480923364,"weather":{"temperature":"5","humidity":"14","info":"多云","img":"1"},"wind":{"direct":"西北风","power":"4级","offset":null,"windspeed":null}}
         * life : {"date":"2016-12-5","info":{"chuanyi":["冷","天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。"],"ganmao":["易发","相对于今天将会出现大幅度降温，易发生感冒，请注意适当增加衣服，加强自我防护避免感冒。"],"kongtiao":["开启制暖空调","您将感到有些冷，可以适当开启制暖空调调节室内温度，以免着凉感冒。"],"xiche":["较适宜","较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"],"yundong":["较不宜","天气较好，但考虑风力较大，天气寒冷，推荐您进行室内运动，若在户外运动须注意保暖。"],"ziwaixian":["中等","属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"]}}
         * weather : [{"date":"2016-12-05","info":{"day":["0","晴","6","北风","3-4 级","07:20"],"night":["0","晴","-5","","微风","16:49"]},"week":"一","nongli":"十一月初七"},{"date":"2016-12-06","info":{"dawn":["0","晴","-5","无持续风向","微风","16:49"],"day":["1","多云","5","","微风","07:21"],"night":["1","多云","-3","","微风","16:49"]},"week":"二","nongli":"十一月初八"},{"date":"2016-12-07","info":{"dawn":["1","多云","-3","无持续风向","微风","16:49"],"day":["0","晴","8","","微风","07:22"],"night":["2","阴","-3","","微风","16:49"]},"week":"三","nongli":"十一月初九"},{"date":"2016-12-08","info":{"dawn":["2","阴","-3","无持续风向","微风","16:49"],"day":["2","阴","4","北风","4-5 级","07:23"],"night":["0","晴","-3","北风","4-5 级","16:49"]},"week":"四","nongli":"十一月初十 "},{"date":"2016-12-09","info":{"dawn":["0","晴","-3","北风","4-5 级","16:49"],"day":["0","晴","4","","微风","07:24"],"night":["0","晴","-5","","微风","16:49"]},"week":"五","nongli":"十一月十一"}]
         * f3h : {"temperature":[{"jg":"20161205140000","jb":"5"},{"jg":"20161205170000","jb":"3"},{"jg":"20161205200000","jb":"0"},{"jg":"20161205230000","jb":"-1"},{"jg":"20161206020000","jb":"-4"},{"jg":"20161206050000","jb":"-3"},{"jg":"20161206080000","jb":"-2"},{"jg":"20161206110000","jb":"-2"},{"jg":"20161206140000","jb":"4"}],"precipitation":[{"jg":"20161205140000","jf":"0"},{"jg":"20161205170000","jf":"0"},{"jg":"20161205200000","jf":"0"},{"jg":"20161205230000","jf":"0"},{"jg":"20161206020000","jf":"0"},{"jg":"20161206050000","jf":"0"},{"jg":"20161206080000","jf":"0"},{"jg":"20161206110000","jf":"0"},{"jg":"20161206140000","jf":"0"}]}
         * pm25 : {"key":"Beijing","show_desc":0,"pm25":{"curPm":"39","pm25":"6","pm10":"42","level":1,"quality":"优","des":"可正常活动。"},"dateTime":"2016年12月05日15时","cityName":"北京"}
         * jingqu :
         * jingqutq :
         * date :
         * isForeign : 0
         */

        private DataEntity data;

        public DataEntity getData() {
            return data;
        }

        public void setData(DataEntity data) {
            this.data = data;
        }

        public static class DataEntity {
            private String pubdate;
            private String pubtime;
            /**
             * city_code : 101010100
             * city_name : 北京
             * date : 2016-12-05
             * time : 15:00:00
             * week : 1
             * moon : 十一月初七
             * dataUptime : 1480923364
             * weather : {"temperature":"5","humidity":"14","info":"多云","img":"1"}
             * wind : {"direct":"西北风","power":"4级","offset":null,"windspeed":null}
             */

            private RealtimeEntity realtime;
            /**
             * date : 2016-12-5
             * info : {"chuanyi":["冷","天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。"],"ganmao":["易发","相对于今天将会出现大幅度降温，易发生感冒，请注意适当增加衣服，加强自我防护避免感冒。"],"kongtiao":["开启制暖空调","您将感到有些冷，可以适当开启制暖空调调节室内温度，以免着凉感冒。"],"xiche":["较适宜","较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"],"yundong":["较不宜","天气较好，但考虑风力较大，天气寒冷，推荐您进行室内运动，若在户外运动须注意保暖。"],"ziwaixian":["中等","属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"]}
             */

            private LifeEntity life;
            private F3hEntity f3h;
            /**
             * key : Beijing
             * show_desc : 0
             * pm25 : {"curPm":"39","pm25":"6","pm10":"42","level":1,"quality":"优","des":"可正常活动。"}
             * dateTime : 2016年12月05日15时
             * cityName : 北京
             */

            private Pm25Entity pm25;
            private String jingqu;
            private String jingqutq;
            private String date;
            private String isForeign;
            /**
             * date : 2016-12-05
             * info : {"day":["0","晴","6","北风","3-4 级","07:20"],"night":["0","晴","-5","","微风","16:49"]}
             * week : 一
             * nongli : 十一月初七
             */

            private List<WeatherEntity> weather;

            public String getPubdate() {
                return pubdate;
            }

            public void setPubdate(String pubdate) {
                this.pubdate = pubdate;
            }

            public String getPubtime() {
                return pubtime;
            }

            public void setPubtime(String pubtime) {
                this.pubtime = pubtime;
            }

            public RealtimeEntity getRealtime() {
                return realtime;
            }

            public void setRealtime(RealtimeEntity realtime) {
                this.realtime = realtime;
            }

            public LifeEntity getLife() {
                return life;
            }

            public void setLife(LifeEntity life) {
                this.life = life;
            }

            public F3hEntity  getF3h() {
                return f3h;
            }

            public void setF3h(F3hEntity f3h) {
                this.f3h = f3h;
            }

            public Pm25Entity getPm25() {
                return pm25;
            }

            public void setPm25(Pm25Entity pm25) {
                this.pm25 = pm25;
            }

            public String getJingqu() {
                return jingqu;
            }

            public void setJingqu(String jingqu) {
                this.jingqu = jingqu;
            }

            public String getJingqutq() {
                return jingqutq;
            }

            public void setJingqutq(String jingqutq) {
                this.jingqutq = jingqutq;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getIsForeign() {
                return isForeign;
            }

            public void setIsForeign(String isForeign) {
                this.isForeign = isForeign;
            }

            public List<WeatherEntity> getWeather() {
                return weather;
            }

            public void setWeather(List<WeatherEntity> weather) {
                this.weather = weather;
            }

            public static class RealtimeEntity {
                private String city_code;
                private String city_name;
                private String date;
                private String time;
                private int week;
                private String moon;
                private int dataUptime;
                /**
                 * temperature : 5
                 * humidity : 14
                 * info : 多云
                 * img : 1
                 */

                private WeatherEntity weather;
                /**
                 * direct : 西北风
                 * power : 4级
                 * offset : null
                 * windspeed : null
                 */

                private WindEntity wind;

                public String getCity_code() {
                    return city_code;
                }

                public void setCity_code(String city_code) {
                    this.city_code = city_code;
                }

                public String getCity_name() {
                    return city_name;
                }

                public void setCity_name(String city_name) {
                    this.city_name = city_name;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public int getWeek() {
                    return week;
                }

                public void setWeek(int week) {
                    this.week = week;
                }

                public String getMoon() {
                    return moon;
                }

                public void setMoon(String moon) {
                    this.moon = moon;
                }

                public int getDataUptime() {
                    return dataUptime;
                }

                public void setDataUptime(int dataUptime) {
                    this.dataUptime = dataUptime;
                }

                public WeatherEntity getWeather() {
                    return weather;
                }

                public void setWeather(WeatherEntity weather) {
                    this.weather = weather;
                }

                public WindEntity getWind() {
                    return wind;
                }

                public void setWind(WindEntity wind) {
                    this.wind = wind;
                }

                public static class WeatherEntity {
                    private String temperature;
                    private String humidity;
                    private String info;
                    private String img;

                    public String getTemperature() {
                        return temperature;
                    }

                    public void setTemperature(String temperature) {
                        this.temperature = temperature;
                    }

                    public String getHumidity() {
                        return humidity;
                    }

                    public void setHumidity(String humidity) {
                        this.humidity = humidity;
                    }

                    public String getInfo() {
                        return info;
                    }

                    public void setInfo(String info) {
                        this.info = info;
                    }

                    public String getImg() {
                        return img;
                    }

                    public void setImg(String img) {
                        this.img = img;
                    }
                }

                public static class WindEntity {
                    private String direct;
                    private String power;
                    private Object offset;
                    private Object windspeed;

                    public String getDirect() {
                        return direct;
                    }

                    public void setDirect(String direct) {
                        this.direct = direct;
                    }

                    public String getPower() {
                        return power;
                    }

                    public void setPower(String power) {
                        this.power = power;
                    }

                    public Object getOffset() {
                        return offset;
                    }

                    public void setOffset(Object offset) {
                        this.offset = offset;
                    }

                    public Object getWindspeed() {
                        return windspeed;
                    }

                    public void setWindspeed(Object windspeed) {
                        this.windspeed = windspeed;
                    }
                }
            }

            public static class LifeEntity {
                private String date;
                private InfoEntity info;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public InfoEntity getInfo() {
                    return info;
                }

                public void setInfo(InfoEntity info) {
                    this.info = info;
                }

                public static class InfoEntity {
                    private List<String> chuanyi;
                    private List<String> ganmao;
                    private List<String> kongtiao;
                    private List<String> xiche;
                    private List<String> yundong;
                    private List<String> ziwaixian;

                    public List<String> getChuanyi() {
                        return chuanyi;
                    }

                    public void setChuanyi(List<String> chuanyi) {
                        this.chuanyi = chuanyi;
                    }

                    public List<String> getGanmao() {
                        return ganmao;
                    }

                    public void setGanmao(List<String> ganmao) {
                        this.ganmao = ganmao;
                    }

                    public List<String> getKongtiao() {
                        return kongtiao;
                    }

                    public void setKongtiao(List<String> kongtiao) {
                        this.kongtiao = kongtiao;
                    }

                    public List<String> getXiche() {
                        return xiche;
                    }

                    public void setXiche(List<String> xiche) {
                        this.xiche = xiche;
                    }

                    public List<String> getYundong() {
                        return yundong;
                    }

                    public void setYundong(List<String> yundong) {
                        this.yundong = yundong;
                    }

                    public List<String> getZiwaixian() {
                        return ziwaixian;
                    }

                    public void setZiwaixian(List<String> ziwaixian) {
                        this.ziwaixian = ziwaixian;
                    }
                }
            }

            public static class F3hEntity {
                /**
                 * jg : 20161205140000
                 * jb : 5
                 */

                private List<TemperatureEntity> temperature;
                /**
                 * jg : 20161205140000
                 * jf : 0
                 */

                private List<PrecipitationEntity> precipitation;

                public List<TemperatureEntity> getTemperature() {
                    return temperature;
                }

                public void setTemperature(List<TemperatureEntity> temperature) {
                    this.temperature = temperature;
                }

                public List<PrecipitationEntity> getPrecipitation() {
                    return precipitation;
                }

                public void setPrecipitation(List<PrecipitationEntity> precipitation) {
                    this.precipitation = precipitation;
                }

                public static class TemperatureEntity {
                    private String jg;
                    private String jb;

                    public String getJg() {
                        return jg;
                    }

                    public void setJg(String jg) {
                        this.jg = jg;
                    }

                    public String getJb() {
                        return jb;
                    }

                    public void setJb(String jb) {
                        this.jb = jb;
                    }
                }

                public static class PrecipitationEntity {
                    private String jg;
                    private String jf;

                    public String getJg() {
                        return jg;
                    }

                    public void setJg(String jg) {
                        this.jg = jg;
                    }

                    public String getJf() {
                        return jf;
                    }

                    public void setJf(String jf) {
                        this.jf = jf;
                    }
                }
            }

            public static class Pm25Entity {
                private String key;
                private int show_desc;
                /**
                 * curPm : 39
                 * pm25 : 6
                 * pm10 : 42
                 * level : 1
                 * quality : 优
                 * des : 可正常活动。
                 */

                private Pm25MessageEntity pm25;
                private String dateTime;
                private String cityName;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public int getShow_desc() {
                    return show_desc;
                }

                public void setShow_desc(int show_desc) {
                    this.show_desc = show_desc;
                }

                public Pm25MessageEntity getPm25() {
                    return pm25;
                }

                public void setPm25(Pm25MessageEntity pm25) {
                    this.pm25 = pm25;
                }

                public String getDateTime() {
                    return dateTime;
                }

                public void setDateTime(String dateTime) {
                    this.dateTime = dateTime;
                }

                public String getCityName() {
                    return cityName;
                }

                public void setCityName(String cityName) {
                    this.cityName = cityName;
                }

                public static class Pm25MessageEntity {
                    private String curPm;
                    private String pm25;
                    private String pm10;
                    private int level;
                    private String quality;
                    private String des;

                    public String getCurPm() {
                        return curPm;
                    }

                    public void setCurPm(String curPm) {
                        this.curPm = curPm;
                    }

                    public String getPm25() {
                        return pm25;
                    }

                    public void setPm25(String pm25) {
                        this.pm25 = pm25;
                    }

                    public String getPm10() {
                        return pm10;
                    }

                    public void setPm10(String pm10) {
                        this.pm10 = pm10;
                    }

                    public int getLevel() {
                        return level;
                    }

                    public void setLevel(int level) {
                        this.level = level;
                    }

                    public String getQuality() {
                        return quality;
                    }

                    public void setQuality(String quality) {
                        this.quality = quality;
                    }

                    public String getDes() {
                        return des;
                    }

                    public void setDes(String des) {
                        this.des = des;
                    }
                }
            }

            public static class WeatherEntity {
                private String date;
                private InfoEntity info;
                private String week;
                private String nongli;

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public InfoEntity getInfo() {
                    return info;
                }

                public void setInfo(InfoEntity info) {
                    this.info = info;
                }

                public String getWeek() {
                    return week;
                }

                public void setWeek(String week) {
                    this.week = week;
                }

                public String getNongli() {
                    return nongli;
                }

                public void setNongli(String nongli) {
                    this.nongli = nongli;
                }

                public static class InfoEntity {
                    private List<String> day;
                    private List<String> night;

                    public List<String> getDay() {
                        return day;
                    }

                    public void setDay(List<String> day) {
                        this.day = day;
                    }

                    public List<String> getNight() {
                        return night;
                    }

                    public void setNight(List<String> night) {
                        this.night = night;
                    }
                }
            }
        }
    }
}
