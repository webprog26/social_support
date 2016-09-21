package com.example.webprog26.support.interfaces;

import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.PersonClient;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by webprog26 on 31.03.2016.
 */
public interface OnClientDataOperator {
    long insertFamilyClientDataToDb(FamilyClient client);
    long insertPersonDataToDb(PersonClient client);

    ArrayList<FamilyClient> getFamilyClientData() throws ParseException;
    ArrayList<PersonClient> getPersonClientData() throws ParseException;

    long updateFamilyClientData(FamilyClient client);
    long updatePersonClientData(PersonClient client);

    void deleteFamilyClient(long familyClientId);
    void deletePersonClientId(long personClientId);

    ArrayList<FamilyClient> getFamiliesSupportContinues() throws ParseException;
    ArrayList<PersonClient> getPersonsSupportContinues() throws ParseException;

    ArrayList<FamilyClient> getFamiliesSupportStoppedPositive() throws ParseException;
    ArrayList<PersonClient> getPersonsSupportStoppedPositive() throws ParseException;

    ArrayList<FamilyClient> getFamiliesSupportStoopedNegative() throws ParseException;
    ArrayList<PersonClient> getPersonsSupportStoppedNegative() throws ParseException;

}
