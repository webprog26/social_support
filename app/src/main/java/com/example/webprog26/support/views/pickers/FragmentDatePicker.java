package com.example.webprog26.support.views.pickers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import com.example.webprog26.support.R;
import com.example.webprog26.support.constants.Constants;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by webprog26 on 10.03.2016.
 */
public class FragmentDatePicker extends DialogFragment{



    private Date mDate;

    public static FragmentDatePicker newInstance(Date date)
    {
        Bundle args = new Bundle();
        args.putSerializable(Constants.INIT_DATE, date);
        FragmentDatePicker datePicker = new FragmentDatePicker();
        datePicker.setArguments(args);
        return datePicker;
    }

    public static FragmentDatePicker newInstance(int mode, Date date)
    {
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRA_DATE_SUPPORT, mode);
        args.putSerializable(Constants.INIT_DATE, date);
        FragmentDatePicker datePicker = new FragmentDatePicker();
        datePicker.setArguments(args);
        return datePicker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDate = (Date) getArguments().getSerializable(Constants.INIT_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);

        DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
            }
        });


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null) return;

        Intent intent = new Intent();
        if(getArguments() != null){
            if(getArguments().getInt(Constants.EXTRA_DATE_SUPPORT) == Constants.SUPPORT_STARTED_MODE){
                intent.putExtra(Constants.EXTRA_DATE_SUPPORT_STARTED, mDate);
            } else if(getArguments().getInt(Constants.EXTRA_DATE_SUPPORT) == Constants.SUPPORT_FINISHED_MODE) {
                intent.putExtra(Constants.EXTRA_DATE_SUPPORT_FINISHED, mDate);
            }
        }

        intent.putExtra(Constants.EXTRA_DATE, mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
