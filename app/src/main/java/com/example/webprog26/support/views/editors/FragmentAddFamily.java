package com.example.webprog26.support.views.editors;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.webprog26.support.R;
import com.example.webprog26.support.activities.ProfileActivity;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.Photo;
import com.example.webprog26.support.models.Transporter;
import com.example.webprog26.support.providers.FamilyClientProvider;
import com.example.webprog26.support.utils.PictureUtils;
import com.example.webprog26.support.utils.SimpleCameraUtils;
import com.example.webprog26.support.views.pickers.FragmentDatePicker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by webprog26 on 06.03.2016.
 */
public class FragmentAddFamily extends Fragment {

    private static final String TAG = "FragmentAddFamily";
    private static final int REQUEST_ADD_DATE_SUPPORT_STARTED = 2;
    private static final int REQUEST_ADD_DATE_SUPPORT_FINISHED = 3;
    private static final int REQUEST_PHOTO = 4;

    private AppCompatSpinner mSpinnerCategories;
    private TextInputLayout mFirstNameInputLayout,
            mPatronymicInputLayout,
            mSecondNameInputLayout,
            mAddressInputLayout,
            mProblemInputLayout,
            mKidsAmountInputLayout,
            mAdultsAmountInputLayout;

    private EditText mInputFirstName,
            mInputPatronymic,
            mInputSecondName,
            mInputAddress,
            mInputProblem,
            mInputKidsAmount,
            mInputAdultsAmount;

    private AppCompatButton mBtnDateSupportStarts, mBtnDateSupportFinished;
    private AppCompatCheckBox mChbDLC, mChbSupport, mChbIsFinishedSupport;
    private de.hdodenhof.circleimageview.CircleImageView mClientPhoto;

    private String[] mSpinnerEntries;
    private FamilyClientProvider mProvider;

    private Photo mPhoto;
    private Uri mFileUri;

    private Date mDateSupportStarted, mDateSupportFinished;
    private RadioGroup mSupportResultsRadioGroup;

    private LinearLayout mSupportFinishedLayout, mSupportResultLayout;

    public static FragmentAddFamily newInstance(){
        FragmentAddFamily fragment = new FragmentAddFamily();
        return fragment;
    }

    public static FragmentAddFamily newInstance(int mode){
        Bundle args = new Bundle();
        args.putInt(Constants.MODE_TO_ADD_CLIENT, mode);
        FragmentAddFamily fragment = new FragmentAddFamily();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mProvider = new FamilyClientProvider(getActivity());
        mSpinnerEntries = getResources().getStringArray(R.array.families_categories);
        mDateSupportStarted = mDateSupportFinished = new Date(System.currentTimeMillis());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_family, container, false);

        final CoordinatorLayout mParentLayout = (CoordinatorLayout) view.findViewById(R.id.parentLayout);

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        getActionBar(activity, mToolbar);

        mSupportFinishedLayout = (LinearLayout) view.findViewById(R.id.supportFinishedLayout);
        mSupportResultLayout = (LinearLayout) view.findViewById(R.id.supportResultLayout);

        mSupportResultsRadioGroup = (RadioGroup) view.findViewById(R.id.rgSupportResult);


