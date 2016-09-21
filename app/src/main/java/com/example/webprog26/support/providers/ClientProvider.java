package com.example.webprog26.support.providers;

import android.app.Activity;
import android.content.Context;

import com.example.webprog26.support.interfaces.OnClientDataOperator;
import com.example.webprog26.support.interfaces.OnGetReportData;
import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.PersonClient;
import com.example.webprog26.support.models.Visit;
import com.example.webprog26.support.sqlite.SQLiteHelper;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by webprog26 on 06.03.2016.
 */
public abstract class ClientProvider implements OnClientDataOperator, OnGetReportData{

    protected Activity mActivity;
    protected Context mContext;
    protected SQLiteHelper mSQLiteHelper;

    public ClientProvider(Activity activity){
        mActivity = activity;
        mContext = mActivity.getApplicationContext();
        mSQLiteHelper = new SQLiteHelper(mContext);
    }

    @Override
    public long insertFamilyClientDataToDb(FamilyClient client) {
        return 0;
    }

    @Override
    public long insertPersonDataToDb(PersonClient client) {
        return 0;
    }

    @Override
    public ArrayList<FamilyClient> getFamilyClientData() throws ParseException{
        return null;
    }

    @Override
    public ArrayList<PersonClient> getPersonClientData() throws ParseException{
        return null;
    }

    @Override
    public long updateFamilyClientData(FamilyClient client) {
        return 0;
    }

    @Override
    public long updatePersonClientData(PersonClient client) {
        return 0;
    }

    @Override
    public void deleteFamilyClient(long familyClientId) {}

    @Override
    public void deletePersonClientId(long personClientId) {}

    @Override
    public ArrayList<FamilyClient> getFamiliesSupportContinues() throws ParseException {
        return null;
    }

    @Override
    public ArrayList<PersonClient> getPersonsSupportContinues() throws ParseException {
        return null;
    }

    @Override
    public ArrayList<FamilyClient> getFamiliesSupportStoppedPositive() throws ParseException{
        return null;
    }

    @Override
    public ArrayList<PersonClient> getPersonsSupportStoppedPositive() throws ParseException {
        return null;
    }

    @Override
    public ArrayList<FamilyClient> getFamiliesSupportStoopedNegative() throws ParseException {
        return null;
    }

    @Override
    public ArrayList<PersonClient> getPersonsSupportStoppedNegative() throws ParseException {
        return null;
    }
    //Requests to form report

    @Override
    public int getFamiliesClientsTotalNum() {
        return 0;
    }

    @Override
    public int getPersonsClientsTotalNum() {
        return 0;
    }

    @Override
    public int getFamiliesClientsAdultsNum() {
        return 0;
    }

    @Override
    public int getFamiliesClientsKidsNum() {
        return 0;
    }

    @Override
    public int getFamiliesServicesTotalNum() {
        return 0;
    }

    @Override
    public int getPersonsServicesTotalNum() {
        return 0;
    }

    @Override
    public int getFamiliesClientsInDLCTotalNum() {
        return 0;
    }

    @Override
    public int getPersonsClientsInDLCTotalNum() {
        return 0;
    }

    @Override
    public int getFamiliesAdultsInDLCNum() {
        return 0;
    }

    @Override
    public int getFamiliesKidsInDLCNum() {
        return 0;
    }

    @Override
    public int getFamiliesInDLCServicesTotalNum() {
        return 0;
    }

    @Override
    public int getPersonsInDLCServicesTotalNum() {
        return 0;
    }

    @Override
    public int getFamiliesUnderSupportTotalNum() {
        return 0;
    }

    @Override
    public int getPersonsUnderSupportTotalNum() {
        return 0;
    }



    @Override
    public int getFamiliesUnderSupportServicesTotalNum() {
        return 0;
    }

    @Override
    public int getPersonsUnderSupportServicesTotalNum() {
        return 0;
    }

    @Override
    public int getFamiliesSupportStoppedWithPositiveResultTotalNum() {
        return 0;
    }

    @Override
    public int getPersonsStoppedWithPositiveResultTotalNum() {
        return 0;
    }

    @Override
    public int getFamiliesSupportStoppedWithNegativeResultTotalNum() {
        return 0;
    }

    @Override
    public int getPersonsSupportStoppedWithNegativeResultTotalNum() {
        return 0;
    }

    @Override
    public int getFamiliesVisitsTotalNum() {
        return 0;
    }

    @Override
    public int getPersonsVisitsTotalNum() {
        return 0;
    }

    @Override
    public int getFamiliesFirstVisitsTotalNum() {
        return 0;
    }

    @Override
    public int getPersonsFirstVisitsTotalNum() {
        return 0;
    }
}
