package com.example.webprog26.support.providers.markers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.webprog26.support.models.ClientMarker;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.sqlite.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by webprog26 on 24.03.2016.
 */
public class MarkersProvider {

    private Context mContext;
    private SQLiteHelper mSqLiteHelper;

    public MarkersProvider(Context mContext) {
        this.mContext = mContext;
        mSqLiteHelper = new SQLiteHelper(mContext);
    }

    public long insertFamilyClientsMarker(ClientMarker clientMarker){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.FAMILIES_MARKERS_CLIENT_ID, clientMarker.getClientId());
        contentValues.put(SQLiteHelper.FAMILIES_MARKERS_TITLE, clientMarker.getTitle());
        contentValues.put(SQLiteHelper.FAMILIES_MARKERS_SNIPPET, clientMarker.getSnippet());
        contentValues.put(SQLiteHelper.FAMILIES_MARKERS_CATEGORY, clientMarker.getCategory());
        contentValues.put(SQLiteHelper.FAMILIES_MARKERS_LATITUDE, clientMarker.getLatitude());
        contentValues.put(SQLiteHelper.FAMILIES_MARKERS_LONGITUDE, clientMarker.getLongitude());
        return mSqLiteHelper.getWritableDatabase().insert(SQLiteHelper.FAMILIES_MARKERS_TABLE_TITLE, null, contentValues);
    }

    public long insertPersonClientsMarker(ClientMarker clientMarker){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.PERSONS_MARKERS_CLIENT_ID, clientMarker.getClientId());
        contentValues.put(SQLiteHelper.PERSONS_MARKERS_TITLE, clientMarker.getTitle());
        contentValues.put(SQLiteHelper.PERSONS_MARKERS_SNIPPET, clientMarker.getSnippet());
        contentValues.put(SQLiteHelper.PERSONS_MARKERS_CATEGORY, clientMarker.getCategory());
        contentValues.put(SQLiteHelper.PERSONS_MARKERS_LATITUDE, clientMarker.getLatitude());
        contentValues.put(SQLiteHelper.PERSONS_MARKERS_LONGITUDE, clientMarker.getLongitude());
        return mSqLiteHelper.getWritableDatabase().insert(SQLiteHelper.PERSONS_MARKERS_TABLE_TITLE, null, contentValues);
    }

    public ArrayList<ClientMarker> getFamilyClientMarkers(long clientId){
        ArrayList<ClientMarker> clientMarkers = new ArrayList<ClientMarker>();

        Cursor cursor = mSqLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.FAMILIES_MARKERS_TABLE_TITLE, null, null, null, null, null, SQLiteHelper.FAMILIES_MARKERS_ID);

        while (cursor.moveToNext()){
            ClientMarker.Builder builder = ClientMarker.newBuilder();
            builder.setMarkerId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_MARKERS_ID)))
                    .setClientMarkerClientId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_MARKERS_CLIENT_ID)))
                    .setClientMarkerTitle(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_MARKERS_TITLE)))
                    .setClientMarkerSnippet(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_MARKERS_SNIPPET)))
                    .setClientMarkerCategory(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_MARKERS_CATEGORY)))
                    .setClientMarkerLatitude(cursor.getDouble(cursor.getColumnIndex(SQLiteHelper.FAMILIES_MARKERS_LATITUDE)))
                    .setClientMarkerLongitude(cursor.getDouble(cursor.getColumnIndex(SQLiteHelper.FAMILIES_MARKERS_LONGITUDE)));
            clientMarkers.add(builder.build());
        }
        cursor.close();
        return clientMarkers;
    }

    public ArrayList<ClientMarker> getPersonClientMarkers(long clientId){
        ArrayList<ClientMarker> clientMarkers = new ArrayList<ClientMarker>();

        Cursor cursor = mSqLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.PERSONS_MARKERS_TABLE_TITLE, null, null, null, null, null, SQLiteHelper.PERSONS_MARKERS_ID);

        while (cursor.moveToNext()){
            ClientMarker.Builder builder = ClientMarker.newBuilder();
            builder.setMarkerId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_MARKERS_ID)))
                        .setClientMarkerClientId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_MARKERS_CLIENT_ID)))
                        .setClientMarkerTitle(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_MARKERS_TITLE)))
                        .setClientMarkerSnippet(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_MARKERS_SNIPPET)))
                        .setClientMarkerCategory(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.PERSONS_MARKERS_CATEGORY)))
                        .setClientMarkerLatitude(cursor.getDouble(cursor.getColumnIndex(SQLiteHelper.PERSONS_MARKERS_LATITUDE)))
                        .setClientMarkerLongitude(cursor.getDouble(cursor.getColumnIndex(SQLiteHelper.PERSONS_MARKERS_LONGITUDE)));
            clientMarkers.add(builder.build());
        }
        cursor.close();
        return clientMarkers;
    }

    public long updateFamilyClientMarker(ClientMarker clientMarker) {
        String strFilter = SQLiteHelper.FAMILIES_MARKERS_ID + " = " + String.valueOf(clientMarker.getMarkerId());
        String table = SQLiteHelper.FAMILIES_MARKERS_TABLE_TITLE;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.FAMILIES_MARKERS_TITLE, clientMarker.getTitle());
        contentValues.put(SQLiteHelper.FAMILIES_MARKERS_CATEGORY, clientMarker.getCategory());
        contentValues.put(SQLiteHelper.FAMILIES_MARKERS_SNIPPET, clientMarker.getSnippet());
        contentValues.put(SQLiteHelper.FAMILIES_MARKERS_CLIENT_ID, clientMarker.getClientId());
        contentValues.put(SQLiteHelper.FAMILIES_MARKERS_LATITUDE, clientMarker.getLatitude());
        contentValues.put(SQLiteHelper.FAMILIES_MARKERS_LONGITUDE, clientMarker.getLongitude());
        return mSqLiteHelper.getWritableDatabase().update(table, contentValues, strFilter, null);
    }

    public long updatePersonClientMarker(ClientMarker clientMarker) {
        String strFilter = SQLiteHelper.PERSONS_MARKERS_ID + " = " + String.valueOf(clientMarker.getMarkerId());
        String table = SQLiteHelper.PERSONS_MARKERS_TABLE_TITLE;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.PERSONS_MARKERS_TITLE, clientMarker.getTitle());
        contentValues.put(SQLiteHelper.PERSONS_MARKERS_CATEGORY, clientMarker.getCategory());
        contentValues.put(SQLiteHelper.PERSONS_MARKERS_SNIPPET, clientMarker.getSnippet());
        contentValues.put(SQLiteHelper.PERSONS_MARKERS_CLIENT_ID, clientMarker.getClientId());
        contentValues.put(SQLiteHelper.PERSONS_MARKERS_LATITUDE, clientMarker.getLatitude());
        contentValues.put(SQLiteHelper.PERSONS_MARKERS_LONGITUDE, clientMarker.getLongitude());
        return mSqLiteHelper.getWritableDatabase().update(table, contentValues, strFilter, null);
    }

    public void deleteFamilyClientMarker(long markerId) {
        mSqLiteHelper.getWritableDatabase().delete(SQLiteHelper.FAMILIES_MARKERS_TABLE_TITLE, SQLiteHelper.FAMILIES_MARKERS_ID + " = " + markerId, null);
    }

    public void deletePersonClientMarker(long markerId) {
        mSqLiteHelper.getWritableDatabase().delete(SQLiteHelper.PERSONS_MARKERS_TABLE_TITLE, SQLiteHelper.PERSONS_MARKERS_ID + " = " + markerId, null);
    }
}
