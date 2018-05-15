package cn.edu.gdmec.android.weatherdemo.mvp.View;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TimerTask;

import cn.edu.gdmec.android.weatherdemo.R;
import cn.edu.gdmec.android.weatherdemo.mvp.WeatherBean;
import cn.edu.gdmec.android.weatherdemo.mvp.presenter.WeatherPresenter;

public class ActivityMainActivity extends AppCompatActivity implements IWeatherView,View.OnClickListener {
    private TextView tvWeather;
    private TextView tvWeatherYesterday;
    private ProgressDialog progressDialog;
    private WeatherPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_search).setOnClickListener(this);
        tvWeather=findViewById(R.id.tv_weather);
        findViewById(R.id.btn_beijing_search).setOnClickListener(this);
        tvWeatherYesterday=findViewById(R.id.tv_weather_yesterday);
        presenter=new WeatherPresenter(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_search:
                presenter.loadWeather("广州");
                break;
            case R.id.btn_beijing_search:
                presenter.loadWeather("北京");
                break;
        }

    }

    @Override
    public void showProgress() {
        if(progressDialog != null&& progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        progressDialog = progressDialog.show(ActivityMainActivity.this,"","正在获取");

    }

    @Override
    public void hideProgress() {
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }

    @Override
    public void showWeatherData(final WeatherBean weatherBean) {
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                if (weatherBean.getStatus() == 304) {
                    Toast.makeText(ActivityMainActivity.this, weatherBean.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    tvWeather.setText("城市：" + weatherBean.getCity()
                            + "日期：" + weatherBean.getDate()
                            + "温度：" + weatherBean.getData().getWendu());
                    tvWeatherYesterday.setText("昨日天气："
                            + weatherBean.getData().getYesterday().getLow() + " "
                            + weatherBean.getData().getYesterday().getHigh());
                }
            }
        });
    }


    @Override
    public void showLoadFailMsg(final Exception e) {
            runOnUiThread(new TimerTask() {
                @Override
                public void run() {
                    tvWeather.setText("加载数据失败：" + e.toString());
                }
            });
    }
}
