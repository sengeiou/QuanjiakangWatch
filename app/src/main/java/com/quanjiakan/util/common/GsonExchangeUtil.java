package com.quanjiakan.util.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/13.
 */

public class GsonExchangeUtil {
    /**
     * JsonArray convert to ArrayList;
     * @param array
     * @return
     */
    public static List<JsonObject> array2list(JsonArray array){
        List<JsonObject> list = new ArrayList<>();
        if(array == null){
            return list;
        }else {
            for (int i = 0; i < array.size(); i++) {
                list.add(array.get(i).getAsJsonObject());
            }
            return list;
        }
    }

    /**
     * @param mlist
     * @return
     */
    public static JsonArray list2array(List<JsonObject> mlist){
        JsonArray array = new JsonArray();
        for (int i = 0; i < mlist.size(); i++) {
            array.add(mlist.get(i));
        }
        return array;
    }
}
