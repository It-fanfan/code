/**
 * 
 */
package tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author feng
 * 
 */
public class CmEncryption
{

	//
	// 全局字符串花名数组
	//
	private final static String[] STR_DIGITS = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 返回形式为数字跟字符串
	 */
	private static String byteToArrayString(byte bByte)
	{
		int iRet = bByte;

		if (iRet < 0)
		{
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return STR_DIGITS[iD1] + STR_DIGITS[iD2];
	}

	/**
	 * 转换字节数组为 16 进制字串
	 */
	private static String byteToString(byte[] bByte)
	{
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++)
		{
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	/**
	 * 获取 md5 代码的方法
	 */
	public static String GetMD5Code(String strObj)
	{
		String resultString = null;

		try
		{
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteToString(md.digest(strObj.getBytes()));
		}
		catch (NoSuchAlgorithmException ex)
		{
			ex.printStackTrace();
		}
		return resultString;
	}

	/**
	 * 获取编解码的整型值
	 */
	public static String getEncryptKeyValue(int startValue)
	{
		int index = 0;
		int hashValue = 378551;
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 8; i++)
		{
			int value = startValue % 10;

			startValue /= 10;
			hashValue ^= ((hashValue << 5) + value + (hashValue >> 2));
			index = hashValue % 26;

			if (index < 0)
			{
				index = -index;
			}

			sb.append((char)('a' + index));
		}

		return sb.toString();
	}

	/**
	 * FS 加密的方法
	 */
	public static byte[] fsEncryptToBytes(byte[] bytes, String encryptKey)
			throws Exception
	{
		byte[] pattern = encryptKey.getBytes("GB2312");

		for (int i = 0; i < bytes.length; i++)
		{
			bytes[i] = (byte) (bytes[i] ^ pattern[i % pattern.length]);
		}

		return bytes;
	}

	/**
	 * FS 解密的方法
	 */
	public static byte[] fsDecryptByBytes(byte[] encryptBytes, String decryptKey)
			throws Exception
	{
		byte[] pattern = decryptKey.getBytes("GB2312");

		for (int i = 0; i < encryptBytes.length; i++)
		{
			encryptBytes[i] = (byte) (encryptBytes[i] ^ pattern[i % pattern.length]);
		}

		return encryptBytes;
	}

}
