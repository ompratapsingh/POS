package com.mak.pos.Model;

import com.mak.pos.Model.POJO.MenuItemInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MAKsuD on 9/1/2019.
 */

public class TableModel implements Serializable {
    private String table_no;
    private String table_status;
    private PartyInfoModel partyInfo;
    private ArrayList<MenuItem> menuItems;

    public HashMap<String, ArrayList<MenuItemInfo>> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, ArrayList<MenuItemInfo>> hashMap) {
        this.hashMap = hashMap;
    }

    private HashMap<String,ArrayList<MenuItemInfo>> hashMap;
    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public String getTable_status() {
        return table_status;
    }

    public void setTable_status(String table_status) {
        this.table_status = table_status;
    }

    public PartyInfoModel getPartyInfo() {
        return partyInfo;
    }

    public void setPartyInfo(PartyInfoModel partyInfo) {
        this.partyInfo = partyInfo;
    }

    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
