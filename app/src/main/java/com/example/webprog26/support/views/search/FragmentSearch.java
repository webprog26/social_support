package com.example.webprog26.support.views.search;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.webprog26.support.R;
import com.example.webprog26.support.activities.ProfileActivity;
import com.example.webprog26.support.activities.SearchActivity;
import com.example.webprog26.support.adapters.FamiliesListAdapter;
import com.example.webprog26.support.adapters.PersonsListAdapter;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.handlers.ThumbsDownloader;
import com.example.webprog26.support.interfaces.OnItemClickListener;
import com.example.webprog26.support.interfaces.ThumbsDownloadListener;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.PersonClient;
import com.example.webprog26.support.models.Transporter;
import com.example.webprog26.support.providers.FamilyClientProvider;
import com.example.webprog26.support.providers.search.SearchProvider;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by webprog26 on 17.03.2016.
 */
public class FragmentSearch extends Fragment {

    private static final String TAG = "FragmentSearch";

    private CoordinatorLayout mParentLayout;
    private Toolbar mToolbar;
    private SearchView mSearchView;
    private Animation mAnimation;
    private RecyclerView mRecyclerView;

    private ArrayList<FamilyClient> mFamilyClients;
    private ArrayList<PersonClient> mPersonClients;
    private FamiliesListAdapter mFamiliesListAdapter;
    private PersonsListAdapter mPersonsListAdapter;
    private ThumbsDownloader<FamiliesListAdapter.FamiliesViewHolder> mThumbnailFamiliesThread;
    private ThumbsDownloader<PersonsListAdapter.PersonsViewHolder> mThumbnailPersonsThread;
    private SearchProvider mSearchProvider;

    public static FragmentSearch newInstance(int mode){
        Bundle args = new Bundle();
        args.putInt(Constants.SEARCH_MODE, mode);
        FragmentSearch fragment = new FragmentSearch();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    //    setHasOptionsMenu(true);
        if(getArguments() != null){
            if(getArguments().getInt(Constants.SEARCH_MODE) == Constants.SEARCH_MODE_FAMILY){
                Log.i(TAG, "Fragment is in the family search mode");
            } else if(getArguments().getInt(Constants.SEARCH_MODE) == Constants.SEARCH_MODE_PERSON){
                Log.i(TAG, "Fragment is in the person search mode");
            } else {
                Log.i(TAG, "Something goes wrong with the search mode");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        mParentLayout = (CoordinatorLayout) v.findViewById(R.id.parentLayout);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mSearchView = (SearchView) v.findViewById(R.id.searchView);

        initSearchView(mSearchView);
        initToolbar(mToolbar, (AppCompatActivity) getActivity());

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        final LinearLayout mContainerSearchParams = (LinearLayout) v.findViewById(R.id.containerSearchParams);
        mContainerSearchParams.setVisibility(View.GONE);
        final ImageView mBtnShowOrHideParams = (ImageView) v.findViewById(R.id.showOrHideParams);
        final RelativeLayout mSearchParamsMainContainer = (RelativeLayout) v.findViewById(R.id.searchParamsMainContainer);
        mSearchParamsMainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideSerachParams(mContainerSearchParams, mBtnShowOrHideParams);
            }
        });


