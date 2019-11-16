package com.mak.pos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mak.App;
import com.mak.pos.Model.POJO.Cart;
import com.mak.pos.Model.POJO.Items;
import com.mak.pos.Model.POJO.MenuItemInfo;
import com.mak.pos.Model.TableModel;
import com.mak.pos.Utility.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CartItemSelectActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnCart;
    private Object menuItem;
    ImageView ivMinus,ivPlus;
    TextView tvQuantity,tvDishName,tvDishDesc;
int itemCount=1;
    private int action;
    private MenuItemInfo listMenuItem;
private Items cartItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item_select);
        findView();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
         menuItem=(Object) bundle.get(Constant.CART_ITEM);
         if(menuItem!=null&&menuItem instanceof MenuItemInfo)
         {
              listMenuItem = (MenuItemInfo) menuItem;
             tvDishName.setText(listMenuItem.getItemMasterName());
             tvDishDesc.setText(listMenuItem.getAppGroup());
         }
        if(menuItem!=null&&menuItem instanceof Items)
        {
            cartItem = (Items) menuItem;
        }

         action= bundle.getInt(Constant.ACTION);
        if(action==Constant.INSERT)
        {

        }else
        {
            btnCart.setText("Update Cart");
            itemCount=cartItem.getQty();
            tvQuantity.setText(String.valueOf(itemCount));
        }

    }

    private void findView() {
        btnCart=(Button)findViewById(R.id.btnCart);
        ivPlus=(ImageView)findViewById(R.id.ivPlus);
        ivMinus=(ImageView)findViewById(R.id.ivMinus);
        tvQuantity=(TextView) findViewById(R.id.tvQuantity);
        tvDishName=(TextView) findViewById(R.id.tvDishName);
        tvDishDesc=(TextView) findViewById(R.id.tvDishDesc);
        ivMinus.setOnClickListener(this);
        ivPlus.setOnClickListener(this);
        btnCart.setOnClickListener(this);
    }

    private void addToCart() {
        Cart cart = App.getCartValue(String.valueOf(Constant.CurrentTable));
        if (cart != null) {
            ArrayList<Items> cartlist = cart.getItems();
            boolean isExist = false;
            int itemPos = -1;
            Items currentItem = null;
            if (cartlist != null && cartlist.size() > 0) {

                String itemCode="",itemName="";
                if(listMenuItem!=null)
                {
                    itemCode=listMenuItem.getItemMasterCode();

                }
                else if(cartItem!=null)
                {
                    itemCode=currentItem.getCode();


                }

                    for (int i = 0; i < cartlist.size(); i++) {
                    if (cartlist.get(i) != null && cartlist.get(i).getCode().equals(itemCode)) {
                        itemPos = i;
                        currentItem = cartlist.get(i);

                    }
                }
            }

            if (cartlist == null) {
                cartlist = new ArrayList<>();
            }

            if (currentItem != null && itemPos != -1 && action == Constant.INSERT) {
                int currentQty = currentItem.getQty();
                currentQty = currentQty + itemCount;
                currentItem.setQty(currentQty);

                cartlist.set(itemPos, currentItem);
            } else {

                if (currentItem == null) {
                    Items items = new Items();
                    items.setCode(listMenuItem.getItemMasterCode());
                    items.setItem_name(listMenuItem.getItemMasterName());
                    items.setRate(listMenuItem.getRate());
                    items.setDisc(listMenuItem.getDiscPercent());
                    items.setDiscAmt(calclateTax(listMenuItem.getDiscPercent(),listMenuItem.getRate()));
                    items.setTaxCode(listMenuItem.getTaxCode());
                    items.setRemarks("");
                    items.setTaxamt(calclateTax(listMenuItem.getTaxPercentage(),listMenuItem.getRate()));
                    items.setAddtaxAmt(calclateTax(listMenuItem.getAddTax(),listMenuItem.getRate()));
                    items.setAddtaxAmt2(calclateTax(listMenuItem.getSurcharge(),listMenuItem.getRate()));
                    items.setQty(itemCount);
                    //items.setSno(listMenuItem.getStore_code());
                    cartlist.add(items);
                } else if (currentItem != null && itemPos != -1) {
                    currentItem.setQty(itemCount);
                    cartlist.set(itemPos, currentItem);
                }
                cart.setItems(cartlist);




            }
            if(cartlist!=null&&cartlist.size()>0)
            {
                TableSelectActivity.onTableStatusCart(cart.getTableCode(),Constant.API_TABLE_STATUS_OCCUPIED,CartItemSelectActivity.this);
            }
            App.StoreCartValue(cart);

            
            /*if(cartlist!=null)
            {
                if(cartMap.containsKey(menuItem.getItemMasterCode()))
                {
                    ArrayList<MenuItemInfo> itemList = cartMap.get(menuItem.getItemMasterCode());
                    if(action==Constant.INSERT) {
                        if (itemList == null)
                            itemList = new ArrayList<>();
                    }else
                    {
                        itemList = new ArrayList<>();
                    }

                    for (int i=0;i<itemCount;i++)
                    {
                        itemList.add(menuItem);
                    }
                    cartMap.put(menuItem.getItemMasterCode(),itemList);
                }else
                {
                    ArrayList<MenuItemInfo> items=new ArrayList<>();
                    for (int i=0;i<itemCount;i++)
                    {
                        items.add(menuItem);
                    }
                    cartMap.put(menuItem.getItemMasterCode(),items);

                }
                tableValue.setHashMap(cartMap);
            }else
            {
                HashMap<String,ArrayList<MenuItemInfo>> map=new HashMap<>();
                ArrayList<MenuItemInfo> items=new ArrayList<>();
                for (int i=0;i<itemCount;i++)
                {
                    items.add(menuItem);
                }
                map.put(menuItem.getItemMasterCode(),items);
                tableValue.setHashMap(map);
            }
            App.StoreTableValue(tableValue);
        }*/


//            onSaveCart();
        }
    }
