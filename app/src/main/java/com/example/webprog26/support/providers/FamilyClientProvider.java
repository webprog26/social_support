package com.example.webprog26.support.providers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.DateFormat;
import android.util.Log;

import com.example.webprog26.support.R;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.PersonClient;
import com.example.webprog26.support.models.Photo;
import com.example.webprog26.support.models.Visit;
import com.example.webprog26.support.sqlite.SQLiteHelper;
import com.example.webprog26.support.utils.PictureUtils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by webprog26 on 06.03.2016.
 */
public class FamilyClientProvider extends ClientProvider{

    private static final String TAG = "FamilyClientProvider";

    SQLiteHelper mSQLiteHelper;
    public FamilyClientProvider(Activity activity) {
        super(activity);
        mSQLiteHelper = new SQLiteHelper(activity.getApplicationContext());
    }

    @Override
    public long insertFamilyClientDataToDb(FamilyClient client) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.FAMILIES_FIRST_NAME, client.getClientFirstName());
        contentValues.put(SQLiteHelper.FAMILIES_PATRONYMIC, client.getClientPatronymic());
        contentValues.put(SQLiteHelper.FAMILIES_SECOND_NAME, client.getClientSecondName());
        contentValues.put(SQLiteHelper.FAMILIES_CATEGORY, client.getClientCategory());
        contentValues.put(SQLiteHelper.FAMILIES_ADDRESS, client.getClientAddress());
        contentValues.put(SQLiteHelper.FAMILIES_ADULTS_AMOUNT, client.getClientAdultsAmount());
        contentValues.put(SQLiteHelper.FAMILIES_KIDS_AMOUNT, client.getClientKidsAmount());
        contentValues.put(SQLiteHelper.FAMILIES_PROBLEM, client.getClientProblem());
        contentValues.put(SQLiteHelper.FAMILIES_IS_DCL, client.getClientisDLC());
        contentValues.put(SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT, client.getClientIsUnderSupport());
        if(client.getDateSupportStarted() != null){
            contentValues.put(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED,
                    new SimpleDateFormat("yyyy-MM-dd").format(client.getDateSupportStarted()));
        }
        contentValues.put(SQLiteHelper.FAMILIES_IS_SUPPORT_FINISHED, client.getIsSupportFinished());
        contentValues.put(SQLiteHelper.FAMILIES_SUPPORT_RESULT, client.getSupportResult());
        if(client.getDateSupportFinished() != null){
            contentValues.put(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED,
                    new SimpleDateFormat("yyyy-MM-dd").format(client.getDateSupportFinished()));
        }
        contentValues.put(SQLiteHelper.FAMILIES_REGISTRATION_DATE,
                new SimpleDateFormat("yyyy-MM-dd").format(client.getClientRegDate()));
        contentValues.put(SQLiteHelper.FAMILIES_PHOTO_PATH, client.getPhoto().getFileName());
        return mSQLiteHelper.getWritableDatabase().insert(SQLiteHelper.FAMILIES_TABLE_TITLE, null, contentValues);
    }

    @Override
    public ArrayList<FamilyClient> getFamilyClientData() throws ParseException{
        ArrayList<FamilyClient> familyClients = new ArrayList<FamilyClient>();

        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.FAMILIES_TABLE_TITLE, null, null, null, null, null, SQLiteHelper.FAMILIES_REGISTRATION_DATE);

        while(cursor.moveToNext()){
            FamilyClient.Builder builder = FamilyClient.newBuilder();
            builder.setClientId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))
                    .setClientSecondName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SECOND_NAME)))
                    .setClientFirstName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_FIRST_NAME)))
                    .setClientPatronymic(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PATRONYMIC)))
                    .setClientCategory(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_CATEGORY)))
                    .setClientAddress(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ADDRESS)))
                    .setClientAdultsAmount(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ADULTS_AMOUNT)))
                    .setClientKidsAmount(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_KIDS_AMOUNT)))
                    .setClientProblem(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PROBLEM)))
                    .setClientisDLC(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_DCL)))
                    .setClientIsUnderSupport(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT)))
                    .setClientIsSupportFinished(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_SUPPORT_FINISHED)))
                    .setClientSupportResult(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SUPPORT_RESULT)))
                    .setClientPhoto(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PHOTO_PATH)))
                    .setClientRegDate(
                            new SimpleDateFormat("yyyy-MM-dd")
                                    .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_REGISTRATION_DATE))));
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED)) != null){
                builder.setDateSupportStarted(
                        new SimpleDateFormat("yyyy-MM-dd")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED))));
            }
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED)) != null){
                builder.setDateSupportFinished(
                        new SimpleDateFormat("yyyy-MM-dd")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED))));
            }
            familyClients.add((FamilyClient) builder.build());
        }
        cursor.close();
        return familyClients;
    }

    @Override
    public long updateFamilyClientData(FamilyClient client) {
        String strFilter = SQLiteHelper.FAMILIES_ID + " = " + String.valueOf(client.getClientId());
        Log.i(TAG, "strFilter: " + strFilter);
        String table = SQLiteHelper.FAMILIES_TABLE_TITLE;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.FAMILIES_SECOND_NAME, client.getClientSecondName());
        contentValues.put(SQLiteHelper.FAMILIES_FIRST_NAME, client.getClientFirstName());
        contentValues.put(SQLiteHelper.FAMILIES_PATRONYMIC, client.getClientPatronymic());
        contentValues.put(SQLiteHelper.FAMILIES_ADDRESS, client.getClientAddress());
        contentValues.put(SQLiteHelper.FAMILIES_CATEGORY, client.getClientCategory());
        contentValues.put(SQLiteHelper.FAMILIES_PROBLEM, client.getClientProblem());
        contentValues.put(SQLiteHelper.FAMILIES_ADULTS_AMOUNT, client.getClientAdultsAmount());
        contentValues.put(SQLiteHelper.FAMILIES_KIDS_AMOUNT, client.getClientKidsAmount());
        contentValues.put(SQLiteHelper.FAMILIES_IS_DCL, client.getClientisDLC());
        contentValues.put(SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT, client.getClientIsUnderSupport());
        if(client.getDateSupportStarted() != null){
            contentValues.put(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED,
                    new SimpleDateFormat("yyyy-MM-dd").format(client.getDateSupportStarted()));
        }
        contentValues.put(SQLiteHelper.FAMILIES_IS_SUPPORT_FINISHED, client.getIsSupportFinished());
        contentValues.put(SQLiteHelper.FAMILIES_SUPPORT_RESULT, client.getSupportResult());
        if(client.getDateSupportFinished() != null){
            contentValues.put(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED,
                    new SimpleDateFormat("yyyy-MM-dd").format(client.getDateSupportFinished()));
        }
        contentValues.put(SQLiteHelper.FAMILIES_PHOTO_PATH, client.getPhoto().getFileName());
        return mSQLiteHelper.getWritableDatabase().update(table, contentValues, strFilter, null);
    }

    @Override
    public void deleteFamilyClient(long familyClientId) {
        mSQLiteHelper.getWritableDatabase().delete(SQLiteHelper.FAMILIES_TABLE_TITLE, SQLiteHelper.FAMILIES_ID + " = " + familyClientId, null);
        mSQLiteHelper.getWritableDatabase().delete(SQLiteHelper.FAMILIES_VISITS_TABLE_TITLE, SQLiteHelper.FAMILIES_VISITS_FAMILY_ID + " = " + familyClientId, null);
        mSQLiteHelper.getWritableDatabase().delete(SQLiteHelper.FAMILIES_SERVICES_TABLE_TITLE, SQLiteHelper.FAMILIES_SERVICES_FAMILY_ID + " = " + familyClientId, null);
        mSQLiteHelper.getWritableDatabase().delete(SQLiteHelper.FAMILIES_MARKERS_TABLE_TITLE, SQLiteHelper.FAMILIES_MARKERS_CLIENT_ID + " = " + familyClientId, null);
    }

    @Override
    public ArrayList<FamilyClient> getFamiliesSupportStoppedPositive() throws ParseException{
        ArrayList<FamilyClient> familyClients = new ArrayList<FamilyClient>();

        Cursor cursor = mSQLiteHelper.getReadableDatabase().query(SQLiteHelper.FAMILIES_TABLE_TITLE,
                null, SQLiteHelper.FAMILIES_SUPPORT_RESULT + " = ? AND + " + SQLiteHelper.FAMILIES_IS_SUPPORT_FINISHED + " = ?", new String[]{"positive", "true"}, null, null, null);

        while(cursor.moveToNext()){
            FamilyClient.Builder builder = FamilyClient.newBuilder();
            builder.setClientId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))
                    .setClientSecondName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SECOND_NAME)))
                    .setClientFirstName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_FIRST_NAME)))
                    .setClientPatronymic(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PATRONYMIC)))
                    .setClientCategory(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_CATEGORY)))
                    .setClientAddress(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ADDRESS)))
                    .setClientAdultsAmount(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ADULTS_AMOUNT)))
                    .setClientKidsAmount(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_KIDS_AMOUNT)))
                    .setClientProblem(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PROBLEM)))
                    .setClientisDLC(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_DCL)))
                    .setClientIsUnderSupport(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT)))
                    .setClientIsSupportFinished(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_SUPPORT_FINISHED)))
                    .setClientSupportResult(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SUPPORT_RESULT)))
                    .setClientPhoto(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PHOTO_PATH)))
                    .setClientRegDate(
                            new SimpleDateFormat("yyyy-MM-dd")
                                    .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_REGISTRATION_DATE))));
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED)) != null){
                builder.setDateSupportStarted(
                        new SimpleDateFormat("yyyy-MM-dd")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED))));
            }
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED)) != null){
                builder.setDateSupportFinished(
                        new SimpleDateFormat("yyyy-MM-dd")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED))));
            }
            familyClients.add((FamilyClient) builder.build());
        }
        cursor.close();
        return familyClients;
    }

    @Override
    public ArrayList<FamilyClient> getFamiliesSupportStoopedNegative() throws ParseException {
        ArrayList<FamilyClient> familyClients = new ArrayList<FamilyClient>();

        Cursor cursor = mSQLiteHelper.getReadableDatabase().query(SQLiteHelper.FAMILIES_TABLE_TITLE,
                null, SQLiteHelper.FAMILIES_SUPPORT_RESULT + " = ? AND + " + SQLiteHelper.FAMILIES_IS_SUPPORT_FINISHED + " = ?", new String[]{"negative", "true"}, null, null, null);

        while(cursor.moveToNext()){
            FamilyClient.Builder builder = FamilyClient.newBuilder();
            builder.setClientId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))
                    .setClientSecondName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SECOND_NAME)))
                    .setClientFirstName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_FIRST_NAME)))
                    .setClientPatronymic(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PATRONYMIC)))
                    .setClientCategory(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_CATEGORY)))
                    .setClientAddress(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ADDRESS)))
                    .setClientAdultsAmount(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ADULTS_AMOUNT)))
                    .setClientKidsAmount(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_KIDS_AMOUNT)))
                    .setClientProblem(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PROBLEM)))
                    .setClientisDLC(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_DCL)))
                    .setClientIsUnderSupport(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT)))
                    .setClientIsSupportFinished(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_SUPPORT_FINISHED)))
                    .setClientSupportResult(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SUPPORT_RESULT)))
                    .setClientPhoto(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PHOTO_PATH)))
                    .setClientRegDate(
                            new SimpleDateFormat("yyyy-MM-dd")
                                    .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_REGISTRATION_DATE))));
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED)) != null){
                builder.setDateSupportStarted(
                        new SimpleDateFormat("yyyy-MM-dd")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED))));
            }
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED)) != null){
                builder.setDateSupportFinished(
                        new SimpleDateFormat("yyyy-MM-dd")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED))));
            }
            familyClients.add((FamilyClient) builder.build());
        }
        cursor.close();
        return familyClients;
    }

    @Override
    public ArrayList<FamilyClient> getFamiliesSupportContinues() throws ParseException {
        ArrayList<FamilyClient> familyClients = new ArrayList<FamilyClient>();

        Cursor cursor = mSQLiteHelper.getReadableDatabase().query(SQLiteHelper.FAMILIES_TABLE_TITLE,
                null, SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT + " = ? AND + " + SQLiteHelper.FAMILIES_IS_SUPPORT_FINISHED + " = ?", new String[]{"true", "false"}, null, null, null);
        while(cursor.moveToNext()){
            FamilyClient.Builder builder = FamilyClient.newBuilder();
            builder.setClientId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ID)))
                    .setClientSecondName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SECOND_NAME)))
                    .setClientFirstName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_FIRST_NAME)))
                    .setClientPatronymic(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PATRONYMIC)))
                    .setClientCategory(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_CATEGORY)))
                    .setClientAddress(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ADDRESS)))
                    .setClientAdultsAmount(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_ADULTS_AMOUNT)))
                    .setClientKidsAmount(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.FAMILIES_KIDS_AMOUNT)))
                    .setClientProblem(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PROBLEM)))
                    .setClientisDLC(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_DCL)))
                    .setClientIsUnderSupport(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_UNDER_SUPPORT)))
                    .setClientIsSupportFinished(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_IS_SUPPORT_FINISHED)))
                    .setClientSupportResult(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_SUPPORT_RESULT)))
                    .setClientPhoto(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_PHOTO_PATH)))
                    .setClientRegDate(
                            new SimpleDateFormat("yyyy-MM-dd")
                                    .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_REGISTRATION_DATE))));
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED)) != null){
                builder.setDateSupportStarted(
                        new SimpleDateFormat("yyyy-MM-dd")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_STARTED))));
            }
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED)) != null){
                builder.setDateSupportFinished(
                        new SimpleDateFormat("yyyy-MM-dd")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.FAMILIES_DATE_SUPPORT_FINISHED))));
            }
            familyClients.add((FamilyClient) builder.build());
        }
        cursor.close();
        return familyClients;
    }
}
