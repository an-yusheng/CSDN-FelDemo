package com.example.demo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Anzepeng
 * @title: ParseUtils
 * @projectName calculate
 * @description: TODO
 * @date 2020/5/20 0020下午 17:55
 */
public class ParseUtils {

    /**
     * 根据计算公式解析出计算所依赖的数据项
     * @param formula 计算公式
     * @return 所依赖的指标项编码
     */
    public static List<String> getchidrenIndex(String formula){
        String pattern = "\\$[\\w\\.]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(formula);
        List<String> result=new ArrayList<String>();
        while(m.find()) {
            String a=m.group().substring(1);
            if(!result.contains(a)){
                result.add(a);
            }
        }
        return result;
    }
}
