package io.github.ningyu.jmeter.plugin.util;

import com.jd.jsf.gd.GenericService;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zengweipei
 * @CreateTime: 2021-11-11 18:55
 * @Description:
 */
public class JsfServiceUtils {


    private static Map<String, GenericService> jsfGenericMap = new HashMap<>();


    public static void putJsfGeneric(String name,GenericService genericService){
        jsfGenericMap.put(name,genericService);
    }

    public static GenericService getGenericService(String name){
        return jsfGenericMap.get(name);
    }

    public static boolean containsKey(String name){
        return jsfGenericMap.containsKey(name);
    }



}
