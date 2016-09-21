package com.example.webprog26.support.providers.search;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.webprog26.support.constants.Constants;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.PersonClient;
import com.example.webprog26.support.sqlite.SQLiteHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by webprog26 on 22.03.2016.
 */
public class SearchProvider {

    private static final String TAG = "SearchProvider";

    private Context mContext;
    private SQLiteHelper mSQLiteHelper;
    private int mSearchMode;

    public SearchProvider(Context context, int searchMode) {
        mContext = context;
        mSQLiteHelper = new SQLiteHelper(mContext);
        mSearchMode = searchMode;
    }

    public Cursor getWordMatches(String query, String[] columns) {
        String columnName = null;
        switch (mSearchMode){
            case Constants.SEARCH_MODE_FAMILY:
                columnName = SQLiteHelper.FAMILIES_SECOND_NAME;
                break;
            case Constants.SEARCH_MODE_PERSON:
                columnName = SQLiteHelper.PERSONS_SECOND_NAME;
                break;
        }
        String selection = columnName + " LIKE ?";
        String[] selectionArgs = new String[] {"%"+query+"%"};

        return query(selection, selectionArgs, columns);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        String inTables = null;
        switch (mSearchMode){
            case Constants.SEARCH_MODE_FAMILY:
                inTables = SQLiteHelper.FAMILIES_TABLE_TITLE;
                break;
            case Constants.SEARCH_MODE_PERSON:
                inTables = SQLiteHelper.PERSONS_TABLE_TITLE;
                break;
        }

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(inTables);

        Cursor cursor = builder.query(mSQLiteHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    public ArrayList<FamilyClient> getSearchResultsFamilyClients(String query) throws ParseException{
        ArrayList<FamilyClient> familyClients = new ArrayList<FamilyClient>();

        Cursor cursor = getWordMatches(query, null);
        if(cursor == null) {
            Log.i(TAG, "Cursor is null in getSearchResultsFamilyClients");
            return null;
        }
        while(cursor.getPosition() < cursor.getCount()){
            FamilyClient.Builder familyClientBuilder = FamilyClient.newBuilder();
            familyClientBuilder.setClientId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))
                    .setClientSecondName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SECOND_NAME)))
                    .setClientFirstName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_FIRST_NAME)))
                    .setClientPatronymic(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PATRONYMIC)))
                    .setClientCategory(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_CATEGORY)))
                    .setClientAddress(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ADDRESS)))
                    .setClientKidsAmount(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_KIDS_AMOUNT)))
                    .setClientProblem(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PROBLEM)))
                    .setClientisDLC(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_DCL)))
                    .setClientIsUnderSupport(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT)))
                    .setClientIsSupportFinished(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_SUPPORT_FINISHED)))
                    .setClientSupportResult(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SUPPORT_RESULT)))
                    .setClientPhoto(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PHOTO_PATH)));
                        familyClientBuilder.setClientRegDate(
                                new SimpleDateFormat("yyyy-MM-dd")
                                        .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_REGISTRATION_DATE))));
                        if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED)) != null){
                            familyClientBuilder.setDateSupportStarted(
                                    new SimpleDateFormat("yyyy-MM-dd")
                                            .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED))));

                        }
                        if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED)) != null){
                            familyClientBuilder.setDateSupportFinished(
                                    new SimpleDateFormat("yyyy-MM-dd")
                                            .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED))));
                        }

            Log.i(TAG, "REGISTRATION_DATE BACK: " + cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_REGISTRATION_DATE)));
            Log.i(TAG, "DATE_SUPPORT_STARTED BACK: " + cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED)));
            Log.i(TAG, "DATE_SUPPORT_FINISHED BACK: " + cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED)));
            familyClients.add((FamilyClient) familyClientBuilder.build());
            cursor.moveToNext();
        }
        cursor.close();
        return familyClients;
    }

    public ArrayList<PersonClient> getSearchResultsPersonClients(String query)throws ParseException{
        ArrayList<PersonClient> personClients = new ArrayList<PersonClient>();
        Cursor cursor = getWordMatches(query, null);
        if(cursor == null) {
            Log.i(TAG, "Cursor is null in getSearchResultsFamilyClients");
            return null;
        }
        while(cursor.getPosition() < cursor.getCount()){
            PersonClient.Builder personClientBuilder = PersonClient.newBuilder();
            personClientBuilder.setClientId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_ID)))
                    .setClientSecondName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_SECOND_NAME)))
                    .setClientFirstName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_FIRST_NAME)))
                    .setClientPatronymic(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_PATRONYMIC)))
                    .setClientAddress(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_ADDRESS)))
                    .setClientProblem(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_PROBLEM)))
                    .setClientCategory(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_CATEGORY)))
                    .setClientIsUnderSupport(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_UNDER_SUPPORT)))
                    .setClientIsSupportFinished(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_SUPPORT_FINISHED)))
                    .setClientSupportResult(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_SUPPORT_RESULT)))
                    .setClientPhoto(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_PHOTO_PATH)))
                    .setClientisDLC(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_DCL)));
                        personClientBuilder.setClientRegDate(
                                new SimpleDateFormat("yyyy-MM-dd")
                                        .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_REGISTRATION_DATE))));
                        if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_STARTED)) != null){
                            personClientBuilder.setDateSupportStarted(
                                    new SimpleDateFormat("yyyy-MM-dd")
                                            .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_STARTED))));
                        }
                        if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED)) != null){
                            personClientBuilder.setDateSupportFinished(
                                    new SimpleDateFormat("yyyy-MM-dd")
                                            .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED))));
                        }
            personClients.add((PersonClient) personClientBuilder.build());
            cursor.moveToNext();
        }
        cursor.close();
        return personClients;
    }


}
