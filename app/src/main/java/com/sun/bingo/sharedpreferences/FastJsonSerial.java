package com.sun.bingo.sharedpreferences;

import com.alibaba.fastjson.JSON;

import de.devland.esperandro.serialization.Serializer;

/**
 * Created by sunfusheng on 2015/9/30.
 */
public class FastJsonSerial implements Serializer {

    @Override
    public String serialize(Object object) {
        String result = JSON.toJSONString(object);
        return result;
    }

    @Override
    public <T> T deserialize(String serializedObject, Class<T> clazz) {
        T result = JSON.parseObject(serializedObject, clazz);
        return result;
    }
}
