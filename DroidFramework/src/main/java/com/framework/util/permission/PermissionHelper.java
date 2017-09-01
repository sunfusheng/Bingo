package com.framework.util.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.framework.DroidFramework;
import com.framework.util.OsVersionUtils;
import com.framework.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sunfusheng on 2017/8/21.
 */
@TargetApi(Build.VERSION_CODES.M)
public class PermissionHelper {

    private static final int REQUEST_CODE = 0X88;

    private final IPermission iPermission;
    private final List<String> checkPermissions = new ArrayList<>();

    private IPermissionCallback iPermissionCallback;

    public PermissionHelper(IPermission iPermission) {
        this.iPermission = iPermission;
        if (!(iPermission instanceof Activity) && !(iPermission instanceof Fragment)) {
            throw new IllegalArgumentException("iPermission must be activity or fragment");
        }
    }

    @Nullable
    public static IPermission getIPermission(Object obj) {
        IPermission iPermission = null;
        if (obj instanceof IPermission) {
            iPermission = (IPermission) obj;
        } else if (obj instanceof ContextWrapper) {
            Context baseContext = ((ContextWrapper) obj).getBaseContext();
            if (baseContext instanceof IPermission) {
                iPermission = (IPermission) baseContext;
            }
        }
        return iPermission;
    }

    public static boolean hasPermissions(String[] permissions) {
        return getDeniedPermission(permissions).isEmpty();
    }

    public void checkPermission(@NonNull String[] permissions, IPermissionCallback callback) {
        iPermissionCallback = callback;
        List<String> deniedPermissions = getDeniedPermission(permissions);

        if (Utils.isEmpty(deniedPermissions)) {
            iPermissionCallback.onCheckPermission(deniedPermissions);
            return;
        }

        if (iPermission instanceof Activity) {
            ((Activity) iPermission).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), REQUEST_CODE);
        } else if (iPermission instanceof Fragment) {
            ((Fragment) iPermission).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), REQUEST_CODE);
        }

        checkPermissions.clear();
        checkPermissions.addAll(deniedPermissions);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != REQUEST_CODE) {
            return;
        }

        List<String> failedPermissions = new ArrayList<>(checkPermissions);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                failedPermissions.remove(permissions[i]);
            }
        }

        iPermissionCallback.onCheckPermission(failedPermissions);
    }

    private static List<String> getDeniedPermission(String[] permissions) {
        if (!OsVersionUtils.hasM() || Utils.isEmpty(permissions)) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        for (String permission : permissions) {
            if (DroidFramework.getContext().checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                result.add(permission);
            }
        }

        return result;
    }

}
