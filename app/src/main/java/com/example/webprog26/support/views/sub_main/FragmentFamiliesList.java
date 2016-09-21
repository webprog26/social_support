package com.example.webprog26.support.views.sub_main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.webprog26.support.R;
import com.example.webprog26.support.activities.ProfileActivity;
import com.example.webprog26.support.adapters.FamiliesListAdapter;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.handlers.ThumbsDownloader;
import com.example.webprog26.support.interfaces.OnItemClickListener;
import com.example.webprog26.support.interfaces.ThumbsDownloadListener;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.PersonClient;
import com.example.webprog26.support.models.Transporter;
import com.example.webprog26.support.providers.FamilyClientProvider;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by webprog26 on 06.03.2016.
 */
public class FragmentFamiliesList extends Fragment {

    private static final String TAG = "FragmentFamiliesList";

    private FamilyClientProvider mFamilyClientProvider;
    private ArrayList<FamilyClient> mFamilyClients;
    private FamiliesListAdapter mAdapter;
    private ThumbsDownloader<FamiliesListAdapter.FamiliesViewHolder> mThumbnailThread;

    public static FragmentFamiliesList newInstance(int mode){
        Bundle args = new Bundle();
        args.putInt(Constants.MODE_SUPPORT_STOPPED, mode);
        FragmentFamiliesList fragment = new FragmentFamiliesList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFamilyClientProvider = new FamilyClientProvider(getActivity());
        int mode = getArguments().getInt(Constants.MODE_SUPPORT_STOPPED);
        try{
            switch (mode){
                case Constants.SHOW_ALL:
                    mFamilyClients = mFamilyClientProvider.getFamilyClientData();
                    break;
                case Constants.SUPPORT_CONTINUES:
                    mFamilyClients = mFamilyClientProvider.getFamiliesSupportContinues();
                    break;
                case Constants.SUPPORT_STOPPED_POSITIVE:
                    mFamilyClients = mFamilyClientProvider.getFamiliesSupportStoppedPositive();
                    break;
                case Constants.SUPPORT_STOPPED_NEGATIVE:
                    mFamilyClients = mFamilyClientProvider.getFamiliesSupportStoopedNegative();
                    break;
            }

        } catch (ParseException pe){
            Log.e(TAG, "ParseException: ", pe);
        }
        Log.i(TAG, "MODE: " + mode);
        Handler responseHandler = new Handler();
        mThumbnailThread = new ThumbsDownloader<>(responseHandler);
        mThumbnailThread.setThumbsDownloadListener(new ThumbsDownloadListener<FamiliesListAdapter.FamiliesViewHolder>() {
            @Override
            public void onThumbNailDownloaded(FamiliesListAdapter.FamiliesViewHolder target, Bitmap thumbnail) {
                target.bindClientImage(thumbnail);
            }
        });

        mThumbnailThread.start();
        mThumbnailThread.getLooper();

        Log.i("ThumbsDownloader", "Bacground thread started");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new FamiliesListAdapter(getActivity(), mFamilyClients, new OnItemClickListener() {
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
        }, mThumbnailThread);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        int mode = getArguments().getInt(Constants.MODE_SUPPORT_STOPPED);
        try{
            switch (mode){
                case Constants.SHOW_ALL:
                    mFamilyClients = mFamilyClientProvider.getFamilyClientData();
                    break;
                case Constants.SUPPORT_STOPPED_POSITIVE:
                    mFamilyClients = mFamilyClientProvider.getFamiliesSupportStoppedPositive();
                    break;
            }
        } catch (ParseException pe){
            Log.e(TAG, "ParseException: ", pe);
        }
        mAdapter.updateList(mFamilyClients);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailThread.quit();
        Log.i("ThumbsDownloader", "Bacground thread destroyed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }
}