        mClientPhoto = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.clientPhoto);

        mFirstNameInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_first_name);
        mPatronymicInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_patronymic);
        mSecondNameInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_second_name);
        mAddressInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_address);
        mKidsAmountInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_kids_amount);
        mAdultsAmountInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_adults_amount);
        mProblemInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_problem);

        mInputFirstName = (EditText) view.findViewById(R.id.input_first_name);
        mInputPatronymic = (EditText) view.findViewById(R.id.input_patronymic);
        mInputSecondName = (EditText) view.findViewById(R.id.input_second_name);
        mInputAddress = (EditText) view.findViewById(R.id.input_address);
        mInputKidsAmount = (EditText) view.findViewById(R.id.input_kids_amount);
        mInputAdultsAmount = (EditText) view.findViewById(R.id.input_adults_amount);
        mInputProblem = (EditText) view.findViewById(R.id.input_problem);

        mSpinnerCategories = (AppCompatSpinner) view.findViewById(R.id.spinnerClientCategory);
        ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.support.design.R.layout.support_simple_spinner_dropdown_item, mSpinnerEntries);
        mSpinnerCategories.setAdapter(mSpinnerAdapter);

        mBtnDateSupportStarts = (AppCompatButton) view.findViewById(R.id.btnDateSupportStarts);
        mBtnDateSupportFinished = (AppCompatButton) view.findViewById(R.id.btnDateSupportFinished);

        mChbDLC = (AppCompatCheckBox) view.findViewById(R.id.chbIsDLC);
        mChbSupport = (AppCompatCheckBox) view.findViewById(R.id.chbIsUnderSupport);
        if(mChbSupport.isChecked()) mSupportFinishedLayout.setVisibility(View.VISIBLE);
        mChbDLC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChbSupport.isChecked()){
                    Snackbar snackBar = Snackbar.make(mParentLayout, R.string.disable_support, Snackbar.LENGTH_LONG);
                    snackBar.show();
                }
            }
        });
        mChbDLC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mChbSupport.isChecked()) {
                    buttonView.setChecked(true);
                }
            }
        });
        mChbSupport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!mChbDLC.isChecked()) {
                        mChbDLC.setChecked(true);
                    }
                    mSupportFinishedLayout.setVisibility(View.VISIBLE);

                    if(!mBtnDateSupportStarts.isEnabled()){
                        mBtnDateSupportStarts.setEnabled(true);
                    }
                } else {
                    if(mBtnDateSupportStarts.isEnabled()){
                        mBtnDateSupportStarts.setEnabled(false);
                        mChbIsFinishedSupport.setChecked(false);
                        mSupportFinishedLayout.setVisibility(View.GONE);
                    }
                }
            }
        });

        mChbIsFinishedSupport = (AppCompatCheckBox) view.findViewById(R.id.chbIsFinishedSupport);
        mChbIsFinishedSupport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mBtnDateSupportFinished.setEnabled(true);
                    mSupportResultLayout.setVisibility(View.VISIBLE);
                } else {
                    mBtnDateSupportFinished.setEnabled(false);
                    mSupportResultLayout.setVisibility(View.GONE);
                }
            }
        });
        mBtnDateSupportStarts.setText(getResources().getString(R.string.date_support_starts,
                new SimpleDateFormat("dd:MM:yyy").format(mDateSupportStarted)));
        mBtnDateSupportStarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mChbSupport.isChecked()) return;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentDatePicker dialog = FragmentDatePicker.newInstance(Constants.SUPPORT_STARTED_MODE, mDateSupportStarted);
                dialog.setTargetFragment(FragmentAddFamily.this, REQUEST_ADD_DATE_SUPPORT_STARTED);
                dialog.show(fm, Constants.DIALOG_DATE);
            }
        });

        mBtnDateSupportFinished.setText(getResources().getString(R.string.date_support_finished,
                new SimpleDateFormat("dd:MM:yyy").format(mDateSupportFinished)));
        mBtnDateSupportFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mChbIsFinishedSupport.isChecked()) return;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentDatePicker dialog = FragmentDatePicker.newInstance(Constants.SUPPORT_FINISHED_MODE, mDateSupportFinished);
                dialog.setTargetFragment(FragmentAddFamily.this, REQUEST_ADD_DATE_SUPPORT_FINISHED);
                dialog.show(fm, Constants.DIALOG_DATE);
            }
        });

        AppCompatButton mAddInfoButton = (AppCompatButton) view.findViewById(R.id.btnAdd);
        mAddInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserDataToDB();
                Log.i(TAG, "Info added ");
            }
        });


        AppCompatButton mCancelAddInfoButton = (AppCompatButton) view.findViewById(R.id.btnCancel);
        mCancelAddInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArguments() != null) {
                    if (getArguments().getInt(Constants.MODE_TO_ADD_CLIENT) == Constants.FAMILY_EDIT) {
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        intent.putExtra(Constants.PROFILE_TO_OPEN, Constants.FAMILY);
                        startActivity(intent);
                    }
                }
                getActivity().finish();
            }
        });

        if(getArguments() != null){
            if(getArguments().getInt(Constants.MODE_TO_ADD_CLIENT) == Constants.FAMILY_EDIT){
                FamilyClient client = (FamilyClient) Transporter.newInstance().getObject();
                intializeClientFields(client);
                if(savedInstanceState != null){
                    if(savedInstanceState.getString(TAG) != null){
                        mClientPhoto.setImageBitmap(SimpleCameraUtils.newInstance()
                                .cropAndScale(getActivity(), Uri.parse(savedInstanceState.getString(TAG)), 80));
                        mPhoto = new Photo(savedInstanceState.getString(TAG));
                        mPhoto.setFileName(savedInstanceState.getString(TAG));
                    }
                } else {
                    mClientPhoto.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_client_avatar));
                }
            }
        }

        return view;
    }

    private android.support.v7.app.ActionBar getActionBar(AppCompatActivity activity, Toolbar toolbar){
        activity.setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setLogo(R.drawable.app_icon);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        return actionBar;
    }

    //Проверка валидности введенных данных
    private boolean validateUserInput(EditText editText, TextInputLayout textInputLayout) {
        String errorMessage = null;

        switch (editText.getId()) {
            case R.id.input_first_name:
                errorMessage = getString(R.string.error_first_name);
                break;
            case R.id.input_patronymic:
                errorMessage = getString(R.string.error_patronymic);
                break;
            case R.id.input_second_name:
                errorMessage = getString(R.string.error_second_name);
                break;
            case R.id.input_address:
                errorMessage = getString(R.string.error_address);
                break;
            case R.id.input_kids_amount:
                errorMessage = getString(R.string.error_kids_amount);
                break;
            case R.id.input_adults_amount:
                errorMessage = getString(R.string.error_adults_amount);
                break;
            case R.id.input_problem:
                errorMessage = getString(R.string.error_problem);
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

    private void setUserDataToDB() {

        EditText[] inputs = new EditText[]{mInputFirstName, mInputPatronymic, mInputSecondName, mInputProblem, mInputKidsAmount, mInputAdultsAmount, mInputAddress};
        TextInputLayout[] inputLayouts = new TextInputLayout[]{mFirstNameInputLayout, mPatronymicInputLayout, mSecondNameInputLayout, mProblemInputLayout, mKidsAmountInputLayout, mAdultsAmountInputLayout, mAddressInputLayout};

        for(int i = 0; i < inputLayouts.length; i++){
            if(!validateUserInput(inputs[i], inputLayouts[i])) return;
        }

        FamilyClient familyClient = new FamilyClient();
        setFamilyDataToDb(familyClient);
        getActivity().finish();
    }

    private void setFamilyDataToDb(FamilyClient familyClient){
        final String familyClientFirstName = mInputFirstName.getText().toString();
        final String familyClientPatronymic = mInputPatronymic.getText().toString();
        final String familyClientSecondName = mInputSecondName.getText().toString();
        final String familyClientAddress = mInputAddress.getText().toString();
        final String familyClientCategory = mSpinnerCategories.getAdapter().getItem(mSpinnerCategories.getSelectedItemPosition()).toString();
        final int familyKidsAmount = Integer.parseInt(mInputKidsAmount.getText().toString());
        final int familyAdultsAmount = Integer.parseInt(mInputAdultsAmount.getText().toString());
        final String familyClientProblem = mInputProblem.getText().toString();
        final String familyIsDLC = String.valueOf(mChbDLC.isChecked());
        final String familyIsUnderSupport = String.valueOf(mChbSupport.isChecked());
        final String familyIsSupportFinished = String.valueOf(mChbIsFinishedSupport.isChecked());


        final Date familyClientRegisteredDate = new Date(System.currentTimeMillis());

        FamilyClient.Builder builder = FamilyClient.newBuilder();
        builder.setClientSecondName(familyClientSecondName)
                .setClientFirstName(familyClientFirstName)
                .setClientPatronymic(familyClientPatronymic)
                .setClientAddress(familyClientAddress)
                .setClientProblem(familyClientProblem)
                .setClientAdultsAmount(familyAdultsAmount)
                .setClientKidsAmount(familyKidsAmount)
                .setClientCategory(familyClientCategory)
                .setClientIsUnderSupport(familyIsUnderSupport)
                .setClientisDLC(familyIsDLC)
                .setClientIsSupportFinished(familyIsSupportFinished)
                .setClientRegDate(familyClientRegisteredDate);

                if(mPhoto != null){
                    builder.setClientPhoto(mPhoto.getFileName());
                } else {
                    builder.setClientPhoto(null);
                }


                if(mChbSupport.isChecked()){
                    builder.setDateSupportStarted(mDateSupportStarted);
                } else {
                    builder.setDateSupportStarted(null);
                }


        switch(mSupportResultsRadioGroup.getCheckedRadioButtonId()){
            case R.id.rbResultPositive:
                builder.setClientSupportResult("positive");
                break;
            case R.id.rbResultNegative:
                builder.setClientSupportResult("negative");
                break;
        }

        Log.i(TAG, "mDateSupportStarted: " + mDateSupportStarted);
        Log.i(TAG, "mDateSupportFinished: " + mDateSupportFinished);
        Log.i(TAG, "familyClientRegisteredDate: " + familyClientRegisteredDate);

                if(mChbIsFinishedSupport.isChecked()){
                    builder.setDateSupportFinished(mDateSupportFinished);
                } else {
                    builder.setDateSupportFinished(null);
                }

        if (getArguments() != null) {
            if (getArguments().getInt(Constants.MODE_TO_ADD_CLIENT) == Constants.FAMILY_EDIT) {
                FamilyClient client = (FamilyClient) Transporter.newInstance().getObject();
                builder.setClientId(client.getClientId());
                if(mPhoto != null){
                    builder.setClientPhoto(mPhoto.getFileName());
                } else {
                    builder.setClientPhoto(null);
                }
                client = (FamilyClient) builder.build();
                mProvider.updateFamilyClientData(client);

                Transporter.newInstance().setObject(client);
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(Constants.PROFILE_TO_OPEN, Constants.FAMILY);
                startActivity(intent);
                return;
            }
        }
                mProvider.insertFamilyClientDataToDb((FamilyClient) builder.build());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_camera){
            PackageManager pm = getActivity().getPackageManager();
            if(!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
                    !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
                return false;

            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mFileUri = SimpleCameraUtils.newInstance().getOutPutMediaFileUri(Constants.MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);

            startActivityForResult(intent, REQUEST_PHOTO);
            return true;
        }
        if(item.getItemId() == android.R.id.home){
            if (getArguments() != null) {
                if (getArguments().getInt(Constants.MODE_TO_ADD_CLIENT) == Constants.FAMILY_EDIT) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra(Constants.PROFILE_TO_OPEN, Constants.FAMILY);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
            NavUtils.navigateUpFromSameTask(getActivity());
            return true;
        }

    return super.onOptionsItemSelected(item);
    }

    private void intializeClientFields(FamilyClient client){
        mInputFirstName.setText(client.getClientFirstName());
        mInputPatronymic.setText(client.getClientPatronymic());
        mInputSecondName.setText(client.getClientSecondName());
        mInputAddress.setText(client.getClientAddress());
        mInputAdultsAmount.setText(String.valueOf(client.getClientAdultsAmount()));
        mInputKidsAmount.setText(String.valueOf(client.getClientKidsAmount()));

        if(client.getPhoto().getFileName() == null){
            mClientPhoto.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_client_avatar));
        } else {
            mClientPhoto.setImageBitmap(SimpleCameraUtils.newInstance()
                    .cropAndScale(getActivity(), Uri.parse(client.getPhoto().getFileName()), 80));
        }

        mInputProblem.setText(client.getClientProblem());
        if(!client.getClientisDLC().isEmpty()){
            if(client.getClientisDLC().equals("true")){
                mChbDLC.setChecked(true);
            } else {
                mChbDLC.setChecked(false);
            }
        }

        if(!client.getClientIsUnderSupport().isEmpty()){
            if(client.getClientIsUnderSupport().equals("true")){
                mChbSupport.setChecked(true);
            } else {
                mChbSupport.setChecked(false);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_ADD_DATE_SUPPORT_STARTED){
            mDateSupportStarted = (Date) data.getSerializableExtra(Constants.EXTRA_DATE_SUPPORT_STARTED);
            mBtnDateSupportStarts.setText(new SimpleDateFormat("dd:MM:yyyy").format(mDateSupportStarted));
        } else if(requestCode == REQUEST_ADD_DATE_SUPPORT_FINISHED){
            mDateSupportFinished = (Date) data.getSerializableExtra(Constants.EXTRA_DATE_SUPPORT_FINISHED);
            mBtnDateSupportFinished.setText(new SimpleDateFormat("dd:MM:yyyy").format(mDateSupportFinished));
        } else if(requestCode == REQUEST_PHOTO){
                mPhoto = new Photo(mFileUri.toString());
                Bitmap bitmap = SimpleCameraUtils.newInstance().cropAndScale(getActivity(), mFileUri, 240);
                mClientPhoto.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPhoto != null) {
            outState.putString(TAG, mPhoto.getFileName());
            Log.i(TAG, "Added to savedState: " + mPhoto.getFileName());
        }
    }
}
