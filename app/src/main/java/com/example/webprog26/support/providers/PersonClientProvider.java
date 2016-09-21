package com.example.webprog26.support.providers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
public class PersonClientProvider extends ClientProvider {

    private static final String TAG = "PersonClientProvider";

    SQLiteHelper mSQLiteHelper;
    public PersonClientProvider(Activity activity) {
        super(activity);
        mSQLiteHelper = new SQLiteHelper(activity.getApplicationContext());
    }

    @Override
    public long insertPersonDataToDb(PersonClient client) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.PERSONS_FIRST_NAME, client.getClientFirstName());
        contentValues.put(SQLiteHelper.PERSONS_PATRONYMIC, client.getClientPatronymic());
        contentValues.put(SQLiteHelper.PERSONS_SECOND_NAME, client.getClientSecondName());
        contentValues.put(SQLiteHelper.PERSONS_CATEGORY, client.getClientCategory());
        contentValues.put(SQLiteHelper.PERSONS_ADDRESS, client.getClientAddress());
        contentValues.put(SQLiteHelper.PERSONS_PROBLEM, client.getClientProblem());
        contentValues.put(SQLiteHelper.PERSONS_IS_DCL, client.getClientisDLC());
        contentValues.put(SQLiteHelper.PERSONS_IS_UNDER_SUPPORT, client.getClientIsUnderSupport());
        if(client.getDateSupportStarted() != null){
            contentValues.put(SQLiteHelper.PERSONS_DATE_SUPPORT_STARTED,
                    new SimpleDateFormat("dd:MM:yyyy").format(client.getDateSupportStarted()));
        }
        contentValues.put(SQLiteHelper.PERSONS_IS_SUPPORT_FINISHED, client.getIsSupportFinished());
        contentValues.put(SQLiteHelper.PERSONS_SUPPORT_RESULT, client.getSupportResult());
        if(client.getDateSupportFinished() != null){
            contentValues.put(SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED,
                    new SimpleDateFormat("dd:MM:yyyy").format(client.getDateSupportFinished()));
        }
        contentValues.put(SQLiteHelper.PERSONS_REGISTRATION_DATE,
                new SimpleDateFormat("dd:MM:yyyy").format(client.getClientRegDate()));
        contentValues.put(SQLiteHelper.PERSONS_PHOTO_PATH, client.getPhoto().getFileName());
        return mSQLiteHelper.getWritableDatabase().insert(SQLiteHelper.PERSONS_TABLE_TITLE, null, contentValues);
    }

    @Override
    public ArrayList<PersonClient> getPersonClientData() throws ParseException{
        ArrayList<PersonClient> personClients = new ArrayList<PersonClient>();

        Cursor cursor = mSQLiteHelper.getReadableDatabase()
                .query(SQLiteHelper.PERSONS_TABLE_TITLE, null, null, null, null, null, SQLiteHelper.PERSONS_REGISTRATION_DATE);

        while(cursor.moveToNext()){
            PersonClient.Builder builder = PersonClient.newBuilder();
            builder.setClientId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_ID)))
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
                    .setClientisDLC(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_DCL)))
                    .setClientRegDate(
                            new SimpleDateFormat("dd:MM:yyyy")
                                    .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_REGISTRATION_DATE))));

            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_STARTED)) != null){
                builder.setDateSupportStarted(
                        new SimpleDateFormat("dd:MM:yyyy")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_STARTED))));
            }
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED)) != null){
                builder.setDateSupportFinished(
                        new SimpleDateFormat("dd:MM:yyyy")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED))));
            }
            personClients.add((PersonClient) builder.build());
        }
        cursor.close();
        return personClients;
    }

    @Override
    public long updatePersonClientData(PersonClient client) {
        String strFilter = SQLiteHelper.PERSONS_ID + " = " + String.valueOf(client.getClientId());
        Log.i(TAG, "strFilter: " + strFilter);
        String table = SQLiteHelper.PERSONS_TABLE_TITLE;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.PERSONS_SECOND_NAME, client.getClientSecondName());
        contentValues.put(SQLiteHelper.PERSONS_FIRST_NAME, client.getClientFirstName());
        contentValues.put(SQLiteHelper.PERSONS_PATRONYMIC, client.getClientPatronymic());
        contentValues.put(SQLiteHelper.PERSONS_ADDRESS, client.getClientAddress());
        contentValues.put(SQLiteHelper.PERSONS_CATEGORY, client.getClientCategory());
        contentValues.put(SQLiteHelper.PERSONS_PROBLEM, client.getClientProblem());
        contentValues.put(SQLiteHelper.PERSONS_IS_DCL, client.getClientisDLC());
        contentValues.put(SQLiteHelper.PERSONS_IS_UNDER_SUPPORT, client.getClientIsUnderSupport());
        if(client.getDateSupportStarted() != null){
            contentValues.put(SQLiteHelper.PERSONS_DATE_SUPPORT_STARTED,
                    new SimpleDateFormat("dd:MM:yyyy").format(client.getDateSupportStarted()));
        }
        contentValues.put(SQLiteHelper.PERSONS_IS_SUPPORT_FINISHED, client.getIsSupportFinished());
        contentValues.put(SQLiteHelper.PERSONS_SUPPORT_RESULT, client.getSupportResult());
        if(client.getDateSupportFinished() != null){
            contentValues.put(SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED,
                    new SimpleDateFormat("dd:MM:yyyy").format(client.getDateSupportFinished()));
        }
        contentValues.put(SQLiteHelper.PERSONS_PHOTO_PATH, client.getPhoto().getFileName());
        return mSQLiteHelper.getWritableDatabase().update(table, contentValues, strFilter, null);
    }

    @Override
    public void deletePersonClientId(long personClientId) {
        mSQLiteHelper.getWritableDatabase().delete(SQLiteHelper.PERSONS_TABLE_TITLE, SQLiteHelper.PERSONS_ID + " = " + personClientId, null);
        mSQLiteHelper.getWritableDatabase().delete(SQLiteHelper.PERSONS_VISITS_TABLE_TITLE, SQLiteHelper.PERSONS_VISITS_PERSON_ID + " = " + personClientId, null);
        mSQLiteHelper.getWritableDatabase().delete(SQLiteHelper.PERSONS_SERVICES_TABLE_TITLE, SQLiteHelper.PERSONS_SERVICES_PERSON_ID + " = " + personClientId, null);
        mSQLiteHelper.getWritableDatabase().delete(SQLiteHelper.PERSONS_MARKERS_TABLE_TITLE, SQLiteHelper.PERSONS_MARKERS_CLIENT_ID + " = " + personClientId, null);
    }

    @Override
    public ArrayList<PersonClient> getPersonsSupportStoppedPositive() throws ParseException {
        ArrayList<PersonClient> personClients = new ArrayList<PersonClient>();

        Cursor cursor = mSQLiteHelper.getReadableDatabase().query(SQLiteHelper.PERSONS_TABLE_TITLE,
                null, SQLiteHelper.PERSONS_SUPPORT_RESULT + " = ? AND + " + SQLiteHelper.PERSONS_IS_SUPPORT_FINISHED + " = ?", new String[]{"positive", "true"}, null, null, null);

        while(cursor.moveToNext()){
            PersonClient.Builder builder = PersonClient.newBuilder();
            builder.setClientId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_ID)))
                    .setClientSecondName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_SECOND_NAME)))
                    .setClientFirstName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_FIRST_NAME)))
                    .setClientPatronymic(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_PATRONYMIC)))
                    .setClientCategory(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_CATEGORY)))
                    .setClientAddress(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_ADDRESS)))
                    .setClientProblem(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_PROBLEM)))
                    .setClientisDLC(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_DCL)))
                    .setClientIsUnderSupport(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_UNDER_SUPPORT)))
                    .setClientIsSupportFinished(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_SUPPORT_FINISHED)))
                    .setClientSupportResult(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_SUPPORT_RESULT)))
                    .setClientPhoto(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_PHOTO_PATH)))
                    .setClientRegDate(
                            new SimpleDateFormat("dd:MM:yyyy")
                                    .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_REGISTRATION_DATE))));
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_STARTED)) != null){
                builder.setDateSupportStarted(
                        new SimpleDateFormat("dd:MM:yyyy")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_STARTED))));
            }
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED)) != null){
                builder.setDateSupportFinished(
                        new SimpleDateFormat("dd:MM:yyyy")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED))));
            }
            personClients.add((PersonClient) builder.build());
        }
        cursor.close();
        return personClients;
    }

    @Override
    public ArrayList<PersonClient> getPersonsSupportStoppedNegative() throws ParseException {
        ArrayList<PersonClient> personClients = new ArrayList<PersonClient>();

        Cursor cursor = mSQLiteHelper.getReadableDatabase().query(SQLiteHelper.PERSONS_TABLE_TITLE,
                null, SQLiteHelper.PERSONS_SUPPORT_RESULT + " = ? AND + " + SQLiteHelper.PERSONS_IS_SUPPORT_FINISHED + " = ?", new String[]{"negative", "true"}, null, null, null);

        while(cursor.moveToNext()){
            PersonClient.Builder builder = PersonClient.newBuilder();
            builder.setClientId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_ID)))
                    .setClientSecondName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_SECOND_NAME)))
                    .setClientFirstName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_FIRST_NAME)))
                    .setClientPatronymic(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_PATRONYMIC)))
                    .setClientCategory(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_CATEGORY)))
                    .setClientAddress(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_ADDRESS)))
                    .setClientProblem(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_PROBLEM)))
                    .setClientisDLC(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_DCL)))
                    .setClientIsUnderSupport(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_UNDER_SUPPORT)))
                    .setClientIsSupportFinished(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_SUPPORT_FINISHED)))
                    .setClientSupportResult(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_SUPPORT_RESULT)))
                    .setClientPhoto(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_PHOTO_PATH)))
                    .setClientRegDate(
                            new SimpleDateFormat("dd:MM:yyyy")
                                    .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_REGISTRATION_DATE))));
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_STARTED)) != null){
                builder.setDateSupportStarted(
                        new SimpleDateFormat("dd:MM:yyyy")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_STARTED))));
            }
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED)) != null){
                builder.setDateSupportFinished(
                        new SimpleDateFormat("dd:MM:yyyy")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED))));
            }
            personClients.add((PersonClient) builder.build());
        }
        cursor.close();
        return personClients;
    }

    @Override
    public ArrayList<PersonClient> getPersonsSupportContinues() throws ParseException {
        ArrayList<PersonClient> personClients = new ArrayList<PersonClient>();

        Cursor cursor = mSQLiteHelper.getReadableDatabase().query(SQLiteHelper.PERSONS_TABLE_TITLE,
                null, SQLiteHelper.PERSONS_IS_UNDER_SUPPORT + " = ? AND + " + SQLiteHelper.PERSONS_IS_SUPPORT_FINISHED + " = ?", new String[]{"true", "false"}, null, null, null);

        while(cursor.moveToNext()){
            PersonClient.Builder builder = PersonClient.newBuilder();
            builder.setClientId(cursor.getLong(cursor.getColumnIndex(SQLiteHelper.PERSONS_ID)))
                    .setClientSecondName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_SECOND_NAME)))
                    .setClientFirstName(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_FIRST_NAME)))
                    .setClientPatronymic(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_PATRONYMIC)))
                    .setClientCategory(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_CATEGORY)))
                    .setClientAddress(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_ADDRESS)))
                    .setClientProblem(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_PROBLEM)))
                    .setClientisDLC(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_DCL)))
                    .setClientIsUnderSupport(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_UNDER_SUPPORT)))
                    .setClientIsSupportFinished(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_IS_SUPPORT_FINISHED)))
                    .setClientSupportResult(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_SUPPORT_RESULT)))
                    .setClientPhoto(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_PHOTO_PATH)))
                    .setClientRegDate(
                            new SimpleDateFormat("dd:MM:yyyy")
                                    .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_REGISTRATION_DATE))));
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_STARTED)) != null){
                builder.setDateSupportStarted(
                        new SimpleDateFormat("dd:MM:yyyy")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_STARTED))));
            }
            if(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED)) != null){
                builder.setDateSupportFinished(
                        new SimpleDateFormat("dd:MM:yyyy")
                                .parse(cursor.getString(cursor.getColumnIndex(SQLiteHelper.PERSONS_DATE_SUPPORT_FINISHED))));
            }
            personClients.add((PersonClient) builder.build());
        }
        cursor.close();
        return personClients;
    }
}
