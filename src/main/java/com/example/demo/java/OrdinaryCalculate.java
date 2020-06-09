package com.example.demo.java;

import java.util.Stack;

/**
 * @author Anzepeng
 * @title: OrdinaryCalculate
 * @projectName demo
 * @description: 普通运算
 * @date 2020/6/9 0009上午 11:39
 */
public class OrdinaryCalculate {

    public static boolean judgeNumber(char s) {
        if(s >= '0' && s <= '9')
            return true;
        return false;
    }

    public static boolean judgeChar(char s) {
        if(s >= 'a' && s <= 'z' || s >= 'A' && s <= 'Z')
            return true;
        return false;
    }

    /**
    　　* @description: 公式解析
    　　* @param parm
    　　* @return String
    　　* @throws
    　　* @author Anzepeng
    　　* @date 2020/6/9 0009 下午 13:18
    　　*/
    public static String formulaParsing(String parm) {
        // 运行符和数据分成两个数组
        String[] o = new String[parm.length()];
        String[] n = new String[parm.length()];
        for (int i = 0; i < parm.length(); i++) {
            if (parm.charAt(i) == '+' || parm.charAt(i) == '-' || parm.charAt(i) == '*' || parm.charAt(i) == '/' || parm.charAt(i) == '(' || parm.charAt(i) == ')') {
                o[i] = String.valueOf(parm.charAt(i));
            } else if (judgeNumber(parm.charAt(i))) {
                n[i] = String.valueOf(parm.charAt(i));
            }
        }

        // 写到一起运行符以%间隔以便后面解析
        String formula = new String();
        for (int i = 0; i < o.length; i++) {
            if (o[i] != null) {
                formula += "%" + o[i] + "%";
            } else if (n[i] != null) {
                formula += n[i];
            }
        }

        // 使用栈存储
        Stack stack = new Stack();
        String[] formulas = formula.split("%");
        String[] f = new String[formula.length()];
        int fSum = 0;
        // 去除多个% split产生的空数组位数
        for (int i = 0; i < formulas.length; i++) {
            if (formulas[i]!=null && !"".equals(formulas[i])){
                f[fSum] = formulas[i];
                fSum++;
            }
        }
        formulas = f;
        // 以实际有数据的位数作为循环最大值
        for (int i = 0; i < fSum; i++) {
            // 若为*或/时则进行括号相加其他直接累加
            if ("*".equals(formulas[i]) ||"/".equals(formulas[i])) {
                String a = (String) stack.pop();
                // 验证上一位是否是括号，如果是理应括住整个括号，这里没有扩，需要判断之前的括号有多少位
                if (")".equals(a)){
                    String setF = a;
                    // 前面的括号处理
                    int sz = stack.size();// 记录栈的长度，不能再for直接用，否则出栈循环数也会减少
                    int eN = 1;// 记录后括号几个，因为已经出过一个后括号所以从1开始
                    int sN = 0;// 记录前括号几个，入股两个值一样则停止
                    for (int z = 0; z < sz; z++){
                        String zs = (String) stack.pop();
                        if (")".equals(zs)){
                            eN++;
                        }else if ("(".equals(zs)){
                            sN++;
                        }
                        setF = zs + setF;
                        if (eN == sN){
                            break;
                        }
                    }
                    String b = new String();
                    if ("(".equals(formulas[i + 1])){
                        // 后面的括号处理
                        boolean bs = true;
                        int wi = 2;// 这是为了循环定义的值,因为确定i+1已经是（所以从i+2开始
                        int sW = 1;// 记录前括号几个，因为已经出过一个前括号所以从1开始
                        int eW = 0;// 记录后括号几个
                        String setW = "(";// 这里直接定义接受前括号以便上面两个int值不一样
                        while (bs){
                            // 这里没有校验括号内的*和/给加上括号
                            String ws = formulas[i+wi];
                            if ("(".equals(ws)){
                                sW++;
                            }else if (")".equals(ws)){
                                eW++;
                            }
                            setW = setW + ws;
                            if (sW == eW){
                                break;
                            }else{
                                wi++;
                            }
                        }
                        b = "(" + setF + formulas[i] + setW + ")";
                        stack.push(b);
                        i = i+(wi);
                    }else{
                        b = "(" + setF + formulas[i] + formulas[i + 1] + ")";
                        stack.push(b);
                        i++;
                    }
                }else if("(".equals(formulas[i + 1])){
                    String b = new String();
                    if ("(".equals(formulas[i + 1])){
                        // 后面的括号处理
                        boolean bs = true;
                        int wi = 2;// 这是为了循环定义的值,因为确定i+1已经是（所以从i+2开始
                        int sW = 1;
                        int eW = 0;
                        String setW = "(";
                        while (bs){
                            String ws = formulas[i+wi];
                            if ("(".equals(ws)){
                                sW++;
                            }else if (")".equals(ws)){
                                eW++;
                            }
                            setW = setW + ws;
                            if (sW == eW){
                                break;
                            }else{
                                wi++;
                            }
                        }
                        b = "(" + a + formulas[i] + setW + ")";
                        stack.push(b);
                        i = i+(wi);
                    }else{
                        b = "(" + a + formulas[i] + formulas[i + 1] + ")";
                        stack.push(b);
                        i++;
                    }
                } else{
                    String b = "(" + a + formulas[i] + formulas[i + 1] + ")";
                    stack.push(b);
                    i++;
                }
            } else {
                stack.push(formulas[i]);
            }
        }

        String starkStr = (String) stack.pop();
        int z = stack.size();
        for (int i = 0; i < z; i++) {
            starkStr = stack.pop() +  starkStr;
        }
        return starkStr;
    }

