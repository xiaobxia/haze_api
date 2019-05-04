package com.vxianjin.gringotts.pay.common.util.fuiou;

import org.nuxeo.common.xmap.XMap;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public final class XMapUtil {
	private static final String charset = Charset.defaultCharset().name();

	private static final String xmlHeadStart = "<?xml ";

	private static final String xmlHead = xmlHeadStart + "version=\"1.0\" encoding=\"" + charset + "\"?>";

	private static final String encodeStart = "encoding=\"";

	@SuppressWarnings("unchecked")
	public static <T> T parseStr2Obj(Class<T> clazz, String spsDataStr) {
		try {
			//预处理字符串
			spsDataStr = preHandlerSpsDataStr(spsDataStr);
			//获取字符编码信息
			String charset = getCharsetByStr(spsDataStr);
			//转成xmap
			InputStream ips = new ByteArrayInputStream(spsDataStr.getBytes(charset));
			XMap xmap = new XMap();
			xmap.register(clazz);
			Object obj = xmap.load(ips);
			return (T) obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("解析XML异常");
		}
	}

	/**
	 * 使用指定编码解析byte[]至XObject
	 * 
	 * @param charset
	 *            解码字节数组byte[]的编码
	 * */
	public static <T> T parseBytes2Obj(Class<T> clazz, byte[] bytes, String charset) {
		try {
			return parseStr2Obj(clazz, new String(bytes, charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("解析XML异常");
		}

	}

	/**
	 * 使用指定编码解析byte[]至XObject <h1>该方法默认字节数组用系统默认编码进行解码，否则请调用含编码的方法</h1>
	 * 
	 * @see #parseBytes2Obj(Class, byte[], String)
	 * 
	 * */
	public static <T> T parseBytes2Obj(Class<T> clazz, byte[] bytes) {
		try {
			return parseStr2Obj(clazz, new String(bytes, Charset.defaultCharset().name()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("解析XML异常");
		}

	}

	/**
	 * 预处理xml字符串
	 * @param spsDataStr
	 */
	private static String preHandlerSpsDataStr(String spsDataStr) {
		spsDataStr = spsDataStr.trim();
		if (!spsDataStr.startsWith(xmlHeadStart))
			spsDataStr = xmlHead + spsDataStr;
		return spsDataStr;
	}

	/**
	 * 获取字符编码
	 * @param spsDataStr
	 * @return
	 */
	private static String getCharsetByStr(String spsDataStr) {
		if (spsDataStr.contains(encodeStart)) {
			//获取字符编码
			int i = spsDataStr.indexOf("encoding=\"");
			String splitChar = spsDataStr.substring(i + 9, i + 10);
			String encodeSub = spsDataStr.substring(i + 10);
			int j = encodeSub.indexOf(splitChar);
			return encodeSub.substring(0, j);
		} else
			return charset;
	}

	/**
	 * 通过tag获取xml子字符串
	 * @param data
	 * @param tagName
	 * @return
	 */
	public static String getSubXmlByTagName(String data, String tagName) {
		data = data.substring(data.indexOf("<" + tagName), data.indexOf("</" + tagName + ">") + tagName.length() + 3);
		return data;
	}

	/**
	 * 转成xml文件
	 * @return
	 * @throws Exception
	 */
	public static <T> String toXMLGBK(T bean) throws Exception {
		return toXML(bean, "GBK");
	}

	/**
	 * 转成xml文件
	 * @return
	 * @throws Exception
	 */
	public static <T> String toXMLUTF8(T bean) throws Exception {
		return toXML(bean, "UTF-8");
	}

	/**
	 * 转成xml文件
	 * @return
	 * @throws Exception
	 */
	public static <T> String toXML(T bean, String charset) throws Exception {
		XMap xmap = new XMap();
		xmap.register(bean.getClass());
		String str = xmap.asXmlString(bean, charset, null);
		System.out.println("返回xml："+str);
		return str;
	}

	/**
	 * 转换对象到字符串
	 * 
	 * */
	public static <T> String convertObj2Str(T obj) throws IllegalArgumentException {
		try {
			XMap map = new XMap();
			map.register(obj.getClass());
			return map.asXmlString(obj, "GBK", null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("解析数据异常");
		}
	}
	
	public static <T> String toXMLE(T bean, String charset) {
		XMap xmap = new XMap();
		xmap.register(bean.getClass());
		String str="";
		try {
			str = xmap.asXmlString(bean, charset, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//LogWriter.info("返回xml："+str);
		return str;
	}
}
