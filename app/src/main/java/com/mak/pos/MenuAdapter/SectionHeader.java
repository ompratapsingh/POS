package com.mak.pos.MenuAdapter;

import com.intrusoft.sectionedrecyclerview.Section;
import com.mak.pos.Model.POJO.MenuCategory;
import com.mak.pos.Model.POJO.MenuItemInfo;


import java.util.List;

/**
 * Created by apple on 11/7/16.
 */

public class SectionHeader implements Section<MenuItemInfo>, Comparable<SectionHeader> {

    List<MenuItemInfo> childList;
    MenuCategory category;
    int index;

    public SectionHeader(List<MenuItemInfo> childList, MenuCategory category, int index) {
        this.childList = childList;
        this.category = category;
        this.index = index;
    }

    @Override
    public List<MenuItemInfo> getChildItems() {
        return childList;
    }

    public MenuCategory getSectionText() {
        return category;
    }

    @Override
    public int compareTo(SectionHeader another) {
        if (this.index > another.index) {
            return -1;
        } else {
            return 1;
        }
    }
}