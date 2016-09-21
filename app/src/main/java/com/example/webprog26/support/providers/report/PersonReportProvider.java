package com.example.webprog26.support.providers.report;

import android.app.Activity;
import android.database.Cursor;

import com.example.webprog26.support.providers.ClientProvider;
import com.example.webprog26.support.sqlite.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by webprog26 on 07.04.2016.
 */
public class PersonReportProvider extends ClientProvider{

    private static final String TAG = "";

    private Date mStartDate, mFinishDate;
    private ArrayList<Long> IDs = new ArrayList<Long>();

    private SQLiteHelper mSqLiteHelper;

    public PersonReportProvider(Activity activity, Date startDate, Date finishDate) {
        super(activity);
        mStartDate = startDate;
        mFinishDate = finishDate;
        mSQLiteHelper = new SQLiteHelper(activity.getApplicationContext());
        IDs = getClientsPerPeriod();
    }

    private ArrayList<Long> getClientsPerPeriod(){
        ArrayList<Long> IDs = new ArrayList<Long>();
        String countQuery = "SELECT * FROM " + SQLiteHelper.PERSONS_SERVICES_TABLE_TITLE + " WHERE "
                + SQLiteHelper.PERSONS_SERVICES_DATE
                + " BETWEEN " + "'" + new SimpleDateFormat("yyyy-MM-dd").format(mStartDate)
                + "' AND '" + new SimpleDateFormat("yyyy-MM-dd").format(mFinishDate) + "'";

        Cursor cursor = mSQLiteHelper.getReadableDatabase().rawQuery(countQuery, null);

        while(cursor.moveToNext()){
            if(!IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_SERVICES_PERSON_ID)))){
                IDs.add(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_SERVICES_PERSON_ID)));
            }
        }
        cursor.close();
        return IDs;
    }

    @Override
    public int getPersonsClientsTotalNum() {
        return IDs.size();
    }

    @Override
    public int getPersonsServicesTotalNum() {
        int servicesNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.PERSONS_SERVICES_TABLE_TITLE, new String[]{SQLiteHelper.PERSONS_SERVICES_AMOUNT, SQLiteHelper.PERSONS_SERVICES_PERSON_ID}, null, null, null, null, null);
        while(cursor.moveToNext()){
            if(IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_SERVICES_PERSON_ID)))) {
                servicesNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.PERSONS_SERVICES_AMOUNT));
            }
        }
        cursor.close();
        return servicesNum;
    }

    @Override
    public int getPersonsClientsInDLCTotalNum() {
        int personsInDLCNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.PERSONS_TABLE_TITLE, new String[]{SQLiteHelper.PERSONS_IS_DCL, SQLiteHelper.PERSONS_ID}, null, null, null, null, null);
        while(cursor.moveToNext()){
            if(IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_ID)))) {
                if (cursor.getString((cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_DCL))).equalsIgnoreCase("true")) {
                    personsInDLCNum++;
                }
            }
        }
        cursor.close();
        return personsInDLCNum;
    }

    @Override
    public int getPersonsInDLCServicesTotalNum() {
        int servicesNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.PERSONS_TABLE_TITLE, new String[]{SQLiteHelper.PERSONS_ID, SQLiteHelper.PERSONS_IS_DCL}, null, null, null, null, null);
        while(cursor.moveToNext()) {
            if (IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_ID)))) {
                if (cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_DCL)).equalsIgnoreCase("true")) {
                    Cursor cursor1 = mSQLiteHelper.getReadableDatabase()
                            .query(SQLiteHelper.PERSONS_SERVICES_TABLE_TITLE, new String[]{SQLiteHelper.PERSONS_SERVICES_AMOUNT}, SQLiteHelper.PERSONS_SERVICES_PERSON_ID + " = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.PERSONS_ID)))}, null, null, null);
                    while (cursor1.moveToNext()) {
                        servicesNum += cursor1.getInt(cursor1.getColumnIndex(SQLiteHelper.PERSONS_SERVICES_AMOUNT));
                    }
                    cursor1.close();
                }
            }
        }
        cursor.close();
        return servicesNum;
    }

    @Override
    public int getPersonsUnderSupportTotalNum() {
        int personsUnderSupportNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.PERSONS_TABLE_TITLE, new String[]{SQLiteHelper.PERSONS_IS_UNDER_SUPPORT}, null, null, null, null, null);
        while(cursor.moveToNext()){
            if(cursor.getString((cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_UNDER_SUPPORT))).equalsIgnoreCase("true")){
                personsUnderSupportNum++;
            }
        }
        cursor.close();
        return personsUnderSupportNum;
    }

    @Override
    public int getPersonsUnderSupportServicesTotalNum() {
        int servicesNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.PERSONS_TABLE_TITLE, new String[]{SQLiteHelper.PERSONS_ID, SQLiteHelper.PERSONS_IS_UNDER_SUPPORT}, null, null, null, null, null);
        while(cursor.moveToNext()) {
            if (IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_ID)))) {
                if (cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_UNDER_SUPPORT)).equalsIgnoreCase("true")) {
                    Cursor cursor1 = mSQLiteHelper.getReadableDatabase()
                            .query(SQLiteHelper.PERSONS_SERVICES_TABLE_TITLE, new String[]{SQLiteHelper.PERSONS_SERVICES_AMOUNT}, SQLiteHelper.PERSONS_SERVICES_PERSON_ID + " = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.PERSONS_ID)))}, null, null, null);
                    while (cursor1.moveToNext()) {
                        servicesNum += cursor1.getInt(cursor1.getColumnIndex(SQLiteHelper.PERSONS_SERVICES_AMOUNT));
                    }
                    cursor1.close();
                }
            }
        }
        cursor.close();
        return servicesNum;
    }

    @Override
    public int getPersonsStoppedWithPositiveResultTotalNum() {
        int personsSupportStoppedWithPositiveResult = 0;
        String countQuery = "SELECT * FROM " + SQLiteHelper.PERSONS_TABLE_TITLE + " WHERE "
                + SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED
                + " BETWEEN " + "'" + new SimpleDateFormat("yyyy-MM-dd").format(mStartDate)
                + "' AND '" + new SimpleDateFormat("yyyy-MM-dd").format(mFinishDate) + "'";

        Cursor cursor = mSQLiteHelper.getReadableDatabase().rawQuery(countQuery, null);
        while(cursor.moveToNext()){

            if(cursor.getString((cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_UNDER_SUPPORT))).equalsIgnoreCase("true")
                    && cursor.getString((cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_SUPPORT_FINISHED))).equalsIgnoreCase("true")
                    && cursor.getString((cursor.getColumnIndex(SQLiteHelper.PERSONS_SUPPORT_RESULT))).equalsIgnoreCase("positive")){
                personsSupportStoppedWithPositiveResult++;
            }
        }
        cursor.close();
        return personsSupportStoppedWithPositiveResult;
    }

    @Override
    public int getPersonsSupportStoppedWithNegativeResultTotalNum() {
        int personsSupportStoppedWithNegativeResult = 0;
        String countQuery = "SELECT * FROM " + SQLiteHelper.PERSONS_TABLE_TITLE + " WHERE "
                + SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED
                + " BETWEEN " + "'" + new SimpleDateFormat("yyyy-MM-dd").format(mStartDate)
                + "' AND '" + new SimpleDateFormat("yyyy-MM-dd").format(mFinishDate) + "'";

        Cursor cursor = mSQLiteHelper.getReadableDatabase().rawQuery(countQuery, null);
        while(cursor.moveToNext()){
            if(cursor.getString((cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_UNDER_SUPPORT))).equalsIgnoreCase("true")
                    && cursor.getString((cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_SUPPORT_FINISHED))).equalsIgnoreCase("true")
                    && cursor.getString((cursor.getColumnIndex(SQLiteHelper.PERSONS_SUPPORT_RESULT))).equalsIgnoreCase("negative")){
                personsSupportStoppedWithNegativeResult++;
            }
        }
        cursor.close();
        return personsSupportStoppedWithNegativeResult;
    }

    @Override
    public int getPersonsVisitsTotalNum() {
        int visitsNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.PERSONS_VISITS_TABLE_TITLE, new String[]{SQLiteHelper.PERSONS_VISITS_PERSON_ID}, null, null, null, null, null);
        while(cursor.moveToNext()){
            if (IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_VISITS_PERSON_ID)))) {
                visitsNum++;
            }
        }
        cursor.close();
        return visitsNum;
    }

    @Override
    public int getPersonsFirstVisitsTotalNum() {
        int firstVisitsNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.PERSONS_FIRST_VISITS_TABLE_TITLE, new String[]{SQLiteHelper.PERSONS_FIRST_VISITS_PERSON_ID}, null, null, null, null, null);
        while(cursor.moveToNext()){
            if (IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_FIRST_VISITS_PERSON_ID)))) {
                firstVisitsNum++;
            }
        }
        cursor.close();
        return firstVisitsNum;
    }
}
