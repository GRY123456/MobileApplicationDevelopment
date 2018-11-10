package cn.edu.pku.gongrunyu.projectcreate_1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.gongrunyu.app.MyApplication;
import cn.edu.pku.gongrunyu.bean.City;
import cn.edu.pku.gongrunyu.bean.TodayWeather;
import cn.edu.pku.gongrunyu.util.NetUtil;


public class MainActivity extends Activity implements View.OnClickListener{
    private static final int UPDATE_TODAY_WEATHER = 1;
    private static final int DB = 2;

    private ImageView mUpdateBtn;

    private ImageView mCitySelect;

    private ImageView mTitleLocation;

    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,
            temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;

    private ProgressBar progressBar;

    public LocationClient mLocationClient = null;
    private MyLocationListerner myListener = new MyLocationListerner();

    ViewPager pager = null;
    ArrayList<View> viewContainter = new ArrayList<View>();

    TodayWeather nowWeather = null;
    TodayWeather tomorrowWeather = null;
    TodayWeather after2 = null;
    TodayWeather after3 = null;
    TodayWeather after4 = null;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateImage((TodayWeather) msg.obj);
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState){

        tomorrowWeather = new TodayWeather();
        nowWeather = new TodayWeather();
        after2 = new TodayWeather();
        after3 = new TodayWeather();
        after4 = new TodayWeather();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);


        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        if(NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
            Log.d("myWeather","网络OK了！");
            Toast.makeText(MainActivity.this,"网络OK!",Toast.LENGTH_LONG).show();
        }
        else {
            Log.d("myWeather","网络gg了！");
            Toast.makeText(MainActivity.this,"网络gg了！",Toast.LENGTH_LONG).show();
        }

        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        initLocation();

        mTitleLocation = (ImageView) findViewById(R.id.title_location);
        mTitleLocation.setOnClickListener(this);

        initView();


        pager = (ViewPager)this.findViewById(R.id.viewpager);
        View view1 = LayoutInflater.from(this).inflate(R.layout.today, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.tomorrow, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.after2, null);
        View view4 = LayoutInflater.from(this).inflate(R.layout.after3, null);
        View view5 = LayoutInflater.from(this).inflate(R.layout.after4, null);
        viewContainter.add(view1);
        viewContainter.add(view2);
        viewContainter.add(view3);
        viewContainter.add(view4);
        viewContainter.add(view5);
        pager.setAdapter(new MyPagerAdapters());
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0){
                    initView();
                    upViewPage(nowWeather);
                    Log.d("myWeather","00");
                }
                if (i == 1){
                    weatherImg = (ImageView) findViewById(R.id.weather_img1);
                    weekTv = (TextView) findViewById(R.id.week_today1);
                    temperatureTv = (TextView) findViewById(R.id.temperature1);
                    climateTv = (TextView) findViewById(R.id.climate1);
                    windTv = (TextView) findViewById(R.id.wind1);
                    upViewPage(tomorrowWeather);
                    Log.d("myWeather","01");
                }
                if (i == 2){
                    weatherImg = (ImageView) findViewById(R.id.weather_img2);
                    weekTv = (TextView) findViewById(R.id.week_today2);
                    temperatureTv = (TextView) findViewById(R.id.temperature2);
                    climateTv = (TextView) findViewById(R.id.climate2);
                    windTv = (TextView) findViewById(R.id.wind2);
                    upViewPage(after2);
                    Log.d("myWeather","02");
                }
                if (i == 3){
                    weatherImg = (ImageView) findViewById(R.id.weather_img3);
                    weekTv = (TextView) findViewById(R.id.week_today3);
                    temperatureTv = (TextView) findViewById(R.id.temperature3);
                    climateTv = (TextView) findViewById(R.id.climate3);
                    windTv = (TextView) findViewById(R.id.wind3);
                    upViewPage(after3);
                    Log.d("myWeather","03");
                }
                if (i == 4){
                    weatherImg = (ImageView) findViewById(R.id.weather_img4);
                    weekTv = (TextView) findViewById(R.id.week_today4);
                    temperatureTv = (TextView) findViewById(R.id.temperature4);
                    climateTv = (TextView) findViewById(R.id.climate4);
                    windTv = (TextView) findViewById(R.id.wind4);
                    upViewPage(after4);
                    Log.d("myWeather","04");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    class MyPagerAdapters extends PagerAdapter{

        @Override
        public int getCount() {
            return viewContainter.size();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container,
                                int position, @NonNull Object object) {
            ((ViewPager) container).removeView(viewContainter.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ((ViewPager) container).addView(viewContainter.get(position));
            return viewContainter.get(position);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    //一些初始化
    void initView(){
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        progressBar = (ProgressBar) findViewById(R.id.progress);


        //city_name_Tv.setText("N/A");
        //cityTv.setText("N/A");
        //timeTv.setText("N/A");
        //humidityTv.setText("N/A");
        //pmDataTv.setText("N/A");
        //pmQualityTv.setText("N/A");
        //weekTv.setText("N/A");
        //temperatureTv.setText("N/A");
        //climateTv.setText("N/A");
        //windTv.setText("N/A");
    }

    //监听点击的事件
    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.title_city_manager){
            Intent i = new Intent(this,SelectCity.class);
            //startActivity(i);
            startActivityForResult(i,1);
        }

        if(view.getId() == R.id.title_update_btn){

            SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code","101010100");
            Log.d("myWeather",cityCode);

            if(NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                Log.d("myWeather","网络OK了！");
                queryWeatherCode(cityCode);
            }
            else {
                progressBar.setVisibility(ProgressBar.GONE);
                Log.d("myWeather","网络gg了！");
                Toast.makeText(MainActivity.this,"网络gg了！",Toast.LENGTH_LONG).show();
            }

        }

        if(view.getId() == R.id.title_location){
            if (mLocationClient.isStarted()){
                mLocationClient.stop();
            }
            mLocationClient.start();

            final Handler BDHandler = new Handler(){
                public void handleMessage(Message msg){
                    switch (msg.what){
                        case DB:
                            if (msg.obj != null){
                                if (NetUtil.getNetworkState(MainActivity.this) != NetUtil.NETWORN_NONE){
                                    Log.d("myWeather","网络OK了！");
                                    queryWeatherCode(myListener.cityCode);
                                }else {
                                    Log.d("myWeather","网络gg了！");
                                    Toast.makeText(MainActivity.this,"网络gg了！",Toast.LENGTH_LONG).show();
                                }
                            }
                            myListener.cityCode = null;
                            break;
                        default:
                            break;
                    }
                }
            };

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        /*
                        while (myListener.cityCode == null){
                            Thread.sleep(2000);
                            Log.d("mylocation","循环中");
                        }
                        */

                        List<City> mCityList;
                        MyApplication myApplication;
                        myApplication = MyApplication.getInstance();
                        mCityList = myApplication.getCityList();
                        for (City cityl:mCityList){
                            if (cityl.getCity().equals(myListener.recity)){
                                myListener.cityCode = cityl.getNumber();
                                Log.d("location_code",myListener.cityCode);
                            }
                        }
                        Message msg = new Message();
                        msg.what = DB;
                        msg.obj = myListener.cityCode;
                        BDHandler.sendMessage(msg);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //返回选择城市的代码
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode == 1 && resultCode == RESULT_OK){
            String newCityCode = data.getStringExtra("cityCode");
            Log.d("myWeather","选择的城市代码为"+newCityCode);

            if(NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                Log.d("myWeather","网络OK了！");
                queryWeatherCode(newCityCode);
            }
            else {
                Log.d("myWeather","网络gg了！");
                Toast.makeText(MainActivity.this,"网络gg了！",Toast.LENGTH_LONG).show();
            }
        }
    }

    //查询cityCode，即某个城市的天气信息
    private void queryWeatherCode(String cityCode){
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try{
                    URL url = new URL(address);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str=reader.readLine()) != null){
                        response.append(str);
                        Log.d("myWeather",str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather",responseStr);
                    todayWeather = parseXML(responseStr);

                    if(todayWeather != null){
                        Log.d("myWeather",todayWeather.toString());

                        for (int i=1;i<=100;i++){
                            try{
                                progressBar.setProgress(i);
                                Thread.sleep(3);
                            }
                            catch (Exception e ){
                            }
                        }

                        Message msg =new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    //解析XML文件，并且显示里面的数值
    private TodayWeather parseXML(String xmldata){
        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try{
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather","parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    //  判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //  判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")){
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null){
                            if (xmlPullParser.getName().equals("city")){
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            }
                            else if (xmlPullParser.getName().equals("updatetime")){
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            }
                            else if (xmlPullParser.getName().equals("shidu")){
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            }
                            else if (xmlPullParser.getName().equals("wendu")){
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            }
                            else if (xmlPullParser.getName().equals("pm25")){
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            }
                            else if (xmlPullParser.getName().equals("quality")){
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            }
                            else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0){
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                nowWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            }
                            else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 1){
                                eventType = xmlPullParser.next();
                                tomorrowWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            }
                            else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 2){
                                eventType = xmlPullParser.next();
                                after2.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            }
                            else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 3){
                                eventType = xmlPullParser.next();
                                after3.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            }
                            else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 4){
                                eventType = xmlPullParser.next();
                                after4.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            }
                            else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0){
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                nowWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            }
                            else if (xmlPullParser.getName().equals("fengli") && fengliCount == 1){
                                eventType = xmlPullParser.next();
                                tomorrowWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            }
                            else if (xmlPullParser.getName().equals("fengli") && fengliCount == 2){
                                eventType = xmlPullParser.next();
                                after2.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            }
                            else if (xmlPullParser.getName().equals("fengli") && fengliCount == 3){
                                eventType = xmlPullParser.next();
                                after3.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            }
                            else if (xmlPullParser.getName().equals("fengli") && fengliCount == 4){
                                eventType = xmlPullParser.next();
                                after4.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            }
                            else if (xmlPullParser.getName().equals("date") && dateCount == 0){
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                nowWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            }
                            else if (xmlPullParser.getName().equals("date") && dateCount == 1){
                                eventType = xmlPullParser.next();
                                tomorrowWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            }
                            else if (xmlPullParser.getName().equals("date") && dateCount == 2){
                                eventType = xmlPullParser.next();
                                after2.setDate(xmlPullParser.getText());
                                dateCount++;
                            }
                            else if (xmlPullParser.getName().equals("date") && dateCount == 3){
                                eventType = xmlPullParser.next();
                                after3.setDate(xmlPullParser.getText());
                                dateCount++;
                            }
                            else if (xmlPullParser.getName().equals("date") && dateCount == 4){
                                eventType = xmlPullParser.next();
                                after4.setDate(xmlPullParser.getText());
                                dateCount++;
                            }
                            else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                nowWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }
                            else if (xmlPullParser.getName().equals("high") && highCount == 1) {
                                eventType = xmlPullParser.next();
                                tomorrowWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }
                            else if (xmlPullParser.getName().equals("high") && highCount == 2) {
                                eventType = xmlPullParser.next();
                                after2.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }
                            else if (xmlPullParser.getName().equals("high") && highCount == 3) {
                                eventType = xmlPullParser.next();
                                after3.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }
                            else if (xmlPullParser.getName().equals("high") && highCount == 4) {
                                eventType = xmlPullParser.next();
                                after4.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }
                            else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                nowWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }
                            else if (xmlPullParser.getName().equals("low") && lowCount == 1) {
                                eventType = xmlPullParser.next();
                                tomorrowWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }
                            else if (xmlPullParser.getName().equals("low") && lowCount == 2) {
                                eventType = xmlPullParser.next();
                                after2.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }
                            else if (xmlPullParser.getName().equals("low") && lowCount == 3) {
                                eventType = xmlPullParser.next();
                                after3.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }
                            else if (xmlPullParser.getName().equals("low") && lowCount == 4) {
                                eventType = xmlPullParser.next();
                                after4.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }
                            else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                nowWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                            else if (xmlPullParser.getName().equals("type") && typeCount == 1) {
                                eventType = xmlPullParser.next();
                                tomorrowWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                            else if (xmlPullParser.getName().equals("type") && typeCount == 2) {
                                eventType = xmlPullParser.next();
                                after2.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                            else if (xmlPullParser.getName().equals("type") && typeCount == 3) {
                                eventType = xmlPullParser.next();
                                after3.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                            else if (xmlPullParser.getName().equals("type") && typeCount == 4) {
                                eventType = xmlPullParser.next();
                                after4.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                            break;
                        }
                    //  判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                //进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        }
        catch (XmlPullParserException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return todayWeather;
    }


    //更新得到的天气数值
    void updateTodayWeather(TodayWeather todayWeather){
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime()+ "发布");
        humidityTv.setText("湿度："+todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
    }

    //更新图片
    void updateImage(TodayWeather todayWeather){
        int pm25 = 0;
        if(todayWeather.getPm25()!=null){
            pm25 = Integer.valueOf(todayWeather.getPm25()).intValue();
        }
        if (pm25>=0 && pm25<=50){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        }
        else if (pm25>50 && pm25<=100){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
        }
        else if (pm25>100 && pm25<=150){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
        }
        else if (pm25>150 && pm25<=200){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        }
        else if (pm25>200 && pm25<=300){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        }
        else {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        }

    }

    void upViewPage(TodayWeather todayWeather){
        if (todayWeather.getDate() != null)
            weekTv.setText(todayWeather.getDate());
        if (todayWeather.getHigh() != null && todayWeather.getLow() != null)
            temperatureTv.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        if (todayWeather.getType() != null)
            climateTv.setText(todayWeather.getType());
        if (todayWeather.getFengli() != null)
            windTv.setText("风力:"+todayWeather.getFengli());

        String climate = todayWeather.getType();
        if(climate != null) {
            if (climate.equals("多云")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
            } else if (climate.equals("暴雪")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
            } else if (climate.equals("暴雨")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
            } else if (climate.equals("大暴雨")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
            } else if (climate.equals("大雪")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
            } else if (climate.equals("大雨")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
            } else if (climate.equals("多云")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
            } else if (climate.equals("雷阵雨")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
            } else if (climate.equals("雷阵雨冰雹")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
            } else if (climate.equals("晴")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
            } else if (climate.equals("沙尘暴")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
            } else if (climate.equals("特大暴雨")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
            } else if (climate.equals("雾")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
            } else if (climate.equals("小雪")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
            } else if (climate.equals("小雨")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
            } else if (climate.equals("阴")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
            } else if (climate.equals("雨夹雪")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
            } else if (climate.equals("阵雪")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
            } else if (climate.equals("阵雨")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
            } else if (climate.equals("中雨")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
            } else if (climate.equals("中雨")) {
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
            }
        }
    }
}