package com.vxianjin.gringotts.util;

import com.alibaba.fastjson.JSONObject;
import com.vxianjin.gringotts.constant.Constant;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//@Deprecated
public class HttpUtil {

    public static final String CHARSET = "UTF-8";
    public static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    public static HttpUtil httpUtil;

    //定义sms
    //dc 数据类型
    private static final int DATACODING = 15;
    //rf 响应格式
    private static final int REPSPONSEFORMAT = 2;
    //rd 是否需要状态报告
    private static final int REPORTDATA = 1;
    //tf 短信内容的传输编码
    private static final int TRANSFERENCODING = 3;
    public static HttpUtil getInstance() {
        if (httpUtil == null) {
            httpUtil = new HttpUtil();
        }
        return httpUtil;
    }

    /**
     * 格式化数据
     *
     * @param paraMap
     * @param encode
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String formatMap(Map<String, String> paraMap,
                                   Boolean encode) throws UnsupportedEncodingException {
        StringBuilder value = new StringBuilder("");

        for (String key : paraMap.keySet()) {

            if (encode) {
                value.append(key + "=" + URLEncoder.encode(paraMap.get(key), "UTF-8") + "&");
            } else {
                value.append(key + "=" + paraMap.get(key) + "&");
            }

        }
        return value.toString();
    }

    /**
     * 扫描图片信息
     *
     * @param urlStr
     * @param textMap
     * @param fileMap
     * @param contentType 没有传入文件类型默认采用application/octet-stream
     *                    contentType非空采用filename匹配默认的图片类型
     * @return 返回response数据
     */
    @SuppressWarnings("rawtypes")
    public static String formUploadImage(String urlStr, Map<String, String> textMap, Map<String, String> fileMap, String contentType) {
        String res = "";
        HttpURLConnection conn = null;
        OutputStream out = null;
        BufferedReader reader = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------123821742118716";
        try {
            URL url = new URL(urlStr);

            conn = getConnection(url);

            out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes(CHARSET));
            }
            // file
            if (fileMap != null) {
                Iterator iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();//图片地址
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    logger.info("filename = " + filename);
                    //没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
                    contentType = new MimetypesFileTypeMap().getContentType(file);
                    logger.info("contentType = " + contentType);
                    //contentType非空采用filename匹配默认的图片类型
                    if (!"".equals(contentType)) {
                        if (filename.endsWith(".png")) {
                            contentType = "image/png";
                        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
                            contentType = "image/jpeg";
                        } else if (filename.endsWith(".gif")) {
                            contentType = "image/gif";
                        } else if (filename.endsWith(".ico")) {
                            contentType = "image/image/x-icon";
                        }
                    }
                    if (contentType == null || "".equals(contentType)) {
                        contentType = "application/octet-stream";
                    }
                    logger.info("contentType = " + contentType);
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"; filename=\"" + filename
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes(CHARSET));
/*                    DataInputStream in = new DataInputStream(
                            new FileInputStream(file));*/
                    InputStream is = null;
                    HttpURLConnection qnConn = null;
                    try {
                        logger.info("IMG_URL:" + Constant.HTTP + inputValue.replaceAll("\\\\", "\\/"));
                        URL url2 = new URL(Constant.HTTP + inputValue.replaceAll("\\\\", "\\/"));
                        qnConn = getConnection(url2);
//                        qnConn.getOutputStream();

                        is = qnConn.getInputStream();
                        int bytes = 0;
                        byte[] bufferOut = new byte[1024];
                        while ((bytes = is.read(bufferOut)) != -1) {
                            out.write(bufferOut, 0, bytes);
                        }
                    } catch (Exception e) {
                        logger.error("Exception:" + e.getMessage());
                    } finally {
                        if (null != is) {
                            is.close();
                        }
                        if (null != qnConn) {
                            qnConn.disconnect();
                        }

                    }
                }
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // 读取返回数据
                StringBuffer strBuf = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), CHARSET));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    strBuf.append(line).append("\n");
                }
                res = strBuf.toString();
                reader = null;
            } else {
                StringBuffer error = new StringBuffer();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        conn.getErrorStream(), CHARSET));
                String line1 = null;
                while ((line1 = bufferedReader.readLine()) != null) {
                    error.append(line1).append("\n");
                }
                res = error.toString();
                bufferedReader.close();
                bufferedReader = null;
            }
            logger.info("返回请求参数:" + responseCode + " msg=" + res);
        } catch (Exception e) {
            logger.info("发送POST请求出错。" + urlStr);
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != conn) {
                conn.disconnect();
            }
        }
        return res;
    }

    private static HttpURLConnection getConnection(URL url) throws Exception {
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716";
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
        conn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);
        return conn;
    }

    public static String post(String url,JSONObject params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        String body = null;
        try {
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            if (params != null) {
                List<NameValuePair> kvList = new ArrayList<>();
                for (String key : params.keySet()) {
                    kvList.add(new BasicNameValuePair(key, params.getString(key)));
                }
                StringEntity entity = new UrlEncodedFormEntity(kvList, "utf-8");
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("httpclient statusCode:" + statusCode);
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Method failed:" + response.getStatusLine());
            }
            body = EntityUtils.toString(response.getEntity(),"UTF-8");
        } catch (IOException e) {
            // 网络错误
            logger.error("http post exception :{}", e);
        }catch (Exception e){
            logger.error("http post has error : ", e);
        }
        return body;
    }

    /**
     * 调用 API
     *
     * @return str
     */
    public static String postJson(String apiURL, String params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(apiURL);
        String body = null;
        try {
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            if (params != null) {
                // 设置字符集
                StringEntity stringEntity = new StringEntity(params, "utf-8");
                // 设置参数实体
                httpPost.setEntity(stringEntity);
            }

            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Method failed:" + response.getStatusLine());
            }

            // Read the response body
            body = EntityUtils.toString(response.getEntity(),"UTF-8");

        } catch (IOException e) {
            // 网络错误
            logger.error("http post exception :{}", e);
        }
        return body;
    }

    public static String postJson(String apiURL, String params, int timeOut) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(apiURL);
        String body = null;
        try {
            // 设置超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(timeOut).setConnectTimeout(timeOut).build();
            httpPost.setConfig(requestConfig);
            // 建立一个NameValuePair数组，用于存储欲传送的参数
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            if (params != null) {
                // 设置字符集
                StringEntity stringEntity = new StringEntity(params, "utf-8");
                // 设置参数实体
                httpPost.setEntity(stringEntity);
            }
//          httpPost.setEntity(new SerializableEntity(parameters, Charset.forName("UTF-8")));

            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Method failed:{}", response.getStatusLine());
            }

            // Read the response body
            body = EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            // 网络错误
            logger.error("http post exception :{}", e);
        }
        return body;
    }

    /**
     * 天畅云 短信发送
     * @param url url
     * @param paramMap map
     * @return str
     */
    public static String sendPost(String url, Map<String, Object> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // conn.setRequestProperty("Charset", "UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            paramMap.put("dc", DATACODING);
            paramMap.put("rf", REPSPONSEFORMAT);
            paramMap.put("rd", REPORTDATA);
            paramMap.put("tf", TRANSFERENCODING);
            // 设置请求属性
            String param = "";
            if (paramMap != null && paramMap.size() > 0) {
                Iterator<String> ite = paramMap.keySet().iterator();
                while (ite.hasNext()) {
                    // key
                    String key = ite.next();
                    Object value = paramMap.get(key);
                    param += key + "=" + value + "&";
                }
                param = param.substring(0, param.length() - 1);
                //System.out.println(param);
            }

            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("sendPost error:{}",e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }



    public static String postForm(String url, Map<String, Object> params) {
        URL u = null;
        HttpURLConnection con = null;
        // 构建请求参数
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        // 尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            //// POST 只能为大写，严格限制，post会不识别
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(1000);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(sb.toString());
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            //一定要有返回值，否则无法把请求发送给server端。
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }
}
