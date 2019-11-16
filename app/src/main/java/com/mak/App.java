package com.mak;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.mak.pos.Model.POJO.Cart;
import com.mak.pos.Model.TableModel;
import com.mak.pos.Model.POJO.UserModel;
import com.mak.pos.Utility.PreferencesManager;

/**
 * Created by MAKsuD on 8/26/2019.
 */

public class App extends Application {
    public  static  Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        MultiDex.install(this);
        Fresco.initialize(this);
        PreferencesManager.initializeInstance(context);

    }
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
public static Context getContext()
{
    return context;
}
    public static PreferencesManager getPrefs() {
        return PreferencesManager.getInstance();
    }

public static  void StoreTableValue(TableModel infoTable)
{
    if(infoTable!=null) {
         String tblInfo=null;
        Gson gson = new Gson();
        try {
           tblInfo = gson.toJson(infoTable);
        }catch (Exception e)
        {

        }

        if(tblInfo!=null&&!tblInfo.isEmpty())
        {
            App.getPrefs().setString(infoTable.getTable_no(), tblInfo);
        }


    }
}

    public static TableModel getTableValue(String key)
    {
        TableModel  model=null;
        String tableValue=App.getPrefs().getValue(key);
        if(tableValue!=null&&!tableValue.isEmpty()) {
            Gson gson = new Gson();
             model = gson.fromJson(tableValue, TableModel.class);
        }

       return model;
    }

    public static  void StoreCartValue(Cart infoTable) {
        if (infoTable != null) {
            String tblInfo = null;
            Gson gson = new Gson();
            try {
                tblInfo = gson.toJson(infoTable);
            } catch (Exception e) {

            }

            if (tblInfo != null && !tblInfo.isEmpty()) {
                App.getPrefs().setString(infoTable.getTableCode(), tblInfo);
            }


        }
    }
    public static Cart getCartValue(String key)
    {
        Cart  model=null;
        String cartValue=App.getPrefs().getValue(key);
        if(cartValue!=null&&!cartValue.isEmpty()) {
            Gson gson = new Gson();
            model = gson.fromJson(cartValue, Cart.class);
        }

        return model;
    }
    public static UserModel getCurrentUser()
    {
        UserModel  uModel=null;
        String userInfo=App.getPrefs().getValue(UserModel.class.getSimpleName());
        if(userInfo!=null&&!userInfo.isEmpty()) {
            Gson gson = new Gson();
            uModel = gson.fromJson(userInfo, UserModel.class);
        }
        return uModel;
    }


    public static void StoreUser(UserModel user) {
        if(user!=null) {
            String userInfo=null;
            Gson gson = new Gson();
            try {
                userInfo = gson.toJson(user);
            }catch (Exception e)
            {

            }

            if(userInfo!=null&&!userInfo.isEmpty())
            {
                App.getPrefs().setString(UserModel.class.getSimpleName(),userInfo);
            }


        }
    }

}