    public static Stack<String> operation = new Stack<String>();  //存放运算符
    public static Stack<Character> bracket = new Stack<Character>(); //存放左括号
    public static Stack<Integer> number = new Stack<Integer>(); //存放运算参数
    public static Stack<Integer> count = new Stack<Integer>(); //存放运算符参数个数

    public static int jia(int[] N) {
        return N[0] + N[1];
    }

    public static int jian(int[] N) {
        return N[0] - N[1];
    }

    public static int cheng(int[] N) {
        return N[0] * N[1];
    }

    public static int chu(int[] N) {
        return N[0] / N[1];
    }

    /**
    　　* @description: 计算
    　　* @param ${tags}
    　　* @return void
    　　* @throws
    　　* @author Anzepeng
    　　* @date 2020/6/9 0009 下午 13:20
    　　*/
    public static void calculate(String formula){
        String temp = "";
        for(int i = 0;i < formula.length();i++) {
            if(formula.charAt(i) == '(') {
                bracket.push(formula.charAt(i));
            } else if(judgeNumber(formula.charAt(i))) {
                temp = temp + formula.charAt(i);
                i = i + 1;
                while (judgeNumber(formula.charAt(i))) {
                    temp = temp + formula.charAt(i);
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
            }else if(formula.charAt(i) == '+' || formula.charAt(i) == '-' || formula.charAt(i) == '*' || formula.charAt(i) == '/'){
                operation.push(String.valueOf(formula.charAt(i)));
            } else if(formula.charAt(i) == ')') {  //此时要进行运算
                bracket.pop();  //栈顶左括号出栈
                String tempOpera = operation.pop();
                int end = number.pop();// 因为先进后出的特性，所以后值先出
                int send = number.pop();// 前值后出
                int[] N = {send,end};
                System.out.println("公式 "+send + tempOpera + end);
                int result = 0;
                if(tempOpera.equals("+"))
                    result = jia(N);
                else if(tempOpera.equals("-"))
                    result = jian(N);
                else if(tempOpera.equals("*"))
                    result = cheng(N);
                else if(tempOpera.equals("/"))
                    result = chu(N);
                System.out.println("结果 "+result);
                number.push(result);
            }
        }
    }

    public static void main(String[] args){
        String formula = "4+(7-24)*91*(40/17+1)";
        String formulaParsing = "("+formulaParsing(formula)+")";
        System.out.println(formulaParsing);
        calculate(formulaParsing);
        System.out.println(number.pop());
    }
}
