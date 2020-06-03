package com.example.demo.util;


import com.greenpineyu.fel.Expression;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.common.FelBuilder;
import com.greenpineyu.fel.common.NumberUtil;
import com.greenpineyu.fel.common.ObjectUtils;
import com.greenpineyu.fel.compile.CompileService;
import com.greenpineyu.fel.context.ArrayCtxImpl;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.Var;
import com.greenpineyu.fel.function.CommonFunction;
import com.greenpineyu.fel.function.FunMgr;
import com.greenpineyu.fel.function.Function;
import com.greenpineyu.fel.optimizer.Optimizer;
import com.greenpineyu.fel.optimizer.VarVisitOpti;
import com.greenpineyu.fel.parser.AntlrParser;
import com.greenpineyu.fel.parser.FelNode;
import com.greenpineyu.fel.parser.Parser;
import com.greenpineyu.fel.security.SecurityMgr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * @author Anzepeng
 * @title: FelEngineImpl
 * @projectName calculate
 * @description: TODO
 * @date 2020/5/20 0020下午 17:44
 */
public class FelEngineImpl implements FelEngine {
    private FelContext context;
    private CompileService compiler;
    private Parser parser;
    private FunMgr funMgr;
    private SecurityMgr securityMgr;

    public SecurityMgr getSecurityMgr() {
        return this.securityMgr;
    }

    public void setSecurityMgr(SecurityMgr var1) {
        this.securityMgr = var1;
    }

    public FelEngineImpl(FelContext var1) {
        this.securityMgr = FelBuilder.newSecurityMgr();
        this.context = var1;
        this.compiler = new CompileService();
        this.parser = new AntlrParser(this);
        this.funMgr = new FunMgr();
    }

