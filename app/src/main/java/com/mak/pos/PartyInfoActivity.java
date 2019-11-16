package com.mak.pos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mak.Api.ApiClient;
import com.mak.Api.ApiInterface;
import com.mak.App;
import com.mak.pos.Model.POJO.Cart;
import com.mak.pos.Model.PartyInfoModel;
import com.mak.pos.Model.POJO.SalesMenMode;
import com.mak.pos.Model.TableModel;
import com.mak.pos.Utility.Constant;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import retrofit2.Callback;
import retrofit2.Response;

public class PartyInfoActivity extends AppCompatActivity implements View.OnClickListener {
    EditText tvPartyName, tvPartyAddress, tvPartyNumber;
    public static TextView btnSkip, tvPersonNumber;
    private ArrayList<SalesMenMode> salesMenList = new ArrayList<>();
    private NiceSpinner spSalesMenList;
    private SalesMenMode currentSalesMen = new SalesMenMode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_info);

        findView();
        hideSoftKeyboard();
        getSalesMenList();
    }

    private void getSalesMenList() {


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<JsonObject> call = apiInterface.onGetSalesMenList(App.getCurrentUser().getBranch());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                com.mak.Constant.cancelDialogue();
                if (response != null && response.isSuccessful() && response != null && response.body() != null && response.body().size() > 0) {
                    JsonObject salesMenObject = response.body();
                    Set<String> allKeys = salesMenObject.keySet();
                    List<String> allNames = new ArrayList<>();
                    for (String key : allKeys) {
                        SalesMenMode menMode = new SalesMenMode();
                        menMode.setSalesMenId(key);
                        menMode.setSalesMenName(salesMenObject.get(key).getAsString());
                        allNames.add(menMode.getSalesMenName());
                        salesMenList.add(menMode);
                    }
                    List<String> dataset = new LinkedList<>(allNames);
                    spSalesMenList.attachDataSource(dataset);
                    if (salesMenList != null && salesMenList.size() > 0) {
                        currentSalesMen = salesMenList.get(0);

                    }
                    spSalesMenList.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                        @Override
                        public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                            currentSalesMen = salesMenList.get(position);
                        }
                    });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                Log.e("DATA", "DATA");
                com.mak.Constant.cancelDialogue();
            }
        });


    }

    private void findView() {

        tvPartyName = (EditText) findViewById(R.id.tvPartyName);
        tvPartyAddress = (EditText) findViewById(R.id.tvPartyAddress);
        tvPartyNumber = (EditText) findViewById(R.id.tvPartyNumber);
        tvPersonNumber = (TextView) findViewById(R.id.tvPersonNumber);
        btnSkip = (TextView) findViewById(R.id.btnSkip);
        //tvPartyName.requestFocus();
        tvPartyAddress.clearFocus();
        btnSkip.setOnClickListener(this);
        tvPersonNumber.setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);
        spSalesMenList = (NiceSpinner) findViewById(R.id.nice_spinner);


    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                onTableOccupied();

                break;

            case R.id.btnSkip:
                onTableOccupied();
                break;
            case R.id.tvPersonNumber:
                Constant.selectPersonDialog(PartyInfoActivity.this);

                break;
        }

    }

    private void onTableOccupied() {

        if (tvPersonNumber.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please select number of person", Toast.LENGTH_LONG).show();
        } else {

            /*TableModel currentTableSelect = new TableModel();
            currentTableSelect.setTable_no(String.valueOf(Constant.CurrentTable));
            currentTableSelect.setTable_status(Constant.TABLE_STATUS_OCCUPIED);
*/
            /*

            PartyInfoModel pInfo = new PartyInfoModel();
            pInfo.setParty_name(tvPartyName.getText().toString());
            pInfo.setParty_address(tvPartyAddress.getText().toString());
            pInfo.setParty_number(tvPartyNumber.getText().toString());
            pInfo.setParty_person(tvPersonNumber.getText().toString());
            pInfo.setSalesmen_id(currentSalesMen.getSalesMenId());
            pInfo.setSalesmen_name(currentSalesMen.getSalesMenName());

            currentTableSelect.setPartyInfo(pInfo);
            App.StoreTableValue(currentTableSelect);*/


            Cart cart = new Cart();
            cart.setTableCode(String.valueOf(Constant.CurrentTable));

            cart.setPartyName(tvPartyName.getText().toString());
            cart.setPartyAddr(tvPartyAddress.getText().toString());
            cart.setPartyContact(tvPartyNumber.getText().toString());
            cart.setPaxNo(tvPersonNumber.getText().toString());
            cart.setCaptain(currentSalesMen.getSalesMenId());
            cart.setEnteredBy(currentSalesMen.getSalesMenName());
            cart.setStoreCode(String.valueOf(Constant.StoreCode));
            App.StoreCartValue(cart);
            startActivity(new Intent(getApplicationContext(), MenuItemsActivity.class).putExtra(Constant.PARTY_INFO, cart));
            finish();
        }
    }
}
