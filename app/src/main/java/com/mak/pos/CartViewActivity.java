package com.mak.pos;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.mak.Api.ApiClient;
import com.mak.Api.ApiInterface;
import com.mak.App;
import com.mak.pos.Adapter.CartItemListAdapter;
import com.mak.pos.Model.POJO.Cart;
import com.mak.pos.Model.POJO.Items;
import com.mak.pos.Utility.Constant;

import net.ralphpina.permissionsmanager.PermissionsManager;
import net.ralphpina.permissionsmanager.PermissionsResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

public class CartViewActivity extends AppCompatActivity implements View.OnClickListener {

    private static int itemCount;
    RecyclerView rvCart;
    float total = 0;
    TextView tvTotal, tvTax, tvNet, tvAddTax, tvSurCharge, tvSave, tvDiscount, tvGBill, tvSettlement, tvLocalCart, tvSevrverCart, tvRoundOff;
    private LinearLayout llPaymentMethod, llCash, llCard, llChq, llDebit;
    CardView cvCash, cvDebit, cvCard, cvCheq;
    EditText edtCashAmount, edtCCBank, edtCardAmount, edtCardNumber, edtChequeNumber, edtChequeAmount, edtChequeBank, edtDebitAmount, edtDebitorName, edtCashRecivedAmount, edtCashRefundAmount, edtDebitRecivedAmount, edtDebitRefundAmount, edtChequeRecivedAmount,
            edtChequeRefundAmount, edtCardRefundAmount, edtCardRecivedAmount;
    private Button btnDone;
    private ImageView ivBack;
    private LinearLayout[] llPaymentTypes;
    private EditText[] edtAll, edtAmount, edtDisable;
    public static Dialog dialog, onViewCartDialog, updateItemdialog, specialDiscdialog;
    ArrayList<Items> allItems;
    private Cart serverCart;
    private Cart genrateBillCart;
    public static CartViewActivity cartCtx;
    private boolean isLocalCartView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);
        cartCtx = CartViewActivity.this;
        findView();


        getCart(String.valueOf(Constant.CurrentTable));
        Cart cartValue = App.getCartValue(String.valueOf(Constant.CurrentTable));
        if (cartValue != null && cartValue.getBillSrl() != null) {
            tvGBill.setTextColor(Color.GRAY);
        }
        tvLocalCart.performClick();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