/*
    private void addToCart() {
        TableModel tableValue=App.getTableValue(String.valueOf(Constant.CurrentTable));
        if(tableValue!=null)
        {
            HashMap<String, ArrayList<MenuItemInfo>> cartMap = tableValue.getHashMap();
            if(cartMap!=null)
            {
                if(cartMap.containsKey(menuItem.getItemMasterCode()))
                {
                    ArrayList<MenuItemInfo> itemList = cartMap.get(menuItem.getItemMasterCode());
                    if(action==Constant.INSERT) {
                        if (itemList == null)
                            itemList = new ArrayList<>();
                    }else
                    {
                        itemList = new ArrayList<>();
                    }

                    for (int i=0;i<itemCount;i++)
                    {
                        itemList.add(menuItem);
                    }
                    cartMap.put(menuItem.getItemMasterCode(),itemList);
                }else
                {
                    ArrayList<MenuItemInfo> items=new ArrayList<>();
                    for (int i=0;i<itemCount;i++)
                    {
                        items.add(menuItem);
                    }
                    cartMap.put(menuItem.getItemMasterCode(),items);

                }
                tableValue.setHashMap(cartMap);
            }else
            {
                HashMap<String,ArrayList<MenuItemInfo>> map=new HashMap<>();
                ArrayList<MenuItemInfo> items=new ArrayList<>();
                for (int i=0;i<itemCount;i++)
                {
                    items.add(menuItem);
                }
                map.put(menuItem.getItemMasterCode(),items);
                tableValue.setHashMap(map);
            }
            App.StoreTableValue(tableValue);
        }


        onSaveCart();
    }
*/

    private float calclateTax(float percentage, float rate) {

        rate = rate * percentage;
        rate = rate / 100;
        return rate;
    }

    @Override
    public void onClick(View v) {
    switch (v.getId())
    {
        case R.id.btnCart:

                addToCart();
                finish();
            break;
        case R.id.ivMinus:
            if(itemCount>1)
            {
                itemCount--;
            }
            tvQuantity.setText(String.valueOf(itemCount));
            break;
        case R.id.ivPlus:
            itemCount++;
            tvQuantity.setText(String.valueOf(itemCount));
            break;

    }
    }
}
