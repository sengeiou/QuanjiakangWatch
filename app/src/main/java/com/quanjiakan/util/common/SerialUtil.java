package com.quanjiakan.util.common;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * 解析json，将String 类型的JSON对象直接转换成对应类型的对象
 *
 * Created by Administrator on 2016/6/14 0014.
 */
public class SerialUtil {
    private static final Gson gson = new Gson();

    /**
     *
     * @param jsonData
     * @param classes new TypeToken<T>(){}.getType();
     * @return T需要进行强制转换
     */
    public static Object jsonToObject(String jsonData, Type classes){
        return gson.fromJson(jsonData,classes);
    }

}
