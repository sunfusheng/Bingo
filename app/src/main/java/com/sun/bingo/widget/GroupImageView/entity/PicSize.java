package com.sun.bingo.widget.GroupImageView.entity;

import java.io.Serializable;

/**
 * Created by sunfusheng on 15/8/3.
 */
public class PicSize implements Serializable {

    private String key;
    private int width;
    private int height;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
