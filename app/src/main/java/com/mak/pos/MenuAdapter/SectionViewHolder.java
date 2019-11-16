package com.mak.pos.MenuAdapter;

import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.mak.pos.R;


/**
 * Created by shanky on 11/12/2016.
 */

public class SectionViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    View itemView;
    public SectionViewHolder(View itemView) {
        super(itemView);
        this.itemView=itemView;
         name = (TextView) itemView.findViewById(R.id.sectionHeader);


    }
}


