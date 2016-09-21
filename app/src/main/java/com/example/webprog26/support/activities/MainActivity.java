package com.example.webprog26.support.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.example.webprog26.support.R;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.views.FragmentMain;

public class MainActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new FragmentMain();
    }


}
