package com.example.webprog26.support.models;

import java.util.Date;

/**
 * Created by webprog26 on 10.03.2016.
 */
public class Visit {

    private long visitId;
    private Date visitDate;
    private long clientId;

    public static Builder newBuilder(){
        return new Visit().new Builder();
    }

    public class Builder{

        private Builder(){}


        public Builder setClientId(long clientId){
            Visit.this.clientId = clientId;
            return this;
        }

        public Builder setVisitDate(Date visitDate){
            Visit.this.visitDate = visitDate;
            return this;
        }

       public Builder setVisitId(long visitId){
           Visit.this.visitId = visitId;
           return this;
       }

        public Visit build(){
            return Visit.this;
        }
    }

    public long getVisitId() {
        return visitId;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public long getClientId() {
        return clientId;
    }
}
