package com.mak.pos.MenuAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter;
import com.mak.pos.CartItemSelectActivity;
import com.mak.pos.Model.POJO.MenuItemInfo;
import com.mak.pos.R;
import com.mak.pos.Utility.Constant;


import java.util.List;

/**
 * Created by apple on 11/7/16.
 */

public class AdapterSectionRecycler extends SectionRecyclerViewAdapter<SectionHeader, MenuItemInfo, SectionViewHolder, ChildViewHolder> {

    Context context;
    List<SectionHeader> sectionHeaderItemList1;
    public AdapterSectionRecycler(Context context, List<SectionHeader> sectionHeaderItemList) {
        super(context, sectionHeaderItemList);
        this.context = context;
        this.sectionHeaderItemList1=sectionHeaderItemList;
    }

    @Override
    public SectionViewHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.section_item, sectionViewGroup, false);
        return new SectionViewHolder(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, childViewGroup, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindSectionViewHolder(SectionViewHolder sectionViewHolder, int sectionPosition, SectionHeader sectionHeader) {


       if(sectionHeader.getSectionText().getDescription().equals("SearchFilter"))
        {
         sectionViewHolder.itemView.getLayoutParams().height=0;
       sectionViewHolder.itemView.getLayoutParams().width=0;
        }

        sectionViewHolder.name.setText( sectionHeader.getSectionText().getDescription());


    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int i, int i1, final MenuItemInfo menuItem) {
        childViewHolder.tvDishName.setText(menuItem.getItemMasterName());
        if(menuItem.getRate()!=-1)
        childViewHolder.tvDishPrice.setText("Rs " + Constant.twoDigitValue(menuItem.getRate()));
        childViewHolder.tvDishDesc.setText(menuItem.getAppGroup());
        if(menuItem.getDiscPercent()!=-1) {
            childViewHolder.tvDiscount.setText(Constant.twoDigitValue(menuItem.getDiscPercent()));
        }else
            childViewHolder.llOff.setVisibility(View.INVISIBLE);

        if(menuItem.getTaxPercentage()!=-1&&!menuItem.getTaxCode().isEmpty())
        childViewHolder.tvTax.setText(Constant.twoDigitValue(menuItem.getTaxPercentage()));
        else {
            childViewHolder.llTax.setVisibility(View.INVISIBLE);
        }
        childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    context.startActivity(new Intent(context, CartItemSelectActivity.class).putExtra(Constant.CART_ITEM,menuItem).putExtra(Constant.ACTION,Constant.INSERT));
            }
        });


    }





}
