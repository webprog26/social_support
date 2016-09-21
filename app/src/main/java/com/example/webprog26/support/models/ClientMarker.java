package com.example.webprog26.support.models;

/**
 * Created by webprog26 on 24.03.2016.
 */
public class ClientMarker {

    private long markerId;
    private long mClientId;
    private String mTitle;
    private String mSnippet;
    private int mCategory;
    private double mLatitude;
    private double mLongitude;

    public long getMarkerId() {
        return markerId;
    }

    public long getClientId() {
        return mClientId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSnippet() {
        return mSnippet;
    }

    public int getCategory() {
        return mCategory;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public static Builder newBuilder(){
        return new ClientMarker(). new Builder();
    }

    public class Builder{
        private Builder(){}

        public Builder setMarkerId(long markerId){
            ClientMarker.this.markerId = markerId;
            return this;
        }

        public Builder setClientMarkerClientId(long clientId){
            ClientMarker.this.mClientId = clientId;
            return this;
        }

        public Builder setClientMarkerTitle(String title){
            ClientMarker.this.mTitle = title;
            return this;
        }

        public Builder setClientMarkerSnippet(String snippet){
            ClientMarker.this.mSnippet = snippet;
            return this;
        }

        public Builder setClientMarkerCategory(int category){
            ClientMarker.this.mCategory = category;
            return this;
        }

        public Builder setClientMarkerLatitude(double latitude){
            ClientMarker.this.mLatitude = latitude;
            return this;
        }

        public Builder setClientMarkerLongitude(double longitude){
            ClientMarker.this.mLongitude = longitude;
            return this;
        }

        public ClientMarker build(){
            return ClientMarker.this;
        }
    }
}
