package com.mak.pos;

import android.app.Dialog;
import android.content.Intent;
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
import com.mak.Api.ApiClient;
import com.mak.Api.ApiInterface;
import com.mak.App;
import com.mak.pos.Adapter.CartItemListAdapter;
import com.mak.pos.Model.POJO.Cart;
import com.mak.pos.Model.POJO.Items;
import com.mak.pos.Model.POJO.MenuItemInfo;
import com.mak.pos.Model.TableModel;
import com.mak.pos.Utility.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

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
    public static Dialog dialog, onViewCartDialog, updateItemdialog;
    ArrayList<Items> allItems;
    private Cart serverCart;
    private Cart genrateBillCart;
    public static CartViewActivity cartCtx;
    private boolean isLocalCartView=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_view);
        cartCtx = CartViewActivity.this;
        findView();


        getCart(String.valueOf(Constant.CurrentTable));
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
                    taxAmt = calclateTax(item.getTaxamt(), item.getRate()) * item.getQty();
                    ;
                    taxAmount = taxAmount + taxAmt;

                    addtax = calclateTax(item.getAddtaxAmt(), item.getRate()) * item.getQty();
                    addTax = addTax + addtax;

                    subchrg = calclateTax(item.getAddtaxAmt2(), item.getRate()) * item.getQty();
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

    private float calclateTax(float percentage, float rate) {

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
                tvGBill.setEnabled(false);
                tvGBill.setClickable(false);
                onGenrateBill(Constant.CurrentTable);
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
                isLocalCartView=true;
                tvSave.setVisibility(View.VISIBLE);
                tvGBill.setVisibility(View.GONE);
                tvSettlement.setVisibility(View.GONE);
                tvLocalCart.setTextColor(getResources().getColor(R.color.settlement_color));
                tvSevrverCart.setTextColor(getResources().getColor(R.color.white));
                onCartViewSet(true);
                break;
            case R.id.tvSevrverCart:
                isLocalCartView=false;
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

    private void onCartViewSet(boolean isLocal) {
        ArrayList<Items> allItems = new ArrayList<>();
        if (!isLocal) {
            float total = 0, discAmount = 0, taxAmount = 0, addTax = 0, surCharge = 0;
            if (serverCart != null && serverCart.getItems() != null) {
                allItems = serverCart.getItems();


                if (allItems != null) {
                    for (Items items : allItems) {
                        if(items.getQty()>0) {
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
                tvRoundOff.setText("-"+Constant.twoDigitValue(Float.parseFloat(serverCart.getRoundoff())));
            } else {
                tvRoundOff.setText("-0.00");
            }

            tvSurCharge.setText("+" + Constant.twoDigitValue(surCharge));
            tvAddTax.setText("+" + Constant.twoDigitValue(addTax));
            tvDiscount.setText("-" + Constant.twoDigitValue(discAmount));
            if (serverCart != null && serverCart.getTotalbillAmount() != 1)
                tvNet.setText(String.valueOf((int)serverCart.getTotalbillAmount()));
            tvTax.setText("+" + Constant.twoDigitValue(taxAmount));
            if (allItems != null)
                rvCart.setAdapter(new CartItemListAdapter(CartViewActivity.this, allItems,isLocal,serverCart));


        } else {

            float totalDiscAmt = 0, totalbillAmount = 0;

            float total = 0, taxAmt = 0, addtax = 0, subchrg = 0;
            Cart localCart = App.getCartValue(String.valueOf(Constant.CurrentTable));
            if (localCart != null && localCart.getItems() != null) {
                allItems = localCart.getItems();
                for (Items item : allItems) {
                    totalDiscAmt = totalDiscAmt + (item.getDisc() * item.getQty());
                    totalbillAmount = totalbillAmount + (item.getRate() * item.getQty());
                    taxAmt = taxAmt + (item.getTaxamt() * item.getQty());
                    addtax = addtax + (item.getAddtaxAmt() * item.getQty());
                    subchrg = subchrg + (item.getAddtaxAmt2() * item.getQty());
                }


            }

            float finalBillAmount = (totalbillAmount - totalDiscAmt) + taxAmt + addtax + subchrg;

            String roundOff = String.valueOf(Constant.twoDigitValue(finalBillAmount));


            if (roundOff.contains(".")) {
                String[] roundOffValue = roundOff.split("\\.");

                if (roundOffValue.length > 1) {
                    tvRoundOff.setText("-0."+roundOffValue[1]);

                } else {
                    tvRoundOff.setText("-0.00");
                }

            } else {
                tvRoundOff.setText("-0.00");
            }
            tvTotal.setText(Constant.twoDigitValue(totalbillAmount));
            tvSurCharge.setText("+" + Constant.twoDigitValue(subchrg));
            tvAddTax.setText("+" + Constant.twoDigitValue(addtax));
            tvDiscount.setText("-" + Constant.twoDigitValue(totalDiscAmt));
            tvNet.setText(String.valueOf((int)finalBillAmount));
            tvTax.setText("+" + Constant.twoDigitValue(taxAmt));
            if (allItems != null)
                rvCart.setAdapter(new CartItemListAdapter(CartViewActivity.this, allItems,isLocal,serverCart));

        }

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
            onPaymentSettlement(edtDebitRefundAmount);
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
            onPaymentSettlement(edtChequeRefundAmount);

        }

        //setTblStatus(Constant.API_TABLE_STATUS_VACANT);

    }

    private void onCardPayment() {
        if (edtCardAmount.getText().toString().length() == 0 || edtCardAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter valid amount", Toast.LENGTH_LONG).show();
        } else if (edtCardRecivedAmount.getText().toString().length() == 0 || edtCardRecivedAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter recived amount", Toast.LENGTH_LONG).show();
        } else {
            onPaymentSettlement(edtCardRecivedAmount);
            // setTblStatus(Constant.API_TABLE_STATUS_VACANT);
        }
    }

    private void onCashPayment() {
        if (edtCashAmount.getText().toString().length() == 0 || edtCashAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter valid amount", Toast.LENGTH_LONG).show();
        } else if (edtCashRecivedAmount.getText().toString().length() == 0 || edtCashRecivedAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter recived amount", Toast.LENGTH_LONG).show();
        } else {
            onPaymentSettlement(edtCashRefundAmount);
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
            if (genrateBillCart != null && genrateBillCart.getTotalbillAmount() != -1)
                editText.setText(String.valueOf((int)genrateBillCart.getTotalbillAmount()));
            else if (serverCart != null && serverCart.getTotalbillAmount() != -1)
                editText.setText(String.valueOf((int)serverCart.getTotalbillAmount()));

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
            else if(Constant.StoreCode!=-1)
                tableCart.addProperty("storeCode", Constant.StoreCode);

            if (App.getCurrentUser().getId() != null)
                tableCart.addProperty("enteredBy", App.getCurrentUser().getId());
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
                itemObject.addProperty("rate", item.getRate());
                itemObject.addProperty("disc", item.getDisc() * item.getQty());
                itemObject.addProperty("discAmt", item.getDiscAmt() * item.getQty());
                itemObject.addProperty("taxCode", item.getTaxCode());
                itemObject.addProperty("remarks", "");
                itemObject.addProperty("taxamt", item.getTaxamt() * item.getQty());
                itemObject.addProperty("addtaxAmt", item.getAddtaxAmt() * item.getQty());
                itemObject.addProperty("addtaxAmt2", item.getAddtaxAmt2() * item.getQty());
                //tableCart.addProperty("storeCode",item.getStore());
                totalDiscAmt = totalDiscAmt + (item.getDisc() * item.getQty());
                totalbillAmount = totalbillAmount + (item.getRate() * item.getQty());
                taxAmt = taxAmt + (item.getTaxamt() * item.getQty());
                addtax = addtax + (item.getAddtaxAmt() * item.getQty());
                subchrg = subchrg + (item.getAddtaxAmt2() * item.getQty());
                itemsArray.add(itemObject);
            }
            float finalBillAmount = (totalbillAmount - totalDiscAmt) + taxAmt + addtax + subchrg;
            tableCart.addProperty("totalDiscAmt", totalDiscAmt);
            tableCart.addProperty("totalbillAmount", (int)finalBillAmount);
            String roundOff = String.valueOf(Constant.twoDigitValue(finalBillAmount));


            if (roundOff.contains(".")) {

                String[] roundOffValue = roundOff.split("\\.");

                if (roundOffValue.length > 1) {
                    tableCart.addProperty("roundoff", "0." + roundOffValue[1]);
                } else {
                    tableCart.addProperty("roundoff", "0.00");
                }

            } else {
                tableCart.addProperty("roundoff", "0.00");
            }


            tableCart.add("items", itemsArray);

            onStoreCart(tableCart,true);
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
                updateCart.addProperty("storeCode",Constant.StoreCode);

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


            float disc= item.getDisc()/item.getQty();
            float discAmt= item.getDiscAmt() / item.getQty();
            float taxamt= item.getTaxamt() / item.getQty();
            float addtaxAmt= item.getAddtaxAmt() / item.getQty();
            float addtaxAmt2= item.getAddtaxAmt2() / item.getQty();
            item.setQty(itemCount);
                JsonObject itemObject = new JsonObject();
                itemObject.addProperty("sno", item.getSno());
                itemObject.addProperty("code", item.getCode());
                itemObject.addProperty("Qty", "-"+String.valueOf(item.getQty()));
                itemObject.addProperty("rate", item.getRate());
                itemObject.addProperty("disc", disc * item.getQty());
                itemObject.addProperty("discAmt", discAmt* item.getQty());
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
             updateCart.addProperty("roundoff","0");




            onStoreCart(updateCart,false);
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
                    if(isLocalCart) {
                        Cart cartValue = App.getCartValue(String.valueOf(Constant.CurrentTable));
                        cartValue.setItems(null);
                        App.StoreCartValue(cartValue);
                        Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_LONG).show();
                       // tvLocalCart.performClick();
                        startActivity(new Intent(CartViewActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_LONG).show();

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

    public  void onUpdateItemDialog(final Items selectedItem, final boolean isLocal) {
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

        if (isLocal)
        {
            tvQuantity.setText(String.valueOf(itemCount));
        }else
        {
            tvQuantity.setText("-"+String.valueOf(itemCount));
        }

        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemCount > 0) {
                    itemCount--;
                }
                if (isLocal)
                {
                    tvQuantity.setText(String.valueOf(itemCount));
                }else
                {
                    tvQuantity.setText("-"+String.valueOf(itemCount));
                }

            }
        });
        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isLocal)
                {
                    if(itemCount<selectedItem.getQty())
                    {
                        itemCount++;
                    }
                }else
                {
                    itemCount++;
                }

                if(isLocal)
                    tvQuantity.setText(String.valueOf(itemCount));
                else
                    tvQuantity.setText("-"+String.valueOf(itemCount));

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
                                    }
                                }
                            }
                        }

                        if(tempCart.size()==0)
                        {
                            tempCart=null;
                        }
                        cart.setItems(tempCart);
                        App.StoreCartValue(cart);
                        tvLocalCart.performClick();
                    }
                }else
                {

                    Items iupdatedItem = null;
                    if(serverCart!=null&&serverCart.getItems()!=null)
                    {
                        for(Items items:serverCart.getItems())
                        {
                            if(items.getCode().equals(selectedItem.getCode()))
                            {
                                iupdatedItem=items;
                                //iupdatedItem.setQty();
break;
                            }
                        }
                        if(iupdatedItem!=null)
                        {
                            onUpdateCart(iupdatedItem,itemCount);
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
                    if(serverCart.getTableStatus()!=null&&serverCart.getTableStatus().equals(Constant.TABLE_STATUS_SETTLEMENT_PENDING))
                    {
                        tvSettlement.setOnClickListener(CartViewActivity.this);

                    }
                  //  onCartSet();
                    if(!isLocalCartView)
                    onCartViewSet(false);


                }
            }

            @Override
            public void onFailure(retrofit2.Call<Cart> call, Throwable t) {
                com.mak.Constant.cancelDialogue();
            }
        });
    }

    public void onGenrateBill(int tableId) {
        com.mak.Constant.ShowProgressHud(CartViewActivity.this, "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        JsonObject object = new JsonObject();
        object.addProperty("tableCode", tableId);
        if(serverCart!=null&&serverCart.getSrl()!=null) {
            object.addProperty("srl", serverCart.getSrl());
        }
        retrofit2.Call<Cart> call = apiInterface.onGenrateBill(object);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(retrofit2.Call<Cart> call, Response<Cart> response) {
                com.mak.Constant.cancelDialogue();
                tvGBill.setEnabled(true);
                tvGBill.setClickable(true);
                if (response != null && response.isSuccessful() && response.body() != null) {
                    genrateBillCart = response.body();
                    serverCart = response.body();
                    if(serverCart.getTableStatus()!=null&&serverCart.getTableStatus().equals(Constant.TABLE_STATUS_SETTLEMENT_PENDING))
                    {
                        tvSettlement.setOnClickListener(CartViewActivity.this);

                    }
                    Toast.makeText(getApplicationContext(), "GenerateBill Successfully", Toast.LENGTH_LONG).show();
startActivity(new Intent(CartViewActivity.this,LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK));
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

    public void onPaymentSettlement(EditText refundAmt) {
        com.mak.Constant.ShowProgressHud(CartViewActivity.this, "");
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        JsonObject object = new JsonObject();
        object.addProperty("tableCode", Constant.CurrentTable);
        object.addProperty("cashAmt", edtCashAmount.getText().toString().trim());
        object.addProperty("cCBank", edtCCBank.getText().toString().trim());
        object.addProperty("cardAmt", edtCardAmount.getText().toString().trim());
        object.addProperty("cardnumber", edtCardNumber.getText().toString().trim());
        object.addProperty("chqAmt", edtChequeAmount.getText().toString().trim());
        object.addProperty("chqno", edtChequeNumber.getText().toString().trim());
        object.addProperty("chqBank", edtChequeBank.getText().toString().trim());
        object.addProperty("debitAmt", edtDebitAmount.getText().toString().trim());
        object.addProperty("debtors", edtDebitorName.getText().toString().trim());
        object.addProperty("refundAmt", refundAmt.getText().toString().trim());
        object.addProperty("status", "S");
        if (genrateBillCart != null)
            object.addProperty("srl", genrateBillCart.getSrl());
        else if (serverCart != null)
            object.addProperty("srl", serverCart.getSrl());
        object.addProperty("settleby", App.getCurrentUser().getId());
        retrofit2.Call<ResponseBody> call = apiInterface.onPaymentSettlement(object);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                com.mak.Constant.cancelDialogue();
                if (response != null && response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getApplicationContext(), "Payment Successfully", Toast.LENGTH_LONG).show();
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
        edtDisable = new EditText[]{edtCashRefundAmount, edtDebitRefundAmount,edtCardRefundAmount,edtChequeRefundAmount};
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
                    if (serverCart != null && serverCart.getTotalbillAmount() != -1) {
                        billAmount =(int) serverCart.getTotalbillAmount();
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
                    if (serverCart != null && serverCart.getTotalbillAmount() != -1) {
                        billAmount =(int) serverCart.getTotalbillAmount();
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
                    if (serverCart != null && serverCart.getTotalbillAmount() != -1) {
                        billAmount =(int)serverCart.getTotalbillAmount();       }/*else  if(serverCart!=null&&serverCart.getTotalbillAmount()!=-1)
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
                    if (serverCart != null && serverCart.getTotalbillAmount() != -1) {
                        billAmount = (int)serverCart.getTotalbillAmount(); }/*else  if(serverCart!=null&&serverCart.getTotalbillAmount()!=-1)
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

}
