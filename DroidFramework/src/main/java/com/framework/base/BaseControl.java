/*
*                #####################################################
*                #                                                   #
*                #                       _oo0oo_                     #
*                #                      o8888888o                    #
*                #                      88" . "88                    #
*                #                      (| -_- |)                    #
*                #                      0\  =  /0                    #
*                #                    ___/`---'\___                  #
*                #                  .' \\|     |# '.                 #
*                #                 / \\|||  :  |||# \                #
*                #                / _||||| -:- |||||- \              #
*                #               |   | \\\  -  #/ |   |              #
*                #               | \_|  ''\---/''  |_/ |             #
*                #               \  .-\__  '-'  ___/-. /             #
*                #             ___'. .'  /--.--\  `. .'___           #
*                #          ."" '<  `.___\_<|>_/___.' >' "".         #
*                #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
*                #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
*                #     =====`-.____`.___ \_____/___.-`___.-'=====    #
*                #                       `=---='                     #
*                #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
*                #                                                   #
*                #               佛祖保佑生哥         永无BUG           #
*                #                                                   #
*                #####################################################
*
*/
package com.framework.base;

import android.database.SQLException;
import android.os.Bundle;
import android.os.Message;

import com.framework.DroidFramework;
import com.framework.proxy.MessageArg;
import com.framework.proxy.MessageProxy;
import com.framework.proxy.ModelMap;
import com.orhanobut.logger.Logger;

import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;

public class BaseControl {

    protected ModelMap mModel;
    protected MessageProxy mMessageCallBack; //消息分发代理类型
    

    public BaseControl(MessageProxy mMessageCallBack) {
        this.mMessageCallBack = mMessageCallBack;
    }

    public void setModel(ModelMap model) {
        this.mModel = model;
    }

    public void onStart() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    /**
     * 界面不可见的时候，清除界面相关的消息发送传递
     */
    public void onStop() {
        mMessageCallBack.clearAllMessage();
    }

    public void onDestroyView() {
        mMessageCallBack.clearAllMessage();
    }

    /**
     * 清除数据,比如model中的数据变量什么的。
     */
    public void onDestroy() {
        if (mMessageCallBack != null) {
            mMessageCallBack.clearAllMessage();
        }
    }

    protected void dealWithException(Exception e) {
        if (DroidFramework.LOG) {
            if (e instanceof InterruptedIOException) {
                sendToastMessage("下载内容超时");
            } else if (e instanceof UnknownHostException) {
                sendToastMessage("连接服务器超时，请检查网络");
            } else if (e instanceof SQLException) {
                sendToastMessage("数据库操作异常");
            } else if (e instanceof com.alibaba.fastjson.JSONException || e instanceof org.json.JSONException) {
                sendToastMessage("数据解析数据异常");
            } else if (e instanceof NoSuchElementException) {
                sendToastMessage("网络异常，请重新尝试");
            } else {
                sendToastMessage("其它异常");
                e.printStackTrace();
            }
        } else {
            sendToastMessage("连接服务器超时");
        }
    }

    protected void dealWithExceptionMessage(String s) {
        Logger.e("error: ", s + "");
    }

    /**
     * 直接发送toast消息
     *
     * @param toast
     */
    protected void sendToastMessage(String toast) {
        Message msg = mMessageCallBack.obtionMessage( MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 =  MessageArg.ARG1.TOAST_MESSAGE;
        msg.obj = toast;
        mMessageCallBack.sendMessage(msg);
    }

    protected void sendToastMessage(int toast) {
        Message msg = mMessageCallBack.obtionMessage( MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 =  MessageArg.ARG1.TOAST_MESSAGE;
        msg.obj = toast;
        mMessageCallBack.sendMessage(msg);
    }

    /**
     * 直接向指定方法发送消息
     *
     * @param method 指定的方法名称（方法可以有参数Bundle 也可以没有回调参数Bundle）
     */
    protected void sendMessage(String method) {
        Message msg = mMessageCallBack.obtionMessage( MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 =  MessageArg.ARG1.CALL_BACK_METHOD;
        msg.obj = method;
        mMessageCallBack.sendMessage(msg);
    }

    /**
     * 直接向指定方法发送消息
     *
     * @param method 指定的方法名称（方法可以有参数Bundle 也可以没有回调参数Bundle）
     * @param delayMillis
     */
    protected void sendMessage(String method, long delayMillis) {
        Message msg = mMessageCallBack.obtionMessage( MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 =  MessageArg.ARG1.CALL_BACK_METHOD;
        msg.obj = method;
        mMessageCallBack.sendMessageDelay(msg, delayMillis);
    }

    /**
     * 直接向指定方法发送消息, 同时将Bundle数据传递过去
     *
     * @param method
     * @param bundle
     */
    protected void sendMessage(String method, Bundle bundle) {
        Message msg = mMessageCallBack.obtionMessage( MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 =  MessageArg.ARG1.CALL_BACK_METHOD;
        msg.obj = method;
        msg.setData(bundle);
        mMessageCallBack.sendMessage(msg);
    }

    /**
     * 直接向指定方法发送消息,同时将Bundle数据传递过去
     *
     * @param method
     * @param bundle
     * @param delayMillis
     */
    protected void sendMessage(String method, Bundle bundle, long delayMillis) {
        Message msg = mMessageCallBack.obtionMessage(MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 = MessageArg.ARG1.CALL_BACK_METHOD;
        msg.obj = method;
        msg.setData(bundle);
        mMessageCallBack.sendMessageDelay(msg, delayMillis);
    }

    /**
     * 回调参数的方法
     *
     * @param bundle
     * @return
     */
    protected Message getDataMessage(Bundle bundle) {
        Message msg = mMessageCallBack.obtionMessage(MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 = MessageArg.ARG1.CALL_BACK_METHOD;
        msg.setData(bundle);
        return msg;
    }

    /**
     * 回调参数的方法
     *
     * @param method
     * @param bundle
     * @return
     */
    protected Message getDataMessage(String method, Bundle bundle) {
        Message msg = mMessageCallBack.obtionMessage(MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 = MessageArg.ARG1.CALL_BACK_METHOD;
        msg.obj = method;
        msg.setData(bundle);
        return msg;
    }

    /**
     * 回调参数的方法
     *
     * @return
     */
    protected Message getMessage() {
        Message msg = mMessageCallBack.obtionMessage( MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 =  MessageArg.ARG1.CALL_BACK_METHOD;
        return msg;
    }

    /**
     * 返回method消息
     *
     * @param method 方法名字
     * @return Message 返回类型
     */
    protected Message getMethodMessage(String method) {
        Message msg = mMessageCallBack.obtionMessage( MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 =  MessageArg.ARG1.CALL_BACK_METHOD;
        msg.obj = method;
        return msg;
    }

    /**
     * 返回toast消息
     *
     * @param toastMsg
     * @return
     */
    protected Message getToastMessage(String toastMsg) {
        Message msg = mMessageCallBack.obtionMessage( MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 =  MessageArg.ARG1.TOAST_MESSAGE;
        msg.obj = toastMsg;
        return msg;
    }
}