        final RadioGroup mRgSearchParams = (RadioGroup) v.findViewById(R.id.rGsearchParams);
        final android.support.v7.widget.AppCompatRadioButton mRbSearchModeFamily = (android.support.v7.widget.AppCompatRadioButton) v.findViewById(R.id.rbSearchParamsFamily);
        final android.support.v7.widget.AppCompatRadioButton mRbSearchModePerson = (android.support.v7.widget.AppCompatRadioButton) v.findViewById(R.id.rbSearchParamsPerson);
        if(getArguments().getInt(Constants.SEARCH_MODE) == Constants.SEARCH_MODE_FAMILY){
            if(mRbSearchModePerson.isChecked()){
                mRbSearchModePerson.setChecked(false);
                mRbSearchModeFamily.setChecked(true);
            }

        } else if(getArguments().getInt(Constants.SEARCH_MODE) == Constants.SEARCH_MODE_PERSON){
            if(mRbSearchModeFamily.isChecked()){
                mRbSearchModeFamily.setChecked(false);
                mRbSearchModePerson.setChecked(true);
            }
        } else {
            Log.i(TAG, "Something goes wrong with the search mode");
        }
        mRbSearchModeFamily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(getArguments() != null){
                    if(getArguments().getInt(Constants.SEARCH_MODE) == Constants.SEARCH_MODE_PERSON) {
                        if (isChecked) {
                            Intent intent = new Intent(getActivity(), SearchActivity.class);
                            intent.putExtra(Constants.SEARCH_MODE, Constants.SEARCH_MODE_FAMILY);
                            getActivity().finish();
                            startActivity(intent);
                        }
                    }
                }
            }
        });
        mRbSearchModePerson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(getArguments() != null){
                    if(getArguments().getInt(Constants.SEARCH_MODE) == Constants.SEARCH_MODE_FAMILY) {
                        if (isChecked) {
                            Intent intent = new Intent(getActivity(), SearchActivity.class);
                            intent.putExtra(Constants.SEARCH_MODE, Constants.SEARCH_MODE_PERSON);
                            getActivity().finish();
                            startActivity(intent);

                        }
                    }
                }
            }
        });

       mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (getArguments() != null) {
                    if (getArguments().getInt(Constants.SEARCH_MODE) == Constants.SEARCH_MODE_FAMILY) {
                        try{
                            initialFamiliesSearch(Constants.SEARCH_MODE_FAMILY, query);
                        } catch (ParseException pe){
                            pe.printStackTrace();
                        }

                    } else if (getArguments().getInt(Constants.SEARCH_MODE) == Constants.SEARCH_MODE_PERSON) {
                        try{
                            initialPersonsSearch(Constants.SEARCH_MODE_PERSON, query);
                        } catch (ParseException pe){
                            pe.printStackTrace();
                        }

                    } else {
                        Log.i(TAG, "Something goes wrong with the search mode");
                    }
                }
                if (query.isEmpty()) {
                    if (mRecyclerView.getAdapter() != null) {
                        mRecyclerView.setAdapter(null);
                        if(mThumbnailFamiliesThread != null){
                            mThumbnailFamiliesThread.clearQueue();
                            Log.i(TAG, "mThumbnailFamiliesThread thread .clearQueue()");
                        } else if(mThumbnailPersonsThread != null){
                            mThumbnailPersonsThread.clearQueue();
                            Log.i(TAG, "mThumbnailPersonsThread thread .clearQueue()");
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (getArguments() != null) {
                    if (getArguments().getInt(Constants.SEARCH_MODE) == Constants.SEARCH_MODE_FAMILY) {
                        try{
                            initialFamiliesSearch(Constants.SEARCH_MODE_FAMILY, newText);
                        } catch (ParseException pe){
                            pe.printStackTrace();
                        }

                    } else if (getArguments().getInt(Constants.SEARCH_MODE) == Constants.SEARCH_MODE_PERSON) {
                        try{
                            initialPersonsSearch(Constants.SEARCH_MODE_PERSON, newText);
                        } catch (ParseException pe){
                            pe.printStackTrace();
                        }

                    } else {
                        Log.i(TAG, "Something goes wrong with the search mode");
                    }
                }
                if (newText.isEmpty()) {
                    if (mRecyclerView.getAdapter() != null) {
                        mRecyclerView.setAdapter(null);
                    }
                }
                return true;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (mRecyclerView.getAdapter() != null) {
                    mRecyclerView.setAdapter(null);
                    if(mThumbnailFamiliesThread != null){
                        mThumbnailFamiliesThread.quit();
                        Log.i(TAG, "mThumbnailFamiliesThread thread destroyed");
                    } else if(mThumbnailPersonsThread != null){
                        mThumbnailPersonsThread.quit();
                        Log.i(TAG, "mThumbnailPersonsThread thread destroyed");
                    }
                }
                return true;
            }
        });
        return v;
    }

    private void initSearchView(SearchView searchView){
        searchView.setIconified(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        SearchManager searchManager = (SearchManager)getActivity()
                .getSystemService(Context.SEARCH_SERVICE);
        ComponentName name = getActivity().getComponentName();
        SearchableInfo searchInfo = searchManager.getSearchableInfo(name);
        searchView.setSearchableInfo(searchInfo);
    }


    private void initToolbar(android.support.v7.widget.Toolbar toolbar, AppCompatActivity activity){
        activity.setSupportActionBar(toolbar);
        final ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void slideSearchParameters(View v, Animation animation){

        if(animation != null){
            animation.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(animation);
            }
        }
    }

    private void showHideSerachParams(LinearLayout containerLayout, ImageView toggleImageButton){
        if(!containerLayout.isShown()){
            if(containerLayout.getVisibility() == View.GONE){
                mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
                containerLayout.setVisibility(View.VISIBLE);
                slideSearchParameters(containerLayout, mAnimation);
                toggleImageButton.setImageResource(R.drawable.ic_keyboard_arrow_up_black_18dp);
            }
        }else{
            mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
            slideSearchParameters(containerLayout, mAnimation);
            if(containerLayout.getVisibility() == View.VISIBLE){
                containerLayout.setVisibility(View.GONE);
                toggleImageButton.setImageResource(R.drawable.ic_keyboard_arrow_down_black_18dp);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mThumbnailFamiliesThread != null){
            mThumbnailFamiliesThread.quit();
            Log.i(TAG, "mThumbnailFamiliesThread thread destroyed");
        } else if(mThumbnailPersonsThread != null){
            mThumbnailPersonsThread.quit();
            Log.i(TAG, "mThumbnailPersonsThread thread destroyed");
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mThumbnailFamiliesThread != null){
            mThumbnailFamiliesThread.clearQueue();
            Log.i(TAG, "mThumbnailFamiliesThread thread .clearQueue()");
        } else if(mThumbnailPersonsThread != null){
            mThumbnailPersonsThread.clearQueue();
            Log.i(TAG, "mThumbnailPersonsThread thread .clearQueue()");
        }
    }

    public void initialFamiliesSearch(
                                       int mode,
                                      final String query) throws ParseException{
            mSearchProvider = new SearchProvider(getActivity(), mode);

            mFamilyClients = mSearchProvider.getSearchResultsFamilyClients(query);
            if(mFamilyClients != null && mFamilyClients.size() > 0){
            Log.i(TAG, "mFamilyClients.size(): " + mFamilyClients.size());
            mThumbnailFamiliesThread = new ThumbsDownloader<>(new Handler());
            mThumbnailFamiliesThread.setThumbsDownloadListener(new ThumbsDownloadListener<FamiliesListAdapter.FamiliesViewHolder>() {
            @Override
            public void onThumbNailDownloaded(FamiliesListAdapter.FamiliesViewHolder target, Bitmap thumbnail) {
                target.bindClientImage(thumbnail);
            }
        });
        mThumbnailFamiliesThread.start();
        mThumbnailFamiliesThread.getLooper();
        mFamiliesListAdapter = new FamiliesListAdapter(getActivity(), mFamilyClients, new OnItemClickListener() {
            @Override
            public void onItemClick(FamilyClient client) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(Constants.PROFILE_TO_OPEN, 0);
                Transporter.newInstance().setObject(client);
                startActivity(intent);
            }

            @Override
            public void onItemClick(PersonClient client) {
                //
            }
        }, mThumbnailFamiliesThread);
                mRecyclerView.setAdapter(mFamiliesListAdapter);
        } else {
                return;
            }
    }

    public void initialPersonsSearch(
                                       int mode,
                                       String query) throws ParseException{
        mSearchProvider = new SearchProvider(getActivity(), mode);

        mPersonClients = mSearchProvider.getSearchResultsPersonClients(query);
        if(mPersonClients != null && mPersonClients.size() > 0){
        Log.i(TAG, "mPersonsClients.size(): " + mPersonClients.size());
        mThumbnailPersonsThread = new ThumbsDownloader<>(new Handler());
        mThumbnailPersonsThread.setThumbsDownloadListener(new ThumbsDownloadListener<PersonsListAdapter.PersonsViewHolder>() {
            @Override
            public void onThumbNailDownloaded(PersonsListAdapter.PersonsViewHolder target, Bitmap thumbnail) {
                target.bindClientImage(thumbnail);
            }
        });
        mThumbnailPersonsThread.start();
        mThumbnailPersonsThread.getLooper();
        mPersonsListAdapter = new PersonsListAdapter(getActivity(), mPersonClients, new OnItemClickListener() {
            @Override
            public void onItemClick(FamilyClient client) {
                //
            }

            @Override
            public void onItemClick(PersonClient client) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra(Constants.PROFILE_TO_OPEN, 1);
                Transporter.newInstance().setObject(client);
                startActivity(intent);
            }
        }, mThumbnailPersonsThread);
            mRecyclerView.setAdapter(mPersonsListAdapter);
        } else {
            return;
        }
    }

}
