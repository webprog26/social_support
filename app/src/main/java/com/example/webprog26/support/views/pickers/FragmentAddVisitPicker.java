package com.example.webprog26.support.views.pickers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;

import com.example.webprog26.support.R;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.models.Visit;
import com.example.webprog26.support.providers.visit.VisitProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by webprog26 on 11.03.2016.
 */
public class FragmentAddVisitPicker extends DialogFragment{

    private final static String TAG = "FragmentAddVisitPicker";

    private static final int REQUEST_DATE = 0;
    private AppCompatButton btnSetVisitDate;
    private Date mDate;
    private VisitProvider mVisitProvider;
    private long visitsAmount;

    public static FragmentAddVisitPicker newInstance(long clientId, int mode){
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID_TO_VISIT, clientId);
        args.putInt(Constants.MODE_TO_ADD_CLIENT, mode);
        FragmentAddVisitPicker fragment  = new FragmentAddVisitPicker();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View v = getActivity().getLayoutInflater().inflate(R.layout.add_visit, null);
        mVisitProvider = new VisitProvider(getActivity());

        mDate = new Date(System.currentTimeMillis());

        AppCompatButton btnAdd = (AppCompatButton) v.findViewById(R.id.btnAdd);
        AppCompatButton btnCancel = (AppCompatButton) v.findViewById(R.id.btnCancel);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments().getInt(Constants.MODE_TO_ADD_CLIENT) == Constants.FAMILY) {
                    Visit.Builder builder = Visit.newBuilder();
                    builder.setVisitDate(mDate)
                            .setClientId(getArguments().getLong(Constants.CLIENT_ID_TO_VISIT));
                    Visit visit = builder.build();
                    Log.i(TAG, "getArguments().getInt(Constants.MODE_TO_ADD_CLIENT " + getArguments().getInt(Constants.MODE_TO_ADD_CLIENT));
                    Log.i(TAG, "mDate " + mDate.toString());
                    Log.i(TAG, "getArguments().getLong(Constants.CLIENT_ID_TO_VISIT) " + getArguments().getLong(Constants.CLIENT_ID_TO_VISIT));
                    Log.i(TAG, "visit.getClientId " + visit.getClientId());
                    mVisitProvider.insertFamilyVisits(visit);

                    visitsAmount = mVisitProvider.familyVisitsCount(visit.getClientId());
                    sendResult(Activity.RESULT_OK);
                } else if (getArguments().getInt(Constants.MODE_TO_ADD_CLIENT) == Constants.PERSON) {
                    Visit.Builder builder = Visit.newBuilder();
                    builder.setVisitDate(mDate)
                            .setClientId(getArguments().getLong(Constants.CLIENT_ID_TO_VISIT));
                    Visit visit = builder.build();
                    Log.i(TAG, "getArguments().getInt(Constants.MODE_TO_ADD_CLIENT " + getArguments().getInt(Constants.MODE_TO_ADD_CLIENT));
                    Log.i(TAG, "mDate " + mDate.toString());
                    Log.i(TAG, "getArguments().getLong(Constants.CLIENT_ID_TO_VISIT) " + getArguments().getLong(Constants.CLIENT_ID_TO_VISIT));
                    Log.i(TAG, "visit.getClientId " + visit.getClientId());
                    mVisitProvider.insertPersonVisit(visit);

                    visitsAmount = mVisitProvider.personVisitsCount(visit.getClientId());
                    sendResult(Activity.RESULT_OK);
                }
            FragmentAddVisitPicker.this.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAddVisitPicker.this.dismiss();
            }
        });

        btnSetVisitDate = (AppCompatButton) v.findViewById(R.id.btnDateOfVisit);
        btnSetVisitDate.setText(new SimpleDateFormat("dd:MM:yyy").format(mDate));
        btnSetVisitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentDatePicker dialog = FragmentDatePicker.newInstance(mDate);
                dialog.setTargetFragment(FragmentAddVisitPicker.this, REQUEST_DATE);
                dialog.show(fm, Constants.DIALOG_DATE);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_DATE){
            mDate = (Date) data.getSerializableExtra(Constants.EXTRA_DATE);
            btnSetVisitDate.setText(new SimpleDateFormat("dd:MM:yyyy").format(mDate));
        }
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(Constants.VISITS_AMOUNT_TO_PROFILE, visitsAmount);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
