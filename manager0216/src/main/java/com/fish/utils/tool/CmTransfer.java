/**
 *
 */
package com.fish.utils.tool;

/**
 * 通信格式转换
 *
 * Java和一些windows编程语言如c、c++、delphi所写的网络程序进行通讯时，需要进行相应的转换 高、低字节之间的转换
 * windows的字节序为低字节开头 linux,unix的字节序为高字节开头 java则无论平台变化，都是高字节开头
 */

public class CmTransfer
{
    /**
     * 将 int 转为低字节在前，高字节在后的 byte 数组
     *
     * @param n
     *            int
     * @return byte[]
     */
    public static byte[] toLH(int n)
    {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * 将 short 转为低字节在前，高字节在后的 byte 数组
     *
     * @param n
     *            short
     * @return byte[]
     */
    public static byte[] toLHShort(short n)
    {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    /**
     * 将低字节数组转换为字符串
     *
     * @param b
     * @param index
     * @return
     */
    public static String lBytesToString(byte[] b, int index, int length)
    {
        StringBuilder sb = new StringBuilder();
        int finish = index + length;

        for (int i = index; i < finish; i++)
        {
            char c = (char) b[i];
            if (0 == c)
            {
                break;
            }
            sb.append(c);
        }

        return sb.toString();
    }

    /**
     * 将低字节数组转换为字节数组
     */
    public static byte[] lBytesToBytes(byte[] b, int index, int length)
    {
        byte[] target = new byte[length];

        System.arraycopy(b, index, target, 0, length);

        return target;
    }

    /**
     * 将低字节数组转换为 int
     *
     * @param b
     *            byte[]
     * @return int
     */
    public static int lBytesToInt(byte[] b, int index)
    {
        int s = 0;
        byte v0 = b[index];
        byte v1 = b[index + 1];
        byte v2 = b[index + 2];
        byte v3 = b[index + 3];

        if (v3 >= 0)
        {
            s = s + v3;
        } else
        {
            s = s + 256 + v3;
        }
        s = s << 8;

        if (v2 >= 0)
        {
            s = s + v2;
        } else
        {
            s = s + 256 + v2;
        }
        s = s << 8;

        if (v1 >= 0)
        {
            s = s + v1;
        } else
        {
            s = s + 256 + v1;
        }
        s = s << 8;

        if (v0 >= 0)
        {
            s = s + v0;
        } else
        {
            s = s + 256 + v0;
        }

        return s;
    }

    /**
     * 低字节数组到 short 的转换
     *
     * @param b
     *            byte[]
     * @return short
     */
    public static short lBytesToShort(byte[] b, int index)
    {
        int s = 0;
        byte v0 = b[index];
        byte v1 = b[index + 1];

        if (v1 >= 0)
        {
            s = s + v1;
        } else
        {
            s = s + 256 + v1;
        }
        s = s << 8;

        if (v0 >= 0)
        {
            s = s + v0;
        } else
        {
            s = s + 256 + v0;
        }

        short result = (short) s;
        return result;
    }

}