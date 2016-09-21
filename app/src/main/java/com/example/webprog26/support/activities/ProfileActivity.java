package com.example.webprog26.support.activities;

import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.webprog26.support.R;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.interfaces.OnBackKeyPressedListener;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.Transporter;
import com.example.webprog26.support.views.profiles.FragmentFamilyProfile;

import java.util.List;

public class ProfileActivity extends SingleFragmentActivity {

    private static final String TAG = "ProfileLogs";

    @Override
    public Fragment createFragment() {
        if(getIntent().getIntExtra(Constants.PROFILE_TO_OPEN, Constants.FAMILY) == Constants.FAMILY){
            return FragmentFamilyProfile.newInstance(Constants.FAMILY);
        } else if(getIntent().getIntExtra(Constants.PROFILE_TO_OPEN, Constants.PERSON) == Constants.PERSON){
            return FragmentFamilyProfile.newInstance(Constants.PERSON);
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "ANIMATED_FABS" + PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.ANIMATED_FABS, false));
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if(fragmentList != null){
            for(Fragment fragment: fragmentList){
                if(fragment instanceof OnBackKeyPressedListener){
                    if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.ANIMATED_FABS, false)){
                      ((OnBackKeyPressedListener) fragment).onBackPressed();
                    } else {
                      finish();
                      super.onBackPressed();;
                    }
                }
            }
        }
    }
}
