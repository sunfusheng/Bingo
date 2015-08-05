package com.sun.bingo.util;

public class IsNullOrEmpty {
	public static boolean isEmpty(String str) {
		if(str==null||"".equals(str)||"null".equals(str)){
			return true;
		}else{
			return false;
		}
	}

    public static boolean isEmptyZero(String str) {
        if(str==null||"".equals(str)||"null".equals(str)||"0".equals(str)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isEmptyNoLimited(String str) {
        if(str==null||"".equals(str)||"null".equals(str)||"不限".equals(str)){
            return true;
        }else{
            return false;
        }
    }
}