    public FelEngineImpl() {
        this(new ArrayCtxImpl());
        Function funmax= new CommonFunction() {
            public String getName() {
                return "MAX";
            }
            @Override
            public Object call(Object... values) {
                Double max=0-Double.MAX_VALUE;
                for(Object o:values){
                    Double d=NumberUtil.toDouble(o);
                    if(d.compareTo(max)>0){
                        max=d;
                    }
                }
                return max;
            }
        };
        this.addFun(funmax);
        Function funmin= new CommonFunction() {
            public String getName() {
                return "MIN";
            }
            @Override
            public Object call(Object... values) {
                Double min=Double.MAX_VALUE;
                for(Object o:values){
                    Double d=NumberUtil.toDouble(o);
                    if(d.compareTo(min)<0){
                        min=d;
                    }
                }
                return min;
            }
        };
        this.addFun(funmin);
        Function funavg= new CommonFunction() {
            public String getName() {
                return "AVG";
            }
            @Override
            public Object call(Object... values) {
                Double sum=0d;
                for(Object o:values){
                    Double d=NumberUtil.toDouble(o);
                    sum+=d;
                }
                return sum/values.length;
            }
        };
        this.addFun(funavg);

        Function avgeli= new CommonFunction() {
            public String getName() {
                return "AVGELI";
            }
            @Override
            public Object call(Object... values) {
                Double sum=0d;
                int count=0;
                for(Object o:values){
                    if(o!=null){
                        Double d=NumberUtil.toDouble(o);
                        sum+=d;
                        if(d!=0.0){
                            count++;
                        }
                    }
                }
                return sum/count;
            }
        };


        this.addFun(avgeli);
        Function funsum= new CommonFunction() {
            public String getName() {
                return "SUM";
            }
            @Override
            public Object call(Object... values) {
                Double sum=0d;
                for(Object o:values){
                    if(o!=null){
                        Double d=NumberUtil.toDouble(o);
                        sum+=d;
                    }
                }
                return sum;
            }
        };
        this.addFun(funsum);
        Function funabs= new CommonFunction() {
            public String getName() {
                return "ABS";
            }
            @Override
            public Object call(Object... values) {
                Double result=NumberUtil.toDouble(values[0]);
                return Math.abs(result);
            }
        };
        this.addFun(funabs);
        Function funbadmin= new CommonFunction() {
            public String getName() {
                return "BADMIN";
            }
            @Override
            public Object call(Object... values) {
                Double min=Double.MAX_VALUE;
                boolean a=true;
                for(Object o:values){
                    Double d=NumberUtil.toDouble(o);
                    Double b=Math.abs(d);
                    if(b.compareTo(min)<0){
                        min=b;
                        if(d>=0){
                            a=true;
                        }else{
                            a=false;
                        }
                    }
                }
                if(a){
                    return min;
                }else{
                    return 0-min;
                }
            }
        };
        this.addFun(funbadmin);

        Function funbadmax= new CommonFunction() {
            public String getName() {
                return "BADMAX";
            }
            @Override
            public Object call(Object... values) {
                Double max=0d;
                boolean a=true;
                for(Object o:values){
                    Double d=NumberUtil.toDouble(o);
                    if(d.equals(NumberUtil.toDouble(-100))){
                        continue;
                    }
                    Double b=Math.abs(d);
                    if(b.compareTo(max)>0){
                        if(d>=0){
                            a=true;
                        }else{
                            a=false;
                        }
                        max=b;
                    }
                }
                if(a){
                    return max;
                }else{
                    return 0-max;
                }
            }
        };
        this.addFun(funbadmax);
        Function funacos= new CommonFunction() {
            public String getName() {
                return "ACOS";
            }
            @Override
            public Object call(Object... values) {
                Double result=NumberUtil.toDouble(values[0]);
                return Math.acos(result);
            }
        };
        this.addFun(funacos);
        Function funasin= new CommonFunction() {
            public String getName() {
                return "ASIN";
            }
            @Override
            public Object call(Object... values) {
                Double result=NumberUtil.toDouble(values[0]);
                return Math.asin(result);
            }
        };
        this.addFun(funasin);
        Function funatan= new CommonFunction() {
            public String getName() {
                return "ATAN";
            }
            @Override
            public Object call(Object... values) {
                Double result=NumberUtil.toDouble(values[0]);
                return Math.atan(result);
            }
        };
        this.addFun(funatan);

        Function funcos= new CommonFunction() {
            public String getName() {
                return "COS";
            }
            @Override
            public Object call(Object... values) {
                Double result=NumberUtil.toDouble(values[0]);
                return Math.cos(result);
            }
        };
        this.addFun(funcos);
        Function funsin= new CommonFunction() {
            public String getName() {
                return "SIN";
            }
            @Override
            public Object call(Object... values) {
                Double result=NumberUtil.toDouble(values[0]);
                return Math.sin(result);
            }
        };
        this.addFun(funsin);
        Function funtan= new CommonFunction() {
            public String getName() {
                return "TAN";
            }
            @Override
            public Object call(Object... values) {
                Double result=NumberUtil.toDouble(values[0]);
                return Math.tan(result);
            }
        };
        this.addFun(funtan);

        Function funceil= new CommonFunction() {
            public String getName() {
                return "CEIL";
            }
            @Override
            public Object call(Object... values) {
                Double result=NumberUtil.toDouble(values[0]);
                return Math.ceil(result);
            }
        };
        this.addFun(funceil);

        Function funround= new CommonFunction() {
            public String getName() {
                return "ROUND";
            }
            @Override
            public Object call(Object... values) {
                Double result=NumberUtil.toDouble(values[0]);
                return Math.round(result*100)*0.01;
            }
        };
        this.addFun(funround);

        Function funfloor= new CommonFunction() {
            public String getName() {
                return "FLOOR";
            }
            @Override
            public Object call(Object... values) {
                Double result=NumberUtil.toDouble(values[0]);
                return Math.floor(result);
            }
        };
        this.addFun(funfloor);

        Function funsqrt= new CommonFunction() {
            public String getName() {
                return "SQRT";
            }
            @Override
            public Object call(Object... values) {
                Double result=NumberUtil.toDouble(values[0]);
                return Math.sqrt(result);
            }
        };
        this.addFun(funsqrt);

        Function funpow= new CommonFunction() {
            public String getName() {
                return "POW";
            }
            @Override
            public Object call(Object... values) {
                Double arg1=NumberUtil.toDouble(values[0]);
                return Math.pow(arg1,2);
            }
        };
        this.addFun(funpow);

        Function funpown= new CommonFunction() {
            public String getName() {
                return "POWN";
            }
            @Override
            public Object call(Object... values) {
                Double arg1=NumberUtil.toDouble(values[0]);
                Double arg2=NumberUtil.toDouble(values[1]);
                return Math.pow(arg1,arg2);
            }
        };
        this.addFun(funpown);

        Function funsqrtn= new CommonFunction() {
            public String getName() {
                return "SQRTN";
            }
            @Override
            public Object call(Object... values) {
                Double arg1=NumberUtil.toDouble(values[0]);
                Double arg2=NumberUtil.toDouble(values[1]);
                return Math.pow(arg1,1/arg2);
            }
        };
        this.addFun(funsqrtn);

        Function equals= new CommonFunction() {
            public String getName() {
                return "EQUALS";
            }
            @Override
            public Object call(Object... values) {
                return values[0].equals(values[1]);
            }
        };
        this.addFun(equals);

        Function isnull= new CommonFunction() {
            public String getName() {
                return "ISNULL";
            }
            @Override
            public Object call(Object... values) {
                if(values==null||values[0]==null||"".equals(values[0].toString().trim())){
                    return true;
                }
                return false;
            }
        };
        this.addFun(isnull);


        Function nulltozero= new CommonFunction() {
            public String getName() {
                return "NULLTOZERO";
            }
            @Override
            public Object call(Object... values) {
                if(values==null||values[0]==null||"".equals(values[0].toString().trim())){
                    return 0;
                }
                return values[0];
            }
        };
        this.addFun(nulltozero);

        Function concat= new CommonFunction() {
            public String getName() {
                return "CONCAT";
            }
            @Override
            public Object call(Object... values) {
                StringBuffer result=new StringBuffer();
                for(Object v:values){
                    if(v!=null)
                        result.append(v);
                }
                return result.toString();
            }
        };
        this.addFun(concat);

        Function date= new CommonFunction() {
            public String getName() {
                return "DATE";
            }
            @Override
            public Object call(Object... values) {
                SimpleDateFormat format=null;
                if(values==null){
                    format=new SimpleDateFormat("yyyy-MM-dd");
                } else if(values[0].toString().equals("1")){
                    format=new SimpleDateFormat("yyyy-MM-dd-");
                } else if(values[0].toString().equals("2")){
                    format=new SimpleDateFormat("yyyy-MM");
                } else if(values[0].toString().equals("3")){
                    format=new SimpleDateFormat("yyyy");
                }
                return format.format(new Date(System.currentTimeMillis()));
            }
        };
        this.addFun(date);


        Function random= new CommonFunction() {
            public String getName() {
                return "RANDOM";
            }
            @Override
            public Object call(Object... values) {
                Random random1 = new Random(System.currentTimeMillis());
                int a=random1.nextInt(2);
                Double c=Double.valueOf(values[0].toString());
                double b = random1.nextInt((int)(c*Double.valueOf(values[1].toString())*10000))/1000000.0;
                if(a==1){
                    return c+b;
                }else{
                    return c-b;
                }
            }
        };
        this.addFun(random);

        Function predate= new CommonFunction() {
            public String getName() {
                return "PREDATE";
            }
            @Override
            public Object call(Object... values) {
                SimpleDateFormat format=null;
                Calendar calendar=Calendar.getInstance();
                calendar.setFirstDayOfWeek(2);
                if(values==null){
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    format=new SimpleDateFormat("yyyy-MM-dd");
                } else if(values[0].toString().equals("1")){
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    format=new SimpleDateFormat("yyyy-MM-dd");
                } else if(values[0].toString().equals("2")){
                    calendar.add(Calendar.MONTH, -1);
                    format=new SimpleDateFormat("yyyy-MM");
                } else if(values[0].toString().equals("3")){
                    calendar.add(Calendar.YEAR, -1);
                    format=new SimpleDateFormat("yyyy");
                }
                return format.format(calendar.getTime());
            }
        };
        this.addFun(predate);

        Function pre15time= new CommonFunction() {
            public String getName() {
                return "PRE15TIME";
            }
            @Override
            public Object call(Object... values) {
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar calendar=Calendar.getInstance();
                if(values!=null){
                    calendar.setTime(new Date((Long)values[0]));
                }
                calendar.setFirstDayOfWeek(2);
                int minu=calendar.get(Calendar.MINUTE);
                if(minu%15!=0){
                    minu=((int)(minu/15))*15;
                }
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, minu);
                return format.format(calendar.getTime());
            }
        };
        this.addFun(pre15time);
    }

    public FelNode parse(String var1) {
        return this.parser.parse(var1);
    }

    public Object eval(String var1) {
        return this.eval(var1, this.context);
    }

    public Object eval(String var1, Var... var2) {
        FelNode var3 = this.parse(var1);
        VarVisitOpti var4 = new VarVisitOpti(var2);
        var3 = var4.call(this.context, var3);
        return var3.eval(this.context);
    }

    public Object eval(String var1, FelContext var2) {
        return this.parse(var1).eval(var2);
    }

    public Expression compile(String var1, Var... var2) {
        return this.compile(var1, (FelContext)null, new VarVisitOpti(var2));
    }

    public Expression compile(String var1, FelContext var2, Optimizer... var3) {
        if (var2 == null) {
            var2 = this.context;
        }

        FelNode var4 = this.parse(var1);
        if (var3 != null) {
            Optimizer[] var5 = var3;
            int var6 = var3.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Optimizer var8 = var5[var7];
                if (var8 != null) {
                    var4 = var8.call(var2, var4);
                }
            }
        }

        return this.compiler.compile(var2, var4, var1);
    }

    public String toString() {
        return "FelEngine";
    }

    public void addFun(Function var1) {
        this.funMgr.add(var1);
    }

    public FelContext getContext() {
        return this.context;
    }

    public CompileService getCompiler() {
        return this.compiler;
    }

    public void setCompiler(CompileService var1) {
        this.compiler = var1;
    }

    public Parser getParser() {
        return this.parser;
    }

    public void setParser(Parser var1) {
        this.parser = var1;
    }

    public FunMgr getFunMgr() {
        return this.funMgr;
    }

    public void setFunMgr(FunMgr var1) {
        this.funMgr = var1;
    }

    public void setContext(FelContext var1) {
        this.context = var1;
    }
}

