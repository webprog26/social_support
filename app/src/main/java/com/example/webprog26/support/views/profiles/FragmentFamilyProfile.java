package com.example.webprog26.support.views.profiles;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.webprog26.support.R;
import com.example.webprog26.support.activities.AddActivity;
import com.example.webprog26.support.activities.MapActivity;
import com.example.webprog26.support.activities.ProfileActivity;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.interfaces.OnBackKeyPressedListener;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.PersonClient;
import com.example.webprog26.support.models.Photo;
import com.example.webprog26.support.models.Transporter;
import com.example.webprog26.support.providers.service.ServiceProvider;
import com.example.webprog26.support.providers.visit.VisitProvider;
import com.example.webprog26.support.utils.PictureUtils;
import com.example.webprog26.support.utils.SimpleCameraUtils;
import com.example.webprog26.support.views.pickers.FragmentAddServicePicker;
import com.example.webprog26.support.views.pickers.FragmentAddVisitPicker;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by webprog26 on 07.03.2016.
 */
public class FragmentFamilyProfile extends Fragment implements OnBackKeyPressedListener{

    private static final String TAG = "FragmentFamilyProfile";
    private static final int REQUEST_ADD_VISIT = 1;
    private static final int REQUEST_ADD_SERVICE = 2;

    private VisitProvider mVisitProvider;
    private ServiceProvider mServiceProvider;
    private HashMap<String, String> mServicesByTypesMap;
    private android.support.v7.widget.ShareActionProvider mShareActionProvider;
    private SharedPreferences mSharedPreferences;

    private Boolean isFabOpen = false;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private TextView mProfileSecondName, mProfileFirstName, mProfilePatronymic, mProfileAddress,
             mProfileClientCategory, mProfileKidsAmount, mProfileAdultsAmount, mProfileServicesAmount, mProfileProblem, mProfileVisitsAmount, mProfileIsDLC,
             mProfileIsUnderSupport, mProfileDateSupportStarted, mProfileIsSupportFinished, mProfileDateSupportFinished, mProfileSupportResult;
    private TextView mInfoServicesAmount, mSocPedServicesAmount, mSocPsychoServicesAmount, mJuristicalServicesAmount;
    private ImageView mClientPhoto;
    private Animation fab_open,fab_close;
    private CoordinatorLayout mParentLayout;

    private TextView[] mDetailedServicesTextViews;
    private int[] resIDs;
    private String[] mapKeys;

    private FloatingActionButton mActionButton, mAddVisit, mEditProfile, mAddServices, mGetMap;

    public static FragmentFamilyProfile newInstance(int mode){
        Bundle args = new Bundle();
        args.putInt(Constants.FAMILY_PROFILE_RECEIVER, mode);
        FragmentFamilyProfile fragment = new FragmentFamilyProfile();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferences.edit().putBoolean(Constants.ANIMATED_FABS, isFabOpen).commit();
        mVisitProvider = new VisitProvider(getActivity());
        mServiceProvider = new ServiceProvider(getActivity());

        resIDs = new int[]{
                R.string.info,
                R.string.soc_ped,
                R.string.soc_psycho,
                R.string.jurist};

        mapKeys = new String[]{
                Constants.INFORMATION_SERVICE,
                Constants.SOCIO_PEDAGOGICAL_SERVICE,
                Constants.SOCIO_PSYCHOLOGICAL_SERVICE,
                Constants.JURISTICAL_SERVICE};
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.family_profile, container, false);

        mParentLayout = (CoordinatorLayout) view.findViewById(R.id.parentLayout);
        AppBarLayout mAppBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        mClientPhoto = (ImageView) view.findViewById(R.id.clientPhoto);
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        NestedScrollView mNestedScrollView = (NestedScrollView) view.findViewById(R.id.clientProfileNestedScrollView);


        mActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mActionButton.setVisibility(View.VISIBLE);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB();
            }
        });


        mEditProfile  = (FloatingActionButton) view.findViewById(R.id.fab1);
        mAddVisit = (FloatingActionButton) view.findViewById(R.id.fab2);
        mAddVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if(getArguments().getInt(Constants.FAMILY_PROFILE_RECEIVER) == Constants.FAMILY){
                    FamilyClient familyClient = (FamilyClient) Transporter.newInstance().getObject();
                    FragmentAddVisitPicker dialog = FragmentAddVisitPicker.newInstance(familyClient.getClientId(), Constants.FAMILY);
                    dialog.setTargetFragment(FragmentFamilyProfile.this, REQUEST_ADD_VISIT);
                    dialog.show(fm, Constants.DIALOG_DATE);
                }  else if(getArguments().getInt(Constants.FAMILY_PROFILE_RECEIVER) == Constants.PERSON){
                    PersonClient personClient = (PersonClient) Transporter.newInstance().getObject();
                    FragmentAddVisitPicker dialog = FragmentAddVisitPicker.newInstance(personClient.getClientId(), Constants.PERSON);
                    dialog.setTargetFragment(FragmentFamilyProfile.this, REQUEST_ADD_VISIT);
                    dialog.show(fm, Constants.DIALOG_DATE);
                }

            }
        });
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getArguments().getInt(Constants.FAMILY_PROFILE_RECEIVER) == Constants.FAMILY){
                    Intent intent = new Intent(getActivity(), AddActivity.class);
                    intent.putExtra(Constants.MODE_TO_ADD_CLIENT, Constants.FAMILY_EDIT);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Intent intent = new Intent(getActivity(), AddActivity.class);
                    intent.putExtra(Constants.MODE_TO_ADD_CLIENT, Constants.PERSON_EDIT);
                    startActivity(intent);
                    getActivity().finish();
                }

            }
        });

        mAddServices = (FloatingActionButton) view.findViewById(R.id.fab3);
        mAddServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if(getArguments().getInt(Constants.FAMILY_PROFILE_RECEIVER) == Constants.FAMILY){
                    FamilyClient familyClient = (FamilyClient) Transporter.newInstance().getObject();
                    FragmentAddServicePicker dialog = FragmentAddServicePicker.newInstance(familyClient.getClientId(), Constants.FAMILY);
                    dialog.setTargetFragment(FragmentFamilyProfile.this, REQUEST_ADD_SERVICE);
                    dialog.show(fm, Constants.DIALOG_DATE);
                }  else if(getArguments().getInt(Constants.FAMILY_PROFILE_RECEIVER) == Constants.PERSON){
                    PersonClient personClient = (PersonClient) Transporter.newInstance().getObject();
                    FragmentAddServicePicker dialog = FragmentAddServicePicker.newInstance(personClient.getClientId(), Constants.PERSON);
                    dialog.setTargetFragment(FragmentFamilyProfile.this, REQUEST_ADD_SERVICE);
                    dialog.show(fm, Constants.DIALOG_DATE);
                }
            }
        });
        mGetMap = (FloatingActionButton) view.findViewById(R.id.fab4);
        mGetMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mode = 0;
                long clientId = 0;
                if(getArguments().getInt(Constants.FAMILY_PROFILE_RECEIVER) == Constants.FAMILY){
                    FamilyClient familyClient = (FamilyClient) Transporter.newInstance().getObject();
                    Log.i(TAG, "Client MODE FAMILY: " + Constants.FAMILY);
                    mode = Constants.FAMILY;
                    clientId = familyClient.getClientId();
                } else if(getArguments().getInt(Constants.FAMILY_PROFILE_RECEIVER) == Constants.PERSON){
                    PersonClient personClient = (PersonClient) Transporter.newInstance().getObject();
                    Log.i(TAG, "Client MODE PERSON: " + Constants.PERSON);
                    mode = Constants.PERSON;
                    clientId = personClient.getClientId();
                }
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra(Constants.MODE_TO_ADD_CLIENT, mode);
                intent.putExtra(Constants.PROFILE_TO_OPEN, clientId);
                startActivity(intent);
            }
        });

        mProfileSecondName = (TextView) view.findViewById(R.id.clientProfileSecondName);
        mProfileFirstName = (TextView) view.findViewById(R.id.clientProfileFirstName);
        mProfilePatronymic = (TextView) view.findViewById(R.id.clientProfilePatronymic);
        mProfileAddress = (TextView) view.findViewById(R.id.clientProfileAddress);
        mProfileClientCategory = (TextView) view.findViewById(R.id.clientProfileCategory);
        mProfileAdultsAmount = (TextView) view.findViewById(R.id.clientProfileAdultsAmount);
        mProfileKidsAmount = (TextView) view.findViewById(R.id.clientProfileKidsAmount);
        mProfileServicesAmount = (TextView) view.findViewById(R.id.clientProfileServicesAmount);
        mProfileProblem = (TextView) view.findViewById(R.id.clientProfileProblem);
        mProfileVisitsAmount = (TextView) view.findViewById(R.id.clientProfileVisitsAmount);
        mProfileIsDLC = (TextView) view.findViewById(R.id.clientProfileIsDLC);
        mProfileIsUnderSupport = (TextView) view.findViewById(R.id.clientProfileIsUnderSupport);
        mProfileDateSupportStarted = (TextView) view.findViewById(R.id.clientProfileDateSupportStarted);
        mProfileIsSupportFinished = (TextView) view.findViewById(R.id.clientProfileIsSupportFinished);
        mProfileDateSupportFinished = (TextView) view.findViewById(R.id.clientProfileDateSupportFinished);
        mProfileSupportResult = (TextView) view.findViewById(R.id.clientProfileSupportResult);


        mInfoServicesAmount = (TextView) view.findViewById(R.id.clientInfoServicesAmount);
        mSocPedServicesAmount = (TextView) view.findViewById(R.id.clientSocPedServicesAmount);
        mSocPsychoServicesAmount = (TextView) view.findViewById(R.id.clientSocPsychoServicesAmount);
        mJuristicalServicesAmount = (TextView) view.findViewById(R.id.clientJuristPsychoServicesAmount);

        mDetailedServicesTextViews = new TextView[]{
                mInfoServicesAmount,
                mSocPedServicesAmount,
                mSocPsychoServicesAmount,
                mJuristicalServicesAmount};

        initToolbar(mToolbar, (AppCompatActivity) getActivity());
            if(getArguments().getInt(Constants.FAMILY_PROFILE_RECEIVER) == Constants.FAMILY){
                FamilyClient familyClient = (FamilyClient) Transporter.newInstance().getObject();
                Log.i(TAG, "familyClient.getFamilyClientId() " + familyClient.getClientId());
                intializeFieldsWithClientInfo(familyClient);
            } else if(getArguments().getInt(Constants.FAMILY_PROFILE_RECEIVER) == Constants.PERSON){
                PersonClient personClient = (PersonClient) Transporter.newInstance().getObject();
                intializeFieldsWithClientInfo(personClient);
            }
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.black));
            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        return view;
    }

    private void initToolbar(android.support.v7.widget.Toolbar toolbar, AppCompatActivity activity){
        activity.setSupportActionBar(toolbar);
        final ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setLogo(R.drawable.app_icon);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void intializeFieldsWithClientInfo(FamilyClient client){
        mCollapsingToolbarLayout.setTitle(client.getClientFirstName() + " " + client.getClientSecondName());

        mServicesByTypesMap = mServiceProvider.getFamilyServicesByTypes(client.getClientId());
        setServicesAmountByType(mServicesByTypesMap, mapKeys, mDetailedServicesTextViews, resIDs);

        mProfileSecondName.setText(getResources().getString(R.string.profile_second_name, client.getClientSecondName()));
        mProfileFirstName.setText(getResources().getString(R.string.profile_first_name, client.getClientFirstName()));
        mProfilePatronymic.setText(getResources().getString(R.string.profile_patronymic, client.getClientPatronymic()));
        mProfileAddress.setText(getResources().getString(R.string.profile_address, client.getClientAddress()));
        mProfileClientCategory.setText(getResources().getString(R.string.profile_category, client.getClientCategory()));
        mProfileProblem.setText(getResources().getString(R.string.profile_problem, client.getClientProblem()));
        mProfileServicesAmount.setText(getResources().getString(R.string.profile_services_amount, mServiceProvider.familyServicesTotalCount(client.getClientId())));
        mProfileAdultsAmount.setText(getResources().getString(R.string.profile_adults_amount, String.valueOf(client.getClientAdultsAmount())));
        mProfileKidsAmount.setText(getResources().getString(R.string.profile_kids_amount, String.valueOf(client.getClientKidsAmount())));
        mProfileVisitsAmount.setText(getResources().getString(R.string.profile_visits_amount, mVisitProvider.familyVisitsCount(client.getClientId())));
        if(client.getClientisDLC().equals("true")){
            mProfileIsDLC.setText(getResources().getString(R.string.profile_is_dlc, getResources().getString(R.string.yes_support_or_dlc)));
        } else {
            mProfileIsDLC.setText(getResources().getString(R.string.profile_is_dlc, getResources().getString(R.string.no_support_or_dlc)));
        }

        if(client.getClientIsUnderSupport().equals("true")){
            mProfileIsUnderSupport.setText(getResources().getString(R.string.profile_is_under_support, getResources().getString(R.string.yes_support_or_dlc)));
            mProfileDateSupportStarted.setVisibility(View.VISIBLE);
            mProfileDateSupportStarted.setText(getResources().getString(R.string.date_support_starts,
                    new SimpleDateFormat("dd:MM:yyyy").format(client.getDateSupportStarted())));

            if(client.getIsSupportFinished() != null){
                if(client.getIsSupportFinished().equals("true")){
                    mProfileIsUnderSupport.setText(getResources().getString(R.string.profile_is_under_support, getResources().getString(R.string.support_completed)));
                    mProfileIsSupportFinished.setText(getResources().getString(R.string.profile_is_support_finished, getResources().getString(R.string.yes_support_or_dlc)));
                    mProfileDateSupportFinished.setVisibility(View.VISIBLE);
                    mProfileDateSupportFinished.setText(getResources().getString(R.string.profile_support_finished_on_date,
                            new SimpleDateFormat("dd:MM:yyyy").format(client.getDateSupportFinished())));
                    mProfileSupportResult.setVisibility(View.VISIBLE);
                    mProfileSupportResult.setText(printResult(client.getSupportResult()));
                } else {
                    mProfileIsSupportFinished.setText(getResources().getString(R.string.profile_is_support_finished, getResources().getString(R.string.no_support_or_dlc)));
                }
            } else{
                Log.i(TAG, "client.getFamilyIsSupportFinished() " + "NULL");
            }

        } else {
            mProfileIsUnderSupport.setText(getResources().getString(R.string.profile_is_under_support, getResources().getString(R.string.no_support_or_dlc)));
            mProfileIsSupportFinished.setVisibility(View.GONE);
        }

        if(client.getPhoto().getFileName() == null)
        {
            mClientPhoto.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar_profile));
        } else {
            mClientPhoto.setImageBitmap(SimpleCameraUtils.newInstance().cropAndScale(getActivity(),
                    Uri.parse(client.getPhoto().getFileName()), 240));
        }
    }

    private void intializeFieldsWithClientInfo(PersonClient client){

        mProfileAdultsAmount.setVisibility(View.GONE);
        mProfileKidsAmount.setVisibility(View.GONE);

        mCollapsingToolbarLayout.setTitle(client.getClientFirstName() + " " + client.getClientSecondName());

        mServicesByTypesMap = mServiceProvider.getPersonServicesByTypes(client.getClientId());
        setServicesAmountByType(mServicesByTypesMap, mapKeys, mDetailedServicesTextViews, resIDs);

        mProfileSecondName.setText(getResources().getString(R.string.profile_second_name, client.getClientSecondName()));
        mProfileFirstName.setText(getResources().getString(R.string.profile_first_name, client.getClientFirstName()));
        mProfilePatronymic.setText(getResources().getString(R.string.profile_patronymic, client.getClientPatronymic()));
        mProfileAddress.setText(getResources().getString(R.string.profile_address, client.getClientAddress()));
        mProfileClientCategory.setText(getResources().getString(R.string.profile_category, client.getClientCategory()));
        mProfileProblem.setText(getResources().getString(R.string.profile_problem, client.getClientProblem()));
        mProfileServicesAmount.setText(getResources().getString(R.string.profile_services_amount, mServiceProvider.personServicesTotalCount(client.getClientId())));
        mProfileVisitsAmount.setText(getResources().getString(R.string.profile_visits_amount, mVisitProvider.personVisitsCount(client.getClientId())));
        if(client.getClientisDLC().equals("true")){
            mProfileIsDLC.setText(getResources().getString(R.string.profile_is_dlc, getResources().getString(R.string.yes_support_or_dlc)));
        } else {
            mProfileIsDLC.setText(getResources().getString(R.string.profile_is_dlc, getResources().getString(R.string.no_support_or_dlc)));
        }

        if(client.getClientIsUnderSupport().equals("true")){
            mProfileIsUnderSupport.setText(getResources().getString(R.string.profile_is_under_support, getResources().getString(R.string.yes_support_or_dlc)));
            mProfileDateSupportStarted.setVisibility(View.VISIBLE);
            mProfileDateSupportStarted.setText(getResources().getString(R.string.date_support_starts,
                    new SimpleDateFormat("dd:MM:yyyy").format(client.getDateSupportStarted())));
            if(client.getIsSupportFinished() != null){
                if(client.getIsSupportFinished().equals("true")){
                    mProfileIsUnderSupport.setText(getResources().getString(R.string.profile_is_under_support, getResources().getString(R.string.support_completed)));
                    mProfileIsSupportFinished.setText(getResources().getString(R.string.profile_is_support_finished, getResources().getString(R.string.yes_support_or_dlc)));
                    mProfileDateSupportFinished.setVisibility(View.VISIBLE);
                    mProfileDateSupportFinished.setText(getResources().getString(R.string.profile_support_finished_on_date,
                            new SimpleDateFormat("dd:MM:yyyy").format(client.getDateSupportFinished())));
                    mProfileSupportResult.setVisibility(View.VISIBLE);
                    mProfileSupportResult.setText(printResult(client.getSupportResult()));
                } else {
                    mProfileIsSupportFinished.setText(getResources().getString(R.string.profile_is_support_finished, getResources().getString(R.string.no_support_or_dlc)));
                }
            } else{
                Log.i(TAG, "client.getFamilyIsSupportFinished() " + "NULL");
            }
        } else {
            mProfileIsUnderSupport.setText(getResources().getString(R.string.profile_is_under_support, getResources().getString(R.string.no_support_or_dlc)));
            mProfileIsSupportFinished.setVisibility(View.GONE);
        }

        if(client.getPhoto().getFileName() == null)
        {
            mClientPhoto.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar_profile));
        } else {
            mClientPhoto.setImageBitmap(SimpleCameraUtils.newInstance().cropAndScale(getActivity(),
                    Uri.parse(client.getPhoto().getFileName()), 240));
        }

    }
    public void animateFAB(){
        if(isFabOpen){
            mAddVisit.startAnimation(fab_close);
            mEditProfile.startAnimation(fab_close);
            mAddServices.startAnimation(fab_close);
            mGetMap.startAnimation(fab_close);
            mAddVisit.setClickable(false);
            mEditProfile.setClickable(false);
            mAddServices.setClickable(false);
            mGetMap.setClickable(false);
            isFabOpen = false;
            mSharedPreferences.edit().putBoolean(Constants.ANIMATED_FABS, isFabOpen).commit();
        } else {
            mAddVisit.startAnimation(fab_open);
            mEditProfile.startAnimation(fab_open);
            mAddServices.startAnimation(fab_open);
            mGetMap.setAnimation(fab_open);
            mAddVisit.setClickable(true);
            mEditProfile.setClickable(true);
            mAddServices.setClickable(true);
            mGetMap.setClickable(true);
            isFabOpen = true;
            mSharedPreferences.edit().putBoolean(Constants.ANIMATED_FABS, isFabOpen).commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_ADD_VISIT){
            mProfileVisitsAmount.setText(getResources().getString(R.string.profile_visits_amount,
                    String.valueOf(data.getLongExtra(Constants.VISITS_AMOUNT_TO_PROFILE, 0))));
            mShareActionProvider.setShareIntent(getShareIntent());
        } else if(requestCode == REQUEST_ADD_SERVICE){
            mProfileServicesAmount.setText(getResources().getString(R.string.profile_services_amount,
                    String.valueOf(data.getIntExtra(Constants.TOTAL_SERVICES_AMOUNT_TO_PROFILE, 0))));
            mServicesByTypesMap = (HashMap<String, String>)data.getSerializableExtra(Constants.INFORMATION_SERVICES_TO_PROFILE);
            setServicesAmountByType(mServicesByTypesMap, mapKeys, mDetailedServicesTextViews, resIDs);
            mShareActionProvider.setShareIntent(getShareIntent());
            Log.i(TAG, "INFORMATION: " + mServicesByTypesMap.get(Constants.INFORMATION_SERVICE));
            Log.i(TAG, "SOC_PED: " + mServicesByTypesMap.get(Constants.SOCIO_PEDAGOGICAL_SERVICE));
            Log.i(TAG, "SOC_PSYCHO: " + mServicesByTypesMap.get(Constants.SOCIO_PSYCHOLOGICAL_SERVICE));
            Log.i(TAG, "JURIST: " + mServicesByTypesMap.get(Constants.JURISTICAL_SERVICE));
        }
    }

    private String printResult(String inString){
        String printedResultOfSupport = null;
        switch(inString){
            case "positive":
                printedResultOfSupport = getResources().getString(R.string.with_positive_result);
                break;
            case "negative":
                printedResultOfSupport = getResources().getString(R.string.with_negative_result);
                break;
            default:
                printedResultOfSupport = getResources().getString(R.string.support_continues);
        }
        return printedResultOfSupport;
    }

    private void setServicesAmountByType(HashMap<String, String> map, String[] keys, TextView[] textViews, int[] rStringValues){
        String tvValue = null;
        for(int i = 0; i < keys.length; i++) {
            if (Integer.parseInt(map.get(keys[i])) > 0) {
                tvValue = getStringById(rStringValues[i]);
                textViews[i].setText(tvValue + " " + map.get(keys[i]));
                if (textViews[i].getVisibility() != View.VISIBLE) {
                    textViews[i].setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private String getStringById(int resId){
        return getResources().getString(resId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_profile, menu);

        mShareActionProvider = (android.support.v7.widget.ShareActionProvider)MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));
        setSharedIntent(getShareIntent());
    }

    // Call to update the share intent
    private void setSharedIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private Intent getShareIntent(){
        String clientPhotoPath = null;
        String clientName = null;
        if(getArguments().getInt(Constants.FAMILY_PROFILE_RECEIVER) == Constants.FAMILY){
            FamilyClient familyClient = (FamilyClient) Transporter.newInstance().getObject();
            if(familyClient.getPhoto().getFileName() != null){
                clientPhotoPath = familyClient.getPhoto().getFileName();
            }
            clientName = familyClient.getClientFirstName();
        } else if(getArguments().getInt(Constants.FAMILY_PROFILE_RECEIVER) == Constants.PERSON){
            PersonClient personClient = (PersonClient) Transporter.newInstance().getObject();
            if(personClient.getPhoto().getFileName() != null){
                clientPhotoPath = personClient.getPhoto().getFileName();
            }
            clientName = personClient.getClientFirstName();
        }

        Log.i(TAG, clientName);


        Intent intent = new Intent(Intent.ACTION_SEND);

        StringBuilder builder = new StringBuilder();
        setTextToBuilder(mProfileSecondName, builder);
        setTextToBuilder(mProfileFirstName, builder);
        setTextToBuilder(mProfilePatronymic, builder);
        setTextToBuilder(mProfileAddress, builder);
        setTextToBuilder(mProfileClientCategory, builder);
        setTextToBuilder(mProfileProblem, builder);
        setTextToBuilder(mProfileVisitsAmount, builder);
        setTextToBuilder(mProfileServicesAmount, builder);
        setTextToBuilder(mInfoServicesAmount, builder);
        setTextToBuilder(mSocPedServicesAmount, builder);
        setTextToBuilder(mSocPsychoServicesAmount, builder);
        setTextToBuilder(mJuristicalServicesAmount, builder);
        setTextToBuilder(mProfileAdultsAmount, builder);
        setTextToBuilder(mProfileKidsAmount, builder);
        setTextToBuilder(mProfileIsDLC, builder);
        setTextToBuilder(mProfileIsUnderSupport, builder);
        setTextToBuilder(mProfileDateSupportStarted, builder);
        setTextToBuilder(mProfileIsSupportFinished, builder);
        setTextToBuilder(mProfileDateSupportFinished, builder);
        setTextToBuilder(mProfileSupportResult, builder);
        

        intent.setType("*/*");
        if(clientPhotoPath != null){
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(clientPhotoPath));
            Log.i(TAG, clientPhotoPath);
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, builder.toString());
        return intent;
    }

    private void setTextToBuilder(TextView textView, StringBuilder stringBuilder){
        if(textView.getVisibility() != View.GONE){
            stringBuilder.append(textView.getText().toString() + "\n");
        }
    }

    @Override
    public void onBackPressed() {
        if(isFabOpen){
            animateFAB();
            Log.i(TAG, "Fab hide");
        } else {
            return;
        }
    }
}
