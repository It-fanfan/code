package com.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XWHMathTool
{
    /**
     * 默认除法运算精度 小数点后面保留的位数
     */
    private static final int DEFAULT_DIV_SCALE = 10;

    private static Double subtract(String v1, String v2)
    {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }

    private static Double multiply(String v1, String v2)
    {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 格式化数字
     *
     * @param v1
     * @param scale 保留的小数位数
     * @return
     */
    public static double formatMath(Double v1, int scale)
    {
        BigDecimal b1 = new BigDecimal(v1);
        return b1.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 格式化数字
     *
     * @param v1
     * @param scale
     * @return
     */
    public static double formatMath(Integer v1, int scale)
    {
        return divide(v1.toString(), "1", scale);
    }

    /**
     * 格式化数字
     *
     * @param v1
     * @param scale
     * @return
     */
    public static double formatMath(Float v1, int scale)
    {
        return divide(v1.toString(), "1", scale);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_UP
     *
     * @param v1
     * @param v2
     * @return 两个参数的商
     */
    public static double divide(int v1, int v2)
    {
        return divide(Double.parseDouble(v1 + ""), Double.parseDouble(v2 + ""));
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_UP
     *
     * @param v1
     * @param v2
     * @param scale 表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static Double divide(int v1, int v2, int scale)
    {
        if (v1 == 0)
        {
            return 0.0;
        }
        return divide(Double.parseDouble(v1 + ""), Double.parseDouble(v2 + ""), scale);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_UP
     *
     * @param v1
     * @param v2
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2)
    {
        return divide(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_UP
     *
     * @param v1
     * @param v2
     * @param scale 表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale)
    {
        return divide(v1, v2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
     *
     * @param v1
     * @param v2
     * @param scale      表示需要精确到小数点以后几位
     * @param round_mode 表示用户指定的舍入模式
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale, int round_mode)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (v2 == 0)
        {
            throw new ArithmeticException("/ by zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, round_mode).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
     *
     * @param v1
     * @param v2
     * @param scale      表示需要精确到小数点以后几位
     * @param round_mode 表示用户指定的舍入模式
     * @return 两个参数的商，以字符串格式返回
     */
    public static Double divide(String v1, String v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (v2.equals("0"))
        {
            throw new ArithmeticException("/ by zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_UP
     *
     * @param v1
     * @param v2
     * @param scale      表示需要精确到小数点以后几位
     * @param round_mode 表示用户指定的舍入模式
     * @return 两个参数的商，以字符串格式返回
     */
    public static Double divide(long v1, int v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (v2 == 0)
        {
            throw new ArithmeticException("/ by zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_UP
     *
     * @param v1
     * @param v2
     * @param scale      表示需要精确到小数点以后几位
     * @param round_mode 表示用户指定的舍入模式
     * @return 两个参数的商，以字符串格式返回
     */
    public static Double divide(double v1, int v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (v2 == 0)
        {
            throw new ArithmeticException("/ by zero");
        }
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的加法运算
     *
     * @param v1
     * @param v2
     * @return
     */
    public static Double add(Double v1, Double v2)
    {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的加法运算 此加法运算是把多个加数放入Double数组
     *
     * @param Double
     * @return 如果数组为空则返回0 如果数组中的某一项为null则把此项默认为0
     */
    public static Double add(Double[] doubles)
    {
        BigDecimal h = new BigDecimal(0);
        if (doubles == null)
        {
            return h.doubleValue();
        }
        if (doubles.length < 1)
        {
            return h.doubleValue();
        }
        for (Double m : doubles)
        {
            BigDecimal b1 = new BigDecimal(0);
            try
            {
                b1 = new BigDecimal(m);
            } catch (Exception e)
            {
            }
            h = h.add(b1);
        }
        return h.doubleValue();
    }

    /**
     * 提供（相对）精确的减法运算。
     *
     * @param v1
     * @param v2
     * @return
     */
    public static Double subtract(double v1, double v2)
    {
        return subtract(v1 + "", v2 + "");
    }

    /**
     * 提供（相对）精确的乘法法运算。
     *
     * @param v1
     * @param v2
     * @return
     */
    public static Double multiply(double v1, double v2)
    {
        return multiply(v1 + "", v2 + "");
    }

    /**
     * 提供（相对）精确的乘法法运算。
     *
     * @param v1
     * @param v2
     * @return
     */
    public static Double multiply(Integer v1, Integer v2)
    {
        return multiply(v1 + "", v2 + "");
    }

    /**
     * 检测是否数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str)
    {
        if (str == null || str.isEmpty())
        {
            return false;
        }
        return regularExpression(str, "[0-9]*");
    }

    /**
     * 是否为email地址
     *
     * @param str
     * @return
     */
    public static boolean isEmailAddress(String str)
    {
        return regularExpression(str, "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
    }

    /**
     * 是否为网址
     *
     * @param str
     * @return
     */
    public static boolean isNetAddress(String str)
    {
        return regularExpression(str, "[a-zA-z]+://[^\\s]*");
    }

    /**
     * 检测是否中文
     *
     * @param str
     * @return
     */
    public static boolean isChineseWords(String str)
    {
        return regularExpression(str, "[\u4e00-\u9fa5]");
    }

    /**
     * 是否属于JSON字串
     *
     * @param str
     * @return
     */
    public static boolean isJsonWords(String str)
    {
        return regularExpression(str, "\\{.*\\}");
    }

    /**
     * 正则表达式应用
     *
     * @param str
     * @param expression
     * @return
     */
    public static boolean regularExpression(String str, String expression)
    {
        Pattern pattern = Pattern.compile(expression);
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches())
        {
            return false;
        }
        return true;
    }
}
