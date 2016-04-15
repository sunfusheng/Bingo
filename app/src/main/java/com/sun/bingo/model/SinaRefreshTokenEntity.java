package com.sun.bingo.model;

import java.io.Serializable;

/**
 * Created by sunfusheng on 16/2/24.
 */
public class SinaRefreshTokenEntity implements Serializable {

    private String access_token;
    private String remind_in;
    private int expires_in;
    private String refresh_token;
    private String uid;

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setRemind_in(String remind_in) {
        this.remind_in = remind_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRemind_in() {
        return remind_in;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "SinaRefreshTokenEntity{" +
                "access_token='" + access_token + '\'' +
                ", remind_in='" + remind_in + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
