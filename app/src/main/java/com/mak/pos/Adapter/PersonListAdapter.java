package com.mak.pos.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.mak.pos.Model.TableModel;
import com.mak.pos.PartyInfoActivity;
import com.mak.pos.R;
import com.mak.pos.Utility.Constant;

/**
 * Created by iMobTree Bharat on 16-Jan-18.
 */
public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.ViewHolder> {

    Activity context;
    int txtTable;
    int txtAmount;
    int cellHeight;
    Dialog dialog;
    public PersonListAdapter(Dialog dialog, Activity mainCtx) {
        cellHeight=App.getScreenWidth()/5;
        this.context=mainCtx;
        this.dialog=dialog;
        this.txtTable = cellHeight / 10;
        this.txtAmount = cellHeight / 20;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_items, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.getLayoutParams().height =cellHeight ;
        holder.itemView.getLayoutParams().width = cellHeight;
        int table=position+1;
        if(position==getItemCount()-1)
        {
            holder.tvTable.setText("C");
        }else if(position==getItemCount()-2)
        {
            holder.tvTable.setText("0");
        }else
        {
            holder.tvTable.setText(String.valueOf(table));
        }

            holder.tvTable.setTextColor(Color.WHITE);

        holder.tvTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.performClick();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog!=null)
                    dialog.dismiss();
                PartyInfoActivity.tvPersonNumber.setText(holder.tvTable.getText().toString());

            }
        });
    }

    @Override
    public int getItemCount() {
        return 11;
    }





    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTable;
        LinearLayout llMain;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTable=(TextView)itemView.findViewById(R.id.tvTable);
            llMain=(LinearLayout) itemView.findViewById(R.id.llMain);
        }
    }
}
