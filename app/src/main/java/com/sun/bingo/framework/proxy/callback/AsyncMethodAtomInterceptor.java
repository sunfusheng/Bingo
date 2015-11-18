package com.sun.bingo.framework.proxy.callback;

import android.os.Message;
import android.os.Process;

import com.google.dexmaker.stock.ProxyBuilder;
import com.sun.bingo.framework.annotation.AsyncAtomMethod;
import com.sun.bingo.framework.proxy.MessageArg;
import com.sun.bingo.framework.proxy.MessageProxy;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncMethodAtomInterceptor implements Interceptor {

    private static ExecutorService pool = Executors.newCachedThreadPool();
    private MessageProxy mMethodCallBack;
    private Set<String> methodHashSet;

    public AsyncMethodAtomInterceptor(MessageProxy mMethodCallBack) {
        this.mMethodCallBack = mMethodCallBack;
        methodHashSet = Collections.synchronizedSet(new HashSet<String>());
    }

    @Override
    public Object intercept(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final AsyncAtomMethod asyncMethod = method.getAnnotation(AsyncAtomMethod.class);

        if (asyncMethod != null) {
            if (asyncMethod.methodType() == null) {
                //默认原子操作
                if (methodHashSet.contains(method.getName())) {
                    return null;
                } else {
                    methodHashSet.add(method.getName());
                }
            } else {
                switch (asyncMethod.methodType()) {
                    case atom: //原子操作
                        if (methodHashSet.contains(method.getName())) {
                            return null;
                        } else {
                            methodHashSet.add(method.getName());
                        }
                    case normal: //非原子操作
                    default:
                        break;
                }
            }

            if (asyncMethod.withDialog()) {
                mMethodCallBack.showDialog();
            }
            if (asyncMethod.withCancelableDialog()) {
                mMethodCallBack.showCancelableDialog();
            }

            pool.execute(new Runnable() {

                @Override
                public void run() {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    try {
                        dealWithResult(getResult());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

                private Object getResult() throws Throwable {
                    Object obj = null;
                    switch (asyncMethod.methodType()) {
                        case normal:
                            obj = ProxyBuilder.callSuper(proxy, method, args);
                            break;
                        case atom:
                            obj = ProxyBuilder.callSuper(proxy, method, args);
                            methodHashSet.remove(method.getName());
                            break;
                        default:
                            break;
                    }
                    return obj;
                }

                private void dealWithResult(Object result) {
                    if (asyncMethod.withDialog() || asyncMethod.withCancelableDialog()) {
                        mMethodCallBack.hideDialog();
                    }

                    if (result != null && result.getClass() == Message.class) {
                        Message msg = (Message) result;
                        switch (msg.arg1) {
                            case MessageArg.ARG1.TOAST_MESSAGE:
                                break;
                            case MessageArg.ARG1.CALL_BACK_METHOD:
                                if (msg.obj == null || !(msg.obj instanceof String) || "".equals(msg.obj.toString().trim())) {
                                    msg.obj = method.getName() + "CallBack";
                                }
                            default:
                                break;
                        }
                        mMethodCallBack.sendMessage(msg);
                    }
                }
            });
            return null;
        } else {
            return ProxyBuilder.callSuper(proxy, method, args);
        }
    }
}
