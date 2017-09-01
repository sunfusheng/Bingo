package com.framework.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author sunfusheng on 2017/8/21.
 */
public class Utils {

    public static boolean equals(Object l, Object r) {
        boolean result = false;
        if (l == null) {
            result = r == null;
        } else if (r != null) {
            result = l.equals(r);
        }
        return result;
    }

    public static <T> int length(T[] arrays) {
        return null == arrays ? 0 : arrays.length;
    }

    public static <T> int length(Collection<T> list) {
        return null == list ? 0 : list.size();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return null == array || array.length == 0;
    }

    public static <T> boolean isInArray(T[] objects, T token) {
        if (null == objects || 0 == objects.length || null == token) {
            return false;
        }
        for (T item : objects) {
            if (item.equals(token)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInArray(int[] objects, int token) {
        if (null == objects || 0 == objects.length) {
            return false;
        }
        for (int item : objects) {
            if (token == item) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInArray(long[] objects, long token) {
        if (null == objects || 0 == objects.length) {
            return false;
        }
        for (long item : objects) {
            if (token == item) {
                return true;
            }
        }
        return false;
    }

    public static <T> int indexOf(T[] objects, T token) {
        if (null == objects || 0 == objects.length || null == token) {
            return -1;
        }
        for (int i = 0; i < objects.length; ++i) {
            if (objects[i].equals(token)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int lastIndexOf(T[] objects, T token) {
        if (null == objects || 0 == objects.length || null == token) {
            return -1;
        }
        for (int i = objects.length - 1; i >= 0; --i) {
            if (objects[i].equals(token)) {
                return i;
            }
        }
        return -1;
    }
}
