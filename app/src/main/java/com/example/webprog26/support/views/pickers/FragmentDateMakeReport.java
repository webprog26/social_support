package com.example.webprog26.support.views.pickers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.example.webprog26.support.R;
import com.example.webprog26.support.activities.ReportActivity;
import com.example.webprog26.support.constants.Constants;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by webprog26 on 08.04.2016.
 */
public class FragmentDateMakeReport extends DialogFragment {

    private static final String TAG = "FragmentDateMakeReport";
    private Date mStartDate;
    private Date mFinishDate;

    public static FragmentDateMakeReport newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(Constants.INIT_DATE, date);
        FragmentDateMakeReport dateMakeReport = new FragmentDateMakeReport();
        dateMakeReport.setArguments(args);

        return dateMakeReport;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mStartDate = (Date) getArguments().getSerializable(Constants.INIT_DATE);
        mFinishDate = (Date) getArguments().getSerializable(Constants.INIT_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mStartDate);
        final int mYear = calendar.get(Calendar.YEAR);
        final int mMonth = calendar.get(Calendar.MONTH);
        final int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        View v = getActivity().getLayoutInflater().inflate(R.layout.date_make_report, null);

        DatePicker mStartDatePicker = (DatePicker) v.findViewById(R.id.makeReportStartDate);
        mStartDatePicker.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mStartDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
            }
        });
        DatePicker mFinishDatePicker = (DatePicker) v.findViewById(R.id.makeReportFinishDate);
        mFinishDatePicker.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mFinishDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
            }
        });

        android.support.v7.widget.AppCompatButton mMakeReportButton = (android.support.v7.widget.AppCompatButton)
                v.findViewById(R.id.btnAdd);
            mMakeReportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ReportActivity.class);
                    intent.putExtra(Constants.REPORT_MODE, Constants.REPORT_CREATE);
                    intent.putExtra(Constants.DATE_REPORT_START, mStartDate);
                    intent.putExtra(Constants.DATE_REPORT_FINISH, mFinishDate);
                    startActivity(intent);
                    FragmentDateMakeReport.this.dismiss();
                    Log.i(TAG, "startDate: " + mStartDate);
                    Log.i(TAG, "finishDate: " + mFinishDate);
                }
            });
        android.support.v7.widget.AppCompatButton mCancelMakingReportButton = (android.support.v7.widget.AppCompatButton)
                v.findViewById(R.id.btnCancel);
            mCancelMakingReportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentDateMakeReport.this.dismiss();
                }
            });


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .create();
    }
}
