package com.example.webprog26.support.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.webprog26.support.R;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.views.map.FragmentMap;

public class MapActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return FragmentMap.newInstance(getIntent().getIntExtra(Constants.MODE_TO_ADD_CLIENT, 0),
                                       getIntent().getIntExtra(Constants.PROFILE_TO_OPEN, 0));
    }


}
