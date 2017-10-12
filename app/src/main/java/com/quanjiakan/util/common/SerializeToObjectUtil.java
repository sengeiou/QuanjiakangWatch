package com.quanjiakan.util.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * 解析json，将String 类型的JSON对象直接转换成对应类型的对象
 *
 * Created by Administrator on 2016/6/14 0014.
 */
public class SerializeToObjectUtil{

    private SerializeToObjectUtil(){

    }
    /**
     * 静态内部类的单例模式
     * @return
     */
    public SerializeToObjectUtil getInstances(){
        return SerializeToObjectUtilHolder.instances;
    }

    private final Gson gson = new Gson();

    /**
     *
     * @param jsonData
     * @param classes new TypeToken<T>(){}.getType();
     * @return T需要进行强制转换
     */
    public Object jsonToObject(String jsonData, Type classes){
        return gson.fromJson(jsonData,classes);
    }

    /**
     * 私有静态内部类持有
     * @param <T>
     */
    private static class SerializeToObjectUtilHolder<T>{
        private static final SerializeToObjectUtil instances = new SerializeToObjectUtil();
    }

}
