package com.mak.pos.MenuAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mak.pos.R;


/**
 * Created by apple on 11/7/16.
 */

public class ChildViewHolder extends RecyclerView.ViewHolder {

    SimpleDraweeView ivDishImage;
    TextView tvDishName,tvDishPrice,tvDishDesc,tvTax,tvDiscount;
LinearLayout llOff,llTax;

    public ChildViewHolder(View itemView) {
        super(itemView);
        tvDishName = (TextView) itemView.findViewById(R.id.tvDishName);
        tvDishPrice = (TextView) itemView.findViewById(R.id.tvDishPrice);
        tvDishDesc = (TextView) itemView.findViewById(R.id.tvDishDesc);
        tvTax = (TextView) itemView.findViewById(R.id.tvTax);
        tvDiscount = (TextView) itemView.findViewById(R.id.tvDiscount);
        llOff = (LinearLayout) itemView.findViewById(R.id.llOff);
        llTax = (LinearLayout) itemView.findViewById(R.id.llTax);

        ivDishImage = (SimpleDraweeView) itemView.findViewById(R.id.ivDishImage);
    }
}
