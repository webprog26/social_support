package com.example.webprog26.support.activities;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.webprog26.support.R;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.views.FragmentMain;
import com.example.webprog26.support.views.search.FragmentSearch;

import java.text.ParseException;

public class SearchActivity extends SingleFragmentActivity {

    private static final String TAG = "Searchhh";

    @Override
    public Fragment createFragment() {
        return FragmentSearch.newInstance(getIntent().getIntExtra(Constants.SEARCH_MODE, 0));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        FragmentSearch fragmentSearch = (FragmentSearch) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);

            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(Constants.PREF_SEARCH_QUERY, searchQuery)
                    .commit();
            Log.i(TAG, "searchQuery: " + searchQuery);
            if(getIntent().getIntExtra(Constants.SEARCH_MODE, 0) == Constants.FAMILY){
                try {
                    fragmentSearch.initialFamiliesSearch(Constants.FAMILY, searchQuery);
                } catch(ParseException pe){
                    pe.printStackTrace();
                }
            } else if(getIntent().getIntExtra(Constants.SEARCH_MODE, 0) == Constants.PERSON){
                try {
                    fragmentSearch.initialPersonsSearch(Constants.PERSON, searchQuery);
                } catch(ParseException pe){
                    pe.printStackTrace();
                }
            }
        }
    }
}
