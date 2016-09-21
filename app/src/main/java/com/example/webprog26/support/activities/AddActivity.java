package com.example.webprog26.support.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.views.editors.FragmentAddFamily;
import com.example.webprog26.support.views.editors.FragmentAddPerson;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends SingleFragmentActivity {

    private final static String TAG = "AddActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Fragment createFragment() {


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(FragmentAddFamily.newInstance());
        fragments.add(FragmentAddPerson.newInstance());
        fragments.add(FragmentAddFamily.newInstance(Constants.FAMILY_EDIT));
        fragments.add(FragmentAddPerson.newInstance(Constants.PERSON_EDIT));

        final int fragmentDetector = getIntent().getIntExtra(Constants.MODE_TO_ADD_CLIENT, 0);

        Log.i(TAG, "fragmentDetector: " + fragmentDetector);

        return fragments.get(fragmentDetector);
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getIntent().getIntExtra(Constants.MODE_TO_ADD_CLIENT, 0) == Constants.FAMILY_EDIT){
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(Constants.PROFILE_TO_OPEN, Constants.FAMILY);
            startActivity(intent);
            finish();
        } else if(getIntent().getIntExtra(Constants.MODE_TO_ADD_CLIENT, 0) == Constants.PERSON_EDIT){
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(Constants.PROFILE_TO_OPEN, Constants.PERSON);
            startActivity(intent);
            finish();
        }
    }
}
