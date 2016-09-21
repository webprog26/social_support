package com.example.webprog26.support.sqlite;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by webprog26 on 06.03.2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteHelper";

    //Data to create DB
    private static final String DB_NAME = "clients_db";
    private static final int DB_VERSION = 1;

    //Tables titles
    public static final String FAMILIES_TABLE_TITLE = "families_table";
    public static final String PERSONS_TABLE_TITLE = "persons_table";

    public static final String FAMILIES_VISITS_TABLE_TITLE = "families_visits";
    public static final String PERSONS_VISITS_TABLE_TITLE = "persons_visits";

    public static final String FAMILIES_SERVICES_TABLE_TITLE = "families_services_table";
    public static final String PERSONS_SERVICES_TABLE_TITLE = "persons_services_table";

    public static final String FAMILIES_MARKERS_TABLE_TITLE = "families_markers_table";
    public static final String PERSONS_MARKERS_TABLE_TITLE = "persons_markers_table";

    public static final String FAMILIES_FIRST_VISITS_TABLE_TITLE = "families_first_visits_table";
    public static final String PERSONS_FIRST_VISITS_TABLE_TITLE = "persons_first_visits_table";

    public static final String FAMILIES_MARKERS_ID = "_id";
    public static final String FAMILIES_MARKERS_CLIENT_ID = "families_markers_client_id";
    public static final String FAMILIES_MARKERS_TITLE = "families_markers_title";
    public static final String FAMILIES_MARKERS_SNIPPET = "families_markers_snippet";
    public static final String FAMILIES_MARKERS_CATEGORY = "families_markers_category";
    public static final String FAMILIES_MARKERS_LATITUDE = "families_markers_latitude";
    public static final String FAMILIES_MARKERS_LONGITUDE = "families_markers_longitude";

    public static final String PERSONS_MARKERS_ID = "_id";
    public static final String PERSONS_MARKERS_CLIENT_ID = "persons_markers_client_id";
    public static final String PERSONS_MARKERS_TITLE = "persons_markers_title";
    public static final String PERSONS_MARKERS_SNIPPET = "persons_markers_snippet";
    public static final String PERSONS_MARKERS_CATEGORY = "persons_markers_category";
    public static final String PERSONS_MARKERS_LATITUDE = "persons_markers_latitude";
    public static final String PERSONS_MARKERS_LONGITUDE = "persons_markers_longitude";


    //Families visits table columns
    public static final String FAMILIES_VISITS_ID = "_id";
    public static final String FAMILIES_VISITS_DATE = "families_visits_date";
    public static final String FAMILIES_VISITS_FAMILY_ID = "families_visits_family_id";

    //Persons visits table columns
    public static final String PERSONS_VISITS_ID = "_id";
    public static final String PERSONS_VISITS_DATE = "persons_visits_date";
    public static final String PERSONS_VISITS_PERSON_ID = "persons_visits_person_id";

    //Families first visits table columns
    public static final String FAMILIES_FIRST_VISITS_ID = "_id";
    public static final String FAMILIES_FIRST_VISITS_DATE = "families_visits_date";
    public static final String FAMILIES_FIRST_VISITS_FAMILY_ID = "families_visits_family_id";

    //Persons first visits table columns
    public static final String PERSONS_FIRST_VISITS_ID = "_id";
    public static final String PERSONS_FIRST_VISITS_DATE = "persons_visits_date";
    public static final String PERSONS_FIRST_VISITS_PERSON_ID = "persons_visits_person_id";

    //Families services table_columns
    public static final String FAMILIES_SERVICES_ID = "_id";
    public static final String FAMILIES_SERVICES_SERVICE_TYPE = "families_services_service_type";
    public static final String FAMILIES_SERVICES_DURING_VISIT = "families_services_during_visit";
    public static final String FAMILIES_SERVICES_AMOUNT = "families_services_amount";
    public static final String PERSONS_SERVICES_DATE = "persons_services_date";
    public static final String FAMILIES_SERVICES_FAMILY_ID = "families_services_family_id";

    //Persons services table columns
    public static final String PERSONS_SERVICES_ID = "_id";
    public static final String PERSONS_SERVICES_SERVICE_TYPE = "persons_services_service_type";
    public static final String PERSONS_SERVICES_DURING_VISIT = "persons_services_during_visit";
    public static final String PERSONS_SERVICES_AMOUNT = "persons_services_amount";
    public static final String FAMILIES_SERVICES_DATE = "families_services_date";
    public static final String PERSONS_SERVICES_PERSON_ID = "persons_services_family_id";


    //Families table columns
    public static final String FAMILIES_ID = "_id";
    public static final String FAMILIES_CATEGORY = "families_category";
    public static final String FAMILIES_FIRST_NAME = "families_first_name";
    public static final String FAMILIES_PATRONYMIC = "families_patronymic";
    public static final String FAMILIES_SECOND_NAME = "families_second_name";
    public static final String FAMILIES_ADDRESS = "families_address";
    public static final String FAMILIES_ADULTS_AMOUNT = "families_adults";
    public static final String FAMILIES_KIDS_AMOUNT = "families_kids_amount";
    public static final String FAMILIES_PROBLEM = "families_problem";
    public static final String FAMILIES_IS_DCL = "families_is_dcl";
    public static final String FAMILIES_IS_UNDER_SUPPORT = "families_is_under_support";
    public static final String FAMILIES_DATE_SUPPORT_STARTED = "families_date_support_started";
    public static final String FAMILIES_IS_SUPPORT_FINISHED = "families_is_support_finished";
    public static final String FAMILIES_DATE_SUPPORT_FINISHED = "families_date_support_finished";
    public static final String FAMILIES_SUPPORT_RESULT = "families_support_result";
    public static final String FAMILIES_REGISTRATION_DATE = "families_registration_date";
    public static final String FAMILIES_PHOTO_PATH = "families_photo_path";

    //Persons table columns
    public static final String PERSONS_ID = "_id";
    public static final String PERSONS_CATEGORY = "persons_category";
    public static final String PERSONS_FIRST_NAME = "persons_first_name";
    public static final String PERSONS_PATRONYMIC = "persons_patronymic";
    public static final String PERSONS_SECOND_NAME = "persons_second_name";
    public static final String PERSONS_ADDRESS = "persons_address";
    public static final String PERSONS_PROBLEM = "persons_problem";
    public static final String PERSONS_IS_DCL = "persons_is_dcl";
    public static final String PERSONS_IS_UNDER_SUPPORT = "persons_is_under_support";
    public static final String PERSONS_DATE_SUPPORT_STARTED = "persons_date_support_started";
    public static final String PERSONS_IS_SUPPORT_FINISHED = "persons_is_support_finished";
    public static final String PERSONS_DATE_SUPPORT_FINISHED = "persons_date_support_finished";
    public static final String PERSONS_SUPPORT_RESULT = "persons_support_result";
    public static final String PERSONS_REGISTRATION_DATE = "persons_registration_date";
    public static final String PERSONS_PHOTO_PATH = "persons_photo_path";

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FAMILIES_TABLE_TITLE + "("
        + FAMILIES_ID + " integer primary key autoincrement, "
                + FAMILIES_CATEGORY + " varchar(100), "
                + FAMILIES_FIRST_NAME + " varchar(100), "
                + FAMILIES_PATRONYMIC + " varchar(100), "
                + FAMILIES_SECOND_NAME + " varchar(100) COLLATE NOCASE, "
                + FAMILIES_ADDRESS + " varchar(100), "
                + FAMILIES_ADULTS_AMOUNT + " integer, "
                + FAMILIES_KIDS_AMOUNT + " integer, "
                + FAMILIES_PROBLEM + " text, "
                + FAMILIES_IS_DCL + " varchar(10), "
                + FAMILIES_IS_UNDER_SUPPORT + " varchar(10), "
                + FAMILIES_DATE_SUPPORT_STARTED + " varchar(10), "
                + FAMILIES_IS_SUPPORT_FINISHED + " varchar(10), "
                + FAMILIES_DATE_SUPPORT_FINISHED + " varchar(10), "
                + FAMILIES_SUPPORT_RESULT + " varchar(10), "
                + FAMILIES_PHOTO_PATH + " varchar(100), "
                + FAMILIES_REGISTRATION_DATE + " varchar(10))");

        db.execSQL("create table " + PERSONS_TABLE_TITLE + "("
                + PERSONS_ID + " integer primary key autoincrement, "
                + PERSONS_CATEGORY + " varchar(100), "
                + PERSONS_FIRST_NAME + " varchar(100), "
                + PERSONS_PATRONYMIC + " varchar(100), "
                + PERSONS_SECOND_NAME + " varchar(100) COLLATE NOCASE, "
                + PERSONS_ADDRESS + " varchar(100), "
                + PERSONS_PROBLEM + " text, "
                + PERSONS_IS_DCL + " varchar(10), "
                + PERSONS_IS_UNDER_SUPPORT + " varchar(10), "
                + PERSONS_DATE_SUPPORT_STARTED + " varchar(10), "
                + PERSONS_IS_SUPPORT_FINISHED + " varchar(10), "
                + PERSONS_DATE_SUPPORT_FINISHED + " varchar(10), "
                + PERSONS_SUPPORT_RESULT + " varchar(10), "
                + PERSONS_PHOTO_PATH + " varchar(100), "
                + PERSONS_REGISTRATION_DATE + " varchar(10))");

        db.execSQL("create table " + FAMILIES_VISITS_TABLE_TITLE + "("
                + FAMILIES_VISITS_ID + " integer primary key autoincrement, "
                + FAMILIES_VISITS_DATE + " varchar(10), "
                + FAMILIES_VISITS_FAMILY_ID + " integer)");

        db.execSQL("create table " + PERSONS_VISITS_TABLE_TITLE + "("
                + PERSONS_FIRST_VISITS_ID + " integer primary key autoincrement, "
                + PERSONS_FIRST_VISITS_DATE + " varchar(10), "
                + PERSONS_FIRST_VISITS_PERSON_ID + " integer)");

        db.execSQL("create table " + FAMILIES_FIRST_VISITS_TABLE_TITLE + "("
                + FAMILIES_FIRST_VISITS_ID + " integer primary key autoincrement, "
                + FAMILIES_FIRST_VISITS_DATE + " varchar(10), "
                + FAMILIES_FIRST_VISITS_FAMILY_ID + " integer)");

        db.execSQL("create table " + PERSONS_FIRST_VISITS_TABLE_TITLE + "("
                + PERSONS_FIRST_VISITS_ID + " integer primary key autoincrement, "
                + PERSONS_FIRST_VISITS_DATE + " varchar(10), "
                + PERSONS_FIRST_VISITS_PERSON_ID + " integer)");

        db.execSQL("create table " + FAMILIES_SERVICES_TABLE_TITLE + "("
                + FAMILIES_SERVICES_ID + " integer primary key autoincrement, "
                + FAMILIES_SERVICES_SERVICE_TYPE + " varchar(20), "
                + FAMILIES_SERVICES_DURING_VISIT + " varchar(10), "
                + FAMILIES_SERVICES_AMOUNT + " integer, "
                + FAMILIES_SERVICES_DATE + " date, "
                + FAMILIES_SERVICES_FAMILY_ID + " integer)");

        db.execSQL("create table " + PERSONS_SERVICES_TABLE_TITLE + "("
                + PERSONS_SERVICES_ID + " integer primary key autoincrement, "
                + PERSONS_SERVICES_SERVICE_TYPE + " varchar(100), "
                + PERSONS_SERVICES_DURING_VISIT + " varchar(10), "
                + PERSONS_SERVICES_AMOUNT + " integer, "
                + PERSONS_SERVICES_DATE + " varchar(10), "
                + PERSONS_SERVICES_PERSON_ID + " integer)");

        db.execSQL("create table " + FAMILIES_MARKERS_TABLE_TITLE + "("
                + FAMILIES_MARKERS_ID + " integer primary key autoincrement, "
                + FAMILIES_MARKERS_CLIENT_ID + " integer, "
                + FAMILIES_MARKERS_TITLE + " varchar(100), "
                + FAMILIES_MARKERS_SNIPPET + " text, "
                + FAMILIES_MARKERS_CATEGORY + " integer, "
                + FAMILIES_MARKERS_LATITUDE + " real, "
                + FAMILIES_MARKERS_LONGITUDE + " real)");

        db.execSQL("create table " + PERSONS_MARKERS_TABLE_TITLE + "("
                + PERSONS_MARKERS_ID + " integer primary key autoincrement, "
                + PERSONS_MARKERS_CLIENT_ID + " integer, "
                + PERSONS_MARKERS_TITLE + " varchar(100), "
                + PERSONS_MARKERS_SNIPPET + " text, "
                + PERSONS_MARKERS_CATEGORY + " integer, "
                + PERSONS_MARKERS_LATITUDE + " real, "
                + PERSONS_MARKERS_LONGITUDE + " real)");

}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }
}
