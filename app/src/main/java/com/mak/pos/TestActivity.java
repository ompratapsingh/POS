package com.mak.pos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.mak.pos.SearchBar.MaterialSearchBar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class TestActivity extends AppCompatActivity {

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

/*

            if (MainActivity.EdtSearch.getText().toString().length() > 0) {
                MainActivity.mt_clear.setVisibility(View.VISIBLE);
            } else {

                if (MainActivity.EdtSearch.getText().toString().length() > 0) {
                    MainActivity.mt_clear.setVisibility(View.VISIBLE);
                } else {
                    if(MaterialSearchBar.IsCancelSearch) {


                        MainActivity.llBack.performClick();
                    }

                    MainActivity.mt_clear.setVisibility(View.GONE);
                }
            }
            if (MaterialSearchBar.IsCancelSearch) {
                // MainActivity.llBack.performClick();
            }
*/


        }


        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        /*MaterialSearchView searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });*/


        MaterialSearchBar searchView = (MaterialSearchBar) findViewById(R.id.searchBar);

        searchView.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // IsSearch = enabled;
                //  MainActivity.mt_clear.setVisibility(View.GONE);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
               /* AllUserCtx.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.EdtSearch.getText().toString().length() > 0) {
                            if (Constants.USERDataList != null && Constants.USERDataList.size() > 0) {
                                SetFilteredData(MainActivity.EdtSearch.getText().toString().toLowerCase());
                            } else {
                                getUserFromServer("All");
                            }
                        }
                    }
                });
*/
              /*  MainActivity.EdtSearch.setFocusable(false);
                MainActivity.EdtSearch.setFocusableInTouchMode(true);*/

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }
}
