package com.framework.util.permission;

import android.support.annotation.NonNull;

/**
 * @author sunfusheng on 2017/8/21.
 */
public interface IPermission {

    void checkPermission(@NonNull String[] permissions, IPermissionCallback callback);
}
