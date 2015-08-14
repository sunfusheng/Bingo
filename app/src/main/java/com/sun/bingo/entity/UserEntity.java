package com.sun.bingo.entity;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by sunfusheng on 15/7/27.
 */
public class UserEntity extends BmobUser {

    private String userAvatar;
    private String nickName;
    private String userSign;
    private List<String> favoriteList;

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

    public List<String> getFavoriteList() {
        return favoriteList;
    }

    public void setFavoriteList(List<String> favoriteList) {
        this.favoriteList = favoriteList;
    }
}
