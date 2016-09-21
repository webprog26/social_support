package com.example.webprog26.support.views.pickers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.webprog26.support.R;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.models.Service;
import com.example.webprog26.support.providers.service.ServiceProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by webprog26 on 14.03.2016.
 */
public class FragmentAddServicePicker extends DialogFragment {

    private static final String TAG = "FragmentAddService";

    private Date mDate;
    private static final int REQUEST_DATE = 0;
    private AppCompatButton mBtnSetServiceDate;
    private HashMap<String, String> mServicesByTypesMap;

    private int totalServices;
    private ServiceProvider mServiceProvider;

    public static FragmentAddServicePicker newInstance(long clientId, int mode){
        Bundle args = new Bundle();
        args.putLong(Constants.CLIENT_ID_TO_SERVICE, clientId);
        args.putInt(Constants.MODE_TO_ADD_CLIENT, mode);
        FragmentAddServicePicker fragment  = new FragmentAddServicePicker();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.add_service, null);
        mServiceProvider = new ServiceProvider(getActivity());
        final String[] servicesTypesDefs = new String[]{
                Constants.INFORMATION_SERVICE,
                Constants.SOCIO_PEDAGOGICAL_SERVICE,
                Constants.SOCIO_PSYCHOLOGICAL_SERVICE,
                Constants.JURISTICAL_SERVICE};

        mDate = new Date(System.currentTimeMillis());

        String[] mSpinnerEntries = getResources().getStringArray(R.array.services_types);

        final AppCompatSpinner mServicesTypesSpinner = (AppCompatSpinner) v.findViewById(R.id.servicesTypesSpinner);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),
                android.support.design.R.layout.support_simple_spinner_dropdown_item, mSpinnerEntries);
        mServicesTypesSpinner.setAdapter(mAdapter);

        final AppCompatCheckBox mIsDuringVisit = (AppCompatCheckBox) v.findViewById(R.id.chbIsServiceDuringVisit);
        final TextInputLayout mServicesAmountTextLayout = (TextInputLayout) v.findViewById(R.id.servicesAmountTextLayout);
        final EditText mServicesAmountEditText = (EditText) v.findViewById(R.id.etServicesAmount);
        AppCompatButton btnAdd = (AppCompatButton) v.findViewById(R.id.btnAdd);
        AppCompatButton btnCancel = (AppCompatButton) v.findViewById(R.id.btnCancel);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUserInput(mServicesAmountEditText, mServicesAmountTextLayout)) return;
                if(getArguments().getInt(Constants.MODE_TO_ADD_CLIENT) == Constants.FAMILY){
                    Service.Builder builder = Service.newBuilder();
                    builder.setServiceDate(mDate)
                            .setClientId(getArguments().getLong(Constants.CLIENT_ID_TO_SERVICE))
                            .setServicesAmount(Integer.parseInt(mServicesAmountEditText.getText().toString()))
                            .setServiceType(servicesTypesDefs[mServicesTypesSpinner.getSelectedItemPosition()])
                            .setDuringVisit(String.valueOf(mIsDuringVisit.isChecked()));
                    Service service = builder.build();
                    mServiceProvider.insertFamilyServices(service);
                    totalServices = mServiceProvider.familyServicesTotalCount(service.getClientId());
                    Log.i(TAG, "type " + service.getServiceType());
                    Log.i(TAG, "totalServices: " + totalServices);
                    Log.i(TAG, "INFORMATION: " + mServiceProvider.getFamilyServicesByTypes(service.getClientId()).get(Constants.INFORMATION_SERVICE));
                    Log.i(TAG, "SOC_PED: " + mServiceProvider.getFamilyServicesByTypes(service.getClientId()).get(Constants.SOCIO_PEDAGOGICAL_SERVICE));
                    Log.i(TAG, "SOC_PSYCHO: " + mServiceProvider.getFamilyServicesByTypes(service.getClientId()).get(Constants.SOCIO_PSYCHOLOGICAL_SERVICE));
                    Log.i(TAG, "JURIST: " + mServiceProvider.getFamilyServicesByTypes(service.getClientId()).get(Constants.JURISTICAL_SERVICE));
                    mServicesByTypesMap =  mServiceProvider.getFamilyServicesByTypes(service.getClientId());
                    sendResult(Activity.RESULT_OK);
                } else if(getArguments().getInt(Constants.MODE_TO_ADD_CLIENT) == Constants.PERSON){
                    Service.Builder builder = Service.newBuilder();
                    builder.setServiceDate(mDate)
                            .setClientId(getArguments().getLong(Constants.CLIENT_ID_TO_SERVICE))
                            .setServicesAmount(Integer.parseInt(mServicesAmountEditText.getText().toString()))
                            .setServiceType(servicesTypesDefs[mServicesTypesSpinner.getSelectedItemPosition()])
                            .setDuringVisit(String.valueOf(mIsDuringVisit.isChecked()));
                    Service service = builder.build();
                    mServiceProvider.insertPersonServices(service);
                    totalServices = mServiceProvider.personServicesTotalCount(service.getClientId());
                    mServicesByTypesMap =  mServiceProvider.getPersonServicesByTypes(service.getClientId());
                    sendResult(Activity.RESULT_OK);
                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                FragmentAddServicePicker.this.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                FragmentAddServicePicker.this.dismiss();
            }
        });

        mBtnSetServiceDate = (AppCompatButton) v.findViewById(R.id.btnServiceDate);
        mBtnSetServiceDate.setText(new SimpleDateFormat("dd:MM:yyy").format(mDate));
        mBtnSetServiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentDatePicker dialog = FragmentDatePicker.newInstance(mDate);
                dialog.setTargetFragment(FragmentAddServicePicker.this, REQUEST_DATE);
                dialog.show(fm, Constants.DIALOG_DATE);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.add_service)
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_DATE){
            mDate = (Date) data.getSerializableExtra(Constants.EXTRA_DATE);
            mBtnSetServiceDate.setText(new SimpleDateFormat("dd:MM:yyyy").format(mDate));
        }
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null) return;

        Intent intent = new Intent();
        intent.putExtra(Constants.TOTAL_SERVICES_AMOUNT_TO_PROFILE, totalServices);
        intent.putExtra(Constants.INFORMATION_SERVICES_TO_PROFILE, mServicesByTypesMap);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    //Проверка валидности введенных данных
    private boolean validateUserInput(EditText editText, TextInputLayout textInputLayout) {
        String errorMessage = getResources().getString(R.string.error_services_amount);
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(errorMessage);
            requestFocus(editText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view){
        if(view.requestFocus()){
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
