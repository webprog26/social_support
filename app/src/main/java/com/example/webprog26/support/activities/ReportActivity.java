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
import com.example.webprog26.support.views.reports.FragmentReport;

import java.util.Date;

public class ReportActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        if(getIntent().getIntExtra(Constants.REPORT_MODE, Constants.REPORT_CREATE) == Constants.REPORT_CREATE){
            Date mStartDate = (Date)getIntent().getSerializableExtra(Constants.DATE_REPORT_START);
            Date mFinishDate = (Date)getIntent().getSerializableExtra(Constants.DATE_REPORT_FINISH);
            return FragmentReport.newInstance(Constants.REPORT_CREATE, mStartDate, mFinishDate);
        } else{
            return FragmentReport.newInstance(Constants.REPORT_VIEW);
        }
    }
}
