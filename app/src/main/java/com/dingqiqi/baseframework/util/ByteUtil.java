package com.dingqiqi.baseframework.util;

import java.io.UnsupportedEncodingException;

/**
 * 字节处理相关工具类
 * @author
 *
 */
public class ByteUtil {
	private ByteUtil() {}


	public static byte[] toBytes(String data, String charsetName) {
		try {
			return data.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static byte[] toBytes(String data) {
		return toBytes(data, "ISO-8859-1");
	}

	public static byte[] toGBK(String data) {
		return toBytes(data, "GBK");
	}

	public static byte[] toGB2312(String data) {
		return toBytes(data, "GB2312");
	}

	public static byte[] toUtf8(String data) {
		return toBytes(data, "UTF-8");
	}

	public static String fromBytes(byte[] data, String charsetName) {
		try {
			return new String(data, charsetName);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public static String fromBytes(byte[] data) {
		return fromBytes(data, "ISO-8859-1");
	}
	
	public static String fromGBK(byte[] data) {
		return fromBytes(data, "GBK");
	}
	
	public static String fromGB2312(byte[] data) {
		return fromBytes(data, "GB2312");
	}

	public static String fromGB2312New(String data) {
		return fromGB2312(toBytes(data.trim()));
	}

	public static String fromUtf8(byte[] data) {
		return fromBytes(data, "UTF-8");
	}

	 







}

