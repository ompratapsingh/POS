package com.mak;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;


import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mak.pos.Model.POJO.MenuCategory;
import com.mak.pos.Model.POJO.MenuItemInfo;
import com.mak.pos.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by iMobTree on 9/22/2017.
 */

public class Constant {


    private static Dialog npDialog;
    public static ArrayList<com.mak.pos.Model.POJO.MenuCategory> MenuCategory;
    public static ArrayList<MenuItemInfo> MenuCategoryItem;
    public static HashMap<MenuCategory, ArrayList<MenuItemInfo>> categoryItemhMap = new HashMap<>();
    public static HashMap<Integer, HashMap<MenuCategory, ArrayList<MenuItemInfo>>> tableCategoryItemhMap = new HashMap<>();

    public static void ShowAlertDialog(final Activity context, String title, String msg, final String btnstr) {


        if (npDialog != null && npDialog.isShowing()) {
            npDialog.dismiss();
        }
        npDialog = new Dialog(context, R.style.MyDialog);

        npDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        npDialog.setContentView(R.layout.dialog_alert);
        npDialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        npDialog.getWindow().setGravity(Gravity.CENTER);
        npDialog.setCancelable(false);
        npDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        npDialog.getWindow().setDimAmount(0.0f);


        final EditText edit_rename;

        npDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        npDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hideKeyboard((Activity) context);
            }
        });

        TextView tvalert_title = (TextView) npDialog.findViewById(R.id.tvalert_title);
        tvalert_title.setText(title);
        TextView tvalert_msg = (TextView) npDialog.findViewById(R.id.tvalert_msg);
        tvalert_msg.setText(msg);

        TextView tv_OK = (TextView) npDialog.findViewById(R.id.tv_OK);
        tv_OK.setText(btnstr);
        tv_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (MenuSceenAcitivity.GuestLoginDialog != null && MenuSceenAcitivity.GuestLoginDialog.isShowing()) {
                    MenuSceenAcitivity.GuestLoginDialog.dismiss();
                }*/
                if (npDialog != null && npDialog.isShowing())
                    npDialog.dismiss();

            }
        });

        npDialog.show();
    }

    public static void hideKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private static Dialog HuDDialog;

    public static void ShowProgressHud(Activity context, String Msg) {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        HuDDialog = new Dialog(context);
        HuDDialog.requestWindowFeature(1);
        HuDDialog.setContentView(R.layout.progress_hud);
        HuDDialog.getWindow().setLayout(-1, -2);

        HuDDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        HuDDialog.getWindow().setDimAmount(0.5f);
        HuDDialog.getWindow().setSoftInputMode(3);
        HuDDialog.setCancelable(false);
        ((TextView) HuDDialog.findViewById(R.id.message)).setText(Msg);
        /*GoogleProgressBar  google_progress=(GoogleProgressBar) HuDDialog.findViewById(R.id.google_progress);
        google_progress.setIndeterminateDrawable(new FoldingCirclesDrawable.Builder(context).colors(context.getResources().getIntArray(R.array.gplus_colors)).build());*/
        Dialog dialog = HuDDialog;
        if (dialog != null && !dialog.isShowing()) {
            HuDDialog.show();
        }
    }

    public static void cancelDialogue() {


        try {
            HuDDialog.cancel();
        } catch (Exception ee) {
        }
    }


    public static void showAlert(Context context, String title, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("");
        dialog.setCancelable(false);
        TextView declineButton = (TextView) dialog.findViewById(R.id.declineButton);
        ((TextView) dialog.findViewById(R.id.textDialog)).setText(title);
        dialog.show();
        ((TextView) dialog.findViewById(R.id.textMsg)).setText(message);
        declineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static void showToast(final Activity activity, String msg) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
