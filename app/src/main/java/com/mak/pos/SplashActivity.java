package com.mak.pos;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mak.Api.ApiClient;
import com.mak.Api.ApiInterface;
import com.mak.App;
import com.mak.pos.Model.POJO.Cart;
import com.mak.pos.Utility.Constant;

import java.text.DecimalFormat;

import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    TextView tvTodaySeles, tvMonthSeles, tvYearSeles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findView();

        getReport();
    }

    private void findView() {

        tvTodaySeles = (TextView) findViewById(R.id.tvTodaySeles);
        tvMonthSeles = (TextView) findViewById(R.id.tvMonthSeles);
        tvYearSeles = (TextView) findViewById(R.id.tvYearSeles);

    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public void getReport() {
        com.mak.Constant.ShowProgressHud(SplashActivity.this, "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<JsonObject> call = apiInterface.oGetReport();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                com.mak.Constant.cancelDialogue();
                if (response != null && response.isSuccessful() && response.body() != null) {

                    String today = "Today's Sales - ";
                    String month = "Month Sales - ";
                    String yearly = "Yearly Sales - ";
                    if (!response.body().get("todaySale").isJsonNull())
                        today = today + response.body().get("todaySale").getAsString();
                    else
                        today = today + "0000";

                    if (!response.body().get("monthlySale").isJsonNull())
                        month = month + response.body().get("monthlySale").getAsString();
                    else
                        month = month + "0000";

                    if (!response.body().get("yearlySale").isJsonNull())
                        yearly = yearly + response.body().get("yearlySale").getAsString();
                    else
                        yearly = yearly + "0000";

                    tvTodaySeles.setText(today);
                    tvMonthSeles.setText(month);
                    tvYearSeles.setText(yearly);


                  startNextScreen();


                }else
                {
                    startNextScreen();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                com.mak.Constant.cancelDialogue();
                startNextScreen();
            }
        });
    }

    private void startNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(), TableSelectActivity.class));
                finish();
            }
        }, 3000);
    }

}
