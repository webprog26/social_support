package com.example.webprog26.support.views.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.webprog26.support.Manifest;
import com.example.webprog26.support.R;
import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.models.ClientMarker;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.PersonClient;
import com.example.webprog26.support.models.Transporter;
import com.example.webprog26.support.providers.markers.MarkersProvider;
import com.example.webprog26.support.views.pickers.FragmentAddMarker;
import com.example.webprog26.support.views.pickers.FragmentAddServicePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by webprog26 on 24.03.2016.
 */
public class FragmentMap extends Fragment implements LocationListener{

    private static final String TAG = "FragmentMap";
    private static final int REQUEST_ADD_MARKER = 0;

    private CoordinatorLayout mParentLayout;
    private LocationManager mLocationManager;
    private GoogleMap mGoogleMap;
    private MapView mapView;
    private Location mLastLocation;
    private LatLng mLatLng;
    private MarkersProvider markersProvider;
    private FloatingActionButton mFbAddMarker, mFbClearMarker, mFbEditMarker;
    private Boolean isFabOpen = false;
    private boolean isMarkerFabsOpen = false;
    private Animation fab_open,fab_close;
    private double markerLatitude, markerLongitude;
    private ArrayList<ClientMarker> mClientMarkers;
    private String markerTitle;
    private String markerSnippet;
    private Snackbar mEnableGPSSnackbar;

    public static FragmentMap newInstance(int mode, long clientId){
        Bundle args = new Bundle();
        args.putInt(Constants.MODE_TO_ADD_CLIENT, mode);
        args.putLong(Constants.PROFILE_TO_OPEN, clientId);
        FragmentMap fragment = new FragmentMap();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);
        markersProvider = new MarkersProvider(getActivity());
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if(getArguments().getInt(Constants.MODE_TO_ADD_CLIENT, 0) == Constants.FAMILY){
            mClientMarkers = markersProvider.getFamilyClientMarkers(getArguments().getLong(Constants.PROFILE_TO_OPEN));
        } else if(getArguments().getInt(Constants.MODE_TO_ADD_CLIENT, 0) == Constants.PERSON){
            mClientMarkers = markersProvider.getPersonClientMarkers(getArguments().getLong(Constants.PROFILE_TO_OPEN));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mParentLayout = (CoordinatorLayout) v.findViewById(R.id.parentLayout);

        String gpsProvider = LocationManager.GPS_PROVIDER;
        Log.i(TAG, "mLocationManager.isProviderEnabled(gpsProvider): " + mLocationManager.isProviderEnabled(gpsProvider));
        Log.i(TAG, "mLocationManager: " + mLocationManager.toString());



        if(isProviderEnabled(gpsProvider)){
            mLocationManager.requestLocationUpdates(gpsProvider, 1000 * 60 * 60, 100, this);
            if (ContextCompat.checkSelfPermission(getActivity(),
                    "android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = mLocationManager.getLastKnownLocation(gpsProvider);
                mLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                Log.i(TAG, "mLastLocation: " + mLastLocation);
            }
        } else{
            mEnableGPSSnackbar = Snackbar.make(mParentLayout, R.string.gps_disabled, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.enable_gps, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent gpsOptionsIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(gpsOptionsIntent);
                        }
                    })
                    .setActionTextColor(Color.RED);
            mEnableGPSSnackbar.show();
        }

