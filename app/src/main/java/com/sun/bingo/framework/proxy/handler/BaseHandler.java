package com.sun.bingo.framework.proxy.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.sun.bingo.framework.dialog.LoadingDialog;
import com.sun.bingo.framework.dialog.ToastTip;
import com.sun.bingo.framework.proxy.MessageArg;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseHandler<Re> extends Handler implements IContext {

    protected WeakReference<Re> mReference;
    private LoadingDialog dialog;

    public BaseHandler(Re t) {
        mReference = new WeakReference<Re>(t);
        dialog = new LoadingDialog(getContext());
    }

    @Override
    public void handleMessage(Message msg) {
        Re t = checkAvailability();
        if (t != null) {
            switch (msg.arg1) {
                case MessageArg.ARG1.TOAST_MESSAGE:
                    if (msg.obj instanceof String) {
                        ToastTip.showToastDialog(getContext(), msg.obj + "");
                    } else if (msg.obj instanceof Integer) {
                        ToastTip.showToastDialog(getContext(), getContext().getString((Integer) msg.obj) + "");
                    } else {
                        ToastTip.showToastDialog(getContext(), msg.obj + "");
                    }
                    break;
                case MessageArg.ARG1.CALL_BACKMETHOD:
                    invokeMethod(t, msg);
                    break;
                case MessageArg.ARG1.PROGRESSDIALOG_MESSAGE:
                    switch (msg.arg2) {
                        case 1:
                            dialog.show();
                            break;
                        case 2:
                            dialog.showCancelDialog();
                            break;
                        default:
                            dialog.dismiss();
                            break;
                    }
                default:
                    break;
            }
        }
    }

    private void invokeMethod(Re callClazz, Message msg) {
        try {
            if (!(msg.obj instanceof String)) {
                ToastTip.showToastDialog(getContext(), "Method error:" + msg.obj);
                return;
            }
            Method method;
            try {
                method = callClazz.getClass().getMethod(
                        msg.obj + "", Bundle.class);
            } catch (NoSuchMethodException e) {
                invokeNoArgMethod(callClazz, msg);
                return;
            }
            if (method != null) {
                method.setAccessible(true);
                method.invoke(callClazz, msg.getData());
            }
        } catch (NoSuchMethodException e) {
            ToastTip.showToastDialog(getContext(), "NoSuchMethodException: " + e.getMessage());
        } catch (IllegalAccessException e) {
            ToastTip.showToastDialog(getContext(), "IllegalAccessException: " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void invokeNoArgMethod(Re acitivty, Message msg) throws NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        Method method = acitivty.getClass().getMethod(msg.obj + "");
        if (method == null) {
            throw new NoSuchMethodException(msg.obj + "");
        }
        method.setAccessible(true);
        method.invoke(acitivty);
    }

    public void onDestroy() {
        dialog.dismiss();
    }

    protected abstract Re checkAvailability();

}
