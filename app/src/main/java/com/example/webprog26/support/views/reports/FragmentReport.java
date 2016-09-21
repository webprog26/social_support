package com.example.webprog26.support.views.reports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.webprog26.support.R;
import com.example.webprog26.support.adapters.ReportsAdapter;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.exel.WriteExel;
import com.example.webprog26.support.interfaces.OnReportClickListener;
import com.example.webprog26.support.models.FamilyReport;
import com.example.webprog26.support.models.PersonReport;
import com.example.webprog26.support.providers.report.FamilyReportProvider;
import com.example.webprog26.support.providers.report.PersonReportProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by webprog26 on 08.04.2016.
 */
public class FragmentReport extends Fragment {

    private static final String TAG = "FragmentReport";

    private FamilyReportProvider mFamilyReportProvider;
    private PersonReportProvider mPersonReportProvider;

    private android.support.v7.widget.ShareActionProvider mShareActionProvider;

    private Date mStartDate, mFinishDate;
    private WriteExel mWriteExel;
    private SharedPreferences mSharedPreferences;
    private String[] mFamiliesReportTitles;
    private String[] mPersonsReportTitles;
    private ArrayList<Integer> mFamiliesNumbers;
    private ArrayList<Integer> mPersonsNumbers;
    private List<File> mFilesList;
    private ReportsAdapter mAdapter;
    private ArrayList<File> mFilesToShare;
    private boolean mEnableSharing = false;
    private StringBuilder mEmployeeDataStringBuilder;



    private CoordinatorLayout mParentLayout;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    public static FragmentReport newInstance(int mode, Date startDate, Date finishDate){
        Bundle args = new Bundle();
        args.putInt(Constants.REPORT_MODE, mode);
        args.putSerializable(Constants.DATE_REPORT_START, startDate);
        args.putSerializable(Constants.DATE_REPORT_FINISH, finishDate);
        FragmentReport fragmentReport = new FragmentReport();
        fragmentReport.setArguments(args);
        return fragmentReport;
    }

