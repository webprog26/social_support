package com.example.webprog26.support.models;

import java.util.Date;

/**
 * Created by webprog26 on 31.03.2016.
 */
public class Client {

    private long mClientId;
    private String mClientSecondName;
    private String mClientFirstName;
    private String mClientPatronymic;
    private String mClientCategory;
    private String mClientAddress;
    private String mClientProblem;
    private int mClientAdultsAmount;
    private int mClientKidsAmount;
    private Date mClientRegDate;
    private String mClientisDLC;
    private String mClientIsUnderSupport;
    private Date mDateSupportStarted;
    private String mIsSupportFinished;
    private Date mDateSupportFinished;
    private String mSupportResult;
    private Photo mPhoto;

    public long getClientId() {
        return mClientId;
    }

    public String getClientSecondName() {
        return mClientSecondName;
    }

    public String getClientFirstName() {
        return mClientFirstName;
    }

    public String getClientPatronymic() {
        return mClientPatronymic;
    }

    public String getClientCategory() {
        return mClientCategory;
    }

    public String getClientAddress() {
        return mClientAddress;
    }

    public String getClientProblem() {
        return mClientProblem;
    }

    public int getClientAdultsAmount() {
        return mClientAdultsAmount;
    }

    public int getClientKidsAmount() {
        return mClientKidsAmount;
    }

    public Date getClientRegDate() {
        return mClientRegDate;
    }

    public String getClientisDLC() {
        return mClientisDLC;
    }

    public String getClientIsUnderSupport() {
        return mClientIsUnderSupport;
    }

    public Date getDateSupportStarted() {
        return mDateSupportStarted;
    }

    public String getIsSupportFinished() {
        return mIsSupportFinished;
    }

    public Date getDateSupportFinished() {
        return mDateSupportFinished;
    }

    public String getSupportResult() {
        return mSupportResult;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public static Builder newBuilder(){
        return new Client().new Builder();
    }

    public class Builder{

        public Builder setClientId(long clientId){
            Client.this.mClientId = clientId;
            return this;
        }

        public Builder setClientFirstName(String clientFirstName){
            Client.this.mClientFirstName = clientFirstName;
            return this;
        }

        public Builder setClientPatronymic(String clientPatronymic){
            Client.this.mClientPatronymic = clientPatronymic;
            return this;
        }

        public Builder setClientSecondName(String clientSecondName){
            Client.this.mClientSecondName = clientSecondName;
            return this;
        }

        public Builder setClientCategory(String clientCategory){
            Client.this.mClientCategory = clientCategory;
            return this;
        }

        public Builder setClientAddress(String clientAddress){
            Client.this.mClientAddress = clientAddress;
            return this;
        }

        public Builder setClientProblem(String clientProblem){
            Client.this.mClientProblem = clientProblem;
            return this;
        }

        public Builder setClientAdultsAmount(int clientAdultsAmount){
            Client.this.mClientAdultsAmount = clientAdultsAmount;
            return this;
        }

        public Builder setClientKidsAmount(int clientKidsAmount){
            Client.this.mClientKidsAmount = clientKidsAmount;
            return this;
        }

        public Builder setClientRegDate(Date clientRegDate){
            Client.this.mClientRegDate = clientRegDate;
            return this;
        }

        public Builder setClientisDLC(String clientisDLC){
            Client.this.mClientisDLC = clientisDLC;
            return this;
        }

        public Builder setClientIsUnderSupport(String clientIsUnderSupport){
            Client.this.mClientIsUnderSupport = clientIsUnderSupport;
            return this;
        }

        public Builder setDateSupportStarted(Date dateSupportStarted){
            Client.this.mDateSupportStarted = dateSupportStarted;
            return this;
        }

        public Builder setClientIsSupportFinished(String isSupportFinished){
            Client.this.mIsSupportFinished = isSupportFinished;
            return this;
        }

        public Builder setDateSupportFinished(Date dateSupportFinished){
            Client.this.mDateSupportFinished = dateSupportFinished;
            return this;
        }

        public Builder setClientSupportResult(String clientSupportResult){
            Client.this.mSupportResult = clientSupportResult;
            return this;
        }

        public Builder setClientPhoto(String fileUri){
            Photo photo = new Photo(fileUri);
            Client.this.mPhoto = photo;
            return this;
        }

        public Client build(){
            return Client.this;
        }
    }
}