        AppBarLayout mAppBarLayout = (AppBarLayout) v.findViewById(R.id.app_bar_layout);
        Toolbar mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        initToolbar(mToolbar, (AppCompatActivity) getActivity());
        mapView = (MapView) v.findViewById(R.id.mapView);
        final Bundle mapViewSavedInstanceState = savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null;
        mapView.onCreate(mapViewSavedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                addMarkersToTheMap(mClientMarkers);
                mGoogleMap.setMyLocationEnabled(true);

                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                if(mLatLng != null){
                    Log.i(TAG, "mLatLng: " + mLatLng);
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
                }

                mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        Log.i(TAG, "onMapLongClick()" + latLng.latitude + "..." + latLng.longitude);
                        markerLatitude = latLng.latitude;
                        markerLongitude = latLng.longitude;
                        animateFABAdd();
                        hideSnackBar(mEnableGPSSnackbar);
                    }
                });
                mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        hideFABs();
                        hideSnackBar(mEnableGPSSnackbar);
                    }
                });

                mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        animateFABEditClear();
                        Log.i(TAG, "MARKER ID: " + marker.getId());
                        Log.i(TAG, "MARKER TITLE: " + marker.getTitle());
                        markerTitle = marker.getTitle();
                        markerSnippet = marker.getSnippet();
                        marker.showInfoWindow();
                        return true;
                    }
                });
            }


        });

        mFbAddMarker = (FloatingActionButton) v.findViewById(R.id.fbAddMarker);
        mFbAddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //animateFABAdd();
                FragmentAddMarker dialog = FragmentAddMarker.newInstance(Constants.MARKER_MODE_NEW,
                        getArguments().getInt(Constants.MODE_TO_ADD_CLIENT),
                        getArguments().getLong(Constants.PROFILE_TO_OPEN),
                        markerLatitude,
                        markerLongitude);
                dialog.setTargetFragment(FragmentMap.this, REQUEST_ADD_MARKER);
                dialog.show(getActivity().getSupportFragmentManager(), Constants.DIALOG_MARKER);
            }
        });
        mFbEditMarker = (FloatingActionButton) v.findViewById(R.id.fbEditMarker);
        mFbEditMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFABEditClear();
                int category = 0;
                long id = 0;
                for(ClientMarker clientMarker: mClientMarkers){
                    if(clientMarker.getTitle().equalsIgnoreCase(markerTitle)){
                        category = clientMarker.getCategory();
                        id = clientMarker.getMarkerId();
                    }
                }
                FragmentAddMarker dialog = FragmentAddMarker.newInstance(Constants.MARKER_MODE_EDIT,
                        getArguments().getInt(Constants.MODE_TO_ADD_CLIENT),
                        markerTitle,
                        markerSnippet,
                        category,
                        getArguments().getLong(Constants.PROFILE_TO_OPEN),
                        id,
                        markerLatitude,
                        markerLongitude);
                dialog.setTargetFragment(FragmentMap.this, REQUEST_ADD_MARKER);
                dialog.show(getActivity().getSupportFragmentManager(), Constants.DIALOG_MARKER);
            }
        });
        mFbClearMarker = (FloatingActionButton) v.findViewById(R.id.fbClearMarker);
        mFbClearMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFABEditClear();
                final Snackbar snackbar = Snackbar.make(mParentLayout, R.string.dismiss_marker, Snackbar.LENGTH_LONG)
                        .setAction(R.string.dismiss_marker_agree, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (getArguments().getInt(Constants.MODE_TO_ADD_CLIENT, 0) == Constants.FAMILY) {
                                    for (ClientMarker clientMarker : mClientMarkers) {
                                        if (clientMarker.getTitle().equalsIgnoreCase(markerTitle)) {
                                            markersProvider.deleteFamilyClientMarker(clientMarker.getMarkerId());
                                        }
                                    }
                                    mClientMarkers = markersProvider.getFamilyClientMarkers(getArguments().getLong(Constants.PROFILE_TO_OPEN));
                                } else if (getArguments().getInt(Constants.MODE_TO_ADD_CLIENT, 0) == Constants.PERSON) {
                                    for (ClientMarker clientMarker : mClientMarkers) {
                                        if (clientMarker.getTitle().equalsIgnoreCase(markerTitle)) {
                                            markersProvider.deletePersonClientMarker(clientMarker.getMarkerId());
                                        }
                                    }
                                    mClientMarkers = markersProvider.getPersonClientMarkers(getArguments().getLong(Constants.PROFILE_TO_OPEN));
                                }
                                mGoogleMap.clear();
                                addMarkersToTheMap(mClientMarkers);
                            }
                        }).setActionTextColor(Color.RED);
                snackbar.show();
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()  {
        super.onPause();
        if (ContextCompat.checkSelfPermission(getActivity(),
                "android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(this);
        }
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        final Bundle mapViewSaveState = new Bundle(outState);
        mapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle("mapViewSaveState", mapViewSaveState);
        //Add any other variables here.
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private boolean isProviderEnabled(String provider)
    {
        return mLocationManager.isProviderEnabled(provider);
    }

    public void animateFABAdd(){
        if(isFabOpen){
            mFbAddMarker.startAnimation(fab_close);
            mFbAddMarker.setClickable(false);
            isFabOpen = false;
        } else {
            mFbAddMarker.startAnimation(fab_open);
            mFbAddMarker.setClickable(true);
            isFabOpen = true;
        }
    }

    private void hideFABs() {
        if (isFabOpen) {
            mFbAddMarker.startAnimation(fab_close);
            mFbAddMarker.setClickable(false);
            isFabOpen = false;
        } if(isMarkerFabsOpen){
            mFbEditMarker.startAnimation(fab_close);
            mFbClearMarker.startAnimation(fab_close);
            mFbEditMarker.setClickable(false);
            mFbClearMarker.setClickable(false);
            isMarkerFabsOpen = false;
        }
    }

    public void animateFABEditClear(){
        if(isMarkerFabsOpen){
            mFbEditMarker.startAnimation(fab_close);
            mFbClearMarker.startAnimation(fab_close);
            mFbEditMarker.setClickable(false);
            mFbClearMarker.setClickable(false);
            isMarkerFabsOpen = false;
        } else {
            mFbEditMarker.startAnimation(fab_open);
            mFbClearMarker.startAnimation(fab_open);
            mFbEditMarker.setClickable(true);
            mFbClearMarker.setClickable(true);
            isMarkerFabsOpen = true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_ADD_MARKER){
            mGoogleMap.clear();
            if(getArguments().getInt(Constants.MODE_TO_ADD_CLIENT, 0) == Constants.FAMILY){
                mClientMarkers = markersProvider.getFamilyClientMarkers(getArguments().getLong(Constants.PROFILE_TO_OPEN));
            } else if(getArguments().getInt(Constants.MODE_TO_ADD_CLIENT, 0) == Constants.PERSON){
                mClientMarkers = markersProvider.getPersonClientMarkers(getArguments().getLong(Constants.PROFILE_TO_OPEN));
            }

            addMarkersToTheMap(mClientMarkers);
            Snackbar snackbar = Snackbar.make(mParentLayout, getString(R.string.marker_added, data.getStringExtra(Constants.CLIENT_MARKER_TITLE)), Snackbar.LENGTH_SHORT);
            snackbar.show();
            Log.i(TAG, "mode: " + getArguments().getInt(Constants.MODE_TO_ADD_CLIENT, 0));
        }
    }

    private BitmapDescriptor getMarkerIcon(int category){
        int res = R.drawable.home;
        switch (category){
            case 0:
                res = R.drawable.home;
            break;
            case 1:
                res = R.drawable.school;
            break;
            case 2:
                res = R.drawable.work;
        }
    return BitmapDescriptorFactory.fromResource(res);
    }

    private void addMarkersToTheMap(ArrayList<ClientMarker> clientMarkers){
        Log.i(TAG, "clientMarkers.size(): " + clientMarkers.size());
        for(ClientMarker clientMarker: clientMarkers){
            mGoogleMap.addMarker(new MarkerOptions()
            .title(clientMarker.getTitle())
            .snippet(clientMarker.getSnippet())
            .icon(getMarkerIcon(clientMarker.getCategory()))
            .position(getMarkerPosition(clientMarker.getLatitude(), clientMarker.getLongitude())));
        }
    }

    private LatLng getMarkerPosition(double latitude, double longitude){
        return new LatLng(latitude, longitude);
    }

    private void initToolbar(android.support.v7.widget.Toolbar toolbar, AppCompatActivity activity){
        activity.setSupportActionBar(toolbar);
        final ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setLogo(R.drawable.app_icon);
            actionBar.setDisplayHomeAsUpEnabled(true);
            if(getArguments().getInt(Constants.MODE_TO_ADD_CLIENT, 0) == Constants.FAMILY){
                FamilyClient client = (FamilyClient) Transporter.newInstance().getObject();
                actionBar.setTitle(client.getClientSecondName() + " " + client.getClientFirstName());
            } else if(getArguments().getInt(Constants.MODE_TO_ADD_CLIENT, 0) == Constants.PERSON){
                PersonClient client = (PersonClient) Transporter.newInstance().getObject();
                actionBar.setTitle(client.getClientSecondName() + " " + client.getClientFirstName());
            }
        }
    }

    private void hideSnackBar(Snackbar snackbar){
        if(snackbar != null){
            if(snackbar.isShown()){
                snackbar.dismiss();
            }
        } else {
            return;
        }
    }

}
