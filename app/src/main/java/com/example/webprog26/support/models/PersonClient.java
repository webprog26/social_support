package com.example.webprog26.support.models;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by webprog26 on 06.03.2016.
 */
public class PersonClient extends Client {

    private long personClientId;
    private String personClientSecondName;
    private String personClientFirstName;
    private String personClientPatronymic;
    private String personClientCategory;
    private String personClientAddress;
    private String personClientProblem;
    private Date personClientRegDate;
    private String personClientisDLC;
    private String personClientIsUnderSupport;
    private Date personDateSupportStarted;
    private String personIsSupportFinished;
    private Date personDateSupportFinished;
    private String personSupportResult;
    private Photo mPhoto;

    @Override
    public long getClientId() {
        return personClientId;
    }

    @Override
    public String getClientSecondName() {
        return personClientSecondName;
    }

    @Override
    public String getClientFirstName() {
        return personClientFirstName;
    }

    @Override
    public String getClientPatronymic() {
        return personClientPatronymic;
    }

    @Override
    public String getClientCategory() {
        return personClientCategory;
    }

    @Override
    public String getClientAddress() {
        return personClientAddress;
    }

    @Override
    public String getClientProblem() {
        return personClientProblem;
    }

    @Override
    public Date getClientRegDate() {
        return personClientRegDate;
    }

    @Override
    public String getClientisDLC() {
        return personClientisDLC;
    }

    @Override
    public String getClientIsUnderSupport() {
        return personClientIsUnderSupport;
    }

    @Override
    public Date getDateSupportStarted() {
        return personDateSupportStarted;
    }

    @Override
    public String getIsSupportFinished() {
        return personIsSupportFinished;
    }

    @Override
    public Date getDateSupportFinished() {
        return personDateSupportFinished;
    }

    @Override
    public String getSupportResult() {
        return personSupportResult;
    }

    @Override
    public Photo getPhoto() {
        return mPhoto;
    }

    public static PersonClient.Builder newBuilder(){
        return new PersonClient(). new Builder();
    }

    public class Builder extends Client.Builder{
        @Override
        public Client.Builder setClientId(long clientId) {
            PersonClient.this.personClientId = clientId;
            return this;
        }

        @Override
        public Client.Builder setClientFirstName(String clientFirstName) {
            PersonClient.this.personClientFirstName = clientFirstName;
            return this;
        }

        @Override
        public Client.Builder setClientPatronymic(String clientPatronymic) {
            PersonClient.this.personClientPatronymic = clientPatronymic;
            return this;
        }

        @Override
        public Client.Builder setClientSecondName(String clientSecondName) {
            PersonClient.this.personClientSecondName = clientSecondName;
            return this;
        }

        @Override
        public Client.Builder setClientCategory(String clientCategory) {
            PersonClient.this.personClientCategory = clientCategory;
            return this;
        }

        @Override
        public Client.Builder setClientAddress(String clientAddress) {
            PersonClient.this.personClientAddress = clientAddress;
            return this;
        }

        @Override
        public Client.Builder setClientProblem(String clientProblem) {
            PersonClient.this.personClientProblem = clientProblem;
            return this;
        }

        @Override
        public Client.Builder setClientRegDate(Date clientRegDate) {
            PersonClient.this.personClientRegDate = clientRegDate;
            return this;
        }

        @Override
        public Client.Builder setClientisDLC(String clientisDLC) {
            PersonClient.this.personClientisDLC = clientisDLC;
            return this;
        }

        @Override
        public Client.Builder setClientIsUnderSupport(String clientIsUnderSupport) {
            PersonClient.this.personClientIsUnderSupport = clientIsUnderSupport;
            return this;
        }

        @Override
        public Client.Builder setDateSupportStarted(Date dateSupportStarted) {
            PersonClient.this.personDateSupportStarted = dateSupportStarted;
            return this;
        }

        @Override
        public Client.Builder setClientIsSupportFinished(String isSupportFinished) {
            PersonClient.this.personIsSupportFinished = isSupportFinished;
            return this;
        }

        @Override
        public Client.Builder setDateSupportFinished(Date dateSupportFinished) {
            PersonClient.this.personDateSupportFinished = dateSupportFinished;
            return this;
        }

        @Override
        public Client.Builder setClientSupportResult(String clientSupportResult) {
            PersonClient.this.personSupportResult = clientSupportResult;
            return this;
        }

        @Override
        public Client.Builder setClientPhoto(String fileUri) {
            PersonClient.this.mPhoto = new Photo(fileUri);
            return this;
        }

        @Override
        public Client build() {
            return PersonClient.this;
        }
    }
}
