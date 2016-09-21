package com.example.webprog26.support.views.pickers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.webprog26.support.R;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.models.ClientMarker;
import com.example.webprog26.support.providers.markers.MarkersProvider;

/**
 * Created by webprog26 on 25.03.2016.
 */
public class FragmentAddMarker extends DialogFragment {

    private static final String TAG = "FragmentAddMarker";


    private MarkersProvider markersProvider;
    private String markerTitle;
    private double markerLatitude, markerLongitude;


    private AppCompatSpinner mMarkerCategoriesSpinner;
    private EditText mMarkerTitleEditText, mMarkerSnippetEditText;

    public static FragmentAddMarker newInstance(boolean modeToWork, int mode, long clientId, double latitude, double longitude){
        Bundle args = new Bundle();
        args.putBoolean(Constants.CLIENT_MARKER_EDIT_MODE, modeToWork);
        args.putInt(Constants.MODE_TO_ADD_CLIENT, mode);
        args.putLong(Constants.PROFILE_TO_OPEN, clientId);
        args.putDouble(Constants.CLIENT_MARKER_LATITUDE, latitude);
        args.putDouble(Constants.CLIENT_MARKER_LONGITUDE, longitude);
        FragmentAddMarker fragment  = new FragmentAddMarker();
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentAddMarker newInstance(boolean modeToWork, int mode, String title, String snippet, int category, long clientId, long markerId, double latitude, double longitude){
        Bundle args = new Bundle();
        args.putBoolean(Constants.CLIENT_MARKER_EDIT_MODE, modeToWork);
        args.putString(Constants.CLIENT_MARKER_TITLE, title);
        args.putString(Constants.CLIENT_MARKER_SNIPPET, snippet);
        args.putInt(Constants.CLIENT_MARKER_CATEGORY, category);
        args.putInt(Constants.MODE_TO_ADD_CLIENT, mode);
        args.putLong(Constants.PROFILE_TO_OPEN, clientId);
        args.putLong(Constants.CLIENT_MARKER_ID, markerId);
        args.putDouble(Constants.CLIENT_MARKER_LATITUDE, latitude);
        args.putDouble(Constants.CLIENT_MARKER_LONGITUDE, longitude);
        FragmentAddMarker fragment  = new FragmentAddMarker();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View layoutView = getActivity().getLayoutInflater().inflate(R.layout.add_marker, null);
        markersProvider = new MarkersProvider(getActivity());
        markerLatitude = getArguments().getDouble(Constants.CLIENT_MARKER_LATITUDE);
        markerLongitude = getArguments().getDouble(Constants.CLIENT_MARKER_LONGITUDE);

        String[] mSpinnerEntries = getResources().getStringArray(R.array.marker_categories);

        mMarkerCategoriesSpinner = (AppCompatSpinner) layoutView.findViewById(R.id.markerCategoriesSpinner);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(),
                android.support.design.R.layout.support_simple_spinner_dropdown_item, mSpinnerEntries);
        mMarkerCategoriesSpinner.setAdapter(mAdapter);

        final TextInputLayout mMarkerTitleTextLayout = (TextInputLayout) layoutView.findViewById(R.id.inputLayoutMarkerTitle);
        final TextInputLayout mMarkerSnippetTextLayout = (TextInputLayout) layoutView.findViewById(R.id.inputLayoutMarkerSnippet);
        mMarkerTitleEditText = (EditText) layoutView.findViewById(R.id.etMarkerTitle);
        mMarkerSnippetEditText = (EditText) layoutView.findViewById(R.id.etMarkerSnippet);

        if(getArguments().getBoolean(Constants.CLIENT_MARKER_EDIT_MODE) == Constants.MARKER_MODE_EDIT){
            initializeFieslds();
        }

        AppCompatButton btnAdd = (AppCompatButton) layoutView.findViewById(R.id.btnAddMarker);
        AppCompatButton btnCancel = (AppCompatButton) layoutView.findViewById(R.id.btnCancelMarker);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUserInput(mMarkerTitleEditText, mMarkerTitleTextLayout) || !validateUserInput(mMarkerSnippetEditText, mMarkerSnippetTextLayout))
                    return;
                if(getArguments().getInt(Constants.MODE_TO_ADD_CLIENT) == Constants.FAMILY){
                    ClientMarker.Builder builder = ClientMarker.newBuilder();
                        builder.setClientMarkerClientId(getArguments().getLong(Constants.PROFILE_TO_OPEN))
                               .setClientMarkerTitle(mMarkerTitleEditText.getText().toString())
                                .setClientMarkerSnippet(mMarkerSnippetEditText.getText().toString())
                                .setClientMarkerCategory(mMarkerCategoriesSpinner.getSelectedItemPosition())
                                .setClientMarkerLatitude(getArguments().getDouble(Constants.CLIENT_MARKER_LATITUDE))
                                .setClientMarkerLongitude(getArguments().getDouble(Constants.CLIENT_MARKER_LONGITUDE));

                    if(getArguments().getBoolean(Constants.CLIENT_MARKER_EDIT_MODE) == Constants.MARKER_MODE_EDIT){
                        builder.setMarkerId(getArguments().getLong(Constants.CLIENT_MARKER_ID));
                        ClientMarker clientMarker = builder.build();
                        markersProvider.updateFamilyClientMarker(clientMarker);
                        markerTitle = clientMarker.getTitle();
                    } else {
                        ClientMarker clientMarker = builder.build();
                        markersProvider.insertFamilyClientsMarker(clientMarker);
                        markerTitle = clientMarker.getTitle();
                    }
                    sendResult(Activity.RESULT_OK);
                } else if(getArguments().getInt(Constants.MODE_TO_ADD_CLIENT) == Constants.PERSON){

                    ClientMarker.Builder builder = ClientMarker.newBuilder();
                    builder.setClientMarkerClientId(getArguments().getLong(Constants.PROFILE_TO_OPEN))
                            .setClientMarkerTitle(mMarkerTitleEditText.getText().toString())
                            .setClientMarkerSnippet(mMarkerSnippetEditText.getText().toString())
                            .setClientMarkerCategory(mMarkerCategoriesSpinner.getSelectedItemPosition())
                            .setClientMarkerLatitude(getArguments().getDouble(Constants.CLIENT_MARKER_LATITUDE))
                            .setClientMarkerLongitude(getArguments().getDouble(Constants.CLIENT_MARKER_LONGITUDE));
                    if(getArguments().getBoolean(Constants.CLIENT_MARKER_EDIT_MODE) == Constants.MARKER_MODE_EDIT){
                        builder.setMarkerId(getArguments().getLong(Constants.CLIENT_MARKER_ID));
                        ClientMarker clientMarker = builder.build();
                        markersProvider.updatePersonClientMarker(clientMarker);
                        markerTitle = clientMarker.getTitle();
                    } else {
                        ClientMarker clientMarker = builder.build();
                        markersProvider.insertPersonClientsMarker(clientMarker);
                        markerTitle = clientMarker.getTitle();
                    }
                    sendResult(Activity.RESULT_OK);
                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                FragmentAddMarker.this.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                FragmentAddMarker.this.dismiss();
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(layoutView)
                .setTitle(getString(R.string.add_marker))
                .create();
    }

    //Проверка валидности введенных данных
    private boolean validateUserInput(EditText editText, TextInputLayout textInputLayout) {
        String errorMessage = null;

        switch (editText.getId()) {
            case R.id.etMarkerTitle:
                errorMessage = getString(R.string.error_first_name);
                break;
            case R.id.etMarkerSnippet:
                errorMessage = getString(R.string.error_patronymic);
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
        intent.putExtra(Constants.CLIENT_MARKER_TITLE, markerTitle);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    private void initializeFieslds(){
        mMarkerCategoriesSpinner.setSelection(getArguments().getInt(Constants.CLIENT_MARKER_CATEGORY));
        mMarkerTitleEditText.setText(getArguments().getString(Constants.CLIENT_MARKER_TITLE));
        mMarkerSnippetEditText.setText(getArguments().getString(Constants.CLIENT_MARKER_SNIPPET));
    }
}
