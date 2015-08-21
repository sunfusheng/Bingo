package com.sun.bingo.framework.orm;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunfusheng on 15/8/21.
 */
public class DbHelper<T> {

    private Context mContext;
    private DbUtils dbUtils;

    public DbHelper(Context context, Class<T> clz) {
        this.mContext = context;
        DbUtils.DaoConfig config = new DbUtils.DaoConfig(context);
        config.setDbName(clz.getName()+".db");
        config.setDbVersion(1);
        dbUtils = DbUtils.create(config);
        try {
            dbUtils.createTableIfNotExist(clz);
        } catch (DbException e) {
            LogUtils.e(e.getMessage());
        }
    }

    public void saveEntity(T t) {
        try {
            dbUtils.save(t);
        } catch (DbException e) {
            LogUtils.e(e.getMessage());
        }
    }

    public void saveList(List<T> list) {
        try {
            dbUtils.saveAll(list);
        } catch (DbException e) {
            LogUtils.e(e.getMessage());
        }
    }

    public List<T> findList(Class<T> clz, String userId) {
        try {
            if (dbUtils.tableIsExist(clz)) {
                return dbUtils.findAll(Selector.from(clz).where("objectId", "=", userId));
            }
        } catch (DbException e) {
            LogUtils.e(e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<T> findList(Class<T> clz) {
        try {
            if (dbUtils.tableIsExist(clz)) {
                return dbUtils.findAll(clz);
            }
        } catch (DbException e) {
            LogUtils.e(e.getMessage());
        }
        return new ArrayList<>();
    }
}
