package cn.edu.pku.gongrunyu.projectcreate_1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.edu.pku.gongrunyu.util.NetUtil;


public class MainActivity extends Activity implements View.OnClickListener{

    private ImageView mUpdateBtn;
    //String str2 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState){
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
    }


    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.title_update_btn){
            SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code","101010100");
            Log.d("myWeather",cityCode);

            if(NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                Log.d("myWeather","网络OK了！");
                queryWeatherCode(cityCode);
            }
            else {
                Log.d("myWeather","网络gg了！");
                Toast.makeText(MainActivity.this,"网络gg了！",Toast.LENGTH_LONG).show();
            }

        }
    }

    private void queryWeatherCode(String cityCode){
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try{
                    URL url = new URL(address);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    //Log.d("myWeather","test4"); //测试
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    //Log.d("myWeather","test6"); //测试
                    InputStream in = con.getInputStream();
                    //Log.d("myWeather","test7"); //测试
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    //Log.d("myWeather","test8"); //测试
                    StringBuilder response = new StringBuilder();
                    //Log.d("myWeather","test9"); //测试
                    String str;
                    while ((str=reader.readLine()) != null){
                        response.append(str);
                        Log.d("myWeather",str);
                        //str2 = str2 + str;
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather",responseStr);
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
        //Toast.makeText(MainActivity.this,str2,Toast.LENGTH_LONG).show();
    }

}