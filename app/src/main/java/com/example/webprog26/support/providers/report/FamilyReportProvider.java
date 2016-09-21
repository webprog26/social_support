package com.example.webprog26.support.providers.report;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import com.example.webprog26.support.providers.ClientProvider;
import com.example.webprog26.support.providers.FamilyClientProvider;
import com.example.webprog26.support.sqlite.SQLiteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by webprog26 on 07.04.2016.
 */
public class FamilyReportProvider extends ClientProvider {
    
    private final static String TAG = "FamilyReportProvider";
    private Date mStartDate, mFinishDate;
    private ArrayList<Long> IDs = new ArrayList<Long>();

    public FamilyReportProvider(Activity activity, Date startDate, Date finishDate) {
        super(activity);
        mStartDate = startDate;
        mFinishDate = finishDate;
        mSQLiteHelper = new SQLiteHelper(activity.getApplicationContext());
        IDs = getClientsPerPeriod();
    }

    private ArrayList<Long> getClientsPerPeriod(){
        ArrayList<Long> IDs = new ArrayList<Long>();
        String countQuery = "SELECT * FROM " + SQLiteHelper.FAMILIES_SERVICES_TABLE_TITLE + " WHERE "
                + SQLiteHelper.FAMILIES_SERVICES_DATE
                + " BETWEEN " + "'" + new SimpleDateFormat("yyyy-MM-dd").format(mStartDate)
                + "' AND '" + new SimpleDateFormat("yyyy-MM-dd").format(mFinishDate) + "'";

        Cursor cursor = mSQLiteHelper.getReadableDatabase().rawQuery(countQuery, null);

        while(cursor.moveToNext()){
            if(!IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID)))){
                IDs.add(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID)));
            }
        }
        cursor.close();
        return IDs;
    }


    @Override
    public int getFamiliesClientsTotalNum() {
       return IDs.size();
    }

    @Override
    public int getFamiliesClientsAdultsNum() {
    int adultsNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                            .query(SQLiteHelper.FAMILIES_TABLE_TITLE,
                                   new String[]{SQLiteHelper.FAMILIES_ADULTS_AMOUNT, SQLiteHelper.FAMILIES_ID}, null, null, null, null, null);
      while(cursor.moveToNext()){
          if(IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))){
              adultsNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ADULTS_AMOUNT));
          }
      }
      cursor.close();
      return adultsNum;
    }

    @Override
    public int getFamiliesClientsKidsNum() {
        int kidsNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.FAMILIES_TABLE_TITLE,
                        new String[]{SQLiteHelper.FAMILIES_KIDS_AMOUNT, SQLiteHelper.FAMILIES_ID}, null, null, null, null, null);
        while(cursor.moveToNext()){
            if(IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))) {
                kidsNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_KIDS_AMOUNT));
            }
        }
        cursor.close();
        return kidsNum;
    }

    @Override
    public int getFamiliesServicesTotalNum() {
        int servicesNum = 0;
            Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.FAMILIES_SERVICES_TABLE_TITLE, new String[]{SQLiteHelper.FAMILIES_SERVICES_AMOUNT, SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID}, null, null, null, null, null);
            while(cursor.moveToNext()){
                if(IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID)))) {
                    servicesNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SERVICES_AMOUNT));
                }
            }
        cursor.close();
        return servicesNum;
    }

    @Override
    public int getFamiliesClientsInDLCTotalNum() {
        int familiesInDLCNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.FAMILIES_TABLE_TITLE, new String[]{SQLiteHelper.FAMILIES_IS_DCL, SQLiteHelper.FAMILIES_ID}, null, null, null, null, null);
        while(cursor.moveToNext()){
            if(IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))) {
                if (cursor.getString((cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_DCL))).equalsIgnoreCase("true")) {
                    familiesInDLCNum++;
                }
            }
        }
        cursor.close();
        return familiesInDLCNum;
    }

    @Override
    public int getFamiliesAdultsInDLCNum() {
        int familiesInDLCAdultsNum = 0;
        Cursor cursor =   mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.FAMILIES_TABLE_TITLE, new String[]{SQLiteHelper.FAMILIES_IS_DCL, SQLiteHelper.FAMILIES_ADULTS_AMOUNT, SQLiteHelper.FAMILIES_ID}, null, null, null, null, null);

        while(cursor.moveToNext()){
            if(IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))) {
                if (cursor.getString((cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_DCL))).equalsIgnoreCase("true")) {
                    familiesInDLCAdultsNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ADULTS_AMOUNT));
                }
            }
        }
        cursor.close();
        return familiesInDLCAdultsNum;
    }

    @Override
    public int getFamiliesKidsInDLCNum() {
        int familiesInDLCKidsNum = 0;
        Cursor cursor =   mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.FAMILIES_TABLE_TITLE, new String[]{SQLiteHelper.FAMILIES_IS_DCL, SQLiteHelper.FAMILIES_KIDS_AMOUNT, SQLiteHelper.FAMILIES_ID}, null, null, null, null, null);

        while(cursor.moveToNext()){
            if(IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))) {
                if (cursor.getString((cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_DCL))).equalsIgnoreCase("true")) {
                    familiesInDLCKidsNum += cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_KIDS_AMOUNT));
                }
            }
        }
        cursor.close();
        return familiesInDLCKidsNum;
    }

    @Override
    public int getFamiliesInDLCServicesTotalNum() {
        int servicesNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.FAMILIES_TABLE_TITLE, new String[]{SQLiteHelper.FAMILIES_ID, SQLiteHelper.FAMILIES_IS_DCL}, null, null, null, null, null);
        while(cursor.moveToNext()) {
            if (IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))) {
                //
                if (cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_DCL)).equalsIgnoreCase("true")) {
                    Log.i(TAG, "isFamilyinDLC: " + "ID: " + cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)) + " " + cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_DCL)));
                    Cursor cursor1 = mSQLiteHelper.getReadableDatabase()
                            .query(SQLiteHelper.FAMILIES_SERVICES_TABLE_TITLE, new String[]{SQLiteHelper.FAMILIES_SERVICES_AMOUNT}, SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID + " = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))}, null, null, null);
                    while (cursor1.moveToNext()) {
                        Log.i(TAG, "Services amount: " + cursor1.getInt(cursor1.getColumnIndex(SQLiteHelper.FAMILIES_SERVICES_AMOUNT)));
                        servicesNum += cursor1.getInt(cursor1.getColumnIndex(SQLiteHelper.FAMILIES_SERVICES_AMOUNT));
                    }
                    cursor1.close();
                }
            }
        }
        cursor.close();
        return servicesNum;
    }

    @Override
    public int getFamiliesUnderSupportTotalNum() {
        int familiesUnderSupportNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.FAMILIES_TABLE_TITLE, new String[]{SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT}, null, null, null, null, null);
        while(cursor.moveToNext()){
            if(cursor.getString((cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT))).equalsIgnoreCase("true")){
                familiesUnderSupportNum++;
            }
        }
        cursor.close();
        return familiesUnderSupportNum;
    }

    @Override
    public int getFamiliesUnderSupportServicesTotalNum() {
        int servicesNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.FAMILIES_TABLE_TITLE, new String[]{SQLiteHelper.FAMILIES_ID, SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT}, null, null, null, null, null);
        while(cursor.moveToNext()) {
            if (IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))) {
                if (cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT)).equalsIgnoreCase("true")) {
                    Cursor cursor1 = mSQLiteHelper.getReadableDatabase()
                        .query(SQLiteHelper.FAMILIES_SERVICES_TABLE_TITLE, new String[]{SQLiteHelper.FAMILIES_SERVICES_AMOUNT}, SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID + " = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))}, null, null, null);
                    while (cursor1.moveToNext()) {
                        servicesNum += cursor1.getInt(cursor1.getColumnIndex(SQLiteHelper.FAMILIES_SERVICES_AMOUNT));
                    }
                cursor1.close();
                }
            }
        }
        cursor.close();
        return servicesNum;
    }

    @Override
    public int getFamiliesSupportStoppedWithPositiveResultTotalNum() {
        int familiesSupportStoppedWithPositiveResult = 0;
        String countQuery = "SELECT * FROM " + SQLiteHelper.FAMILIES_TABLE_TITLE + " WHERE "
                + SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED
                + " BETWEEN " + "'" + new SimpleDateFormat("yyyy-MM-dd").format(mStartDate)
                + "' AND '" + new SimpleDateFormat("yyyy-MM-dd").format(mFinishDate) + "'";

        Cursor cursor = mSQLiteHelper.getReadableDatabase().rawQuery(countQuery, null);
        while(cursor.moveToNext()){

            if(cursor.getString((cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT))).equalsIgnoreCase("true")
                    && cursor.getString((cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_SUPPORT_FINISHED))).equalsIgnoreCase("true")
                    && cursor.getString((cursor.getColumnIndex(SQLiteHelper.FAMILIES_SUPPORT_RESULT))).equalsIgnoreCase("positive")){
                familiesSupportStoppedWithPositiveResult++;
            }
        }
        cursor.close();
        return familiesSupportStoppedWithPositiveResult;
    }

    @Override
    public int getFamiliesSupportStoppedWithNegativeResultTotalNum() {
        int familiesSupportStoppedWithNegativeResult = 0;
        String countQuery = "SELECT * FROM " + SQLiteHelper.FAMILIES_TABLE_TITLE + " WHERE "
                + SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED
                + " BETWEEN " + "'" + new SimpleDateFormat("yyyy-MM-dd").format(mStartDate)
                + "' AND '" + new SimpleDateFormat("yyyy-MM-dd").format(mFinishDate) + "'";

        Cursor cursor = mSQLiteHelper.getReadableDatabase().rawQuery(countQuery, null);
        while(cursor.moveToNext()){
            if(cursor.getString((cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT))).equalsIgnoreCase("true")
                    && cursor.getString((cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_SUPPORT_FINISHED))).equalsIgnoreCase("true")
                    && cursor.getString((cursor.getColumnIndex(SQLiteHelper.FAMILIES_SUPPORT_RESULT))).equalsIgnoreCase("negative")){
                    familiesSupportStoppedWithNegativeResult++;
            }
        }
        cursor.close();
        return familiesSupportStoppedWithNegativeResult;
    }

    @Override
    public int getFamiliesVisitsTotalNum() {
        int visitsNum = 0;
                Cursor cursor = mSQLiteHelper.getReadableDatabase()
                        .query(SQLiteHelper.FAMILIES_VISITS_TABLE_TITLE, new String[]{SQLiteHelper.FAMILIES_VISITS_FAMILY_ID}, null, null, null, null, null);
                while(cursor.moveToNext()){
                    if (IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_VISITS_FAMILY_ID)))) {
                        visitsNum++;
                    }
            }
        cursor.close();
        return visitsNum;
    }

    @Override
    public int getFamiliesFirstVisitsTotalNum() {
        int firstVisitsNum = 0;
        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.FAMILIES_FIRST_VISITS_TABLE_TITLE, new String[]{SQLiteHelper.FAMILIES_FIRST_VISITS_FAMILY_ID}, null, null, null, null, null);
        while(cursor.moveToNext()){
            if (IDs.contains(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_FIRST_VISITS_FAMILY_ID)))) {
                firstVisitsNum++;
            }
        }
        cursor.close();
        return firstVisitsNum;
    }
}
