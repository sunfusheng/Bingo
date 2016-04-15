package com.sun.bingo.model;

import java.io.Serializable;

/**
 * Created by sunfusheng on 15/8/20.
 */
public class Binary implements Serializable {

    private long fsize;

    public long getFsize() {
        return fsize;
    }

    public void setFsize(long fsize) {
        this.fsize = fsize;
    }

}
