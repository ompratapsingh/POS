package com.mak.pos.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mak.App;
import com.mak.pos.Adapter.PersonListAdapter;
import com.mak.pos.Model.POJO.MenuCategory;
import com.mak.pos.Model.POJO.Items;
import com.mak.pos.Model.POJO.MenuItemInfo;
import com.mak.pos.Model.TableModel;
import com.mak.pos.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by MAKsuD on 9/1/2019.
 */

public class Constant {

    //Table Status
    public static String TABLE_STATUS_VACANT = "A";
    public static String TABLE_STATUS_OCCUPIED = "Y";
    public static String TABLE_STATUS_SETTLEMENT_PENDING = "O";



    public static String API_TABLE_STATUS_VACANT = "TABLE_STATUS_AVAILABLE";
    public static String API_TABLE_STATUS_OCCUPIED = "TABLE_STATUS_OCCUPIED";
    public static String API_TABLE_STATUS_SETTLEMENT_PENDING = "TABLE_STATUS_SETTLEMENT_PENDING";


    public static String TABLE_CODE = "table_code";


    public static int CurrentTable;
    public static String CART_ITEM = "cart_item";
    public static String ACTION = "action";
    public static int INSERT = 1;
    public static int UPDATE = 2;
    public static ArrayList<MenuCategory> MenuCategory;
    public static String PARTY_INFO="party_info";
    public static int StoreCode;


    public static String twoDigitValue(float val)
    {
        String mm = String.format("%.2f", val);
        return   String.format("%.2f",val);
    }
    public static String twoDigitValues(int val)
    {
        double number2 = (int)(Math.round(val * 100))/100.0;
        String mm = String.valueOf(number2);
        return   String.valueOf(number2);
    }
    public static float getTableCartValue(String table) {

        TableModel currentTable = App.getTableValue(String.valueOf(table));
        ArrayList<MenuItemInfo> menuItems = new ArrayList<>();
        float total = 0;
        if (currentTable != null && currentTable.getHashMap() != null && currentTable.getHashMap().size() > 0) {
            HashMap<String, ArrayList<MenuItemInfo>> currentList = currentTable.getHashMap();
            Set<String> keys = currentList.keySet();
            for (String key : keys) {
                ArrayList<MenuItemInfo> totalItm = currentList.get(key);
                if (totalItm != null && totalItm.size() > 0) {
                    MenuItemInfo item = totalItm.get(0);
                    item.setItemQty(totalItm.size());
                    float perItemValue = item.getRate();
                    perItemValue = perItemValue * totalItm.size();
                    total = total + perItemValue;
                    menuItems.add(item);
                }

            }

        }
        return total;

    }
    public static void selectPersonDialog(Activity context)
    {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(false);
        // if button is clicked, close the custom dialog
        RecyclerView rvPerson=(RecyclerView)dialog.findViewById(R.id.rvPerson);
        rvPerson.setLayoutManager(new GridLayoutManager(context,3));

        rvPerson.setAdapter(new PersonListAdapter(dialog,context));


        dialog.show();

        }
    }
