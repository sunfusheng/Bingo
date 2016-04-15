package com.framework.proxy.filter;

import java.lang.reflect.Method;

public interface InterceptorFilter {

    int accept(Method method);

}
