package com.sun.bingo.framework.proxy.helper;

import android.content.Context;

import com.sun.bingo.framework.base.BaseControl;
import com.sun.bingo.framework.proxy.ControlFactory;
import com.sun.bingo.framework.proxy.MessageProxy;
import com.sun.bingo.framework.proxy.ModelMap;
import com.sun.bingo.framework.proxy.common.IRefreshBack;
import com.sun.bingo.framework.proxy.handler.BaseHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseHelper<T extends BaseControl, R extends IRefreshBack> {

    protected T mControl;
    protected MessageProxy messageProxy;
    protected ModelMap mModel;
    protected R mReferenceObj;
    protected BaseHandler mHandler;

    public BaseHelper(R referenceObj, BaseHandler handler) {
        this.mReferenceObj = referenceObj;
        this.mHandler = handler;
    }

    public T getControl() {
        return mControl;
    }

    public MessageProxy getMessageProxy() {
        return messageProxy;
    }

    public ModelMap getModelMap() {
        return mModel;
    }

    public Context getContext() {
        return mHandler.getContext();
    }

    public void onCreate() {
        controlInit();
    }

    public void onStart() {
        if (mControl != null) {
            mControl.onStart();
        }
    }

    public void onResume() {
        if (mControl == null || messageProxy == null) {
            controlInit();
        }
    }

    public void onPause() {
        if (mControl != null) {
            mControl.onPause();
        }
    }

    public void onStop() {
        if (mControl != null) {
            mControl.onStop();
        }
    }

    public void onDestroy() {
        if (mControl != null) {
            mControl.onDestroy();
        }
    }

    private void controlInit() {
        Class<?> clazz;
        clazz = mReferenceObj.getClass();
        generateControl(clazz);
        if (mControl == null) {
            generateControl(clazz.getSuperclass());
        }
    }

    private void generateControl(Class clazz) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            Type[] arrayClasses = p.getActualTypeArguments();
            for (Type item : arrayClasses) {
                if (item instanceof Class) {
                    Class<T> tClass = (Class<T>) item;
                    if (tClass.equals(BaseControl.class) || (tClass.getSuperclass() != null &&
                            tClass.getSuperclass().equals(BaseControl.class))) {
                        messageProxy = new MessageProxy(mHandler);
                        mControl = ControlFactory.getControlInstance(tClass, messageProxy);
                        mModel = new ModelMap();
                        mControl.setModel(mModel);
                        return;
                    }
                }
            }
        }
    }

}
