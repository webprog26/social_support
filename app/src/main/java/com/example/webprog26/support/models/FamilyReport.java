package com.example.webprog26.support.models;

/**
 * Created by webprog26 on 07.04.2016.
 */
public class FamilyReport extends Report{

    private int familiesClientsNum;
    private int familiesAdultsNum;
    private int familiesKidsNum;
    private int familiesClientsServicesNum;
    private int familiesTotalVisitsNum;
    private int familiesFirstVisitsNum;
    private int familiesInDLCNum;
    private int familiesInDLCAdultsNum;
    private int familiesInDLCKidsNum;
    private int familiesInDLCServicesNum;
    private int familiesUnderSupportNum;
    private int familiesUnderSupportServicesNum;
    private int familiesSupportStoppedWithPositiveResult;
    private int familiesSupportStoppedWithNegativeResult;

    @Override
    public int getClientsNum() {
        return familiesClientsNum;
    }

    @Override
    public int getAdultClientsNum() {
        return familiesAdultsNum;
    }

    @Override
    public int getKidsClientsNum() {
        return familiesKidsNum;
    }

    @Override
    public int getFirstTimeVisits() {
        return familiesFirstVisitsNum;
    }

    @Override
    public int getVisits() {
        return familiesTotalVisitsNum;
    }

    @Override
    public int getServicesNum() {
        return familiesClientsServicesNum;
    }

    @Override
    public int getDLCclientsNum() {
        return familiesInDLCNum;
    }

    @Override
    public int getInDLCadultClients() {
        return familiesInDLCAdultsNum;
    }

    @Override
    public int getInDLCkidsClients() {
        return familiesInDLCKidsNum;
    }

    @Override
    public int getInDLCServicesNum() {
        return familiesInDLCServicesNum;
    }

    @Override
    public int getUnderSupportClients() {
        return familiesUnderSupportNum;
    }

    @Override
    public int getUnderSupportClientsServicesNum() {
        return familiesUnderSupportServicesNum;
    }

    @Override
    public int getSupportStoppedWithPositiveResult() {
        return familiesSupportStoppedWithPositiveResult;
    }

    @Override
    public int getSupportStoppedWithNegativeResult() {
        return familiesSupportStoppedWithNegativeResult;
    }

    public static FamilyReportBuilder newBuilder(){
        return new FamilyReport().new FamilyReportBuilder();
    }

    public class FamilyReportBuilder extends Report.Builder{
        @Override
        public Builder setClientsNum(int clientsNum) {
            FamilyReport.this.familiesClientsNum = clientsNum;
            return this;
        }

        @Override
        public Builder setAdultClientNum(int adultClientNum) {
            FamilyReport.this.familiesAdultsNum = adultClientNum;
            return this;
        }

        @Override
        public Builder setKidsClientNum(int kidsClientNum) {
            FamilyReport.this.familiesKidsNum = kidsClientNum;
            return this;
        }

        @Override
        public Builder setFirstTimeVisitsNum(int firstTimeVisitsNum) {
            FamilyReport.this.familiesFirstVisitsNum = firstTimeVisitsNum;
            return this;
        }

        @Override
        public Builder setVisitsNum(int visitsNum) {
            FamilyReport.this.familiesTotalVisitsNum = visitsNum;
            return this;
        }

        @Override
        public Builder setServicesNum(int servicesNum) {
            FamilyReport.this.familiesClientsServicesNum = servicesNum;
            return this;
        }

        @Override
        public Builder setInDLCClientsNum(int inDLCClientsNum) {
            FamilyReport.this.familiesInDLCNum = inDLCClientsNum;
            return this;
        }

        @Override
        public Builder setInDLCAdultClientsNum(int inDLCAdultClientsNum) {
            FamilyReport.this.familiesInDLCAdultsNum = inDLCAdultClientsNum;
            return this;
        }

        @Override
        public Builder setInDLCKidsClientsNum(int inDLCKidsClientsNum) {
            FamilyReport.this.familiesInDLCKidsNum = inDLCKidsClientsNum;
            return this;
        }

        @Override
        public Builder setInDLCServicesNum(int inDLCServicesNum) {
            FamilyReport.this.familiesInDLCServicesNum = inDLCServicesNum;
            return this;
        }

        @Override
        public Builder setUnderSupportClientsNum(int underSupportClientsNum) {
            FamilyReport.this.familiesUnderSupportNum = underSupportClientsNum;
            return this;
        }

        @Override
        public Builder setUnderSupportServicesNum(int underSupportServicesNum) {
            FamilyReport.this.familiesUnderSupportServicesNum = underSupportServicesNum;
            return this;
        }

        @Override
        public Builder setSupportStoppedWithPositiveResultNum(int supportStoppedWithPositiveResultNum) {
            FamilyReport.this.familiesSupportStoppedWithPositiveResult = supportStoppedWithPositiveResultNum;
            return this;
        }

        @Override
        public Builder setSupportStoppedWithNegativeResultsNum(int supportStoppedWithNegativeResultsNum) {
            FamilyReport.this.familiesSupportStoppedWithNegativeResult = supportStoppedWithNegativeResultsNum;
            return this;
        }

        @Override
        public Report build() {
            return FamilyReport.this;
        }
    }
}
