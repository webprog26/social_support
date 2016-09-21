package com.example.webprog26.support.providers.visit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;

import com.example.webprog26.support.models.Visit;
import com.example.webprog26.support.sqlite.SQLiteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by webprog26 on 11.03.2016.
 */
public class VisitProvider {

    private Context mContext;
    private SQLiteHelper mSqLiteHelper;

    public VisitProvider(Context mContext) {
        this.mContext = mContext;
        mSqLiteHelper = new SQLiteHelper(mContext);
    }

    public long insertFamilyVisits(Visit visit){
        if(familyVisitsCount(visit.getClientId()) == 0){
            insertFamilyFirstVisit(visit);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.FAMILIES_VISITS_DATE, visit.getVisitDate().toString());
        contentValues.put(SQLiteHelper.FAMILIES_VISITS_FAMILY_ID, visit.getClientId());

        return mSqLiteHelper.getWritableDatabase().insert(SQLiteHelper.FAMILIES_VISITS_TABLE_TITLE, null, contentValues);
    }

    public long familyVisitsCount(long familyClientId){
        String selection = SQLiteHelper.FAMILIES_VISITS_FAMILY_ID +  " = ?";
        String[] selectionArgs = new String[]{String.valueOf(familyClientId)};
        return DatabaseUtils.queryNumEntries(mSqLiteHelper.getReadableDatabase(), SQLiteHelper.FAMILIES_VISITS_TABLE_TITLE, selection, selectionArgs);

    }

    public long insertPersonVisit(Visit visit){
        if(personVisitsCount(visit.getClientId()) == 0){
           insertPersonFirstVisit(visit);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.PERSONS_VISITS_DATE, visit.getVisitDate().toString());
        contentValues.put(SQLiteHelper.PERSONS_VISITS_PERSON_ID, visit.getClientId());

        return mSqLiteHelper.getWritableDatabase().insert(SQLiteHelper.PERSONS_VISITS_TABLE_TITLE, null, contentValues);
    }

    public long personVisitsCount(long personClientId){
        String selection = SQLiteHelper.PERSONS_VISITS_PERSON_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(personClientId)};
        return DatabaseUtils.queryNumEntries(mSqLiteHelper.getReadableDatabase(), SQLiteHelper.PERSONS_VISITS_TABLE_TITLE, selection, selectionArgs);
    }

    public long insertFamilyFirstVisit(Visit visit){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.FAMILIES_FIRST_VISITS_DATE, visit.getVisitDate().toString());
        contentValues.put(SQLiteHelper.FAMILIES_FIRST_VISITS_FAMILY_ID, visit.getClientId());

        return mSqLiteHelper.getWritableDatabase().insert(SQLiteHelper.FAMILIES_FIRST_VISITS_TABLE_TITLE, null, contentValues);
    }

    public long insertPersonFirstVisit(Visit visit){
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.PERSONS_FIRST_VISITS_DATE, visit.getVisitDate().toString());
        contentValues.put(SQLiteHelper.PERSONS_FIRST_VISITS_PERSON_ID, visit.getClientId());

        return mSqLiteHelper.getWritableDatabase().insert(SQLiteHelper.PERSONS_FIRST_VISITS_TABLE_TITLE, null, contentValues);
    }

}
