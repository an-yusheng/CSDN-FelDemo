package com.example.demo.util;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;

/**
 * @author Anzepeng
 * @title: FelDemo
 * @projectName demo
 * @description: TODO
 * @date 2020/6/3 0003下午 13:42
 */
public class FelDemo {

    public static void main(String[] args){
        // 百万数据速度测试
        cspeedTest();
    }

    /**
    　　* @description: 计算速度测试
    　　* @param ${tags} 
    　　* @return void
    　　* @throws
    　　* @author Anzepeng
    　　* @date 2020/6/3 0003 下午 15:35
    　　*/
    public static void cspeedTest(){
        // 计算核心类
        FelEngine felEngine = new FelEngineImpl();
        long sendTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++){
            // 执行计算的方法eval(公式)
            String formula = "max("+i+"+"+(i-1)+","+(i-5)+")";
            Object result = felEngine.eval(formula);
            System.out.println("公式："+formula+"结果："+result);

        }
        long endTime = System.currentTimeMillis();
        System.out.println("执行时间"+(endTime-sendTime)+"ms");

    }

    /**
    　　* @description: 公式demo
    　　* @param ${tags}
    　　* @return void
    　　* @throws
    　　* @author Anzepeng
    　　* @date 2020/6/3 0003 下午 15:27
    　　*/
    public static void formulaDemo(){
        // 计算核心类
        FelEngine felEngine = new FelEngineImpl();
        // 执行计算的方法eval(公式)
        Object result = felEngine.eval("1+2-3*4/5");
        // 结果
        System.out.println("加减乘除:"+result);

        Object functionResult = felEngine.eval("avg(min(max(sum(1,2,3),9),1),2)");
        System.out.println("聚合函数:"+functionResult);

        Object isResult = felEngine.eval("1>2");
        System.out.println("关系:"+isResult);

        Object ifResult = felEngine.eval("1>2?false:2"); // 1>2如果是false就展示2
        System.out.println("条件:"+ifResult);
    }
}
