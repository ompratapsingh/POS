package com.mak.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mak.Api.ApiClient;
import com.mak.Api.ApiInterface;
import com.mak.App;
import com.mak.Constant;
import com.mak.pos.Model.POJO.UserModel;

import retrofit2.Callback;
import retrofit2.Response;

public class ServerSettingActivity extends AppCompatActivity {

    EditText input_url,input_port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_server);

        input_url=(EditText)findViewById(R.id.input_url);
        input_port=(EditText)findViewById(R.id.input_port);

        findViewById(R.id.btn_Done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_url.getText().toString().trim().length()==0||input_url.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please enter url",Toast.LENGTH_LONG).show();

                }else if(!input_url.getText().toString().trim().contains("http://"))
                {
                    Toast.makeText(getApplicationContext(),"Url start with http://",Toast.LENGTH_LONG).show();

                }
                        else if(input_port.getText().toString().trim().length()==0||input_port.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please enter port",Toast.LENGTH_LONG).show();
                }
                else
                {
                    App.getPrefs().setString("URL",input_url.getText().toString().trim());
                    App.getPrefs().setString("PORT",input_port.getText().toString().trim());

                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }


            }
        });
    }


}
