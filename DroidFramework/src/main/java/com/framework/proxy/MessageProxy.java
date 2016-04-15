package com.framework.proxy;

import android.os.Message;

import com.framework.proxy.handler.BaseHandler;

/**
 * 被封装过的Handler
 */
public class MessageProxy {

    private BaseHandler mHandler;

    public MessageProxy(BaseHandler handler) {
        this.mHandler = handler;
    }

    public Message obtionMessage(int what) {
        return mHandler.obtainMessage(what);
    }

    public void sendMessage(Message msg) {
        mHandler.sendMessage(msg);
    }

    public void sendMessageDelay(Message msg, long delayMillis) {
        mHandler.sendMessageDelayed(msg, delayMillis);
    }

    public void postRunnableDelay(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    public void postRunnable(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void removeRunnable(Runnable r) {
        mHandler.removeCallbacks(r);
    }

    public void removeCallBack(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }

    public void showDialog(String tip) {
        Message msg = mHandler.obtainMessage(MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 = MessageArg.ARG1.PROGRESSDIALOG_MESSAGE;
        msg.arg2 = 1;
        msg.obj = tip;
        mHandler.sendMessage(msg);
    }

    public void showCancelableDialog(String tip) {
        Message msg = mHandler.obtainMessage(MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 = MessageArg.ARG1.PROGRESSDIALOG_MESSAGE;
        msg.arg2 = 2;
        msg.obj = tip;
        mHandler.sendMessage(msg);
    }

    public void hideDialog() {
        Message msg = mHandler.obtainMessage(MessageArg.WHAT.UI_MESSAGE);
        msg.arg1 = MessageArg.ARG1.PROGRESSDIALOG_MESSAGE;
        msg.arg2 = 3;
        mHandler.sendMessage(msg);
    }

    public void clearAllMessage() {
        mHandler.removeMessages(MessageArg.WHAT.UI_MESSAGE);
        mHandler.onDestroy();
    }

}
