package com.example.webprog26.support.models;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.DateFormat;

import com.example.webprog26.support.utils.PictureUtils;

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
public class FamilyClient extends Client {

    private long familyClientId;
    private String familyClientSecondName;
    private String familyClientFirstName;
    private String familyClientPatronymic;
    private String familyClientCategory;
    private String familyClientAddress;
    private String familyClientProblem;
    private int familyClientAdultsAmount;
    private int familyClientKidsAmount;
    private Date familyClientRegDate;
    private String familyClientisDLC;
    private String familyClientIsUnderSupport;
    private Date familyDateSupportStarted;
    private String familyIsSupportFinished;
    private Date familyDateSupportFinished;
    private String familySupportResult;
    private Photo mPhoto;

    @Override
    public long getClientId() {
        return familyClientId;
    }

    @Override
    public String getClientSecondName() {
        return familyClientSecondName;
    }

    @Override
    public String getClientFirstName() {
        return familyClientFirstName;
    }

    @Override
    public String getClientPatronymic() {
        return familyClientPatronymic;
    }

    @Override
    public String getClientCategory() {
        return familyClientCategory;
    }

    @Override
    public String getClientAddress() {
        return familyClientAddress;
    }

    @Override
    public String getClientProblem() {
        return familyClientProblem;
    }

    @Override
    public int getClientAdultsAmount() {
        return familyClientAdultsAmount;
    }

    @Override
    public int getClientKidsAmount() {
        return familyClientKidsAmount;
    }

    @Override
    public Date getClientRegDate() {
        return familyClientRegDate;
    }

    @Override
    public String getClientisDLC() {
        return familyClientisDLC;
    }

    @Override
    public String getClientIsUnderSupport() {
        return familyClientIsUnderSupport;
    }

    @Override
    public Date getDateSupportStarted() {
        return familyDateSupportStarted;
    }

    @Override
    public String getIsSupportFinished() {
        return familyIsSupportFinished;
    }

    @Override
    public Date getDateSupportFinished() {
        return familyDateSupportFinished;
    }

    @Override
    public String getSupportResult() {
        return familySupportResult;
    }

    @Override
    public Photo getPhoto() {
        return mPhoto;
    }

    public static FamilyClient.Builder newBuilder(){
        return new FamilyClient(). new Builder();
    }

    public class Builder extends Client.Builder{
        @Override
        public Client.Builder setClientId(long clientId) {
            FamilyClient.this.familyClientId = clientId;
            return this;
        }

        @Override
        public Client.Builder setClientFirstName(String clientFirstName) {
            FamilyClient.this.familyClientFirstName = clientFirstName;
            return this;
        }

        @Override
        public Client.Builder setClientPatronymic(String clientPatronymic) {
            FamilyClient.this.familyClientPatronymic = clientPatronymic;
            return this;
        }

        @Override
        public Client.Builder setClientSecondName(String clientSecondName) {
            FamilyClient.this.familyClientSecondName = clientSecondName;
            return this;
        }

        @Override
        public Client.Builder setClientCategory(String clientCategory) {
            FamilyClient.this.familyClientCategory = clientCategory;
            return this;
        }

        @Override
        public Client.Builder setClientAddress(String clientAddress) {
            FamilyClient.this.familyClientAddress = clientAddress;
            return this;
        }

        @Override
        public Client.Builder setClientProblem(String clientProblem) {
            FamilyClient.this.familyClientProblem = clientProblem;
            return this;
        }

        @Override
        public Client.Builder setClientAdultsAmount(int clientAdultsAmount) {
            FamilyClient.this.familyClientAdultsAmount = clientAdultsAmount;
            return this;
        }

        @Override
        public Client.Builder setClientKidsAmount(int clientKidsAmount) {
            FamilyClient.this.familyClientKidsAmount = clientKidsAmount;
            return this;
        }

        @Override
        public Client.Builder setClientRegDate(Date clientRegDate) {
            FamilyClient.this.familyClientRegDate = clientRegDate;
            return this;
        }

        @Override
        public Client.Builder setClientisDLC(String clientisDLC) {
            FamilyClient.this.familyClientisDLC = clientisDLC;
            return this;
        }

        @Override
        public Client.Builder setClientIsUnderSupport(String clientIsUnderSupport) {
            FamilyClient.this.familyClientIsUnderSupport = clientIsUnderSupport;
            return this;
        }

        @Override
        public Client.Builder setDateSupportStarted(Date dateSupportStarted) {
            FamilyClient.this.familyDateSupportStarted = dateSupportStarted;
            return this;
        }

        @Override
        public Client.Builder setClientIsSupportFinished(String isSupportFinished) {
            FamilyClient.this.familyIsSupportFinished = isSupportFinished;
            return this;
        }

        @Override
        public Client.Builder setDateSupportFinished(Date dateSupportFinished) {
            FamilyClient.this.familyDateSupportFinished = dateSupportFinished;
            return this;
        }

        @Override
        public Client.Builder setClientSupportResult(String clientSupportResult) {
            FamilyClient.this.familySupportResult = clientSupportResult;
            return this;
        }

        @Override
        public Client.Builder setClientPhoto(String fileUri) {
            FamilyClient.this.mPhoto = new Photo(fileUri);
            return this;
        }

        @Override
        public Client build() {
            return FamilyClient.this;
        }
    }
}
