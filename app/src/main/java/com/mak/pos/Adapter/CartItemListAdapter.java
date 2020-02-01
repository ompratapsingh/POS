package com.mak.pos.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mak.App;
import com.mak.pos.CartViewActivity;
import com.mak.pos.Model.POJO.Cart;
import com.mak.pos.Model.POJO.Items;
import com.mak.pos.R;
import com.mak.pos.Utility.Constant;

import java.util.ArrayList;

/**
 * Created by iMobTree Bharat on 16-Jan-18.
 */
public class CartItemListAdapter extends RecyclerView.Adapter<CartItemListAdapter.ViewHolder> {



    Activity context;
    int cellHeight;
    ArrayList<Items> menuItem;
    boolean isLocal;
    Cart serverCart;
    public CartItemListAdapter(Activity mainCtx, ArrayList<Items> menuItems, boolean isLocal, Cart serverCart) {
        cellHeight=App.getScreenWidth()/3;
        this.context=mainCtx;
        this.menuItem=menuItems;
        this.isLocal=isLocal;
        this.serverCart=serverCart;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.Item=menuItem.get(position);
        holder.tvItemName.setText(holder.Item.getItem_name());
        holder.tvItemPrice.setText(Constant.twoDigitValue(holder.Item.getRate()));
        if(isLocal) {
            holder.tvTaxAmt.setText(Constant.twoDigitValue(calclateTax(holder.Item.getTaxamt(),holder.Item.getRate(),holder.Item.getQty())));
            holder.tvTaxAmt2.setText(Constant.twoDigitValue(calclateTax(holder.Item.getAddtaxAmt(),holder.Item.getRate(),holder.Item.getQty())));
        }else{
            holder.tvTaxAmt.setText(Constant.twoDigitValue(holder.Item.getTaxamt()));
            holder.tvTaxAmt2.setText(Constant.twoDigitValue(holder.Item.getAddtaxAmt()));
        }
        holder.tvItemQty.setText(String.valueOf(holder.Item.getQty()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.Item!=null&&holder.Item.getQty()> 0) {
                    if (serverCart == null)
                        ((CartViewActivity) CartViewActivity.cartCtx).onUpdateItemDialog(holder.Item, isLocal);
                    else if (serverCart != null && serverCart.getTableStatus() == null)
                        ((CartViewActivity) CartViewActivity.cartCtx).onUpdateItemDialog(holder.Item, isLocal);
                    else if (serverCart != null && serverCart.getTableStatus() != null && !serverCart.getTableStatus().equals(Constant.TABLE_STATUS_SETTLEMENT_PENDING))
                        ((CartViewActivity) CartViewActivity.cartCtx).onUpdateItemDialog(holder.Item, isLocal);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return menuItem.size();
    }


    private float calclateTax(float percentage, float rate,int qty) {

        rate = rate * qty;
        rate = rate * percentage;
        rate = rate / 100;
        return rate;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        Items Item;
        TextView tvItemName,tvTaxAmt2,tvItemQty,tvItemPrice,tvTaxAmt;

        public ViewHolder(View itemView) {
            super(itemView);

            tvItemName=(TextView)itemView.findViewById(R.id.tvItemName);
            tvTaxAmt2=(TextView)itemView.findViewById(R.id.tvTaxAmt2);
            tvItemQty=(TextView) itemView.findViewById(R.id.tvItemQty);
            tvItemPrice=(TextView) itemView.findViewById(R.id.tvItemPrice);
            tvTaxAmt=(TextView) itemView.findViewById(R.id.tvTaxAmt);



        }
    }
}