    public static FragmentReport newInstance(int mode){
        Bundle args = new Bundle();
        args.putInt(Constants.REPORT_MODE, mode);
        FragmentReport fragment = new FragmentReport();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mFilesToShare = new ArrayList<File>();

        mFilesList = new ArrayList<File>();

        if(getArguments().getInt(Constants.REPORT_MODE) == Constants.REPORT_CREATE){
            makeReport();
        } else{
            getListFiles((ArrayList<File>) mFilesList);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mParentLayout = (CoordinatorLayout) v.findViewById(R.id.parentLayout);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        initToolbar(mToolbar, (AppCompatActivity) getActivity());
        mAdapter = new ReportsAdapter(getActivity(), mFilesList, new OnReportClickListener() {
            @Override
            public void onReportItemClick(File file) {
                if(!mFilesToShare.contains(file)){
                    mFilesToShare.add(file);
                    Log.i(TAG, "mFilesToShare.size(): " + mFilesToShare.size());
                    mShareActionProvider.setShareIntent(getShareIntent(mFilesToShare));
                } else {
                    mFilesToShare.remove(file);
                    Log.i(TAG, "mFilesToShare.size(): " + mFilesToShare.size());
                    mShareActionProvider.setShareIntent(getShareIntent(mFilesToShare));
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments().getInt(Constants.REPORT_MODE) == Constants.REPORT_VIEW) {
            Snackbar snackbar = Snackbar.make(mParentLayout, getResources().getString(R.string.no_reports_found), Snackbar.LENGTH_SHORT);
            if (mRecyclerView.getAdapter().getItemCount() == 0) {
                snackbar.show();
            }
        }
    }

    private void initToolbar(android.support.v7.widget.Toolbar toolbar, AppCompatActivity activity){
        activity.setSupportActionBar(toolbar);
        final ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(R.string.reports);
            actionBar.setLogo(R.mipmap.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private class FamilyReportTask extends AsyncTask<Void, Void, FamilyReport> {
        public FamilyReportTask() {
            super();
        }

        @Override
        protected void onPostExecute(FamilyReport familyReport) {

            Log.i(TAG, "************************FAMILY REPORT*************************");

            Log.i(TAG, "FamiliesClientsTotalNum: " + familyReport.getClientsNum());
            mFamiliesNumbers.add(familyReport.getClientsNum());
            Log.i(TAG, "FamiliesClientsAdultsNum: " + familyReport.getAdultClientsNum());
            mFamiliesNumbers.add(familyReport.getAdultClientsNum());
            Log.i(TAG, "FamiliesClientsKidsNum: " + familyReport.getKidsClientsNum());
            mFamiliesNumbers.add(familyReport.getKidsClientsNum());
            Log.i(TAG, "FamiliesServicesTotalNum: " + familyReport.getServicesNum());
            mFamiliesNumbers.add(familyReport.getServicesNum());
            Log.i(TAG, "FamiliesFirstVisitsCount: " + familyReport.getFirstTimeVisits());
            mFamiliesNumbers.add(familyReport.getFirstTimeVisits());
            Log.i(TAG, "FamiliesVisitsTotalNum: " + familyReport.getVisits());
            mFamiliesNumbers.add(familyReport.getVisits());
            Log.i(TAG, "FamiliesClientsInDLCTotalNum: " + familyReport.getDLCclientsNum());
            mFamiliesNumbers.add(familyReport.getDLCclientsNum());
            Log.i(TAG, "FamiliesAdultsInDLCNum: " + familyReport.getInDLCadultClients());
            mFamiliesNumbers.add(familyReport.getInDLCadultClients());
            Log.i(TAG, "FamiliesKidsInDLCNum: " + familyReport.getInDLCkidsClients());
            mFamiliesNumbers.add(familyReport.getInDLCkidsClients());
            Log.i(TAG, "FamiliesInDLCServicesTotalNum: " + familyReport.getInDLCServicesNum());
            mFamiliesNumbers.add(familyReport.getInDLCServicesNum());
            Log.i(TAG, "FamiliesUnderSupportTotalNum: " + familyReport.getUnderSupportClients());
            mFamiliesNumbers.add(familyReport.getUnderSupportClients());
            Log.i(TAG, "FamiliesUnderSupportServicesTotalNum: " + familyReport.getUnderSupportClientsServicesNum());
            mFamiliesNumbers.add(familyReport.getUnderSupportClientsServicesNum());
            Log.i(TAG, "FamiliesSupportStoppedWithPositiveResultTotalNum: " + familyReport.getSupportStoppedWithPositiveResult());
            mFamiliesNumbers.add(familyReport.getSupportStoppedWithPositiveResult());
            Log.i(TAG, "FamiliesSupportStoppedWithNegativeResultTotalNum: " + familyReport.getSupportStoppedWithNegativeResult());
            mFamiliesNumbers.add(familyReport.getSupportStoppedWithNegativeResult());
        }

        @Override
        protected FamilyReport doInBackground(Void... params) {
            FamilyReport.FamilyReportBuilder builder = FamilyReport.newBuilder();
                builder.setClientsNum(mFamilyReportProvider.getFamiliesClientsTotalNum());
                builder.setServicesNum(mFamilyReportProvider.getFamiliesServicesTotalNum());
                builder.setAdultClientNum(mFamilyReportProvider.getFamiliesClientsAdultsNum());
                builder.setKidsClientNum(mFamilyReportProvider.getFamiliesClientsKidsNum());
                builder.setInDLCClientsNum(mFamilyReportProvider.getFamiliesClientsInDLCTotalNum());
                builder.setInDLCAdultClientsNum(mFamilyReportProvider.getFamiliesAdultsInDLCNum());
                builder.setInDLCKidsClientsNum(mFamilyReportProvider.getFamiliesKidsInDLCNum());
                builder.setInDLCServicesNum(mFamilyReportProvider.getFamiliesInDLCServicesTotalNum());
                builder.setUnderSupportClientsNum(mFamilyReportProvider.getFamiliesUnderSupportTotalNum());
                builder.setUnderSupportServicesNum(mFamilyReportProvider.getFamiliesUnderSupportServicesTotalNum());
                builder.setSupportStoppedWithPositiveResultNum(mFamilyReportProvider.getFamiliesSupportStoppedWithPositiveResultTotalNum());
                builder.setSupportStoppedWithNegativeResultsNum(mFamilyReportProvider.getFamiliesSupportStoppedWithNegativeResultTotalNum());
                builder.setVisitsNum(mFamilyReportProvider.getFamiliesVisitsTotalNum());
                builder.setFirstTimeVisitsNum(mFamilyReportProvider.getFamiliesFirstVisitsTotalNum());

            return (FamilyReport)builder.build();
        }
    }

    private class PersonReportTask extends AsyncTask<Void, Void, PersonReport>{
        public PersonReportTask() {
            super();
        }

        @Override
        protected PersonReport doInBackground(Void... params) {
            PersonReport.Builder builder = PersonReport.newBuilder();
                builder.setClientsNum(mPersonReportProvider.getPersonsClientsTotalNum())
                        .setServicesNum(mPersonReportProvider.getPersonsServicesTotalNum())
                        .setInDLCClientsNum(mPersonReportProvider.getPersonsClientsInDLCTotalNum())
                        .setInDLCServicesNum(mPersonReportProvider.getPersonsInDLCServicesTotalNum())
                        .setUnderSupportClientsNum(mPersonReportProvider.getPersonsUnderSupportTotalNum())
                        .setUnderSupportServicesNum(mPersonReportProvider.getPersonsUnderSupportServicesTotalNum())
                        .setSupportStoppedWithPositiveResultNum(mPersonReportProvider.getPersonsStoppedWithPositiveResultTotalNum())
                        .setSupportStoppedWithNegativeResultsNum(mPersonReportProvider.getPersonsSupportStoppedWithNegativeResultTotalNum())
                        .setFirstTimeVisitsNum(mPersonReportProvider.getPersonsFirstVisitsTotalNum())
                        .setVisitsNum(mPersonReportProvider.getPersonsVisitsTotalNum());
            return (PersonReport) builder.build();
        }

        @Override
        protected void onPostExecute(PersonReport personReport) {
            Log.i(TAG, "************************PERSON REPORT*************************");

            Log.i(TAG, "PersonsClientsTotalNum: " + personReport.getClientsNum());
                mPersonsNumbers.add(personReport.getClientsNum());
            Log.i(TAG, "PersonsServicesTotalNum: " + personReport.getServicesNum());
            mPersonsNumbers.add(personReport.getServicesNum());
            Log.i(TAG, "PersonsFirstVisitsCount: " + personReport.getFirstTimeVisits());
            mPersonsNumbers.add(personReport.getFirstTimeVisits());
            Log.i(TAG, "PersonsVisitsTotalNum: " + personReport.getVisits());
            mPersonsNumbers.add(personReport.getVisits());
            Log.i(TAG, "PersonsClientsInDLCTotalNum: " + personReport.getDLCclientsNum());
            mPersonsNumbers.add(personReport.getDLCclientsNum());
            Log.i(TAG, "PersonsInDLCServicesTotalNum: " + personReport.getInDLCServicesNum());
            mPersonsNumbers.add(personReport.getInDLCServicesNum());
            Log.i(TAG, "PersonsUnderSupportTotalNum: " + personReport.getUnderSupportClients());
            mPersonsNumbers.add(personReport.getUnderSupportClients());
            Log.i(TAG, "PersonsUnderSupportServicesTotalNum: " + personReport.getUnderSupportClientsServicesNum());
            mPersonsNumbers.add(personReport.getUnderSupportClientsServicesNum());
            Log.i(TAG, "PersonsSupportStoppedWithPositiveResultTotalNum: " + personReport.getSupportStoppedWithPositiveResult());
            mPersonsNumbers.add(personReport.getSupportStoppedWithPositiveResult());
            Log.i(TAG, "PersonsSupportStoppedWithNegativeResultTotalNum: " + personReport.getSupportStoppedWithNegativeResult());
            mPersonsNumbers.add(personReport.getSupportStoppedWithNegativeResult());
        }
    }

    private class WriteReportToExcelTask extends AsyncTask<Void, Void, File[]>{

        @Override
        protected File[] doInBackground(Void... params) {

            String employeeSecondName = "";

            if(mSharedPreferences.getString(Constants.EMPLOYEE_SECOND_NAME, null) != null)
            {
                employeeSecondName = mSharedPreferences.getString(Constants.EMPLOYEE_SECOND_NAME, null);
            }

            mWriteExel.writeExcelBook(getReportStorageDir("social_sup").getAbsolutePath() + "/"
                    + employeeSecondName + "_" + new SimpleDateFormat("dd_MM_yyy").format(mStartDate) + "_-_" + new SimpleDateFormat("dd_MM_yyy").format(mFinishDate) + ".xls");
            File dir = new File(getReportStorageDir("social_sup").getAbsolutePath());
            return dir.listFiles();
        }

        @Override
        protected void onPostExecute(File[] files) {
            super.onPostExecute(files);
            for(int i = 0; i < files.length; i++){
                mFilesList.add(files[i]);
                Log.i(TAG, "mFilesList: " + mFilesList.get(i));
                mAdapter.updateList((ArrayList<File>)mFilesList);
            }
        }
    }

    public File getReportStorageDir(String dirName){
        if(isExternalStorageWritable()){
        File file = null;
            //Prevents app from crashing for API lvl 15 && lower -->
            if(Build.VERSION.SDK_INT <= 15) {
                file = new File(Environment.getExternalStorageDirectory() + "/Documents");
            } else {
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), dirName);
            }
            //<-- Prevents app from crashing for API lvl 15 && lower
            if (!file.exists()) {
                file.mkdir();
            }
            return file;
        } else{
            return null;
        }
    }



    private boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }

    private void makeReport(){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mFamiliesReportTitles = getResources().getStringArray(R.array.families_report_titles);
        mPersonsReportTitles = getResources().getStringArray(R.array.persons_report_titles);

        mFamiliesNumbers = new ArrayList<>();
        mPersonsNumbers = new ArrayList<>();

        mEmployeeDataStringBuilder = new StringBuilder();
        mEmployeeDataStringBuilder.append(mSharedPreferences.getString(Constants.EMPLOYEE_FIRST_NAME, null));
        mEmployeeDataStringBuilder.append(mSharedPreferences.getString(Constants.EMPLOYEE_SECOND_NAME, null));
        mStartDate = (Date) getArguments().getSerializable(Constants.DATE_REPORT_START);
        mFinishDate =(Date) getArguments().getSerializable(Constants.DATE_REPORT_FINISH);
        mFamilyReportProvider = new FamilyReportProvider(getActivity(), mStartDate, mFinishDate);
        FamilyReportTask familyReportTask = new FamilyReportTask();
        familyReportTask.execute();
        mPersonReportProvider = new PersonReportProvider(getActivity(), mStartDate, mFinishDate);
        PersonReportTask personReportTask = new PersonReportTask();
        personReportTask.execute();


        String familiesSheetTitle = getActivity().getResources().getString(R.string.report_sheet_families);
        String personsSheetTitle = getActivity().getResources().getString(R.string.report_sheet_persons);


        mWriteExel = new WriteExel(familiesSheetTitle, personsSheetTitle, mFamiliesReportTitles, mPersonsReportTitles, mFamiliesNumbers, mPersonsNumbers);

        WriteReportToExcelTask reportToExcelTask = new WriteReportToExcelTask();
        reportToExcelTask.execute();
    }

    private void getListFiles(ArrayList<File> filesList) {
        File dir = new File(getReportStorageDir("social_sup").getAbsolutePath());
            for (File f : dir.listFiles()) {
                filesList.add(f);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_report, menu);

        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));
        setSharedIntent(getShareIntent(mFilesToShare));
        Log.i(TAG, "sharedIntent: " + mFilesToShare.size());
    }

    // Call to update the share intent
    private void setSharedIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private Intent getShareIntent(ArrayList<File> files){
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");

        ArrayList<Uri> uris = new ArrayList<>();
        for(File file: files){
            uris.add(Uri.fromFile(file));
        }

        if(files.size() > 0){
            //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        }
        mEmployeeDataStringBuilder = new StringBuilder();
            mEmployeeDataStringBuilder.append(mSharedPreferences.getString(Constants.EMPLOYEE_FIRST_NAME, null));
            mEmployeeDataStringBuilder.append(" ");
            mEmployeeDataStringBuilder.append(mSharedPreferences.getString(Constants.EMPLOYEE_SECOND_NAME, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, mEmployeeDataStringBuilder.toString());
    return intent;
    }
}
