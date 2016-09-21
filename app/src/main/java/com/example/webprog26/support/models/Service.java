package com.example.webprog26.support.models;

import java.util.Date;

/**
 * Created by webprog26 on 14.03.2016.
 */
public class Service {

    private long serviceId;
    private String serviceType;
    private int servicesAmount;
    private String duringVisit;
    private Date serviceDate;
    private long clientId;

    public long getServiceId() {
        return serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public long getClientId() {
        return clientId;
    }

    public String getDuringVisit() {
        return duringVisit;
    }

    public int getServicesAmount() {
        return servicesAmount;
    }

    public static Builder newBuilder(){
        return new Service(). new Builder();
    }

    public class Builder{
        private Builder(){}

        public Builder setServiceId(long serviseId){
            Service.this.serviceId = serviseId;
            return this;
        }

        public Builder setServiceType(String serviceType){
            Service.this.serviceType = serviceType;
            return this;
        }

        public Builder setDuringVisit(String duringVisit){
            Service.this.duringVisit = duringVisit;
            return this;
        }

        public Builder setServiceDate(Date serviceDate){
            Service.this.serviceDate = serviceDate;
            return this;
        }

        public Builder setClientId(long clientId){
            Service.this.clientId = clientId;
            return this;
        }

        public Builder setServicesAmount(int servicesAmount){
            Service.this.servicesAmount = servicesAmount;
            return this;
        }
        public Service build(){
            return Service.this;
        }
    }
}
