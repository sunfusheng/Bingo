package com.sun.bingo.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by sunfusheng on 15/7/27.
 */
@Table(name = "userTable")
public class UserEntity extends BmobUser {

    public int id;
    @Column(column = "nickName")
    public String nickName;
    public String userAvatar;
    public String userSign;
    public List<String> favoriteList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
