package com.example.demo.util;

import com.greenpineyu.fel.Expression;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.common.FelBuilder;
import com.greenpineyu.fel.common.NumberUtil;
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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Anzepeng
 * @title: FelEngineImpl
 * @projectName calculate
 * @description: TODO
 * @date 2020/5/20 0020下午 17:44
 */
public class bf implements FelEngine, Serializable{

    private FelContext context;

    private CompileService compiler;

    private Parser parser;

    private FunMgr funMgr;

    private SecurityMgr securityMgr;

    private static Map<String,Expression> allCompileService=new HashMap<String,Expression>();

    @Override
    public SecurityMgr getSecurityMgr() {
        return securityMgr;
    }

    public void setSecurityMgr(SecurityMgr securityMgr) {
        this.securityMgr = securityMgr;
    }

    public bf(FelContext context) {
        this.context = context;
        compiler = new CompileService();
        parser = new AntlrParser(this);
        this.funMgr=new FunMgr();
    }

    {
        this.securityMgr = FelBuilder.newSecurityMgr();
    }

    public bf() {
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

    @Override
    public FelNode parse(String exp) {
        return parser.parse(exp);
    }

    @Override
    public Object eval(String exp) {
        return this.eval(exp, this.context);
    }

    public Object eval(String exp, Var... vars) {
        FelNode node = parse(exp);
        Optimizer opt = new VarVisitOpti(vars);
        node = opt.call(context, node);
        return node.eval(context);
    }

    @Override
    public Object eval(String exp, FelContext ctx) {
        return parse(exp).eval(ctx);
    }

    public Expression compile(String exp, Var... vars) {
        return compile(exp, null, new VarVisitOpti(vars));
    }

    @Override
    public Expression compile(String exp, FelContext ctx, Optimizer... opts) {
        if (ctx == null) {
            ctx = this.context;
        }
        String pattern = "\\$[\\w]+[\\!|\\=]=null\\?[^\\?&^:]+?\\:[^,]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(exp);
        boolean b=true;
        while(m.find()) {
            b=false;
            String a=m.group();
            String c=a.substring(0,a.indexOf("?"));
            String p=a.substring(a.indexOf("?")+1,a.indexOf(":"));
            String q=a.substring(a.indexOf(":")+1);
            List<String> cs= ParseUtils.getchidrenIndex(c);
            boolean result= true;
            for(String c1:cs){
                if(c.indexOf("==")>=0&&ctx.get("$"+c1)!=null){
                    result=false;
                }else if(c.indexOf("!=")>=0&&ctx.get("$"+c1)==null){
                    result=false;
                }
            }
            if(result){
                exp=exp.replace(a, p);
            }else{
                exp=exp.replace(a, q);
            }
            m = r.matcher(exp);
        }
        if(!exp.toUpperCase().trim().startsWith("SUM")){
            List<String> cs= ParseUtils.getchidrenIndex(exp);
            for(String c:cs){
                if(ctx.get("$"+c)==null){
                    return null;
                }
            }
        }

        FelNode node = parse(exp);
        if(node.toString().equals(exp.toString())&&exp.startsWith("$")){
            if(ctx.get(exp)!=null){
                exp=ctx.get(exp).toString();
            }else{
                return null;
            }
        }
        node = parse(exp);
        if (opts != null) {
            for (Optimizer opt : opts) {
                if (opt != null) {
                    node = opt.call(ctx, node);
                }
            }
        }
        Expression expression=allCompileService.get(exp);
        if(expression!=null){
            return expression;
        }
        expression=compiler.compile(ctx, node, exp);
        allCompileService.put(exp, expression);
        return expression;
    }

    @Override
    public String toString() {
        return "FelEngine";
    }

    @Override
    public void addFun(Function fun) {
        this.funMgr.add(fun);
    }

    @Override
    public FelContext getContext() {
        return this.context;
    }

    @Override
    public CompileService getCompiler() {
        return compiler;
    }


    @Override
    public void setCompiler(CompileService compiler) {
        this.compiler = compiler;
    }


    @Override
    public Parser getParser() {
        return parser;
    }


    @Override
    public void setParser(Parser parser) {
        this.parser = parser;
    }


    @Override
    public FunMgr getFunMgr() {
        return funMgr;
    }


    @Override
    public void setFunMgr(FunMgr funMgr) {
        this.funMgr = funMgr;
    }


    @Override
    public void setContext(FelContext context) {
        this.context = context;
    }

}
