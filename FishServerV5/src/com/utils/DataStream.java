package com.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * 类与数据流相互转化
 *
 * @author 胥伟华 2017.6.6
 */
public class DataStream
{
    // 输出流
    private ByteArrayOutputStream out = null;
    // 读取流
    private byte[] bytes;
    // 是否读
    private boolean isRead;

    public DataStream(byte[] bytes, boolean isRead)
    {
        this.isRead = isRead;
        if (!isRead)
            out = new ByteArrayOutputStream();
        else
            this.bytes = bytes;
    }

    public DataStream()
    {
        this.isRead = false;
        out = new ByteArrayOutputStream();
    }

    /**
     * 进行读取流数据
     *
     * @param binaryStream
     * @throws IOException
     */
    public DataStream(InputStream binaryStream) throws IOException
    {
        out = new ByteArrayOutputStream();
        BufferedInputStream buffis = new BufferedInputStream(binaryStream);
        byte[] temp = new byte[1024 * 10];
        int len = 0;
        while ((len = buffis.read(temp)) > 0)
        {
            out.write(temp, 0, len);
        }
        buffis.close();

    }

    /**
     * 高低位转换
     *
     * @param n
     * @param len
     * @return
     */
    public static byte[] toLH(long n, int len)
    {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++)
        {
            b[i] = (byte) (n >> (8 * i) & 0xff);
        }
        return b;
    }

    /**
     * 底高位转换 Long
     */
    public static long toHL(byte[] b)
    {
        long value = 0L;
        for (int i = 0; i < b.length; i++)
        {
            value |= ((b[i] & 0xffL) << (8L * i));
        }
        return value;
    }

    /**
     * 获取构成数据
     *
     * @return
     * @throws IOException
     */
    public byte[] getBytes() throws IOException
    {
        if (!isRead)
        {
            byte[] bytes = out.toByteArray();
            out.close();
            return bytes;
        }
        return null;
    }

    public int getLastReadByteLen()
    {
        if (!isRead)
            return 0;
        return bytes.length;
    }

    /**
     * 进行写入数据
     *
     * @param value
     * @param size
     * @throws IOException
     */
    public void push(int value, int size) throws IOException
    {
        out.write(toLH(value, size));
    }

    /**
     * 进行写入数据
     *
     * @param value
     * @param size
     * @throws IOException
     */
    public void push(String value, int size) throws IOException
    {
        byte[] bytes = new byte[size];
        byte[] temp = value.getBytes();
        System.arraycopy(temp, 0, bytes, 0, temp.length);
        push(bytes);
    }

    /**
     * 进行写入数据
     *
     * @param value
     * @param size
     * @throws IOException
     */
    public void push(long value, int size) throws IOException
    {
        out.write(toLH(value, size));
    }

    public void push(long value) throws IOException
    {
        out.write(toLH(value, 8));
    }

    /**
     * 进行写入数据
     *
     * @param value
     * @throws IOException
     */
    public void push(String value) throws IOException
    {
        if (value == null)
            value = "";
        byte[] bytes = value.getBytes();
        int length = bytes.length;
        push(length, 4);
        push(bytes);
    }

    /**
     * 进行写入数据
     *
     * @param value
     * @throws IOException
     */
    public void push(Vector<Byte> value) throws IOException
    {
        int length = value.size();
        out.write(toLH(length, 4));
        for (Byte v : value)
            out.write(v);
    }

    /**
     * 进行写入数据
     *
     * @param value
     */
    public void push(byte value)
    {
        out.write(value);
    }

    /**
     * 进行写入数据
     *
     * @param value
     * @throws IOException
     */
    public void push(byte[] value) throws IOException
    {
        out.write(value);
    }

    /**
     * 读取数据
     *
     * @param len
     * @return
     */
    public int pop(int len)
    {
        if (!isRead)
            return 0;
        if (bytes == null || bytes.length < len)
            return 0;
        byte[] tempBytes = new byte[len];
        byte[] des = new byte[bytes.length - len];
        for (int i = 0; i < tempBytes.length; i++)
        {
            tempBytes[i] = bytes[i];
        }
        for (int i = len; i < bytes.length; i++)
        {
            des[i - len] = bytes[i];
        }
        bytes = des;
        return (int) toHL(tempBytes);
    }

    /**
     * 读取数据
     *
     * @return
     */
    public String pop() throws IOException
    {
        if (!isRead)
            return null;
        if (bytes == null || bytes.length < 4)
            return null;
        byte[] sizeByte = new byte[4];
        int len = 0;
        for (int i = 0; i < sizeByte.length; i++)
        {
            sizeByte[i] = bytes[len++];
        }
        int size = (int) toHL(sizeByte);
        if (size >= 16384)
            return null;
        byte[] tempBytes = new byte[size];
        for (int i = 0; i < size; i++)
        {
            tempBytes[i] = bytes[len++];
        }
        String result = new String(tempBytes);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (len < bytes.length)
        {
            out.write(bytes[len++]);
        }
        bytes = out.toByteArray();
        out.close();
        return result;
    }

    /**
     * 获取固定长度字符串
     *
     * @param len
     * @return
     * @throws IOException
     */
    public String popFixedStr(int len) throws IOException
    {
        byte[] bytes = popBytes(len);
        return new String(bytes).trim();
    }

    /**
     * 读取数据
     *
     * @return
     * @throws IOException
     */
    public byte[] popBytes(int size) throws IOException
    {
        if (bytes == null)
            return null;
        int len = 0;
        byte[] b = new byte[size];
        for (int i = 0; i < b.length; i++)
        {
            b[i] = bytes[len++];
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (len < bytes.length)
        {
            out.write(bytes[len++]);
        }
        bytes = out.toByteArray();
        out.close();
        return b;
    }

    /**
     * 读取数据
     *
     * @return
     * @throws IOException
     */
    public byte popByte() throws IOException
    {
        if (bytes == null || bytes.length <= 0)
            return 0;
        int len = 0;
        byte b = bytes[len++];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (len < bytes.length)
        {
            out.write(bytes[len++]);
        }
        bytes = out.toByteArray();
        out.close();
        return b;
    }
}
