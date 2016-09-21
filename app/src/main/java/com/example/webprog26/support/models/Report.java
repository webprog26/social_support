package com.example.webprog26.support.models;

/**
 * Created by webprog26 on 07.04.2016.
 */
public class Report {

    private int clientsNum;
    private int adultClientsNum;
    private int kidsClientsNum;
    private int firstTimeVisits;
    private int visits;
    private int servicesNum;
    private int DLCclientsNum;
    private int inDLCadultClients;
    private int inDLCkidsClients;
    private int inDLCServicesNum;
    private int underSupportClients;
    private int underSupportClientsServicesNum;
    private int supportStoppedWithPositiveResult;
    private int supportStoppedWithNegativeResult;

    public int getClientsNum() {
        return clientsNum;
    }

    public int getAdultClientsNum() {
        return adultClientsNum;
    }

    public int getKidsClientsNum() {
        return kidsClientsNum;
    }

    public int getFirstTimeVisits() {
        return firstTimeVisits;
    }

    public int getVisits() {
        return visits;
    }

    public int getServicesNum() {
        return servicesNum;
    }

    public int getDLCclientsNum() {
        return DLCclientsNum;
    }

    public int getInDLCadultClients() {
        return inDLCadultClients;
    }

    public int getInDLCkidsClients() {
        return inDLCkidsClients;
    }

    public int getInDLCServicesNum() {
        return inDLCServicesNum;
    }

    public int getUnderSupportClients() {
        return underSupportClients;
    }

    public int getUnderSupportClientsServicesNum() {
        return underSupportClientsServicesNum;
    }

    public int getSupportStoppedWithPositiveResult() {
        return supportStoppedWithPositiveResult;
    }

    public int getSupportStoppedWithNegativeResult() {
        return supportStoppedWithNegativeResult;
    }

    public static Builder newBuilder(){
        return new Report().new Builder();
    }

    public class Builder{

        public Builder setClientsNum(int clientsNum){
            Report.this.clientsNum = clientsNum;
            return this;
        }

        public Builder setAdultClientNum(int adultClientNum){
            Report.this.adultClientsNum = adultClientNum;
            return this;
        }

        public Builder setKidsClientNum(int kidsClientNum){
            Report.this.kidsClientsNum = kidsClientNum;
            return this;
        }

        public Builder setFirstTimeVisitsNum(int firstTimeVisitsNum){
            Report.this.firstTimeVisits = firstTimeVisitsNum;
            return this;
        }
        public Builder setVisitsNum(int visitsNum){
            Report.this.visits = visitsNum;
            return this;
        }

        public Builder setServicesNum(int servicesNum){
            Report.this.servicesNum = servicesNum;
            return this;
        }

        public Builder setInDLCClientsNum(int inDLCClientsNum){
            Report.this.DLCclientsNum = inDLCClientsNum;
            return this;
        }

        public Builder setInDLCAdultClientsNum(int inDLCAdultClientsNum){
            Report.this.inDLCadultClients = inDLCAdultClientsNum;
            return this;
        }

        public Builder setInDLCKidsClientsNum(int inDLCKidsClientsNum){
            Report.this.inDLCkidsClients = inDLCKidsClientsNum;
            return this;
        }

        public Builder setInDLCServicesNum(int inDLCServicesNum){
            Report.this.inDLCServicesNum = inDLCServicesNum;
            return this;
        }

        public Builder setUnderSupportClientsNum(int underSupportClientsNum){
            Report.this.underSupportClients = underSupportClientsNum;
            return this;
        }

        public Builder setUnderSupportServicesNum(int underSupportServicesNum){
            Report.this.underSupportClientsServicesNum = underSupportServicesNum;
            return this;
        }

        public Builder setSupportStoppedWithPositiveResultNum(int supportStoppedWithPositiveResultNum){
            Report.this.supportStoppedWithPositiveResult = supportStoppedWithPositiveResultNum;
            return this;
        }

        public Builder setSupportStoppedWithNegativeResultsNum(int supportStoppedWithNegativeResultsNum){
            Report.this.supportStoppedWithNegativeResult = supportStoppedWithNegativeResultsNum;
            return this;
        }

        public Report build(){
            return Report.this;
        }

    }
}
