package com.mak.pos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.pavlospt.CircleView;
import com.mak.Api.ApiClient;
import com.mak.Api.ApiInterface;
import com.mak.App;
import com.mak.Constant;
import com.mak.pos.MenuAdapter.AdapterSectionRecycler;
import com.mak.pos.MenuAdapter.SectionHeader;
import com.mak.pos.Model.POJO.Cart;
import com.mak.pos.Model.POJO.Items;
import com.mak.pos.Model.POJO.MenuCategory;
import com.mak.pos.Model.MenuItem;
import com.mak.pos.Model.POJO.MenuItemInfo;
import com.mak.pos.SearchBar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class MenuItemsActivity extends AppCompatActivity {

    RecyclerView rvMenu;
    AdapterSectionRecycler adapterRecycler;
    List<SectionHeader> sectionHeaders;
    ImageView ivCart, mt_clear;
    CircleView tvBadge;
    EditText EdtSearch;
    private boolean IsSearch;


    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


            if (EdtSearch.getText().toString().length() > 0) {
                mt_clear.setVisibility(View.VISIBLE);
            } else {

                if (EdtSearch.getText().toString().length() > 0) {
                    mt_clear.setVisibility(View.VISIBLE);
                } else {
                    if (MaterialSearchBar.IsCancelSearch) {

                        setAdapter();
                        //    MainActivity.llBack.performClick();
                    }

                    mt_clear.setVisibility(View.GONE);
                }
            }
            if (MaterialSearchBar.IsCancelSearch) {
                // MainActivity.llBack.performClick();
            }


        }


        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        oBadgeSet();
    }

    private void oBadgeSet() {
        Cart cart = App.getCartValue(String.valueOf(com.mak.pos.Utility.Constant.CurrentTable));
        if (cart != null) {
            ArrayList<Items> cartlist = cart.getItems();
            if (cartlist != null && cartlist.size() > 0) {
                tvBadge.setVisibility(View.VISIBLE);
                if (cartlist.size() > 99) {
                    tvBadge.setTitleText("99+");

                } else {
                    tvBadge.setTitleText(String.valueOf(cartlist.size()));

                }

            } else {
                tvBadge.setVisibility(View.GONE);
            }
        } else {
            tvBadge.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);

        findView();
        serechViewSet();

    }

    private void serechViewSet() {


        MaterialSearchBar searchView = (MaterialSearchBar) findViewById(R.id.searchBar);
        EdtSearch = (EditText) searchView.findViewById(R.id.mt_editText);
        mt_clear = (ImageView) searchView.findViewById(R.id.mt_clear);
        EdtSearch.addTextChangedListener(watcher);
        searchView.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                IsSearch = enabled;
                mt_clear.setVisibility(View.GONE);
            }

            @Override
            public void onSearchConfirmed(final CharSequence text) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (EdtSearch.getText().toString().length() > 0) {

                            if (sectionHeaders != null) {
                                List<SectionHeader> searchResult = getFilterData(text.toString());
                                rvMenu.setAdapter(new AdapterSectionRecycler(MenuItemsActivity.this, searchResult));
                            }

                        }
                    }
                });
                EdtSearch.setFocusable(false);
                EdtSearch.setFocusableInTouchMode(true);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    public List<SectionHeader> getFilterData(String searchText) {
        ArrayList<MenuItemInfo> items = new ArrayList<>();
        for (SectionHeader sectionHeader : sectionHeaders) {
            for (MenuItemInfo child : sectionHeader.getChildItems()) {
                if (child.getItemMasterName().toLowerCase().toString().contains(searchText.toLowerCase().toString())) {
                    items.add(child);
                }
            }
        }

        List<SectionHeader> sectionHeaders = new ArrayList<>();
        MenuCategory category = new MenuCategory();
        category.setDescription("SearchFilter");
        category.setMenuItemInfo(items);
        sectionHeaders.add(new SectionHeader(items, category, 1));
        return sectionHeaders;
    }

    private void findView() {

        rvMenu = (RecyclerView) findViewById(R.id.rvMenuItems);
        ivCart = (ImageView) findViewById(R.id.ivCart);
        tvBadge = (CircleView) findViewById(R.id.tvBadge);


        //setLayout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMenu.setLayoutManager(linearLayoutManager);
        rvMenu.setHasFixedSize(true);


        if (Constant.MenuCategory == null || Constant.MenuCategory.size() == 0) {
            Constant.ShowProgressHud(MenuItemsActivity.this, "");

            getCategory();
        } else if (!Constant.tableCategoryItemhMap.containsKey(com.mak.pos.Utility.Constant.CurrentTable)) {
            Constant.ShowProgressHud(MenuItemsActivity.this, "");

            getCategoryItem();
        } else if (Constant.tableCategoryItemhMap.containsKey(com.mak.pos.Utility.Constant.CurrentTable)) {
            setItems();

        }


        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*TableModel currentTable = App.getTableValue(String.valueOf(com.mak.pos.Utility.Constant.CurrentTable));
                if(currentTable!=null&&currentTable.getHashMap()!=null&&currentTable.getHashMap().size()>0) {*/
                startActivity(new Intent(getApplicationContext(), CartViewActivity.class));
                /*}else {
                    Toast.makeText(getApplicationContext(),"Cart is empty",Toast.LENGTH_LONG).show();
                }*/
                //finish();
            }
        });
    }

    private void setItems() {
        HashMap<MenuCategory, ArrayList<MenuItemInfo>> tableItems = Constant.tableCategoryItemhMap.get(com.mak.pos.Utility.Constant.CurrentTable);
        for (int i = 0; i < Constant.MenuCategory.size(); i++) {

            ArrayList<MenuItemInfo> items = tableItems.get(Constant.MenuCategory.get(i));
            if (items != null && items.size() > 0)
                sectionHeaders.add(new SectionHeader(items, Constant.MenuCategory.get(i), i));
        }
        setAdapter();
    }

    private void getCategory() {


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<ArrayList<MenuCategory>> call = apiInterface.onGetAllCategory();
        call.enqueue(new Callback<ArrayList<MenuCategory>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<MenuCategory>> call, Response<ArrayList<MenuCategory>> response) {
                if (response != null && response.isSuccessful() && response.body() != null && response.body().size() > 0) {
                    Constant.MenuCategory = response.body();
                    getCategoryItem();

                } else {
                    Constant.cancelDialogue();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<MenuCategory>> call, Throwable t) {
                Constant.cancelDialogue();
            }
        });

    }

    private void getCategoryItem() {


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        retrofit2.Call<ArrayList<MenuItemInfo>> call = apiInterface.onGetAllCategoryItem(String.valueOf(com.mak.pos.Utility.Constant.CurrentTable));
        call.enqueue(new Callback<ArrayList<MenuItemInfo>>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList<MenuItemInfo>> call, Response<ArrayList<MenuItemInfo>> response) {
                Constant.cancelDialogue();
                if (response != null && response.isSuccessful() && response != null && response.body() != null && response.body().size() > 0) {

                    Constant.MenuCategoryItem = response.body();
                    HashMap<String, ArrayList<MenuItemInfo>> categoryListHM = new HashMap<>();
                    Constant.categoryItemhMap = new HashMap<>();
                    for (MenuItemInfo info : Constant.MenuCategoryItem) {
                        ArrayList<MenuItemInfo> IitemList = null;
                        if (categoryListHM.containsKey(info.getArticle())) {
                            IitemList = categoryListHM.get(info.getArticle());
                        }
                        if (IitemList == null)
                            IitemList = new ArrayList<>();
                        IitemList.add(info);

                        categoryListHM.put(info.getArticle(), IitemList);

                    }

                    for (MenuCategory category : Constant.MenuCategory) {
                        if (category != null && categoryListHM.containsKey(category.getCode())) {
                            category.setMenuItemInfo(categoryListHM.get(category.getCode()));
                            Constant.categoryItemhMap.put(category, categoryListHM.get(category.getCode()));
                        }
                    }
                    Constant.tableCategoryItemhMap.put(com.mak.pos.Utility.Constant.CurrentTable, Constant.categoryItemhMap);
                    setItems();


                }
            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList<MenuItemInfo>> call, Throwable t) {
                Constant.cancelDialogue();
            }
        });


    }

    private void setAdapter() {
        adapterRecycler = new AdapterSectionRecycler(MenuItemsActivity.this, sectionHeaders);
        rvMenu.setAdapter(adapterRecycler);

    }

}
