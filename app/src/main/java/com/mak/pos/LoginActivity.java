package com.mak.pos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    EditText input_email,input_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*App.getPrefs().setString("URL","http://ec2-13-56-233-205.us-west-1.compute.amazonaws.com");
       App.getPrefs().setString("PORT","8080");*/
        if(App.getPrefs().getValue("URL")==null&&App.getPrefs().getValue("PORT")==null)
        {

            startActivity(new Intent(getApplicationContext(),ServerSettingActivity.class));
            finish();
        }
        else if(App.getCurrentUser()!=null&&App.getCurrentUser().getId()!=null)
        {
            startActivity(new Intent(getApplicationContext(),SplashActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        input_email=(EditText)findViewById(R.id.input_email);
        input_password=(EditText)findViewById(R.id.input_password);
       // input_email.setText("root");
        //input_password.setText("123");

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input_email.getText().toString().trim().length()==0||input_email.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please enter username",Toast.LENGTH_LONG).show();

                }else if(input_password.getText().toString().trim().length()==0||input_password.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please enter Password",Toast.LENGTH_LONG).show();
                }
                else
                {
                    onLoginAttempt();

                }
            }
        });
    }

    private void onLoginAttempt() {

Constant.ShowProgressHud(LoginActivity.this,"");
        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
        JsonObject userObj=new JsonObject();
        userObj.addProperty("id",input_email.getText().toString().trim());
        userObj.addProperty("password",input_password.getText().toString().trim());
        retrofit2.Call<UserModel> call = apiInterface.onLoginAttempt(userObj);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(retrofit2.Call<UserModel> call, Response<UserModel> response) {
                Constant.cancelDialogue();
                if(response!=null&&response.isSuccessful()&&response!=null)
                {

                    UserModel user=response.body();
                    App.StoreUser(user);
                    startActivity(new Intent(getApplicationContext(),SplashActivity.class));
                    finish();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Invalid Username Or Password",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<UserModel> call, Throwable t) {

                Constant.cancelDialogue();
            }
        });

    }

}
