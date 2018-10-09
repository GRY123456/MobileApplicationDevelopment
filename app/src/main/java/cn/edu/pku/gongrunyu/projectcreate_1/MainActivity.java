package cn.edu.pku.gongrunyu.projectcreate_1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cn.edu.pku.gongrunyu.util.NetUtil;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);

        if(NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
            Log.d("myWeather","网络OK了！");
            Toast.makeText(MainActivity.this,"网络OK!",Toast.LENGTH_LONG).show();
        }
        else {
            Log.d("myWeather","网络gg了！");
            Toast.makeText(MainActivity.this,"网络gg了！",Toast.LENGTH_LONG).show();
        }
    }
}
