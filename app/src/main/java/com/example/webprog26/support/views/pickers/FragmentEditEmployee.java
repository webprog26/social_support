package com.example.webprog26.support.views.pickers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.webprog26.support.R;
import com.example.webprog26.support.constants.Constants;

/**
 * Created by webprog26 on 29.03.2016.
 */
public class FragmentEditEmployee extends DialogFragment {

    private static final String TAG = "FragmentEditEmployee";

    private EditText mEmployeeFirstName, mEmployeeSecondName;

    public static FragmentEditEmployee newInstance(){

        FragmentEditEmployee fragment = new FragmentEditEmployee();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.edit_employee_data, null);

        final TextInputLayout mEmployeeFNameInputLayout = (TextInputLayout) v.findViewById(R.id.emplFNameInpLayout);
        final TextInputLayout mEmployeeSecondNameInputLayout = (TextInputLayout) v.findViewById(R.id.emplSNameInpLayout);

        mEmployeeFirstName = (EditText) v.findViewById(R.id.etEmplFName);
        mEmployeeSecondName = (EditText) v.findViewById(R.id.etEmplSName);

        AppCompatButton mBtnAdd = (AppCompatButton) v.findViewById(R.id.btnAddEmployeeData);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUserInput(mEmployeeFirstName, mEmployeeFNameInputLayout) || (!validateUserInput(mEmployeeSecondName, mEmployeeSecondNameInputLayout)))
                    return;

                SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                mSharedPreferences.edit()
                        .putString(Constants.EMPLOYEE_FIRST_NAME, mEmployeeFirstName.getText().toString())
                        .putString(Constants.EMPLOYEE_SECOND_NAME, mEmployeeSecondName.getText().toString())
                        .commit();

                sendResult(Activity.RESULT_OK);
                FragmentEditEmployee.this.dismiss();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        AppCompatButton mBtnCancel = (AppCompatButton) v.findViewById(R.id.btnCancelEmployeeData);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentEditEmployee.this.dismiss();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });


        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.enter_your_data)
                .setView(v)
                .create();
    }

    //Проверка валидности введенных данных
    private boolean validateUserInput(EditText editText, TextInputLayout textInputLayout) {
        String errorMessage = null;
        switch (editText.getId()){
            case R.id.etEmplFName:
                errorMessage = getString(R.string.error_employee_first_name);
                break;
            case R.id.etEmplSName:
                errorMessage = getString(R.string.error_employee_second_name);
                break;
        }
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

    private void sendResult(int resultCode){
        if(getTargetFragment() == null) return;

        Intent intent = new Intent();
        intent.putExtra(Constants.EMPLOYEE_FIRST_NAME, mEmployeeFirstName.getText().toString());
        intent.putExtra(Constants.EMPLOYEE_SECOND_NAME, mEmployeeSecondName.getText().toString());
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
