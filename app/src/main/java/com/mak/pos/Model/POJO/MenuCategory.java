package com.mak.pos.Model.POJO;

import com.mak.pos.Model.POJO.MenuItemInfo;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuCategory implements Serializable
{
    private String code;

    private String description;

    private String colorCode;

    public ArrayList<MenuItemInfo> getMenuItemInfo() {
        return menuItemInfo;
    }

    public void setMenuItemInfo(ArrayList<MenuItemInfo> menuItemInfo) {
        this.menuItemInfo = menuItemInfo;
    }

    private ArrayList<MenuItemInfo> menuItemInfo;

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getColorCode ()
    {
        return colorCode;
    }

    public void setColorCode (String colorCode)
    {
        this.colorCode = colorCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [code = "+code+", description = "+description+", colorCode = "+colorCode+"]";
    }
}
