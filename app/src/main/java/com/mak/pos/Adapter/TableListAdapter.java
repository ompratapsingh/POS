package com.mak.pos.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mak.App;
import com.mak.pos.CartViewActivity;
import com.mak.pos.MenuItemsActivity;
import com.mak.pos.Model.POJO.Cart;
import com.mak.pos.Model.POJO.TableInfo;
import com.mak.pos.Model.TableModel;
import com.mak.pos.PartyInfoActivity;
import com.mak.pos.R;
import com.mak.pos.TableSelectActivity;
import com.mak.pos.Utility.Constant;

import java.util.ArrayList;

/**
 * Created by iMobTree Bharat on 16-Jan-18.
 */
public class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.ViewHolder> {


    Activity context;
    int txtTable;
    int txtAmount;
    int cellHeight;
    ArrayList<TableInfo> tableList;
    public TableListAdapter(Activity mainCtx, ArrayList<TableInfo> tableList) {
        cellHeight=App.getScreenWidth()/3;
        this.context=mainCtx;
        this.txtTable = cellHeight / 10;
        this.txtAmount = cellHeight / 20;
        this.tableList=tableList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_categories_items, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.getLayoutParams().height =cellHeight ;
        holder.itemView.getLayoutParams().width = cellHeight;
        int table=position+1;
        holder.tableInfo=tableList.get(position);
                if(holder.tableInfo!=null) {
                    holder.tvTable.setText(holder.tableInfo.getName());
                    if(holder.tableInfo!=null&&holder.tableInfo.getAmount()!=null&&!holder.tableInfo.getAmount().isEmpty())
                        if(holder.tableInfo.getAmount()!=null)
                    holder.tvAmount.setText(Constant.twoDigitValue(Float.parseFloat(holder.tableInfo.getAmount())));

                    holder.tvTable.setTextSize(txtTable);
                    holder.tvAmount.setTextSize(txtAmount);
                }

        //holder.tableModel=App.getCartValue(String.valueOf(holder.tableInfo.getCode()));


        if(holder.tableInfo!=null&&holder.tableInfo.getAvailablity()!=null)
        {
            if(holder.tableInfo.getAvailablity().toLowerCase().equals(Constant.TABLE_STATUS_OCCUPIED.toLowerCase()))
            {
                holder.tvTable.setTextColor(context.getResources().getColor(R.color.occupied_color));
                holder.tvAmount.setTextColor(context.getResources().getColor(R.color.occupied_color));
                if(App.getCartValue(String.valueOf(holder.tableInfo.getCode()))==null)
                {
                   // TableSelectActivity.getCart(String.valueOf(holder.tableInfo.getCode()));
                    Cart cart=new Cart();
                    cart.setStoreCode(holder.tableInfo.getLocation());
                    cart.setTableCode(holder.tableInfo.getCode());
                    App.StoreCartValue(cart);
                }
            }else if(holder.tableInfo.getAvailablity().toLowerCase().equals(Constant.TABLE_STATUS_SETTLEMENT_PENDING.toLowerCase()))
            {
                holder.tvTable.setTextColor(context.getResources().getColor(R.color.settlement_color));
                holder.tvAmount.setTextColor(context.getResources().getColor(R.color.settlement_color));
            }
            else
            {
                holder.tvTable.setTextColor(Color.WHITE);
                holder.tvAmount.setTextColor(Color.WHITE);

            }

        }else
        {
            holder.tvTable.setTextColor(Color.WHITE);
            holder.tvAmount.setTextColor(Color.WHITE);

        }
        holder.tvTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.performClick();
            }
        });holder.tvAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.performClick();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.CurrentTable=Integer.parseInt(holder.tableInfo.getCode());
                Constant.StoreCode=Integer.parseInt(holder.tableInfo.getLocation());
                if(holder.tableInfo.getAvailablity()!=null&&holder.tableInfo.getAvailablity()!=null)
                {
                    if(holder.tableInfo.getAvailablity().toLowerCase().equals(Constant.TABLE_STATUS_OCCUPIED.toLowerCase()))
                    {
                        context.startActivity(new Intent(context,MenuItemsActivity.class));
                    }else if(holder.tableInfo.getAvailablity().toLowerCase().equals(Constant.TABLE_STATUS_SETTLEMENT_PENDING.toLowerCase()))
                    {
                        context.startActivity(new Intent(context,CartViewActivity.class));
                    }
                    else
                    {
                        context.startActivity(new Intent(context,PartyInfoActivity.class));

                    }

                }else
                {
                    context.startActivity(new Intent(context,PartyInfoActivity.class));
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

@Override
public int getItemViewType(int position) {
    return position;
}





    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTable,tvAmount;
        TableInfo tableInfo;
        LinearLayout llMain;
        public ViewHolder(View itemView) {
            super(itemView);

            tvTable=(TextView)itemView.findViewById(R.id.tvTable);
            tvAmount=(TextView)itemView.findViewById(R.id.tvAmount);
            llMain=(LinearLayout) itemView.findViewById(R.id.llMain);



        }
    }
}
