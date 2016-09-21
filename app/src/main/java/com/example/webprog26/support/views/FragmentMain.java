package com.example.webprog26.support.views;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.webprog26.support.R;
import com.example.webprog26.support.activities.AddActivity;
import com.example.webprog26.support.activities.ReportActivity;
import com.example.webprog26.support.activities.SearchActivity;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.FamilyReport;
import com.example.webprog26.support.models.PersonClient;
import com.example.webprog26.support.providers.FamilyClientProvider;
import com.example.webprog26.support.providers.PersonClientProvider;
import com.example.webprog26.support.providers.report.FamilyReportProvider;
import com.example.webprog26.support.views.pickers.FragmentDateMakeReport;
import com.example.webprog26.support.views.pickers.FragmentDatePicker;
import com.example.webprog26.support.views.pickers.FragmentEditEmployee;
import com.example.webprog26.support.views.sub_main.FragmentFamiliesList;
import com.example.webprog26.support.views.sub_main.FragmentPersonsList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by webprog26 on 06.03.2016.
 */
public class FragmentMain extends Fragment {

    private static final String TAG = "FragmentMain";

    private static final int REQUEST_EMPLOYEE_DATA = 0;

    private CoordinatorLayout mParentLayout;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private FloatingActionButton mButtonAdd;


    private android.support.v7.widget.Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private TextView mEmployeeFirstName, mEmployeeSecondName;
    private ImageButton mBtnEditEmployeeData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mSharedPreferences.edit();
        mEditor.putString(Constants.SWIPE_CONTROLS, "false").apply();

