package com.example.webprog26.support.models;

/**
 * Created by webprog26 on 07.04.2016.
 */
public class PersonReport extends Report {

    private int personsClientsNum;
    private int personsClientsServicesNum;
    private int personsTotalVisitsNum;
    private int personsFirstVisitsNum;
    private int personsInDLCNum;
    private int personsInDLCServicesNum;
    private int personsUnderSupportNum;
    private int personsUnderSupportServicesNum;
    private int personsSupportStoppedWithPositiveResult;
    private int personsSupportStoppedWithNegativeResult;

    @Override
    public int getClientsNum() {
        return personsClientsNum;
    }

    @Override
    public int getFirstTimeVisits() {
        return personsFirstVisitsNum;
    }

    @Override
    public int getVisits() {
        return personsTotalVisitsNum;
    }

    @Override
    public int getServicesNum() {
        return personsClientsServicesNum;
    }

    @Override
    public int getDLCclientsNum() {
        return personsInDLCNum;
    }

    @Override
    public int getInDLCServicesNum() {
        return personsInDLCServicesNum;
    }

    @Override
    public int getUnderSupportClients() {
        return personsUnderSupportNum;
    }

    @Override
    public int getUnderSupportClientsServicesNum() {
        return personsUnderSupportServicesNum;
    }

    @Override
    public int getSupportStoppedWithPositiveResult() {
        return personsSupportStoppedWithPositiveResult;
    }

    @Override
    public int getSupportStoppedWithNegativeResult() {
        return personsSupportStoppedWithNegativeResult;
    }

    public static PersonReportBuilder newBuilder(){
        return new PersonReport().new PersonReportBuilder();
    }

    public class PersonReportBuilder extends Report.Builder{

        @Override
        public Builder setClientsNum(int clientsNum) {
            PersonReport.this.personsClientsNum = clientsNum;
            return this;
        }

        @Override
        public Builder setFirstTimeVisitsNum(int firstTimeVisitsNum) {
            PersonReport.this.personsFirstVisitsNum = firstTimeVisitsNum;
            return this;
        }

        @Override
        public Builder setVisitsNum(int visitsNum) {
            PersonReport.this.personsTotalVisitsNum = visitsNum;
            return this;
        }

        @Override
        public Builder setServicesNum(int servicesNum) {
            PersonReport.this.personsClientsServicesNum = servicesNum;
            return this;
        }

        @Override
        public Builder setInDLCClientsNum(int inDLCClientsNum) {
            PersonReport.this.personsInDLCNum = inDLCClientsNum;
            return this;
        }

        @Override
        public Builder setInDLCServicesNum(int inDLCServicesNum) {
            PersonReport.this.personsInDLCServicesNum = inDLCServicesNum;
            return this;
        }

        @Override
        public Builder setUnderSupportClientsNum(int underSupportClientsNum) {
            PersonReport.this.personsUnderSupportNum = underSupportClientsNum;
            return this;
        }

        @Override
        public Builder setUnderSupportServicesNum(int underSupportServicesNum) {
            PersonReport.this.personsUnderSupportServicesNum = underSupportServicesNum;
            return this;
        }

        @Override
        public Builder setSupportStoppedWithPositiveResultNum(int supportStoppedWithPositiveResultNum) {
            PersonReport.this.personsSupportStoppedWithPositiveResult = supportStoppedWithPositiveResultNum;
            return this;
        }

        @Override
        public Builder setSupportStoppedWithNegativeResultsNum(int supportStoppedWithNegativeResultsNum) {
            PersonReport.this.personsSupportStoppedWithNegativeResult = supportStoppedWithNegativeResultsNum;
            return this;
        }

        @Override
        public Report build() {
            return PersonReport.this;
        }
    }
}