/*    private void onCartSet() {
        float total = 0, discAmount = 0, taxAmount = 0, addTax = 0, surCharge = 0;
        TableModel currentTable = App.getTableValue(String.valueOf(Constant.CurrentTable));
        ArrayList<MenuItemInfo> menuItems = new ArrayList<>();

        if (currentTable != null && currentTable.getHashMap() != null && currentTable.getHashMap().size() > 0) {
            HashMap<String, ArrayList<MenuItemInfo>> currentList = currentTable.getHashMap();
            Set<String> keys = currentList.keySet();
            for (String key : keys) {
                ArrayList<MenuItemInfo> totalItm = currentList.get(key);
                if (totalItm != null && totalItm.size() > 0) {
                    MenuItemInfo item = totalItm.get(0);
                    item.setItemQty(totalItm.size());
                    float perItemValue = Float.parseFloat(String.valueOf(item.getRate()));
                    float discAmt = item.getDiscPercent();
                    float taxAmt = 0, addtax = 0, subchrg = 0;

                    perItemValue = perItemValue * totalItm.size();
                    total = total + perItemValue;
                    discAmt = discAmt * totalItm.size();
                    discAmount = discAmount + discAmt;
                    taxAmt = calclateTax(item.getTaxPercentage(), item.getRate()) * totalItm.size();
                    ;
                    taxAmount = taxAmount + taxAmt;

                    addtax = calclateTax(item.getAddTax(), item.getRate()) * totalItm.size();
                    addTax = addTax + addtax;

                    subchrg = calclateTax(item.getSurcharge(), item.getRate()) * totalItm.size();
                    surCharge = surCharge + subchrg;

                    menuItems.add(item);
                }

            }


            float totalCal = (total + surCharge + addTax + taxAmount) - discAmount;
            tvTotal.setText(Constant.twoDigitValue(totalCal));
            tvSurCharge.setText("+" + Constant.twoDigitValue(surCharge));
            tvAddTax.setText("+" + Constant.twoDigitValue(addTax));
            tvDiscount.setText("-" + Constant.twoDigitValue(discAmount));
            tvNet.setText(Constant.twoDigitValue(total));
            tvTax.setText("+" + Constant.twoDigitValue(taxAmount));
            rvCart.setAdapter(new CartItemListAdapter(CartViewActivity.this, menuItems));

        }

    }*/

    private void onCartSet() {
        float total = 0, discAmount = 0, taxAmount = 0, addTax = 0, surCharge = 0;
        Cart currentTable = App.getCartValue(String.valueOf(Constant.CurrentTable));

        if (currentTable != null && currentTable.getItems() != null && currentTable.getItems().size() > 0) {

            ArrayList<Items> allItems = currentTable.getItems();

            for (Items item : allItems) {
                if (item != null) {
                    float perItemValue = Float.parseFloat(String.valueOf(item.getRate()));
                    float discAmt = item.getDiscAmt();
                    float taxAmt = 0, addtax = 0, subchrg = 0;

                    perItemValue = perItemValue * item.getQty();
                    total = total + perItemValue;
                    discAmt = discAmt * item.getQty();
                    discAmount = discAmount + discAmt;
                    taxAmt = calclateTax(item.getTaxamt(), item.getRate(), item.getQty());

                    taxAmount = taxAmount + taxAmt;

                    addtax = calclateTax(item.getAddtaxAmt(), item.getRate(), item.getQty());
                    addTax = addTax + addtax;

                    subchrg = calclateTax(item.getAddtaxAmt2(), item.getRate(), item.getQty());
                    surCharge = surCharge + subchrg;
                    //menuItems.add(item);
                }

            }

/*

            float totalCal = (total + surCharge + addTax + taxAmount) - discAmount;
            tvTotal.setText(Constant.twoDigitValue(totalCal));
            tvSurCharge.setText("+" + Constant.twoDigitValue(surCharge));
            tvAddTax.setText("+" + Constant.twoDigitValue(addTax));
            tvDiscount.setText("-" + Constant.twoDigitValue(discAmount));
            tvNet.setText(Constant.twoDigitValue(total));
            tvTax.setText("+" + Constant.twoDigitValue(taxAmount));
            rvCart.setAdapter(new CartItemListAdapter(CartViewActivity.this, allItems));
*/


        }

    }

    private float calclateTax(float percentage, float rate, int qty) {

        rate = rate * qty;
        rate = rate * percentage;
        rate = rate / 100;
        return rate;
    }

    private void findView() {
        rvCart = (RecyclerView) findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvRoundOff = (TextView) findViewById(R.id.tvRoundOff);
        tvTax = (TextView) findViewById(R.id.tvTax);
        tvNet = (TextView) findViewById(R.id.tvNet);
        tvAddTax = (TextView) findViewById(R.id.tvAddTax);
        tvSurCharge = (TextView) findViewById(R.id.tvSurCharge);
        tvDiscount = (TextView) findViewById(R.id.tvDiscount);

        tvLocalCart = (TextView) findViewById(R.id.tvLocalCart);
        tvSevrverCart = (TextView) findViewById(R.id.tvSevrverCart);

        tvSave = (TextView) findViewById(R.id.tvSave);
        tvGBill = (TextView) findViewById(R.id.tvGBill);
        tvSettlement = (TextView) findViewById(R.id.tvSettlement);
        tvSave.setOnClickListener(this);
        tvLocalCart.setOnClickListener(this);
        tvSevrverCart.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvGBill:
                //setTblStatus(Constant.API_TABLE_STATUS_SETTLEMENT_PENDING);
                askStoragePermission();


                //TableSelectActivity.onTableStatusCart(cart.getTableCode(),Constant.API_TABLE_STATUS_SETTLEMENT_PENDING);
                break;

            case R.id.tvSave:

                /*TableModel tblInfo = App.getTableValue(String.valueOf(Constant.CurrentTable));
                tblInfo.setTable_status(Constant.TABLE_STATUS_SETTLEMENT_PENDING);
                App.StoreTableValue(tblInfo);*/
                onSaveCart();

                break;

            case R.id.tvSettlement:

                tvSettlement.setClickable(false);
                tvSettlement.setEnabled(false);
                //TableSelectActivity.onTableStatusCart(cart.getTableCode(),Constant.API_TABLE_STATUS_VACANT);
                /*Toast.makeText(getApplicationContext(), "Settlement Successfully", Toast.LENGTH_LONG).show();
                App.getPrefs().remove(String.valueOf(Constant.CurrentTable));
                startActivity(new Intent(getApplicationContext(), TableSelectActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();*/
                settlementDialog();


                break;

            case R.id.cvCash:
                edtCashAmount.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                onPaymentMethodSet(llCash);
                break;
            case R.id.cvCard:
                onPaymentMethodSet(llCard);
                break;
            case R.id.cvCheq:
                onPaymentMethodSet(llChq);
                break;
            case R.id.cvDebit:
                onPaymentMethodSet(llDebit);
                break;
            case R.id.ivBack:
                onPaymentMethodSet(null);
                break;
            case R.id.btnDone:
                onValidateSettlement();
                break;
            case R.id.tvLocalCart:
                isLocalCartView = true;
                tvSave.setVisibility(View.VISIBLE);
                tvGBill.setVisibility(View.GONE);
                tvSettlement.setVisibility(View.GONE);
                tvLocalCart.setTextColor(getResources().getColor(R.color.settlement_color));
                tvSevrverCart.setTextColor(getResources().getColor(R.color.white));
                onCartViewSet(true);
                break;
            case R.id.tvSevrverCart:
                isLocalCartView = false;
                getCart(String.valueOf(Constant.CurrentTable));
                tvSave.setVisibility(View.GONE);
                tvGBill.setVisibility(View.VISIBLE);
                tvSettlement.setVisibility(View.VISIBLE);
                onCartViewSet(false);
                tvSevrverCart.setTextColor(getResources().getColor(R.color.settlement_color));
                tvLocalCart.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    public void askStoragePermission() {
        if (PermissionsManager.get()
                .isStorageGranted()) {
            onBillSetup();

        } else if (PermissionsManager.get()
                .neverAskForStorage(CartViewActivity.this)) {
            com.mak.Constant.ShowPermissionSettings("Storage", CartViewActivity.this);

        } else {
            PermissionsManager.get()
                    .requestStoragePermission()
                    .subscribe(new Action1<PermissionsResult>() {
                        @Override
                        public void call(PermissionsResult permissionsResult) {

                            if (permissionsResult.isGranted()) {
                                //createPdf(FileUtils.getAppPath(getActivity()) + "1234.pdf");
                                onBillSetup();
                            }
                        }
                    });
        }
    }

    private void onBillSetup() {
        Cart cartValue = App.getCartValue(String.valueOf(Constant.CurrentTable));
        if (cartValue == null || cartValue.getBillSrl() == null) {

            tvGBill.setEnabled(false);
            tvGBill.setClickable(false);

            if (App.getCartValue(String.valueOf(Constant.CurrentTable)) == null || App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount() == null) {
                onSpecialDisscDialog();
            } else {
                onGenrateBill(Constant.CurrentTable, -1);
            }
        }
    }

    private void onCartViewSet(boolean isLocal) {
        ArrayList<Items> allItems = new ArrayList<>();
        if (!isLocal) {
            float total = 0, discAmount = 0, taxAmount = 0, addTax = 0, surCharge = 0;
            if (serverCart != null && serverCart.getItems() != null) {
                allItems = serverCart.getItems();


                if (allItems != null) {
                    for (Items items : allItems) {
                        if (items.getQty() > 0) {
                            if (items != null && items.getDiscAmt() != -1)
                                discAmount = discAmount + items.getDiscAmt();
                            if (items != null && items.getTaxamt() != -1)
                                taxAmount = taxAmount + items.getTaxamt();

                            if (items != null && items.getAddtaxAmt() != -1)
                                addTax = addTax + items.getAddtaxAmt();

                            if (items != null && items.getAddtaxAmt2() != -1)
                                surCharge = surCharge + items.getAddtaxAmt2();

                            if (items != null && items.getRate() != -1)
                                total = total + (items.getRate() * items.getQty());
                        }
                    }
                }

            }

            tvTotal.setText(Constant.twoDigitValue(total));

            if (serverCart != null && serverCart.getRoundoff() != null && !serverCart.getRoundoff().equals("")) {
                if (serverCart.getRoundoff().contains("-")) {
                    tvRoundOff.setText(Constant.twoDigitValue(Float.parseFloat(serverCart.getRoundoff())));
                } else {
                    tvRoundOff.setText("+" + Constant.twoDigitValue(Float.parseFloat(serverCart.getRoundoff())));
                }
            } else {
                tvRoundOff.setText("-0.00");
            }

            tvSurCharge.setText("+" + Constant.twoDigitValue(surCharge));
            tvAddTax.setText("+" + Constant.twoDigitValue(addTax));
            if (App.getCartValue(String.valueOf(Constant.CurrentTable)) != null && serverCart != null && serverCart.getTotalbillAmount() != -1 && App.getCartValue(String.valueOf(Constant.CurrentTable)) != null && App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount() != null && !App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount().equals("") && !App.getCartValue(String.valueOf(Constant.CurrentTable)).equals("null") && Integer.parseInt(App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount()) > 0) {
                int disc = serverCart.getTotalbillAmount() * Integer.parseInt(App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount());
                disc = disc / 100;

                tvDiscount.setText("-" + Constant.twoDigitValue(disc));
                tvNet.setText(String.valueOf(Math.round(serverCart.getTotalbillAmount() - disc)));

            } else {
                tvDiscount.setText("-" + Constant.twoDigitValue(discAmount));
                if (serverCart != null && serverCart.getTotalbillAmount() != -1)
                    tvNet.setText(String.valueOf(Math.round(serverCart.getTotalbillAmount())));
                else
                    tvNet.setText(String.valueOf(0));
            }


            tvTax.setText("+" + Constant.twoDigitValue(taxAmount));
            if (allItems != null)
                rvCart.setAdapter(new CartItemListAdapter(CartViewActivity.this, allItems, isLocal, serverCart));


        } else {

            float totalDiscAmt = 0, totalbillAmount = 0;

            float total = 0, taxAmt = 0, addtax = 0, subchrg = 0;
            Cart localCart = App.getCartValue(String.valueOf(Constant.CurrentTable));
            if (localCart != null && localCart.getItems() != null) {
                allItems = localCart.getItems();
                for (Items item : allItems) {
                    totalDiscAmt = totalDiscAmt + calclateTax(item.getDisc(), item.getRate(), item.getQty());
                    totalbillAmount = totalbillAmount + (item.getRate() * item.getQty());
                    taxAmt = taxAmt + calclateTax(item.getTaxamt(), item.getRate(), item.getQty());
                    addtax = addtax + calclateTax(item.getAddtaxAmt(), item.getRate(), item.getQty());
                    subchrg = subchrg + calclateTax(item.getAddtaxAmt2(), item.getRate(), item.getQty());
/*
                    totalbillAmount = totalbillAmount + (item.getRate() * item.getQty());
                    taxAmt = taxAmt + (item.getTaxamt() * item.getQty());
                    addtax = addtax + (item.getAddtaxAmt() * item.getQty());
                    subchrg = subchrg + (item.getAddtaxAmt2() * item.getQty(WWWWWW));*/
                }


            }

            taxAmt = Float.parseFloat(String.format("%.2f", taxAmt));
            addtax = Float.parseFloat(String.format("%.2f", addtax));
            float finalBillAmount = (totalbillAmount - totalDiscAmt) + taxAmt + addtax + subchrg;

            String roundOff = getRoundOFF(finalBillAmount);

            if (roundOff.contains("-")) {
                tvRoundOff.setText(roundOff);
            } else {
                tvRoundOff.setText("+" + roundOff);
            }

            tvTotal.setText(Constant.twoDigitValue(totalbillAmount));
            tvSurCharge.setText("+" + Constant.twoDigitValue(subchrg));
            tvAddTax.setText("+" + Constant.twoDigitValue(addtax));

            if (App.getCartValue(String.valueOf(Constant.CurrentTable)) != null && finalBillAmount > 0 && App.getCartValue(String.valueOf(Constant.CurrentTable)) != null && App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount() != null && !App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount().equals("") && !App.getCartValue(String.valueOf(Constant.CurrentTable)).equals("null") && Integer.parseInt(App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount()) > 0) {
                int disc = (int) finalBillAmount * Integer.parseInt(App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount());
                disc = disc / 100;
                tvDiscount.setText("-" + Constant.twoDigitValue(disc));
                tvNet.setText(String.valueOf(Math.round(finalBillAmount - disc)));

            } else {
                tvDiscount.setText("-" + Constant.twoDigitValue(totalDiscAmt));
                tvNet.setText(String.valueOf(Math.round(finalBillAmount)));
            }
            // tvNet.setText(String.valueOf((int) finalBillAmount));
            tvTax.setText("+" + Constant.twoDigitValue(taxAmt));
            if (allItems != null)
                rvCart.setAdapter(new CartItemListAdapter(CartViewActivity.this, allItems, isLocal, serverCart));

        }

    }


    private String getRoundOFF(float value) {
        Log.e("Floating value", String.valueOf(value));
        return String.format(Locale.US, "%.2f", Double.valueOf((Math.round(value))).floatValue() - value);
    }

    private void onValidateSettlement() {
        if (llCash.getVisibility() == View.VISIBLE) {
            onCashPayment();
        } else if (llCard.getVisibility() == View.VISIBLE) {
            onCardPayment();
        } else if (llChq.getVisibility() == View.VISIBLE) {
            onChqPayment();

        } else if (llDebit.getVisibility() == View.VISIBLE) {
            onDebitPayment();
        }
    }

    private void onDebitPayment() {
        if (edtDebitAmount.getText().toString().length() == 0 || edtDebitAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter valid amount", Toast.LENGTH_LONG).show();
        } else if (edtDebitorName.getText().toString().length() == 0 || edtDebitorName.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter debitor name", Toast.LENGTH_LONG).show();
        } else if (edtDebitRecivedAmount.getText().toString().length() == 0 || edtDebitRecivedAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter recived amount", Toast.LENGTH_LONG).show();
        } else {
            onPaymentSettlement(edtDebitAmount, edtDebitRefundAmount);
            // setTblStatus(Constant.API_TABLE_STATUS_VACANT);
        }
    }

    private void onChqPayment() {
        if (edtChequeNumber.getText().toString().length() == 0 || edtChequeNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter valid Cheque Number ", Toast.LENGTH_LONG).show();
        } else if (edtChequeAmount.getText().toString().length() == 0 || edtChequeAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter valid amount", Toast.LENGTH_LONG).show();
        } else if (edtChequeBank.getText().toString().length() == 0 || edtChequeBank.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter bank name", Toast.LENGTH_LONG).show();
        } else if (edtChequeRecivedAmount.getText().toString().length() == 0 || edtChequeRecivedAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter recived amount", Toast.LENGTH_LONG).show();
        } else {
            onPaymentSettlement(edtChequeAmount, edtChequeRefundAmount);

        }

        //setTblStatus(Constant.API_TABLE_STATUS_VACANT);

    }

    private void onCardPayment() {
        if (edtCardAmount.getText().toString().length() == 0 || edtCardAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter valid amount", Toast.LENGTH_LONG).show();
        } else if (edtCardRecivedAmount.getText().toString().length() == 0 || edtCardRecivedAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter recived amount", Toast.LENGTH_LONG).show();
        } else {
            onPaymentSettlement(edtCardAmount, edtCardRecivedAmount);
            // setTblStatus(Constant.API_TABLE_STATUS_VACANT);
        }
    }

    private void onCashPayment() {
        if (edtCashAmount.getText().toString().length() == 0 || edtCashAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter valid amount", Toast.LENGTH_LONG).show();
        } else if (edtCashRecivedAmount.getText().toString().length() == 0 || edtCashRecivedAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter recived amount", Toast.LENGTH_LONG).show();
        } else {
            onPaymentSettlement(edtCashAmount, edtCashRefundAmount);
            // setTblStatus(Constant.API_TABLE_STATUS_VACANT);
        }
    }

    private void onPaymentMethodSet(LinearLayout llType) {
        int isControlShow = View.GONE;
        int isPayMentMethodShow = View.VISIBLE;

        if (llType != null) {
            isControlShow = View.VISIBLE;
            isPayMentMethodShow = View.GONE;
        }
        ivBack.setVisibility(isControlShow);
        btnDone.setVisibility(isControlShow);
        llPaymentMethod.setVisibility(isPayMentMethodShow);
        for (LinearLayout layout : llPaymentTypes) {
            layout.setVisibility(View.GONE);
        }
        for (EditText editText : edtAll) {
            editText.setText("");
        }
        for (EditText editText : edtAmount) {
            if (genrateBillCart != null && genrateBillCart.getTotalbillAmountWithDsic() != -1)
                editText.setText(String.valueOf((int) genrateBillCart.getTotalbillAmountWithDsic()));
            else if (serverCart != null && serverCart.getTotalbillAmountWithDsic() != -1)
                editText.setText(String.valueOf((int) serverCart.getTotalbillAmountWithDsic()));

            editText.setClickable(false);
            editText.setEnabled(false);
        }
        for (EditText editText : edtDisable) {
            editText.setClickable(false);
            editText.setEnabled(false);
        }
        if (llType != null)
            llType.setVisibility(View.VISIBLE);
    }

    protected void setTblStatus(String type) {
        Cart currentTable = App.getCartValue(String.valueOf(Constant.CurrentTable));
        if (currentTable != null)
            TableSelectActivity.onTableStatusCart(currentTable.getTableCode(), type, CartViewActivity.this);
    }

    private void onSaveCart() {
        Cart cartValue = App.getCartValue(String.valueOf(Constant.CurrentTable));
        if (cartValue != null && cartValue.getItems() != null && cartValue.getItems().size() > 0) {
            setTblStatus(Constant.API_TABLE_STATUS_OCCUPIED);
            JsonObject tableCart = new JsonObject();
            if (App.getCurrentUser().getBranch() != null)
                tableCart.addProperty("branch", App.getCurrentUser().getBranch());
            tableCart.addProperty("noofPrints", "1");
            if (cartValue.getPaxNo() != null)
                tableCart.addProperty("paxNo", cartValue.getPaxNo());
            if (cartValue.getTableCode() != null)
                tableCart.addProperty("tableCode", cartValue.getTableCode());
            if (App.getCurrentUser().getHwserial() != null)
                tableCart.addProperty("Hwserial", App.getCurrentUser().getHwserial());
            if (cartValue.getCaptain() != null)
                tableCart.addProperty("captain", cartValue.getCaptain());
            if (cartValue.getStoreCode() != null)
                tableCart.addProperty("storeCode", cartValue.getStoreCode());
            else if (Constant.StoreCode != -1)
                tableCart.addProperty("storeCode", Constant.StoreCode);

            /*if (App.getCurrentUser().getId() != null)
                tableCart.addProperty("enteredBy", App.getCurrentUser().getId());*/
            if (cartValue.getEnteredBy() != null)
                tableCart.addProperty("enteredBy", cartValue.getEnteredBy());

            //tableCart.addProperty("totalDiscAmt",cartValue.getTotalDiscAmt());
            //tableCart.addProperty("roundoff",tableValue.getPartyInfo().getSalesmen_name());
            tableCart.addProperty("partyEmail", "");
            if (cartValue.getPartyContact() != null)
                tableCart.addProperty("partyContact", cartValue.getPartyContact());
            if (cartValue.getPartyAddr() != null)
                tableCart.addProperty("partyAddr", cartValue.getPartyAddr());
            if (cartValue.getPartyName() != null)
                tableCart.addProperty("partyName", cartValue.getPartyName());
            //tableCart.addProperty("totalbillAmount",cartValue.getTotalbillAmount());
            tableCart.addProperty("Prefix", "");
            JsonArray itemsArray = new JsonArray();
        /*"sno": "1",
                "code": "item_code",
                "Qty": "123",
                "rate": "123",
                "disc": "123",
                "discAmt": "123",
                "taxCode": "123",
                "remarks": "123",
                "taxamt": "123",
                "addtaxAmt": "123",
                "addtaxAmt2": "123"
*/

      /*  HashMap<String, ArrayList<MenuItemInfo>> allItems = tableValue.getHashMap();
        Set<String> allItemKeys = tableValue.getHashMap().keySet();

        for(String key:allItemKeys)
        {
            ArrayList<MenuItemInfo> itemList = allItems.get(key);
            MenuItemInfo item = itemList.get(0);
            JsonObject itemObject=new JsonObject();
            itemObject.addProperty("sno",item.getItemMasterCode());
            itemObject.addProperty("code",item.getCode());
            itemObject.addProperty("Qty",itemList.size());

                itemObject.addProperty("rate",item.getRate());
            itemObject.addProperty("disc","");
            itemObject.addProperty("discAmt",item.getDiscPercent());
            itemObject.addProperty("taxCode",item.getTaxCode());
            itemObject.addProperty("remarks","");


            itemObject.addProperty("taxamt","");
            itemObject.addProperty("addtaxAmt","");
            itemObject.addProperty("addtaxAmt2","");

            itemsArray.add(itemObject);
        }
tableCart.add("items",itemsArray);
*/
        /*


        "branch": "Paldi",
                "noofPrints": 1,
                "paxNo": "10",
                "tableCode": "123",
                "captain": "456",
                "Hwserial": "HGIWG",
                "storeCode": "02010",
                "enteredBy": "Ompratap",
                "totalDiscAmt": "125",
                "roundoff": "0.95",
                "partyEmail": "abhishek@gmail.com",
                "partyContact": "9173733269",
                "partyAddr": "Vastral",
                "totalbillAmount": "1250",
                "partyName": "Ashishek",
                "Prefix": "",*/

            ArrayList<Items> allItems = cartValue.getItems();
            float totalDiscAmt = 0, totalbillAmount = 0;

            float taxAmt = 0, addtax = 0, subchrg = 0;
            for (int i = 0; i < allItems.size(); i++) {
                Items item = allItems.get(i);
                JsonObject itemObject = new JsonObject();
                itemObject.addProperty("sno", i + 1);
                itemObject.addProperty("code", item.getCode());
                itemObject.addProperty("Qty", item.getQty());
                itemObject.addProperty("rate", (int) item.getRate());
                itemObject.addProperty("disc", calclateTax(item.getDisc(), item.getRate(), item.getQty()));
                itemObject.addProperty("discAmt", item.getDiscAmt() * item.getQty());
                itemObject.addProperty("taxCode", item.getTaxCode());
                itemObject.addProperty("remarks", "");
                itemObject.addProperty("taxamt", calclateTax(item.getTaxamt(), item.getRate(), item.getQty()));
                itemObject.addProperty("addtaxAmt", calclateTax(item.getAddtaxAmt(), item.getRate(), item.getQty()));
                itemObject.addProperty("addtaxAmt2", calclateTax(item.getAddtaxAmt2(), item.getRate(), item.getQty()));
                //tableCart.addProperty("storeCode",item.getStore());
                totalDiscAmt = totalDiscAmt + calclateTax(item.getDiscAmt(), item.getRate(), item.getQty());
                totalbillAmount = totalbillAmount + (item.getRate() * item.getQty());
                taxAmt = taxAmt + calclateTax(item.getTaxamt(), item.getRate(), item.getQty());
                addtax = addtax + calclateTax(item.getAddtaxAmt(), item.getRate(), item.getQty());
                subchrg = subchrg + calclateTax(item.getAddtaxAmt2(), item.getRate(), item.getQty());
                itemsArray.add(itemObject);
            }
            taxAmt = Float.parseFloat(String.format("%.2f", taxAmt));
            addtax = Float.parseFloat(String.format("%.2f", addtax));
            float finalBillAmount = (totalbillAmount - totalDiscAmt) + taxAmt + addtax + subchrg;
            tableCart.addProperty("totalDiscAmt", totalDiscAmt);
            tableCart.addProperty("totalbillAmount", Math.round(finalBillAmount));
            tableCart.addProperty("roundoff", getRoundOFF(finalBillAmount));

            /*if (roundOff.contains(".")) {
                String[] roundOffValue = roundOff.split("\\.");
                if (roundOffValue.length > 1) {
                    tableCart.addProperty("roundoff", getRoundOFF(Integer.parseInt(roundOffValue[1])));
                } else {
                    tableCart.addProperty("roundoff", "-0.00");
                }

            } else {

            }
*/
            tableCart.add("items", itemsArray);

            onStoreCart(tableCart, true);
        } else {
            Toast.makeText(getApplicationContext(), "No item for SaveKot", Toast.LENGTH_LONG).show();
        }
    }


    private void onUpdateCart(Items updatedItem, int itemCount) {

        if (serverCart != null) {

            JsonObject updateCart = new JsonObject();
            if (App.getCurrentUser().getBranch() != null)
                updateCart.addProperty("branch", App.getCurrentUser().getBranch());
            updateCart.addProperty("noofPrints", "1");
            if (serverCart.getPaxNo() != null)
                updateCart.addProperty("paxNo", serverCart.getPaxNo());
            if (serverCart.getTableCode() != null)
                updateCart.addProperty("tableCode", serverCart.getTableCode());
            if (App.getCurrentUser().getHwserial() != null)
                updateCart.addProperty("Hwserial", App.getCurrentUser().getHwserial());
            if (serverCart.getCaptain() != null)
                updateCart.addProperty("captain", serverCart.getCaptain());
            if (serverCart.getStoreCode() != null)
                updateCart.addProperty("storeCode", serverCart.getStoreCode());
            else if (Constant.StoreCode != -1)
                updateCart.addProperty("storeCode", Constant.StoreCode);

            if (App.getCurrentUser().getId() != null)
                updateCart.addProperty("enteredBy", App.getCurrentUser().getId());

            updateCart.addProperty("partyEmail", "");
            if (serverCart.getPartyContact() != null)
                updateCart.addProperty("partyContact", serverCart.getPartyContact());
            if (serverCart.getPartyAddr() != null)
                updateCart.addProperty("partyAddr", serverCart.getPartyAddr());
            if (serverCart.getPartyName() != null)
                updateCart.addProperty("partyName", serverCart.getPartyName());
            //tableCart.addProperty("totalbillAmount",cartValue.getTotalbillAmount());
            updateCart.addProperty("Prefix", "");

            //updateCart.addProperty("tableCode", serverCart.getTableCode());
            updateCart.addProperty("srl", serverCart.getSrl());


            JsonArray itemsArray = new JsonArray();


            //ArrayList<Items> allItems = cartValue.getItems();
            float totalDiscAmt = 0, totalbillAmount = 0;

            float taxAmt = 0, addtax = 0, subchrg = 0;
            /*for (int i = 0; i < allItems.size(); i++) {
                Items item = allItems.get(i);*/

            Items item = updatedItem;


            float disc = item.getDisc() / item.getQty();
            float discAmt = item.getDiscAmt() / item.getQty();
            float taxamt = item.getTaxamt() / item.getQty();
            float addtaxAmt = item.getAddtaxAmt() / item.getQty();
            float addtaxAmt2 = item.getAddtaxAmt2() / item.getQty();
            item.setQty(itemCount);
            JsonObject itemObject = new JsonObject();
            itemObject.addProperty("sno", item.getSno());
            itemObject.addProperty("code", item.getCode());
            itemObject.addProperty("Qty", "-" + String.valueOf(item.getQty()));
            itemObject.addProperty("rate", (int) item.getRate());
            itemObject.addProperty("disc", disc * item.getQty());
            itemObject.addProperty("discAmt", discAmt * item.getQty());
            itemObject.addProperty("taxCode", item.getTaxCode());
            itemObject.addProperty("remarks", "");
            itemObject.addProperty("taxamt", taxamt * item.getQty());
            itemObject.addProperty("addtaxAmt", addtaxAmt * item.getQty());
            itemObject.addProperty("addtaxAmt2", addtaxAmt2 * item.getQty());
            //itemObject.addProperty("storeCode",item.gets());
              /*  totalDiscAmt = totalDiscAmt + (item.getDisc() * item.getQty());
                totalbillAmount = totalbillAmount + (item.getRate() * item.getQty());
                taxAmt = taxAmt + (item.getTaxamt() * item.getQty());
                addtax = addtax + (item.getAddtaxAmt() * item.getQty());
                subchrg = subchrg + (item.getAddtaxAmt2() * item.getQty());*/
            itemsArray.add(itemObject);
            updateCart.add("items", itemsArray);


            updateCart.addProperty("totalDiscAmt", 0);
            updateCart.addProperty("totalbillAmount", 0);
            updateCart.addProperty("roundoff", "0");


            onStoreCart(updateCart, false);
        }
        //  float finalBillAmount = (totalbillAmount - totalDiscAmt) + taxAmt + addtax + subchrg;
           /* tableCart.addProperty("totalDiscAmt", 0);
            tableCart.addProperty("totalbillAmount", 0);
            String roundOff = String.valueOf(0);


            if (roundOff.contains(".")) {
                String[] roundOffValue = roundOff.split("\\.");

                if (roundOffValue.length > 1) {
                    tableCart.addProperty("roundoff", "." + roundOffValue[1]);

                } else {
                    tableCart.addProperty("roundoff", ".00");
                }

            } else {
                tableCart.addProperty("roundoff", ".00");
            }


            tableCart.add("items", itemsArray);*/

        //onSaveCart(tableCart);
       /* } else {
            Toast.makeText(getApplicationContext(), "No item for SaveKot", Toast.LENGTH_LONG).show();
        }*/
    }

    public void onStoreCart(JsonObject tableCart, final boolean isLocalCart) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<ResponseBody> call = apiInterface.onSaveCart(tableCart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    if (isLocalCart) {
                        Cart cartValue = App.getCartValue(String.valueOf(Constant.CurrentTable));
                        cartValue.setItems(null);
                        App.StoreCartValue(cartValue);
                        com.mak.Constant.showToast(CartViewActivity.this, "Saved Successfully");
                        // tvLocalCart.performClick();
                        startActivity(new Intent(CartViewActivity.this, TableSelectActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    } else {
                        com.mak.Constant.showToast(CartViewActivity.this, "Updated Successfully");

                        tvSevrverCart.performClick();
                    }





                  /*  startActivity(new Intent(getApplicationContext(), TableSelectActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();*/

                } else {
                    Toast.makeText(getApplicationContext(), "Something wrong, Please try again", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.e("DTA", "Data");
            }
        });
    }

    public void settlementDialog() {

        tvSettlement.setClickable(true);
        tvSettlement.setEnabled(true);
        // custom dialog
        dialog = new Dialog(CartViewActivity.this);
        dialog.setContentView(R.layout.settlement_dialog);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        onSetDialogView(dialog);

        // if button is clicked, close the custom dialog
        //cvCheck,cvCash,cvCard
        //llPaymentMethod,llCard,llCash,llChq
        //edtCCBank,edtCardAmount,edtCardNumber,edtChequeNumber,edtChequeNumber,edtChequeBank,edtDebitAmount

        dialog.show();

    }


    public void onSpecialDisscDialog() {

        specialDiscdialog = new Dialog(cartCtx);
        specialDiscdialog.setContentView(R.layout.special_disc_dialog);
        specialDiscdialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(specialDiscdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        specialDiscdialog.getWindow().setAttributes(lp);

        TextView tvTotal = (TextView) specialDiscdialog.findViewById(R.id.tvTotal);
        final TextView tvSpecDiscount = (TextView) specialDiscdialog.findViewById(R.id.tvSpecDiscount);
        final TextView tvPayAmount = (TextView) specialDiscdialog.findViewById(R.id.tvPayAmount);
        final EditText edtDiscount = (EditText) specialDiscdialog.findViewById(R.id.edtDiscount);

        Button btnDone = (Button) specialDiscdialog.findViewById(R.id.btnDone);
        Button btnCancel = (Button) specialDiscdialog.findViewById(R.id.btnCancel);

        if (serverCart != null) {
            if (serverCart.getTotalbillAmount() != -1) {
                tvTotal.setText(String.valueOf(serverCart.getTotalbillAmount()));
                tvPayAmount.setText(String.valueOf(serverCart.getTotalbillAmount()));
                tvSpecDiscount.setText("-0");

            }
        }

        edtDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("") && s.length() > 0) {

                    if (Integer.parseInt(s.toString()) >= 0 && Integer.parseInt(s.toString()) <= 100) {
                        if (serverCart != null && serverCart.getTotalbillAmount() != -1) {
                            int disc = serverCart.getTotalbillAmount() * Integer.parseInt(edtDiscount.getText().toString());
                            disc = disc / 100;

                            tvSpecDiscount.setText("-" + String.valueOf(disc));
                            tvPayAmount.setText(String.valueOf(serverCart.getTotalbillAmount() - disc));

                        }
                    } else {
                        tvSpecDiscount.setText("-0");
                        if (serverCart != null && serverCart.getTotalbillAmount() != -1) {
                            tvPayAmount.setText(String.valueOf(serverCart.getTotalbillAmount()));
                        }
                        Toast.makeText(getApplicationContext(), "Enter Percentage between 0 to 100", Toast.LENGTH_LONG).show();
                    }
                } else {
                    tvSpecDiscount.setText("-0");
                    if (serverCart != null && serverCart.getTotalbillAmount() != -1) {
                        tvPayAmount.setText(String.valueOf(serverCart.getTotalbillAmount()));
                    }
                    Toast.makeText(getApplicationContext(), "Enter Percentage between 0 to 100", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edtDiscount.getText().toString().length() > 0 && Integer.parseInt(edtDiscount.getText().toString()) >= 0 && Integer.parseInt(edtDiscount.getText().toString()) <= 100) {
                    specialDiscdialog.dismiss();
                    onGenrateBill(Constant.CurrentTable, Integer.parseInt(edtDiscount.getText().toString()));
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Percentage between 0 to 100", Toast.LENGTH_LONG).show();
                }


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                specialDiscdialog.dismiss();
                onGenrateBill(Constant.CurrentTable, 0);
                Cart cartValue = App.getCartValue(String.valueOf(Constant.CurrentTable));
                if (cartValue == null)
                    cartValue = new Cart();

                cartValue.setTableCode(String.valueOf(Constant.CurrentTable));
                cartValue.setSpecialDiscount(null);
                App.StoreCartValue(cartValue);

            }
        });

        specialDiscdialog.show();


    }

    public void onUpdateItemDialog(final Items selectedItem, final boolean isLocal) {
        itemCount = 0;

        updateItemdialog = new Dialog(cartCtx);
        updateItemdialog.setContentView(R.layout.update_item_dialog);
        updateItemdialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(updateItemdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        updateItemdialog.getWindow().setAttributes(lp);

        TextView tvItemName = (TextView) updateItemdialog.findViewById(R.id.tvItemName);
        final TextView tvQuantity = (TextView) updateItemdialog.findViewById(R.id.tvQuantity);
        ImageView ivPlus = (ImageView) updateItemdialog.findViewById(R.id.ivPlus);
        ImageView ivMinus = (ImageView) updateItemdialog.findViewById(R.id.ivMinus);
        Button btnUpadate = (Button) updateItemdialog.findViewById(R.id.btnUpadate);
        Button btnCancel = (Button) updateItemdialog.findViewById(R.id.btnCancel);
        itemCount = selectedItem.getQty();
        if (selectedItem.getItem_name() != null) {
            tvItemName.setText(selectedItem.getItem_name());
        }

        if (isLocal) {
            tvQuantity.setText(String.valueOf(itemCount));
        } else {
            tvQuantity.setText("-" + String.valueOf(itemCount));
        }

        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemCount > 0) {
                    itemCount--;
                }
                if (isLocal) {
                    tvQuantity.setText(String.valueOf(itemCount));
                } else {
                    tvQuantity.setText("-" + String.valueOf(itemCount));
                }

            }
        });
        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isLocal) {
                    if (itemCount < selectedItem.getQty()) {
                        itemCount++;
                    }
                } else {
                    itemCount++;
                }

                if (isLocal)
                    tvQuantity.setText(String.valueOf(itemCount));
                else
                    tvQuantity.setText("-" + String.valueOf(itemCount));

            }
        });

        btnUpadate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemdialog.dismiss();
                if (isLocal) {
                    Cart cart = App.getCartValue(String.valueOf(Constant.CurrentTable));
                    if (cart != null) {
                        ArrayList<Items> cartlist = cart.getItems();
                        ArrayList<Items> tempCart = new ArrayList<>();
                        if (cartlist != null) {
                            if (itemCount == 0) {
                                for (Items items : cartlist) {
                                    if (!items.getCode().equals(selectedItem.getCode())) {
                                        tempCart.add(items);
                                    }
                                }
                            } else {
                                for (Items items : cartlist) {
                                    if (items.getCode().equals(selectedItem.getCode())) {
                                        items.setQty(itemCount);
                                        tempCart.add(items);
                                    } else {
                                        tempCart.add(items);

                                    }
                                }
                            }
                        }

                        if (tempCart.size() == 0) {
                            tempCart = null;
                        }
                        cart.setItems(tempCart);
                        App.StoreCartValue(cart);
                        tvLocalCart.performClick();
                    }
                } else {

                    Items iupdatedItem = null;
                    if (serverCart != null && serverCart.getItems() != null) {
                        for (Items items : serverCart.getItems()) {
                            if (items.getCode().equals(selectedItem.getCode())) {
                                iupdatedItem = items;
                                //iupdatedItem.setQty();
                                break;
                            }
                        }
                        if (iupdatedItem != null) {
                            onUpdateCart(iupdatedItem, itemCount);
                        }
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemdialog.dismiss();
            }
        });

        updateItemdialog.show();


    }

    public void onViewCart() {


        // custom dialog
        onViewCartDialog = new Dialog(CartViewActivity.this);
        onViewCartDialog.setContentView(R.layout.settlement_dialog);
        onViewCartDialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(onViewCartDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        onViewCartDialog.getWindow().setAttributes(lp);
        // onSetDialogView(dialog);

        // if button is clicked, close the custom dialog
        //cvCheck,cvCash,cvCard
        //llPaymentMethod,llCard,llCash,llChq
        //edtCCBank,edtCardAmount,edtCardNumber,edtChequeNumber,edtChequeNumber,edtChequeBank,edtDebitAmount

        onViewCartDialog.show();

    }

    public void getCart(String tableId) {
        com.mak.Constant.ShowProgressHud(CartViewActivity.this, "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<Cart> call = apiInterface.onGetCart(tableId);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(retrofit2.Call<Cart> call, Response<Cart> response) {
                com.mak.Constant.cancelDialogue();
                if (response != null && response.isSuccessful() && response.body() != null) {
                    tvGBill.setOnClickListener(CartViewActivity.this);


                    serverCart = response.body();
                    if (App.getCartValue(String.valueOf(Constant.CurrentTable)) != null && serverCart.getTotalbillAmount() != -1 && App.getCartValue(String.valueOf(Constant.CurrentTable)) != null && App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount() != null && !App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount().equals("") && !App.getCartValue(String.valueOf(Constant.CurrentTable)).equals("null") && Integer.parseInt(App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount()) > 0) {
                        int disc = serverCart.getTotalbillAmount() * Integer.parseInt(App.getCartValue(String.valueOf(Constant.CurrentTable)).getSpecialDiscount());
                        disc = disc / 100;
                        serverCart.setTotalbillAmountWithDsic(serverCart.getTotalbillAmount() - disc);


                    } else {
                        serverCart.setTotalbillAmountWithDsic(serverCart.getTotalbillAmount());

                    }

                    Cart cartValue = App.getCartValue(String.valueOf(Constant.CurrentTable));
                    if (cartValue != null && cartValue.getBillSrl() != null && serverCart.getTableStatus() != null && serverCart.getTableStatus().equals(Constant.TABLE_STATUS_SETTLEMENT_PENDING)) {
                        tvSettlement.setOnClickListener(CartViewActivity.this);
                    }
                    //  onCartSet();
                    if (!isLocalCartView)
                        onCartViewSet(false);


                }
            }

            @Override
            public void onFailure(retrofit2.Call<Cart> call, Throwable t) {
                com.mak.Constant.cancelDialogue();
            }
        });
    }

    public void onGenrateBill(int tableId, final int specialDisc) {
        com.mak.Constant.ShowProgressHud(CartViewActivity.this, "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        JsonObject object = new JsonObject();
        object.addProperty("tableCode", tableId);
      /*  if (serverCart != null && serverCart.getSrl() != null) {
            object.addProperty("srl", serverCart.getSrl());
        }*/
        /*if(specialDisc!=-1)
        {*/

        if (specialDisc > 0)
            object.addProperty("specialDiscount", String.valueOf(specialDisc));
        else
            object.addProperty("specialDiscount", String.valueOf(0));
        //}
        Cart cartValue = App.getCartValue(String.valueOf(tableId));
        object.addProperty("enteredBy", App.getCurrentUser().getId());

        retrofit2.Call<Cart> call = apiInterface.onGenrateBill(object);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(retrofit2.Call<Cart> call, Response<Cart> response) {
                com.mak.Constant.cancelDialogue();
                tvGBill.setEnabled(true);
                tvGBill.setClickable(true);
                if (response != null && response.isSuccessful() && response.body() != null) {
                    if (specialDisc != -1) {
                        Cart cartValue = App.getCartValue(String.valueOf(Constant.CurrentTable));
                        if (cartValue == null) {
                            cartValue = new Cart();
                            cartValue.setTableCode(serverCart.getTableCode());
                        }

                        cartValue.setSpecialDiscount(String.valueOf(specialDisc));
                        if (response.body().getSrl() != null)
                            cartValue.setBillSrl(response.body().getSrl());

                        App.StoreCartValue(cartValue);


                         /*  if(cartValue!=null) {

                            cartValue.setSpecialDiscount(String.valueOf(specialDisc));
                            if(response.body().getSrl()!=null)
                                cartValue.setBillSrl(response.body().getSrl());

                        }else
                        {
                            cartValue=new Cart();
                            cartValue.setSpecialDiscount(String.valueOf(specialDisc));
                            if(response.body().getSrl()!=null)
                                cartValue.setBillSrl(response.body().getSrl());
                        }*/
                    }
                    genrateBillCart = response.body();
                    serverCart = response.body();
                    createPdf(genrateBillCart);
                    if (serverCart.getTableStatus() != null && serverCart.getTableStatus().toString().equalsIgnoreCase(Constant.TABLE_STATUS_SETTLEMENT_PENDING)) {
                        tvSettlement.setOnClickListener(CartViewActivity.this);

                    }
                    tvGBill.setTextColor(Color.GRAY);
                    com.mak.Constant.showToast(CartViewActivity.this, "GenerateBill Successfully");

                    startActivity(new Intent(CartViewActivity.this, TableSelectActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();

                }
            }

            @Override
            public void onFailure(retrofit2.Call<Cart> call, Throwable t) {
                tvGBill.setEnabled(true);
                tvGBill.setClickable(true);
                com.mak.Constant.cancelDialogue();
            }
        });
    }

    public void onPaymentSettlement(EditText edtAmount, EditText refundAmt) {
        com.mak.Constant.ShowProgressHud(CartViewActivity.this, "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        JsonObject object = new JsonObject();
        object.addProperty("tableCode", Constant.CurrentTable);
        if (edtAmount.getId() == edtCashAmount.getId())
            object.addProperty("cashAmt", edtCashAmount.getText().toString().trim());
        else
            object.addProperty("cashAmt", 0);
        object.addProperty("cCBank", edtCCBank.getText().toString().trim());
        if (edtAmount.getId() == edtCardAmount.getId())
            object.addProperty("cardAmt", edtCardAmount.getText().toString().trim());
        else
            object.addProperty("cardAmt", 0);

        object.addProperty("cardnumber", edtCardNumber.getText().toString().trim());
        if (edtAmount.getId() == edtChequeAmount.getId())
            object.addProperty("chqAmt", edtChequeAmount.getText().toString().trim());
        else
            object.addProperty("chqAmt", 0);

        object.addProperty("chqno", edtChequeNumber.getText().toString().trim());
        object.addProperty("chqBank", edtChequeBank.getText().toString().trim());

        if (edtAmount.getId() == edtDebitAmount.getId())
            object.addProperty("debitAmt", this.edtDebitAmount.getText().toString().trim());
        else
            object.addProperty("debitAmt", 0);

        object.addProperty("debtors", edtDebitorName.getText().toString().trim());
        object.addProperty("refundAmt", refundAmt.getText().toString().trim());
        object.addProperty("status", "S");

        Cart currentTable = App.getCartValue(String.valueOf(Constant.CurrentTable));

        if (currentTable != null && currentTable.getBillSrl() != null) {
            object.addProperty("srl", currentTable.getBillSrl());
        } else if (genrateBillCart != null && genrateBillCart.getSrl() != null) {
            object.addProperty("srl", genrateBillCart.getSrl());
        }

        object.addProperty("settleby", App.getCurrentUser().getId());
        retrofit2.Call<ResponseBody> call = apiInterface.onPaymentSettlement(object);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                com.mak.Constant.cancelDialogue();
                if (response != null && response.isSuccessful() && response.body() != null) {
                    App.getPrefs().setString(String.valueOf(Constant.CurrentTable), null);

                    com.mak.Constant.showToast(CartViewActivity.this, "Payment Successfully");
                    //Toast.makeText(getApplicationContext(), "Payment Successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CartViewActivity.this, TableSelectActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                    // serverCart = response.body();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                com.mak.Constant.cancelDialogue();
            }
        });
    }

    private void onSetDialogView(Dialog dialog) {


        btnDone = (Button) dialog.findViewById(R.id.btnDone);
        llPaymentMethod = (LinearLayout) dialog.findViewById(R.id.llPaymentMethod);
        llCash = (LinearLayout) dialog.findViewById(R.id.llCash);
        llCard = (LinearLayout) dialog.findViewById(R.id.llCard);
        llChq = (LinearLayout) dialog.findViewById(R.id.llChq);
        llDebit = (LinearLayout) dialog.findViewById(R.id.llDebit);


        cvCash = (CardView) dialog.findViewById(R.id.cvCash);
        cvDebit = (CardView) dialog.findViewById(R.id.cvDebit);
        cvCard = (CardView) dialog.findViewById(R.id.cvCard);
        cvCheq = (CardView) dialog.findViewById(R.id.cvCheq);


        edtCashAmount = (EditText) dialog.findViewById(R.id.edtCashAmount);
        edtCCBank = (EditText) dialog.findViewById(R.id.edtCCBank);
        edtCardAmount = (EditText) dialog.findViewById(R.id.edtCardAmount);
        edtCardNumber = (EditText) dialog.findViewById(R.id.edtCardNumber);
        edtChequeNumber = (EditText) dialog.findViewById(R.id.edtChequeNumber);
        edtChequeAmount = (EditText) dialog.findViewById(R.id.edtChequeAmount);
        edtChequeBank = (EditText) dialog.findViewById(R.id.edtChequeBank);
        edtDebitAmount = (EditText) dialog.findViewById(R.id.edtDebitAmount);
        edtDebitorName = (EditText) dialog.findViewById(R.id.edtDebitorName);
        edtCashRecivedAmount = (EditText) dialog.findViewById(R.id.edtCashRecivedAmount);
        edtCashRefundAmount = (EditText) dialog.findViewById(R.id.edtCashRefundAmount);
        edtDebitRecivedAmount = (EditText) dialog.findViewById(R.id.edtDebitRecivedAmount);
        edtDebitRefundAmount = (EditText) dialog.findViewById(R.id.edtDebitRefundAmount);
        edtChequeRecivedAmount = (EditText) dialog.findViewById(R.id.edtChequeRecivedAmount);
        edtChequeRefundAmount = (EditText) dialog.findViewById(R.id.edtChequeRefundAmount);
        edtCardRefundAmount = (EditText) dialog.findViewById(R.id.edtCardRefundAmount);
        edtCardRecivedAmount = (EditText) dialog.findViewById(R.id.edtCardRecivedAmount);


        ivBack = (ImageView) dialog.findViewById(R.id.ivBack);


        llPaymentTypes = new LinearLayout[]{llCash, llCard, llChq, llDebit};
        edtAll = new EditText[]{edtCashAmount, edtCCBank, edtCardAmount, edtCardNumber, edtChequeNumber, edtChequeAmount, edtChequeBank, edtDebitAmount, edtDebitorName, edtCashRecivedAmount, edtCashRefundAmount
                , edtDebitRecivedAmount, edtDebitRefundAmount, edtChequeRecivedAmount,
                edtChequeRefundAmount, edtCardRefundAmount, edtCardRecivedAmount};
        edtAmount = new EditText[]{edtCashAmount, edtCardAmount, edtChequeAmount, edtDebitAmount};
        edtDisable = new EditText[]{edtCashRefundAmount, edtDebitRefundAmount, edtCardRefundAmount, edtChequeRefundAmount};
        ivBack.setVisibility(View.INVISIBLE);
        llPaymentMethod.setVisibility(View.VISIBLE);
        btnDone.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        cvCash.setOnClickListener(this);
        cvDebit.setOnClickListener(this);
        cvCard.setOnClickListener(this);
        cvCheq.setOnClickListener(this);

        edtCashRecivedAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && !s.equals("") && !s.equals(" ") && s.length() > 0) {
                    int billAmount = 0;
                    float recivedAmount = Float.parseFloat(edtCashRecivedAmount.getText().toString().trim());
                    if (serverCart != null && serverCart.getTotalbillAmountWithDsic() != -1) {
                        billAmount = (int) serverCart.getTotalbillAmountWithDsic();
                    }/*else  if(serverCart!=null&&serverCart.getTotalbillAmount()!=-1)
                    {
                        billAmount=serverCart.getTotalbillAmount();
                    }*/

                    if (recivedAmount < billAmount) {
                        Toast.makeText(CartViewActivity.this, "Recived amount less than bill amount", Toast.LENGTH_SHORT).show();
                        edtCashRefundAmount.setText("0");
                    } else {
                        float refund = recivedAmount - billAmount;
                        edtCashRefundAmount.setText(Constant.twoDigitValue(refund));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtDebitRecivedAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && !s.equals("") && !s.equals(" ") && s.length() > 0) {
                    int billAmount = 0;
                    float recivedAmount = Float.parseFloat(edtDebitRecivedAmount.getText().toString().trim());
                    if (serverCart != null && serverCart.getTotalbillAmountWithDsic() != -1) {
                        billAmount = (int) serverCart.getTotalbillAmountWithDsic();
                    }/*else  if(serverCart!=null&&serverCart.getTotalbillAmount()!=-1)
                    {
                        billAmount=serverCart.getTotalbillAmount();
                    }*/

                    if (recivedAmount < billAmount) {
                        Toast.makeText(CartViewActivity.this, "Recived amount less than bill amount", Toast.LENGTH_SHORT).show();
                        edtDebitRefundAmount.setText("0");
                    } else {
                        edtDebitRefundAmount.setText(Constant.twoDigitValue(recivedAmount - billAmount));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edtChequeRecivedAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && !s.equals("") && !s.equals(" ") && s.length() > 0) {
                    int billAmount = 0;
                    float recivedAmount = Float.parseFloat(edtChequeRecivedAmount.getText().toString().trim());
                    if (serverCart != null && serverCart.getTotalbillAmountWithDsic() != -1) {
                        billAmount = (int) serverCart.getTotalbillAmountWithDsic();
                    }/*else  if(serverCart!=null&&serverCart.getTotalbillAmount()!=-1)
                    {
                        billAmount=serverCart.getTotalbillAmount();
                    }*/

                    if (recivedAmount < billAmount) {
                        Toast.makeText(CartViewActivity.this, "Recived amount less than bill amount", Toast.LENGTH_SHORT).show();
                        edtChequeRefundAmount.setText("0");
                    } else {
                        edtChequeRefundAmount.setText(Constant.twoDigitValue(recivedAmount - billAmount));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        edtCardRecivedAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && !s.equals("") && !s.equals(" ") && s.length() > 0) {
                    int billAmount = 0;
                    float recivedAmount = Float.parseFloat(edtCardRecivedAmount.getText().toString().trim());
                    if (serverCart != null && serverCart.getTotalbillAmountWithDsic() != -1) {
                        billAmount = (int) serverCart.getTotalbillAmountWithDsic();
                    }/*else  if(serverCart!=null&&serverCart.getTotalbillAmount()!=-1)
                    {
                        billAmount=serverCart.getTotalbillAmount();
                    }*/

                    if (recivedAmount < billAmount) {
                        Toast.makeText(CartViewActivity.this, "Recived amount less than bill amount", Toast.LENGTH_SHORT).show();
                        edtCardRefundAmount.setText("0");
                    } else {
                        edtCardRefundAmount.setText(Constant.twoDigitValue(recivedAmount - billAmount));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    protected void createPdf(Cart cart) {


        String imageFileName = "POS_Invoice_" + cart.getSrl();


        String folderPath = Environment.getExternalStorageDirectory() + "/POS/Invoice";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            File wallpaperDirectory = new File(folderPath);
            wallpaperDirectory.mkdirs();
        }
        //create a new file
        File pdfFile = new File(folderPath, imageFileName + ".pdf");

        if (pdfFile.exists()) {
            pdfFile.delete();
        }

        try {
            /**
             * Creating Document
             */
            Document document = new Document();

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile.getAbsolutePath()));

            // Open to write
            document.open();

            // Document Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor(getResources().getString(R.string.app_name));
            document.addCreator(getResources().getString(R.string.app_name));

            /***
             * Variables for further use....
             */
            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;

            /**
             * How to USE FONT....
             */
            BaseFont urName = BaseFont.createFont("assets/arial.ttf", "UTF-8", BaseFont.EMBEDDED);

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
            lineSeparator.setPercentage(80);
            // Title Order Details...
            // Adding Title....


            Font mInvoiceFont = new Font(urName, 28.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mInvoiceChunk = new Chunk("RETAIL INVOICE", mInvoiceFont);
            Paragraph mInvoiceParagraph = new Paragraph(mInvoiceChunk);
            mInvoiceParagraph.setAlignment(Element.ALIGN_CENTER);
            Paragraph mBillph = new Paragraph();

            if (cart.getBillno() != null) {
                Font mBillNumberFont = new Font(urName, 16.0f, Font.NORMAL, BaseColor.BLACK);

                Chunk mBillChunk = new Chunk("BILL NUMBER. :" + cart.getBillno(), mBillNumberFont);
                mBillph = new Paragraph(mBillChunk);
                mBillph.setAlignment(Element.ALIGN_LEFT);
            }


            Paragraph mOrderDetailsTitleParagraph = new Paragraph();
            Paragraph resInfoParagraph = new Paragraph();
            Font mOrderDetailsTitleFont = new Font(urName, 25.0f, Font.BOLD, BaseColor.BLACK);


            if (cart != null && cart.getRestaurantDetails() != null) {
                String resName = "";

                if (cart.getRestaurantDetails().getRName() != null)
                    resName = cart.getRestaurantDetails().getRName();

                Chunk mOrderDetailsTitleChunk = new Chunk(resName, mOrderDetailsTitleFont);
                mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
                mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);


                Font resInfo = new Font(urName, 16.0f, Font.NORMAL, BaseColor.BLACK);
                String resAddress = "";
                if (cart.getRestaurantDetails().getRAddress() != null)
                    resAddress = cart.getRestaurantDetails().getRAddress();
                String resGST = "";
                if (cart.getRestaurantDetails().getRGSTNo() != null)
                    resGST = cart.getRestaurantDetails().getRGSTNo();

                String resEmail = "";
                String resNumber = "";

                if (cart.getRestaurantDetails().getRTelephone() != null)
                    resNumber = "Tel:Ph :" + cart.getRestaurantDetails().getRTelephone();

                //String resInvoiceNumber = "#Invoice No: " + cart.getSrl();
                String billDate = "Date : " + com.mak.Constant.getBillDateTime(new Date());

                String allResInfo = resAddress + " \n" + resGST + " \n" + resEmail + " \n" + resNumber + " \n";
                Chunk resInfoChunk = new Chunk(allResInfo, resInfo);
                resInfoParagraph = new Paragraph(resInfoChunk);
                resInfoParagraph.setAlignment(Element.ALIGN_CENTER);
            }


            PdfPTable billTable = new PdfPTable(6); //one page contains 15 records
            billTable.setWidthPercentage(100);

            billTable.setWidths(new float[]{2, 5, 2, 2, 2, 2});
            billTable.setSpacingBefore(30.0f);


            //BILL INFO
            PdfPTable billInfo = new PdfPTable(4);
            billInfo.setWidthPercentage(100);
            billInfo.addCell(getAccountsCell("TABLE :"));
            billInfo.addCell(getTableInfoCellR(cart.getTableCode()));
            billInfo.addCell(getAccountsCell("DATE:"));
            billInfo.addCell(getTableInfoCellR(cart.getDocdate()));


            PdfPCell tableInfo = new PdfPCell(billInfo);
            tableInfo.setBorder(Rectangle.NO_BORDER);
            tableInfo.setColspan(5);
            billTable.addCell(tableInfo);


            PdfPTable validity1 = new PdfPTable(2);
            validity1.setWidthPercentage(100);
            validity1.addCell(getValidityCell(" "));
            ;
            PdfPCell summaryL1 = new PdfPCell(validity1);
            summaryL1.setBorder(Rectangle.NO_BORDER);

            summaryL1.setColspan(1);
            summaryL1.setPadding(1.0f);
            billTable.addCell(summaryL1);


            //bill 2

            //BILL INFO
            PdfPTable billInfo1 = new PdfPTable(6);
            billInfo1.setWidthPercentage(100);

            billInfo1.addCell(getAccountsCell("TIME :"));
            billInfo1.addCell(getTableInfoCellR(cart.getDoctime()));
            billInfo1.addCell(getAccountsCell(""));
            billInfo1.addCell(getTableInfoCellR(cart.getNoofPrints()));
            billInfo1.addCell(getAccountsCell("PAPX :"));
            billInfo1.addCell(getTableInfoCellR(cart.getPaxNo()));


            PdfPCell tableInfo1 = new PdfPCell(billInfo1);
            tableInfo1.setBorder(Rectangle.NO_BORDER);
            tableInfo1.setColspan(6);
            billTable.addCell(tableInfo1);


           /* PdfPTable validity2 = new PdfPTable(2);
            validity2.setWidthPercentage(100);
            validity2.addCell(getValidityCell(" "));
            ;
            PdfPCell summaryL2 = new PdfPCell (validity2);
            summaryL1.setBorder(Rectangle.NO_BORDER);

            summaryL2.setColspan (2);
            summaryL2.setPadding (1.0f);
            billTable.addCell(summaryL2);
*/


            billTable.addCell(getBillHeaderCell("No"));
            billTable.addCell(getBillHeaderCell("Name"));
            billTable.addCell(getBillHeaderCell("QTY"));
            billTable.addCell(getBillHeaderCell("CGST"));
            billTable.addCell(getBillHeaderCell("SGST"));
            billTable.addCell(getBillHeaderCell("Amount"));
            float total = 0, discAmount = 0, taxAmount = 0, addTax = 0, surCharge = 0;
            if (cart != null && cart.getItems() != null && cart.getItems().size() > 0) {
                for (int i = 0; i < cart.getItems().size(); i++) {
                    Items items = cart.getItems().get(i);


                    if (items != null) {
                        int no = i + 1;

                        billTable.addCell(getBillRowCell(String.valueOf(no)));
                        billTable.addCell(getBillRowCell(getValue(items.getItem_name())));
                        billTable.addCell(getBillRowCell(getValue(String.valueOf(items.getQty()))));
                        billTable.addCell(getBillRowCell(getValue(String.valueOf(items.getTaxamt()))));
                        billTable.addCell(getBillRowCell(getValue(String.valueOf(items.getAddtaxAmt()))));
                        billTable.addCell(getBillRowCell(getValue(String.valueOf(items.getRate()))));

                        if (items.getQty() > 0) {
                            if (items != null && items.getDiscAmt() != -1)
                                discAmount = discAmount + items.getDiscAmt();
                            if (items != null && items.getTaxamt() != -1)
                                taxAmount = taxAmount + items.getTaxamt();

                            if (items != null && items.getAddtaxAmt() != -1)
                                addTax = addTax + items.getAddtaxAmt();

                            if (items != null && items.getAddtaxAmt2() != -1)
                                surCharge = surCharge + items.getAddtaxAmt2();

                            if (items != null && items.getRate() != -1)
                                total = total + (items.getRate() * items.getQty());
                        }

                    }

                }
                for (int i = 0; i < 2; i++) {
                    billTable.addCell(getBillRowCell(" "));
                    billTable.addCell(getBillRowCell(""));
                    billTable.addCell(getBillRowCell(""));
                    billTable.addCell(getBillRowCell(""));
                    billTable.addCell(getBillRowCell(""));
                    billTable.addCell(getBillRowCell(""));
                }
            }

            PdfPTable validity = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            PdfPCell summaryL = new PdfPCell(validity);
            summaryL.setBorder(Rectangle.NO_BORDER);

            summaryL.setColspan(2);
            summaryL.setPadding(1.0f);
            billTable.addCell(summaryL);

            PdfPTable accounts = new PdfPTable(2);
            accounts.setWidthPercentage(100);
            accounts.addCell(getAccountsCell("NET"));
            accounts.addCell(getAccountsCellR(com.mak.pos.Utility.Constant.twoDigitValue(total)));
            accounts.addCell(getAccountsCell("SGST"));
            accounts.addCell(getAccountsCellR("+" + com.mak.pos.Utility.Constant.twoDigitValue(addTax)));
            accounts.addCell(getAccountsCell("CGST"));
            accounts.addCell(getAccountsCellR("+" + com.mak.pos.Utility.Constant.twoDigitValue(taxAmount)));

            if (surCharge > 0) {
                accounts.addCell(getAccountsCell("SURCHARGE"));
                accounts.addCell(getAccountsCellR("+" + com.mak.pos.Utility.Constant.twoDigitValue(surCharge)));
            } else {
                accounts.addCell(getAccountsCell(""));
                accounts.addCell(getAccountsCellR(""));
            }

            float finalBillAmount = (total - discAmount) + taxAmount + addTax + surCharge;

            float disAmt = 0;
            if (cart.getDisPrcnt() != null)
                disAmt = finalBillAmount - cart.getTotalbillAmount();
            else {
                disAmt = discAmount;
            }


            if (disAmt > 0) {
                accounts.addCell(getAccountsCell("DISCOUNT"));
                accounts.addCell(getAccountsCellR("-" + com.mak.pos.Utility.Constant.twoDigitValue((int) disAmt)));

            } else {
                accounts.addCell(getAccountsCell(""));
                accounts.addCell(getAccountsCellR(""));

            }


            if (cart != null && cart.getRoundoff() != null && !cart.getRoundoff().equals("") && !cart.getRoundoff().equals("0.0")) {
                accounts.addCell(getAccountsCell("ROUND OFF"));
                String roundOffvalue = "0";
                char sign = '-';
                if (cart.getRoundoff().startsWith("-") || cart.getRoundoff().startsWith("+")) {
                    sign = cart.getRoundoff().charAt(0);
                    roundOffvalue = cart.getRoundoff().substring(1);
                } else {
                    roundOffvalue = cart.getRoundoff();
                }
                accounts.addCell(getAccountsCellR(sign + com.mak.pos.Utility.Constant.twoDigitValue(Float.parseFloat(roundOffvalue))));
            } else {
                //   accounts.addCell(getAccountsCellR("-0.00"));
                accounts.addCell(getAccountsCell(""));
                accounts.addCell(getAccountsCellR(""));

            }


            accounts.addCell(getAccountsCellGrandTotal("BILL AMOUNT"));
            if (cart != null && cart.getTotalbillAmount() != -1)
                accounts.addCell(getAccountsCellRGrandTotal(com.mak.pos.Utility.Constant.twoDigitValue(Math.round(cart.getTotalbillAmount()))));
            else
                accounts.addCell(getAccountsCellRGrandTotal("0"));

            PdfPCell summaryR = new PdfPCell(accounts);
            summaryR.setBorder(Rectangle.NO_BORDER);
            summaryR.setColspan(4);
            billTable.addCell(summaryR);


            //tabel info

           /* PdfPTable tabInfo1 = new PdfPTable(1);
            validity.setWidthPercentage(100);
            validity.addCell(getValidityCell(" "));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            validity.addCell(getValidityCell(""));
            PdfPCell summaryL = new PdfPCell (validity);
            summaryL.setBorder(Rectangle.NO_BORDER);

            summaryL.setColspan (2);
            summaryL.setPadding (1.0f);
            billTable.addCell(summaryL);*/

            PdfPTable tabInfo = new PdfPTable(2);
            tabInfo.setWidthPercentage(100);
            tabInfo.addCell(getAccountsCell("NET"));
            tabInfo.addCell(getAccountsCellR(com.mak.pos.Utility.Constant.twoDigitValue(total)));
            tabInfo.addCell(getAccountsCell("SGST"));
            tabInfo.addCell(getAccountsCellR("+" + com.mak.pos.Utility.Constant.twoDigitValue(addTax)));

            PdfPCell summaryInfo = new PdfPCell(tabInfo);
            summaryInfo.setBorder(Rectangle.NO_BORDER);
            summaryInfo.setColspan(4);
            billTable.addCell(summaryInfo);






/*
            //BILL INFO
            PdfPTable billInfo1= new PdfPTable(2);
            billInfo1.setWidthPercentage(100);
            billInfo1.addCell(getAccountsCell("TIME :"));
            billInfo1.addCell(getAccountsCellR(cart.getDoctime()));
            billInfo1.addCell(getAccountsCell(""));
            billInfo1.addCell(getAccountsCellR(cart.getNoofPrints()));
            billInfo1.addCell(getAccountsCell("PAPX :"));
            billInfo1.addCell(getAccountsCellR(cart.getPaxNo()));


            PdfPCell tableInfo1 = new PdfPCell (billInfo1);
            tableInfo.setBorder(Rectangle.NO_BORDER);
            tableInfo.setColspan (3);
            billTable.addCell(tableInfo1);*/


            PdfPTable ServiceCode = new PdfPTable(1);
            ServiceCode.setWidthPercentage(100);
            PdfPTable ServiceCode1 = new PdfPTable(1);
            ServiceCode1.setWidthPercentage(100);
            if (cart.getStoreCode() != null) {
                ServiceCode.addCell(getdescCellRight("Service Code: " + cart.getStoreCode()));
            }
            if (cart.getEnteredBy() != null) {
                ServiceCode1.addCell(getdescCellLeft("" + cart.getEnteredBy()));
            } else {
                ServiceCode1.addCell(getdescCellLeft(""));

            }


            PdfPTable time = new PdfPTable(1);
            time.setWidthPercentage(100);
            time.addCell(getdescCellRight("Time : " + com.mak.Constant.getBillTime(new Date())));


            PdfPTable describer = new PdfPTable(1);
            describer.setWidthPercentage(100);
            describer.addCell(getdescCell(" "));

            describer.addCell(getdescCell("No Reverse Charges"));

            document.open();//PDF document opened........


            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            document.add(mOrderDetailsTitleParagraph);
            document.add(resInfoParagraph);
            document.add(new Paragraph("\n"));
            document.add(mBillph);
            document.add(new Paragraph(""));

            document.add(billTable);
            document.add(new Paragraph("\n"));
            document.add(ServiceCode);
            document.add(ServiceCode1);
            document.add(describer);
            document.add(time);
            document.close();


            document.close();

            if (pdfFile != null && pdfFile.exists()) {
                onShareInvoice(pdfFile);

                try {
                    MediaScannerConnection.scanFile(CartViewActivity.this,
                            new String[]{pdfFile.toString()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {

                                }
                            });
                } catch (Exception e) {
                }
            }


            //   FileUtils.openFile(mContext, new File(dest));


        } catch (IOException | DocumentException ie) {
            Log.e("createPdf: Error ", " ");
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(CartViewActivity.this, "Something wrong .", Toast.LENGTH_SHORT).show();
        }
    }

    private String getValue(String itemVaue) {
        if (itemVaue == null)
            return " ";
        else
            return itemVaue;
    }

    public static PdfPCell getBillHeaderCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 15);
        font.setColor(BaseColor.BLACK);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5.0f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(BaseColor.GRAY);
        return cell;
    }

    public static PdfPCell getBillRowCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5.0f);
        cell.setBorderWidthBottom(0);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorderWidthTop(0);
        return cell;
    }

    public static PdfPCell getAccountsCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 15);

        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5.0f);
        return cell;
    }

    public static PdfPCell getAccountsCellGrandTotal(String text) {
        FontSelector fs = new FontSelector();

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        font.setStyle(Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5.0f);
        return cell;
    }

    public static PdfPCell getAccountsCellR(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5.0f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingRight(20.0f);
        return cell;
    }

    public static PdfPCell getTableInfoCellR(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(5.0f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingRight(20.0f);
        return cell;
    }

    public static PdfPCell getAccountsCellRGrandTotal(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        font.setStyle(Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setPadding(5.0f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingRight(20.0f);
        return cell;
    }

    public static PdfPCell getdescCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 22);
        //font.setColor(BaseColor.BLACK);
        //font.setStyle(Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);

        cell.setBorder(0);
        return cell;
    }

    public static PdfPCell getdescCellRight(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        // font.setColor(BaseColor.BLACK);
        //font.setStyle(Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);

        cell.setBorder(0);
        return cell;
    }

    public static PdfPCell getdescCellLeft(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 18);
        //    font.setColor(BaseColor.BLACK);
//        font.setStyle(Font.BOLD);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);

        cell.setBorder(0);
        return cell;
    }

    public static PdfPCell getValidityCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorder(0);
        return cell;
    }

    private void onShareInvoice(File pdfFile) {

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);


        Uri uri = FileProvider.getUriForFile(CartViewActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                pdfFile);
        //  Uri contentUri = FileProvider.getUriForFile(this, "com.mydomain.fileprovider", newFile);
        if (pdfFile.exists()) {


            try {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                //shareIntent.putExtra(Intent.EXTRA_TEXT, title + "," + Constants.ShareTweeterLink);
                //  shareIntent.putExtra(Intent.EXTRA_TEXT,Html.fromHtml(HTMl));
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("application/pdf");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.app_name)));

            } catch (Exception e) {
                Log.e("Error", "g");
            }


        }
    }
}
