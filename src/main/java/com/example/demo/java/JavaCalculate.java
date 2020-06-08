package com.example.demo.java;

import java.time.Month;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author Anzepeng
 * @title: JavaCalculate
 * @projectName demo
 * @description: TODO
 * @date 2020/6/4 0004上午 9:06
 */
public class JavaCalculate {
    public static Stack<String> operation = new Stack<String>();  //存放运算符
    public static Stack<Character> bracket = new Stack<Character>(); //存放左括号
    public static Stack<Integer> number = new Stack<Integer>(); //存放运算参数
    public static Stack<Integer> count = new Stack<Integer>(); //存放运算符参数个数

    public int add(int[] N) {
        int i = 0;
        for (int f = 0; f < N.length; f++){
            i += N[f];
        }
        return i;
    }

    public int max(int[] N) {
        int i = 0;
        for (int f = 0; f < N.length; f++){
            if (f == 0){
                i = N[f];
            }else{
                if (i<N[f]){
                    i = N[f];
                }
            }
        }
        return i;
    }

    public int min(int[] N) {
        int i = 0;
        for (int f = 0; f < N.length; f++){
            if (f == 0){
                i = N[f];
            }else{
                if (i>N[f]){
                    i = N[f];
                }
            }
        }
        return i;
    }

    public int doubleMe(int[] N) {
        return 2 * N[0];
    }

    public boolean judgeChar(char s) {
        if(s >= 'a' && s <= 'z' || s >= 'A' && s <= 'Z')
            return true;
        return false;
    }

    public boolean judgeNumber(char s) {
        if(s >= '0' && s <= '9')
            return true;
        return false;
    }

    public void getResult(String A) {
        String temp = "";
        for(int i = 0;i < A.length();i++) {
            // 验证是否在a和z之间
            if(judgeChar(A.charAt(i))) {
                temp = temp + A.charAt(i);
                i = i + 1;
                while(judgeChar(A.charAt(i))) {
                    temp = temp + A.charAt(i);
                    i++;
                }
                i = i - 1;
                operation.push(temp);
                count.push(0);  //刚寻找到一个运算符，并初始化一个参数个数为0
                temp = "";
            } else if(A.charAt(i) == '(') {
                bracket.push(A.charAt(i));
            } else if(judgeNumber(A.charAt(i))) {
                temp = temp + A.charAt(i);
                i = i + 1;
                while (judgeNumber(A.charAt(i))) {
                    temp = temp + A.charAt(i);
                    i++;
                }
                i = i - 1;
                number.push(Integer.valueOf(temp));
                if (count.size() > 0) {
                    count.push(count.pop() + 1);    //此处用于计算当前栈顶运算符实际参数个数
                } else {
                    count.push(0 + 1);
                }
                temp = "";
            } else if(A.charAt(i) == '+' || A.charAt(i) == '-' || A.charAt(i) == '*' || A.charAt(i) == '/'){
                // 先将i-1为获取前一个值，当前是计算符号
                i = i-1;
                // 初始化前值
                String a = String.valueOf(A.charAt(i));
                if (")".equals(a)){
                    int sum = number.pop();
                    a = String.valueOf(sum);
                    number.push(sum);
                    i = i + 1;
                }else{
                    // i-1往前循环判断如果有数值类型则字符串相加，比如18+1 检索到前一个字符为8 循环判断之前如果还是数值则相加，结果为18
                    i = i-1;
                    while (judgeNumber(A.charAt(i))){
                        a = A.charAt(i) + a;
                        i--;
                    }
                    // 循环完毕i恢复原始位置
                    i = i + (a.length()+1);
                }

                // 先将i+1为获取后一个值，当前是计算符号
                i = i+1;
                // 初始化后值
                String temps = String.valueOf(A.charAt(i));
                // i+1往后循环判断如果有数值类型则字符串相加，比如1+28 检索到第一个字符为2 循环判断之前如果还是数值则相加，结果为18
                i = i+1;
                while (judgeNumber(A.charAt(i))){
                    temps = A.charAt(i) + temps;
                    i++;
                }
                // 循环完毕i恢复原始位置
                i = i - (temps.length()+1);
                String b = temps;


                // 得到值，求结果
                int c = 0;
                if (A.charAt(i) == '+'){
                    c = Integer.valueOf(a)+Integer.valueOf(b);
                }if (A.charAt(i) == '-'){
                    c = Integer.valueOf(a)-Integer.valueOf(b);
                }if (A.charAt(i) == '*'){
                    c = Integer.valueOf(a)*Integer.valueOf(b);
                }if (A.charAt(i) == '/'){
                    c = Integer.valueOf(a)/Integer.valueOf(b);
                }
                // 先摘出加号之前的值，我们要存的是总值
                number.pop();
                // push结果数据，count栈不增加，因为在加号之前已经加过一次参数值当作此次的个数
                number.push(c);
                i = i + 1;
            } else if(A.charAt(i) == ')') {  //此时要进行运算
                bracket.pop();  //栈顶左括号出栈
                String tempOpera = operation.pop();
                int[] N = new int[count.pop()];
                if(!count.empty())
                    count.push(count.pop() + 1);
                for(int j = 0;j < N.length;j++){
                    N[j] = number.pop();
                }
                int result = 0;
                if(tempOpera.equals("add"))
                    result = add(N);
                else if(tempOpera.equals("max"))
                    result = max(N);
                else if(tempOpera.equals("min"))
                    result = min(N);
                else if(tempOpera.equals("doubleMe"))
                    result = doubleMe(N);
                number.push(result);
            }
        }
    }

    public static void main(String[] args) {
        formula();
    }

    public static void formula(){
        JavaCalculate test = new JavaCalculate();
        test.getResult("max(add(min(1,2,4),max(2,18-1,20000/2))+444,4444/12)");
        System.out.println("函数多参数："+number.pop());
        /*test.getResult("add(min(2,4),max(2,8))");
        System.out.println("规定格式："+number.pop());
        test.getResult("add(min(1,2,4),max(2,8,12))");
        System.out.println("函数多参数："+number.pop());
        test.getResult("add(min(5,3-1),max(2,8))");
        System.out.println("错误结果："+number.pop());
        test.getResult("1+add(min(5,3),max(2,8))");
        System.out.println("前缀+："+number.pop());*/
    }

    public static void speed(){
        JavaCalculate test = new JavaCalculate();
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++){
            test.getResult("max(add(min(1,2,4),max(2,18-1,20000/2))+444,4444/12)");
        }
        long eTime = System.currentTimeMillis();
        System.out.println("耗时+："+(eTime-sTime));
    }
}
