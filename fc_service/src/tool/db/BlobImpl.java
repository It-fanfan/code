package tool.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.Log4j;

import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobImpl implements Blob
{
    private static Logger LOG = LoggerFactory.getLogger(BlobImpl.class);
    private byte[] _bytes = new byte[0];

    private int _length = 0;

    /**
     * 进行读取流数据
     *
     * @param input 流
     * @return byte[]
     * @throws IOException io read exception
     */
    public static byte[] read(InputStream input) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream reader = new BufferedInputStream(input);
        byte[] temp = new byte[1024 * 4];
        int len;
        while ((len = reader.read(temp)) > 0)
        {
            out.write(temp, 0, len);
        }
        out.close();
        return out.toByteArray();
    }

    /**
     * 构造函数，以blob重新构建blob
     *
     * @param blob Blob entity
     */
    public BlobImpl(Blob blob)
    {
        init(blobToBytes(blob));
    }

    /**
     * 初始化byte[]
     *
     * @param bytes byte data
     */
    private void init(byte[] bytes)
    {
        _bytes = bytes;
        _length = _bytes.length;
    }

    /**
     * 将blob转为byte[]
     *
     * @param blob Blob entity
     * @return byte[]
     */
    private byte[] blobToBytes(Blob blob)
    {
        BufferedInputStream is = null;
        try
        {
            is = new BufferedInputStream(blob.getBinaryStream());
            byte[] bytes = new byte[(int) blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read;
            while (offset < len && (read = is.read(bytes, offset, len - offset)) >= 0)
            {
                offset += read;
            }
            is.close();
            return bytes;
        } catch (Exception e)
        {
            return null;
        } finally
        {
            try
            {
                if (is != null)
                    is.close();
            } catch (IOException e)
            {
                LOG.error(Log4j.getExceptionInfo(e));
            }

        }
    }

    /**
     * 获得blob中数据实际长度
     *
     * @return byte len
     */
    public long length()
    {
        return _bytes.length;
    }

    /**
     * 返回指定长度的byte[]
     *
     * @param pos position
     * @param len length
     * @return byte[]
     * @throws SQLException SQL Exception
     */
    public byte[] getBytes(long pos, int len) throws SQLException
    {
        if (pos == 0 && len == length())
            return _bytes;
        try
        {
            if (len == length())
                return _bytes;
            byte[] newbytes = new byte[len];
            System.arraycopy(_bytes, (int) pos, newbytes, 0, len);
            return newbytes;
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new SQLException("Inoperable scope of this array");
        }
    }

    /**
     * 返回InputStream
     *
     * @return input stream
     */
    public InputStream getBinaryStream()
    {
        return new ByteArrayInputStream(_bytes);
    }

    /**
     * 获取此byte[]中start的字节位置
     *
     * @param pattern src byte[]
     * @param start   start position
     * @return last position
     * @throws SQLException SQL exception
     */
    public long position(byte[] pattern, long start) throws SQLException
    {
        start--;
        if (start < 0)
        {
            throw new SQLException("start < 0");
        }
        if (start >= _length)
        {
            throw new SQLException("start >= max length");
        }
        if (pattern == null)
        {
            throw new SQLException("pattern == null");
        }
        if (pattern.length == 0 || pattern.length > _length)
        {
            return -1;
        }
        return -1;
    }

    /**
     * 获取指定的blob中start的字节位置
     *
     * @param pattern blob entity
     * @param start   byte
     * @return position
     * @throws SQLException sql exception
     */
    public long position(Blob pattern, long start) throws SQLException
    {
        return position(blobToBytes(pattern), start);
    }

    /**
     * 不支持操作异常抛出
     */
    private void nonsupport()
    {
        throw new UnsupportedOperationException("This method is not supported！");
    }

    /**
     * 释放Blob对象资源
     */
    public void free()
    {
        _bytes = new byte[0];
        _length = 0;
    }

    /**
     * 返回指定长度部分的InputStream，并返回InputStream
     *
     * @param pos position
     * @param len length
     * @return input stream
     * @throws SQLException SQL exception
     */
    public InputStream getBinaryStream(long pos, long len) throws SQLException
    {
        return new ByteArrayInputStream(getBytes(pos, (int) len));
    }

    /**
     * 以指定指定长度将二进制流写入OutputStream，并返回OutputStream
     *
     * @param pos position
     * @return out stream
     * @throws SQLException sql exception
     */
    public OutputStream setBinaryStream(long pos) throws SQLException
    {
        // 暂不支持
        nonsupport();
        pos--;
        if (pos < 0)
        {
            throw new SQLException("pos < 0");
        }
        if (pos > _length)
        {
            throw new SQLException("pos > length");
        }
        // 将byte[]转为ByteArrayInputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(_bytes);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] bytes;
        try
        {
            bytes = new byte[inputStream.available()];
            int read;
            while ((read = inputStream.read(bytes)) >= 0)
            {
                os.write(bytes, 0, read);
            }

        } catch (IOException e)
        {
            e.getStackTrace();
        }

        // 返回OutputStream
        return os;
    }

    /**
     * 设定byte[]
     *
     * @param pos    position
     * @param bytes  data
     * @param offset offset
     * @param size   size
     * @return length
     * @throws SQLException sql exception
     */
    public int setBytes(long pos, byte[] bytes, int offset, int size) throws SQLException
    {
        // 暂不支持
        nonsupport();
        pos--;
        if (pos < 0)
        {
            throw new SQLException("pos < 0");
        }
        if (pos > _length)
        {
            throw new SQLException("pos > max length");
        }
        if (bytes == null)
        {
            throw new SQLException("bytes == null");
        }
        if (offset < 0 || offset > bytes.length)
        {
            throw new SQLException("offset < 0 || offset > bytes.length");
        }
        if (size < 0 || pos + size > (long) Integer.MAX_VALUE || offset + size > bytes.length)
        {
            throw new SQLException();
        }
        // 当copy数据时
        _bytes = new byte[size];
        System.arraycopy(bytes, offset, _bytes, 0, size);
        return size;
    }

    /**
     * 设定指定开始位置byte[]
     *
     * @param pos   position
     * @param bytes byte data
     * @return length
     * @throws SQLException sql exception
     */
    public int setBytes(long pos, byte[] bytes) throws SQLException
    {
        // 暂不支持
        nonsupport();
        return setBytes(pos, bytes, 0, bytes.length);
    }

    /**
     * 截取相应部分数据
     *
     * @param len length
     * @throws SQLException sql exception
     */
    public void truncate(long len) throws SQLException
    {
        if (len < 0)
        {
            throw new SQLException("len < 0");
        }
        if (len > _length)
        {
            throw new SQLException("len > max length");
        }
        _length = (int) len;
    }

}