        //SearchQuery
        String searchQuery = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.PREF_SEARCH_QUERY, null);
        if(searchQuery != null){
            Log.i(TAG, "searchQuery: " + searchQuery);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       final View view = inflater.inflate(R.layout.fragment_main, container, false);

        mParentLayout = (CoordinatorLayout) view.findViewById(R.id.parentLayout);

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);

        mNavigationView = (NavigationView) view.findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                if (item.getItemId() == R.id.drawer_search) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.showAll){
                    initViewPagerAndTabs(view, mViewPager, mTabLayout, Constants.SHOW_ALL);
                }
                if(item.getItemId() == R.id.supportContinues){
                    initViewPagerAndTabs(view, mViewPager, mTabLayout, Constants.SUPPORT_CONTINUES);
                }
                if(item.getItemId() == R.id.positiveResult){
                    initViewPagerAndTabs(view, mViewPager, mTabLayout, Constants.SUPPORT_STOPPED_POSITIVE);
                }
                if(item.getItemId() == R.id.negativeResult){
                    initViewPagerAndTabs(view, mViewPager, mTabLayout, Constants.SUPPORT_STOPPED_NEGATIVE);
                }
                if(item.getItemId() == R.id.myReports){
                    Intent intent = new Intent(getActivity(), ReportActivity.class);
                    intent.putExtra(Constants.REPORT_MODE, Constants.REPORT_VIEW);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.makeReport){
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentDateMakeReport dialog = FragmentDateMakeReport.newInstance(new Date(System.currentTimeMillis()));
                    dialog.show(fm, Constants.DIALOG_DATE);
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        mToolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        initToolbar(mToolbar, (AppCompatActivity) getActivity());

        initViewPagerAndTabs(view, mViewPager, mTabLayout, Constants.SHOW_ALL);

        mActionBarDrawerToggle = new ActionBarDrawerToggle((AppCompatActivity) getActivity(), mDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.invalidate();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();

        mButtonAdd = (FloatingActionButton) view.findViewById(R.id.fabButton);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra(Constants.MODE_TO_ADD_CLIENT, mViewPager.getCurrentItem());
                startActivity(intent);
            }
        });



        mEmployeeFirstName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tvEmployeeFirstName);
        mEmployeeSecondName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tvEmployeeSecondName);
        mBtnEditEmployeeData = (ImageButton) mNavigationView.getHeaderView(0).findViewById(R.id.btnEditEmployeeData);
        mBtnEditEmployeeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentEditEmployee dialogEditEmployee = FragmentEditEmployee.newInstance();
                dialogEditEmployee.setTargetFragment(FragmentMain.this, REQUEST_EMPLOYEE_DATA);
                dialogEditEmployee.show(fragmentManager, null);
            }
        });

        setEmployeeData();

        return view;
    }

    private void initToolbar(android.support.v7.widget.Toolbar toolbar, AppCompatActivity activity){
        activity.setSupportActionBar(toolbar);
        final ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(R.string.app_name);
            actionBar.setLogo(R.drawable.app_icon);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViewPagerAndTabs(View contextView, ViewPager viewPager, TabLayout tabLayout, int mode){
        PagerAdapter pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());

        pagerAdapter.addFragmentToList(FragmentFamiliesList.newInstance(mode), getResources().getString(R.string.families_tab_title));
        pagerAdapter.addFragmentToList(FragmentPersonsList.newInstance(mode), getResources().getString(R.string.persons_tab_title));
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    public static class PagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<Fragment>();
        private List<String> fragmentTitleList = new ArrayList<String>();


        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }

        public void addFragmentToList(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(isNavDrawerOpen()) {
            closeDrawer();
        }
    }

    protected boolean isNavDrawerOpen(){
        return  mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeDrawer(){
        if(mDrawerLayout != null){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem swipeItem = menu.findItem(R.id.action_swipe_mode);
        if(isSwipeOn()) swipeItem.setTitle(getResources().getString(R.string.disable_swipe));
        else {
            swipeItem.setTitle(getResources().getString(R.string.enable_swipe));
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_swipe_mode:
                String prefsMode = mSharedPreferences.getString(Constants.SWIPE_CONTROLS, null);
                String snackBarMessage = getResources().getString(R.string.swipe_disabled);
                if(prefsMode != null){
                    if(prefsMode.equalsIgnoreCase("false")){
                        mEditor.putString(Constants.SWIPE_CONTROLS, "true").commit();
                        item.setTitle(getResources().getString(R.string.disable_swipe));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            getActivity().invalidateOptionsMenu();
                        prefsMode = mSharedPreferences.getString(Constants.SWIPE_CONTROLS, "");
                        snackBarMessage = getResources().getString(R.string.swipe_enabled);
                    } else {
                        mEditor.putString(Constants.SWIPE_CONTROLS, "false").commit();
                        item.setTitle(getResources().getString(R.string.enable_swipe));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            getActivity().invalidateOptionsMenu();
                        prefsMode = mSharedPreferences.getString(Constants.SWIPE_CONTROLS, "");
                    }
                }
                Snackbar snackbar = Snackbar.make(mParentLayout, snackBarMessage, Snackbar.LENGTH_LONG);
                snackbar.show();
                return true;
            case R.id.action_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra(Constants.SEARCH_MODE, mViewPager.getCurrentItem());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isSwipeOn(){
        if(mSharedPreferences.getString(Constants.SWIPE_CONTROLS, null) != null){
            if(mSharedPreferences.getString(Constants.SWIPE_CONTROLS, null).equals("true")){
                return  true;
            }
        }
        return false;
    }

    private void setEmployeeData() {
        String employeeFirstName, employeeSecondName;
        if(mSharedPreferences.getString(Constants.EMPLOYEE_FIRST_NAME, null) == null && mSharedPreferences.getString(Constants.EMPLOYEE_SECOND_NAME, null) == null){
            mSharedPreferences.edit().putString(Constants.EMPLOYEE_FIRST_NAME, getString(R.string.employee_first_name))
                                     .putString(Constants.EMPLOYEE_SECOND_NAME, getString(R.string.employee_second_name)).commit();
        }
        employeeFirstName = mSharedPreferences.getString(Constants.EMPLOYEE_FIRST_NAME, null);
        employeeSecondName = mSharedPreferences.getString(Constants.EMPLOYEE_SECOND_NAME, null);
        mEmployeeFirstName.setText(employeeFirstName);
        mEmployeeSecondName.setText(employeeSecondName);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_EMPLOYEE_DATA){
            mEmployeeFirstName.setText(data.getStringExtra(Constants.EMPLOYEE_FIRST_NAME));
            mEmployeeSecondName.setText(data.getStringExtra(Constants.EMPLOYEE_SECOND_NAME));
            mSharedPreferences.edit().putString(Constants.EMPLOYEE_FIRST_NAME, data.getStringExtra(Constants.EMPLOYEE_FIRST_NAME))
                    .putString(Constants.EMPLOYEE_SECOND_NAME, data.getStringExtra(Constants.EMPLOYEE_SECOND_NAME)).commit();
        }
    }





}
