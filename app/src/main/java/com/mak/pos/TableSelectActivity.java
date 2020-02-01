package com.mak.pos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mak.Api.ApiClient;
import com.mak.Api.ApiInterface;
import com.mak.App;
import com.mak.Constant;
import com.mak.pos.Adapter.TableListAdapter;
import com.mak.pos.Model.POJO.Cart;
import com.mak.pos.Model.POJO.TableInfo;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class TableSelectActivity extends AppCompatActivity {

    private static Activity ctxTable;
    RecyclerView rvTable;
    private ArrayList<TableInfo> tableList;
ImageView tvLogOut;
boolean isFirst=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctxTable=TableSelectActivity.this;
        setContentView(R.layout.activity_table_select);
        rvTable=(RecyclerView)findViewById(R.id.rvTable);
        tvLogOut=(ImageView) findViewById(R.id.tvLogOut);
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onLogOut();
            }
        });
        rvTable.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));



    }




    private void onGetTableInfo() {

                if(isFirst) {
                    isFirst=false;
                    Constant.ShowProgressHud(TableSelectActivity.this, "");
                }
            ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

            retrofit2.Call<ArrayList<TableInfo>> call = apiInterface.onGetTableInfo();
            call.enqueue(new Callback<ArrayList<TableInfo>>() {
                @Override
                public void onResponse(retrofit2.Call<ArrayList<TableInfo>> call, Response<ArrayList<TableInfo>> response) {
                    Log.e("DATA ","DATA");
                    Constant.cancelDialogue();
                    if(response!=null&&response.isSuccessful()&&response.body()!=null&&response.body().size()>0)
                    {
                        tableList=response.body();
                        setAdapter();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ArrayList<TableInfo>> call, Throwable t) {
                    Log.e("DATA ","DATA");
                    Constant.cancelDialogue();
                }
            });

        }

    private void setAdapter() {
        /*onGetTableInfo();*/
        if(tableList!=null)
        rvTable.setAdapter(new TableListAdapter(TableSelectActivity.this,tableList));
    }


    @Override
    protected void onResume() {
        super.onResume();
        onGetTableInfo();
    }


    private void onLogOut() {
        Constant.ShowProgressHud(TableSelectActivity.this,"");

        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<ResponseBody> call = apiInterface.onUserLogOut(App.getCurrentUser().getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                Constant.cancelDialogue();
                onLogOutCall();

            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Constant.cancelDialogue();
                //onLogOutCall();
            }
        });

    }

    private void onLogOutCall() {
        App.getPrefs().clear();
        App.StoreUser(null);
        if(Constant.tableCategoryItemhMap!=null)
        Constant.tableCategoryItemhMap.clear();
        if(Constant.MenuCategoryItem!=null)
        Constant.MenuCategoryItem.clear();
        if(Constant.categoryItemhMap!=null)
        Constant.categoryItemhMap.clear();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class) );
        finish();
    }


    public static void onTableStatusCart(String id, final String tableStatus, final Activity activity) {
        Constant.ShowProgressHud(activity,"");

        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<ResponseBody> call = apiInterface.onTableStatusUpdate(id,tableStatus);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                Constant.cancelDialogue();
                if(activity instanceof CartViewActivity &&tableStatus.equals(com.mak.pos.Utility.Constant.API_TABLE_STATUS_VACANT))
                {
                    if(CartViewActivity.dialog!=null)
                    CartViewActivity.dialog.dismiss();
                    if(tableStatus.equals(com.mak.pos.Utility.Constant.API_TABLE_STATUS_VACANT))
                    {
                        activity.startActivity(new Intent(activity,TableSelectActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        activity.finish();
                    }

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Constant.cancelDialogue();
            }
        });
    }

    public static void onTableStatusCartAvalib(String id, final String tableStatus, final Activity activity) {
        Constant.ShowProgressHud(activity,"");

        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<ResponseBody> call = apiInterface.onTableStatusUpdate(id,tableStatus);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                Constant.cancelDialogue();
                if(activity instanceof CartViewActivity &&tableStatus.equals(com.mak.pos.Utility.Constant.API_TABLE_STATUS_VACANT))
                {
                    if(CartViewActivity.dialog!=null)
                        CartViewActivity.dialog.dismiss();


                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Constant.cancelDialogue();
            }
        });
    }


    public static void getCart(String tableId) {
        ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<Cart> call = apiInterface.onGetCart(tableId);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(retrofit2.Call<Cart> call, Response<Cart> response) {
if(response!=null&&response.isSuccessful()&&response.body()!=null)
{
    App.StoreCartValue(response.body());
}
            }

            @Override
            public void onFailure(retrofit2.Call<Cart> call, Throwable t) {

            }
        });
    }
}
