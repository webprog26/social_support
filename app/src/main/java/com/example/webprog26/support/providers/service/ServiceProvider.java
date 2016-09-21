package com.example.webprog26.support.providers.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.models.Service;
import com.example.webprog26.support.sqlite.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by webprog26 on 14.03.2016.
 */
public class ServiceProvider {

    private static final String TAG = "ServiceProvider";

    private Context mContext;
    private SQLiteHelper mSqLiteHelper;

    public ServiceProvider(Context mContext) {
        this.mContext = mContext;
        mSqLiteHelper = new SQLiteHelper(mContext);
    }

    public long insertFamilyServices(Service service){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.FAMILIES_SERVICES_SERVICE_TYPE, service.getServiceType());
        contentValues.put(SQLiteHelper.FAMILIES_SERVICES_AMOUNT, service.getServicesAmount());
        contentValues.put(SQLiteHelper.FAMILIES_SERVICES_DURING_VISIT, service.getDuringVisit());
        contentValues.put(SQLiteHelper.FAMILIES_SERVICES_DATE, new SimpleDateFormat("yyyy-MM-dd").format(service.getServiceDate()));
        contentValues.put(SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID, service.getClientId());
        return mSqLiteHelper.getWritableDatabase().insert(SQLiteHelper.FAMILIES_SERVICES_TABLE_TITLE, null, contentValues);
    }

    public long insertPersonServices(Service service){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.PERSONS_SERVICES_SERVICE_TYPE, service.getServiceType());
        contentValues.put(SQLiteHelper.PERSONS_SERVICES_AMOUNT, service.getServicesAmount());
        contentValues.put(SQLiteHelper.PERSONS_SERVICES_DURING_VISIT, service.getDuringVisit());
        contentValues.put(SQLiteHelper.PERSONS_SERVICES_DATE, new SimpleDateFormat("yyyy-MM-dd").format(service.getServiceDate()));
        contentValues.put(SQLiteHelper.PERSONS_SERVICES_PERSON_ID, service.getClientId());
        return mSqLiteHelper.getWritableDatabase().insert(SQLiteHelper.PERSONS_SERVICES_TABLE_TITLE, null, contentValues);
    }

    public int familyServicesTotalCount(long familyClientId){
        int totalServicesNum = 0;
        String selection = SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID +  " = ?";
        String[] selectionArgs = new String[]{String.valueOf(familyClientId)};
        Cursor cursor = mSqLiteHelper.getReadableDatabase().query(SQLiteHelper.FAMILIES_SERVICES_TABLE_TITLE, null, selection, selectionArgs, null, null, SQLiteHelper.FAMILIES_SERVICES_DATE);
        while(cursor.moveToNext()){
            totalServicesNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SERVICES_AMOUNT));
        }
        cursor.close();
        return totalServicesNum;
    }

    private int getFamilyInformationServices(long familyClientId){
        int totalServicesNum = 0;
        String selection = SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID +  " = ? AND " + SQLiteHelper.FAMILIES_SERVICES_SERVICE_TYPE + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(familyClientId), Constants.INFORMATION_SERVICE};
        Cursor cursor = mSqLiteHelper.getReadableDatabase().query(SQLiteHelper.FAMILIES_SERVICES_TABLE_TITLE, null, selection, selectionArgs, null, null, SQLiteHelper.FAMILIES_SERVICES_DATE);
        while(cursor.moveToNext()){
            totalServicesNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SERVICES_AMOUNT));
        }
        cursor.close();
        return totalServicesNum;
    }

    private int getFamilySocPedServices(long familyClientId){
        int totalServicesNum = 0;
        String selection = SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID +  " = ? AND " + SQLiteHelper.FAMILIES_SERVICES_SERVICE_TYPE + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(familyClientId), Constants.SOCIO_PEDAGOGICAL_SERVICE};
        Cursor cursor = mSqLiteHelper.getReadableDatabase().query(SQLiteHelper.FAMILIES_SERVICES_TABLE_TITLE, null, selection, selectionArgs, null, null, SQLiteHelper.FAMILIES_SERVICES_DATE);
        while(cursor.moveToNext()){
            totalServicesNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SERVICES_AMOUNT));
        }
        cursor.close();
        return totalServicesNum;
    }

    private int getFamilySocPsychoServices(long familyClientId){
        int totalServicesNum = 0;
        String selection = SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID +  " = ? AND " + SQLiteHelper.FAMILIES_SERVICES_SERVICE_TYPE + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(familyClientId), Constants.SOCIO_PSYCHOLOGICAL_SERVICE};
        Cursor cursor = mSqLiteHelper.getReadableDatabase().query(SQLiteHelper.FAMILIES_SERVICES_TABLE_TITLE, null, selection, selectionArgs, null, null, SQLiteHelper.FAMILIES_SERVICES_DATE);
        while(cursor.moveToNext()){
            totalServicesNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SERVICES_AMOUNT));
        }
        cursor.close();
        return totalServicesNum;
    }

    private int getFamilyJuristServices(long familyClientId){
        int totalServicesNum = 0;
        String selection = SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID +  " = ? AND " + SQLiteHelper.FAMILIES_SERVICES_SERVICE_TYPE + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(familyClientId), Constants.JURISTICAL_SERVICE};
        Cursor cursor = mSqLiteHelper.getReadableDatabase().query(SQLiteHelper.FAMILIES_SERVICES_TABLE_TITLE, null, selection, selectionArgs, null, null, SQLiteHelper.FAMILIES_SERVICES_DATE);
        while(cursor.moveToNext()){
            totalServicesNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SERVICES_AMOUNT));
        }
        cursor.close();
        return totalServicesNum;
    }

    public HashMap<String, String> getFamilyServicesByTypes(long familyClientId){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Constants.INFORMATION_SERVICE, String.valueOf(getFamilyInformationServices(familyClientId)));
        map.put(Constants.SOCIO_PEDAGOGICAL_SERVICE, String.valueOf(getFamilySocPedServices(familyClientId)));
        map.put(Constants.SOCIO_PSYCHOLOGICAL_SERVICE, String.valueOf(getFamilySocPsychoServices(familyClientId)));
        map.put(Constants.JURISTICAL_SERVICE, String.valueOf(getFamilyJuristServices(familyClientId)));
        return map;
    }

    public int personServicesTotalCount(long personClientId){
        int totalServicesNum = 0;
        String selection = SQLiteHelper.PERSONS_SERVICES_PERSON_ID +  " = ?";
        String[] selectionArgs = new String[]{String.valueOf(personClientId)};
        Cursor cursor = mSqLiteHelper.getReadableDatabase().query(SQLiteHelper.PERSONS_SERVICES_TABLE_TITLE, null, selection, selectionArgs, null, null, SQLiteHelper.PERSONS_SERVICES_DATE);
        while(cursor.moveToNext()){
            totalServicesNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.PERSONS_SERVICES_AMOUNT));
        }
        cursor.close();
        return totalServicesNum;
    }

    private int getPersonInformationServices(long personClientId){
        int totalServicesNum = 0;
        String selection = SQLiteHelper.PERSONS_SERVICES_PERSON_ID +  " = ? AND " + SQLiteHelper.PERSONS_SERVICES_SERVICE_TYPE + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(personClientId), Constants.INFORMATION_SERVICE};
        Cursor cursor = mSqLiteHelper.getReadableDatabase().query(SQLiteHelper.PERSONS_SERVICES_TABLE_TITLE, null, selection, selectionArgs, null, null, SQLiteHelper.PERSONS_SERVICES_DATE);
        while(cursor.moveToNext()){
            totalServicesNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.PERSONS_SERVICES_AMOUNT));
        }
        cursor.close();
        return totalServicesNum;
    }

    private int getPersonSocPedServices(long personClientId){
        int totalServicesNum = 0;
        String selection = SQLiteHelper.PERSONS_SERVICES_PERSON_ID +  " = ? AND " + SQLiteHelper.PERSONS_SERVICES_SERVICE_TYPE + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(personClientId), Constants.SOCIO_PEDAGOGICAL_SERVICE};
        Cursor cursor = mSqLiteHelper.getReadableDatabase().query(SQLiteHelper.PERSONS_SERVICES_TABLE_TITLE, null, selection, selectionArgs, null, null, SQLiteHelper.PERSONS_SERVICES_DATE);
        while(cursor.moveToNext()){
            totalServicesNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.PERSONS_SERVICES_AMOUNT));
        }
        cursor.close();
        return totalServicesNum;
    }

    private int getPersonSocPsychoServices(long personClientId){
        int totalServicesNum = 0;
        String selection = SQLiteHelper.PERSONS_SERVICES_PERSON_ID +  " = ? AND " + SQLiteHelper.PERSONS_SERVICES_SERVICE_TYPE + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(personClientId), Constants.SOCIO_PSYCHOLOGICAL_SERVICE};
        Cursor cursor = mSqLiteHelper.getReadableDatabase().query(SQLiteHelper.PERSONS_SERVICES_TABLE_TITLE, null, selection, selectionArgs, null, null, SQLiteHelper.PERSONS_SERVICES_DATE);
        while(cursor.moveToNext()){
            totalServicesNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.PERSONS_SERVICES_AMOUNT));
        }
        cursor.close();
        return totalServicesNum;
    }

    private int getPersonJuristServices(long personClientId){
        int totalServicesNum = 0;
        String selection = SQLiteHelper.PERSONS_SERVICES_PERSON_ID +  " = ? AND " + SQLiteHelper.PERSONS_SERVICES_SERVICE_TYPE + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(personClientId), Constants.JURISTICAL_SERVICE};
        Cursor cursor = mSqLiteHelper.getReadableDatabase().query(SQLiteHelper.PERSONS_SERVICES_TABLE_TITLE, null, selection, selectionArgs, null, null, SQLiteHelper.PERSONS_SERVICES_DATE);
        while(cursor.moveToNext()){
            totalServicesNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.PERSONS_SERVICES_AMOUNT));
        }
        cursor.close();
        return totalServicesNum;
    }

    public HashMap<String, String> getPersonServicesByTypes(long personClientId){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Constants.INFORMATION_SERVICE, String.valueOf(getPersonInformationServices(personClientId)));
        map.put(Constants.SOCIO_PEDAGOGICAL_SERVICE, String.valueOf(getPersonSocPedServices(personClientId)));
        map.put(Constants.SOCIO_PSYCHOLOGICAL_SERVICE, String.valueOf(getPersonSocPsychoServices(personClientId)));
        map.put(Constants.JURISTICAL_SERVICE, String.valueOf(getPersonJuristServices(personClientId)));
        return map;
    }



    private String longToString(long count){
        return String.valueOf(count);
    }

    public long familyServicesDuringVisitsTotalCount(long familyClientId){
        String selection = SQLiteHelper.PERSONS_SERVICES_PERSON_ID +  " = ? AND " + SQLiteHelper.FAMILIES_SERVICES_DURING_VISIT + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(familyClientId), "true"};
        return DatabaseUtils.queryNumEntries(mSqLiteHelper.getReadableDatabase(), SQLiteHelper.FAMILIES_SERVICES_TABLE_TITLE, selection, selectionArgs);
    }

    public long personServicesDuringVisitsTotalCount(long personClientId){
        String selection = SQLiteHelper.PERSONS_SERVICES_PERSON_ID +  " = ? AND " + SQLiteHelper.PERSONS_SERVICES_DURING_VISIT + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(personClientId), "true"};
        return DatabaseUtils.queryNumEntries(mSqLiteHelper.getReadableDatabase(), SQLiteHelper.PERSONS_SERVICES_TABLE_TITLE, selection, selectionArgs);
    }
}
