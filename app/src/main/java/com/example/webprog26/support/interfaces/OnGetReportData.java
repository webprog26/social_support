package com.example.webprog26.support.interfaces;

/**
 * Created by webprog26 on 07.04.2016.
 */
public interface OnGetReportData {

    int getFamiliesClientsTotalNum();
    int getPersonsClientsTotalNum();

    int getFamiliesClientsAdultsNum();

    int getFamiliesClientsKidsNum();

    int getFamiliesServicesTotalNum();
    int getPersonsServicesTotalNum();

    int getFamiliesFirstVisitsTotalNum();
    int getPersonsFirstVisitsTotalNum();

    int getFamiliesVisitsTotalNum();
    int getPersonsVisitsTotalNum();

    int getFamiliesClientsInDLCTotalNum();
    int getPersonsClientsInDLCTotalNum();

    int getFamiliesAdultsInDLCNum();

    int getFamiliesKidsInDLCNum();

    int getFamiliesInDLCServicesTotalNum();
    int getPersonsInDLCServicesTotalNum();

    int getFamiliesUnderSupportTotalNum();
    int getPersonsUnderSupportTotalNum();

    int getFamiliesUnderSupportServicesTotalNum();
    int getPersonsUnderSupportServicesTotalNum();

    int getFamiliesSupportStoppedWithPositiveResultTotalNum();
    int getPersonsStoppedWithPositiveResultTotalNum();

    int getFamiliesSupportStoppedWithNegativeResultTotalNum();
    int getPersonsSupportStoppedWithNegativeResultTotalNum();
}
